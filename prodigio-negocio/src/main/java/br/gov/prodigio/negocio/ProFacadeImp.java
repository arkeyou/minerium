/*******************************************************************************
 * Minerium Meta Framework
 *
 * Licença: GNU Lesser General Public License (LGPL), version 3.
 *  
 * Copyright (C) (2013-2018) Prodemge. Todos os direitos reservados.
 *   
 * Este arquivo é parte do Minerium Meta Framework
 * O Minerium Meta Framework é um software livre; você pode redistribuí-lo e/ou 
 * modificá-lo dentro dos termos da GNU Lesser General Public License (LGPL), version 3.
 *
 * Este framework é distribuído na esperança de que possa ser  útil, 
 * mas SEM NENHUMA GARANTIA; sem uma garantia implícita de ADEQUAÇÃO
 * a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. 
 * Ver arquivo LICENSE.md no diretório raiz ou acessar <https://www.gnu.org/licenses/lgpl.html>
 *******************************************************************************/
package br.gov.prodigio.negocio;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.gov.prodigio.comuns.BuilderExemplo;
import br.gov.prodigio.comuns.IProFacadeLocal;
import br.gov.prodigio.comuns.IProFacadeRemote;
import br.gov.prodigio.comuns.anotacoes.RegraDeNegocio;
import br.gov.prodigio.comuns.excecoes.ViolacaoDeRegraEx;
import br.gov.prodigio.comuns.utils.Context;
import br.gov.prodigio.comuns.utils.ServiceLocatorRN;
import br.gov.prodigio.entidades.ProBaseVO;

public class ProFacadeImp implements IProFacadeLocal, IProFacadeRemote {
	private static final Logger log = LoggerFactory.getLogger(ProFacadeImp.class);

	@Override
	public <E extends ProBaseVO> Set<E> listar(E AppBaseVO) throws Exception {
		ProBaseRN appRn = getAppRN(AppBaseVO);
		Set<E> aux = null;
		try {
			iniciarTransacao();
			aux = (Set<E>) appRn.listar(AppBaseVO);
			limparTransacao();
			return aux;
		} catch (Exception e) {
			log.error("Erro ao pesquisar", e);
			cancelarTransacao(e);
			throw e;
		} finally {
			limparTransacao();
		}
	}

	@Override
	public Object gravar(ProBaseVO t) throws Exception {
		ProBaseRN appRn = getAppRN(t);
		try {
			Context.setAttribute("objetoAtual", t);
			iniciarTransacao();
			Object obj = appRn.gravar(t);
			confirmarTransacao();
			/*
			 * ProBaseVO aux = recuperaObjeto(t);TODO: Rever esta implementação. Num contexto remoto este código não faz sentido, pois, não há passagem por referência em VM distindas t.setNrVersao(aux.getNrVersao());
			 */return obj;
		} catch (Exception e) {
			cancelarTransacao(e);
			throw e;
		}
	}

	@Override
	public void editar(ProBaseVO t) throws Exception {
		ProBaseRN appRn = getAppRN(t);
		try {
			ProBaseVO aux = t;
			iniciarTransacao();
			t = appRn.recuperaObjeto(t);
			t.setCdLoginMovimentacao(aux.getCdLoginMovimentacao());
			appRn.editar(t);
			confirmarTransacao();
		} catch (Exception e) {
			cancelarTransacao(e);
			throw e;
		}
	}

	public <T> ProBaseRN getAppRN(T t) {
		Object o = ServiceLocatorRN.getInstance().recuperaObjetoRN(t.getClass());
		ProBaseRN appRN = null;
		if (o instanceof ProBaseRN) {
			appRN = (ProBaseRN) o;
		} else {
			appRN = (ProBaseRN) ServiceLocatorRN.getInstance().recuperaObjetoRN(ProBaseVO.class);
		}
		return appRN;
	}

	protected ProBaseRN getRNPadrao() {
		ProBaseRN appRN = null;
		try {
			appRN = getAppRN(getVOPadrao());
		} catch (InstantiationException e) {
			log.error("Erro ao recuperar classe de negócio padrão", e);
		} catch (IllegalAccessException e) {
			log.error("Erro ao recuperar classe de negócio padrão", e);
		}
		return appRN;
	}

	protected ProBaseVO getVOPadrao() throws InstantiationException, IllegalAccessException {
		final ProBaseVO proBaseVO = ProBaseVO.class.newInstance();
		return proBaseVO;
	}

	protected void iniciarTransacao() {
		try {
			getRNPadrao().getAppDAO(getVOPadrao()).iniciarTransacao();
		} catch (Exception e) {
			throw new RuntimeException("Erro ao  iniciar transação", e);
		}
	}

	protected void confirmarTransacao() throws Exception {

		try {
			getRNPadrao().getAppDAO(getVOPadrao()).confirmarTransacao();
			aposConfirmarTransacao();

		} catch (Exception e) {
			throw new RuntimeException("Erro ao confirmar transação", e);
		}
		log.debug(" Objeto gravado com sucesso!!!");
	}

	protected void aposConfirmarTransacao() {
	}

	protected void limparTransacao() {

		try {
			getRNPadrao().getAppDAO(getVOPadrao()).limparTransacao();
		} catch (Exception e) {
			throw new RuntimeException("Erro ao limpar transação", e);
		}
	}

	protected void cancelarTransacao(Exception throwable) throws Exception {
		try {
			getRNPadrao().getAppDAO(getVOPadrao()).cancelarTransacao(throwable);
		} catch (Exception e) {
			throw new RuntimeException("Erro ao limpar transacao", e);
		}
	}

	@Override
	public void excluir(ProBaseVO t) throws Exception {
		ProBaseRN appRn = getAppRN(t);
		try {
			Context.setAttribute("objetoAtual", t);
			iniciarTransacao();
			appRn.excluir(t);
			confirmarTransacao();
		} catch (Exception e) {
			cancelarTransacao(e);
			throw e;
		}
	}

	@Override
	public <E extends ProBaseVO> E recuperaObjeto(E AppBaseVO) throws Exception {
		ProBaseRN appRn = getAppRN(AppBaseVO);
		try {
			iniciarTransacao();
			AppBaseVO = appRn.recuperaObjeto(AppBaseVO);
			limparTransacao();
		} catch (Exception e) {
			throw e;
		} finally {
			appRn.getAppDAO(AppBaseVO).cancelarTransacao(null);
		}
		return AppBaseVO;
	}

	private Object executaRegraDeNegocio(String nomeDoMetodo, Object[] args, boolean gravacao) throws Exception {
		if (args[0] == null) {
			return null;
		}
		Object retorno = null;
		ProBaseRN appRn = getAppRN(args[0]);
		try {
			Method metodo = null;
			Method[] metodos = appRn.getClass().getMethods();
			for (Method metodoAux : metodos) {
				boolean annotationPresent = metodoAux.isAnnotationPresent(RegraDeNegocio.class);
				if (annotationPresent) {
					RegraDeNegocio regraDeNegocio = metodoAux.getAnnotation(RegraDeNegocio.class);
					if (metodoAux.getName().equals(nomeDoMetodo) || regraDeNegocio.codigo().equals(nomeDoMetodo)) {
						metodo = metodoAux;
						break;
					}
				}
			}
			if (metodo == null) {
				throw new ViolacaoDeRegraEx("Método não exite na classe " + appRn.getClass().getSimpleName() + " ou não esta anotado como regra de negocio.");
			}
			iniciarTransacao();
			retorno = metodo.invoke(appRn, args);
			if (gravacao) {
				confirmarTransacao();
			} else {
				cancelarTransacao(null);
			}
		} catch (InvocationTargetException e) {
			cancelarTransacao(e);
			if (!(e.getTargetException() instanceof ViolacaoDeRegraEx)) {
				log.error("Erro ao executar regra de negócio", e);
			}
			Exception aux = (Exception) e.getTargetException();
			while (aux instanceof InvocationTargetException) {
				aux = (Exception) ((InvocationTargetException) aux).getTargetException();
			}
			throw aux;
		} catch (Exception e) {
			cancelarTransacao(e);
			if (!(e instanceof ViolacaoDeRegraEx)) {
				throw e;
			} else {
				throw ((ViolacaoDeRegraEx) e);
			}
		}
		return retorno;
	}

	@Override
	public <E extends ProBaseVO> Set<E> listarBaseadoNoExemplo(E exemplo, E exemplo2, int primeiroRegistro, int quantidadeDeRegistros, String... projecoes) throws Exception {
		ProBaseRN appRn = getAppRN(exemplo);
		Set aux = null;
		try {
			iniciarTransacao();
			aux = appRn.listarBaseadoNoExemplo(exemplo, exemplo2, primeiroRegistro, quantidadeDeRegistros, projecoes);
			return aux;
		} catch (Exception e) {
			cancelarTransacao(e);
			throw e;
		} finally {
			limparTransacao();
		}
	}

	public <E extends ProBaseVO> Set<E> listar(BuilderExemplo builder) throws Exception {
		return (Set<E>) listarBaseadoNoExemplo(builder.getExemplo(), builder.getExemplo2(), builder.getPrimeiroRegistro(), builder.getQuantidadeDeRegistros(), builder.getProjecoes());
	}

	@Override
	@Deprecated
	public <E extends ProBaseVO> Set<E> listarBaseadoNoExemplo(E exemplo, E exemplo2, E pojo, String... projecoes) throws Exception {
		ProBaseRN appRn = getAppRN(exemplo);
		Set aux = null;
		try {
			iniciarTransacao();
			aux = appRn.listarBaseadoNoExemplo(exemplo, exemplo2, pojo, projecoes);
			return aux;
		} catch (Exception e) {
			cancelarTransacao(e);
			throw e;
		} finally {
			limparTransacao();
		}
	}

	@Override
	public void excluirDetalhe(ProBaseVO t) throws Exception {
		ProBaseRN appRn = getAppRN(t);
		try {
			iniciarTransacao();
			appRn.excluirDetalhe(t);
			confirmarTransacao();
		} catch (Exception e) {
			cancelarTransacao(e);
			throw e;
		}
	}

	@Override
	public void aprovar(ProBaseVO t) throws Exception {
		executaRegraDeNegocio("aprovar", new Object[] { t }, true);
	}

	@Override
	public Object concluir(ProBaseVO t, Map parametros) throws Exception {
		ProBaseRN appRn = getAppRN(t);
		try {
			iniciarTransacao();
			t = (ProBaseVO) appRn.concluir(t, parametros);
			confirmarTransacao();

		} catch (Exception e) {
			cancelarTransacao(e);
			if (e instanceof ViolacaoDeRegraEx) {
				throw e;
			}
			throw e;
		}
		return t;

	}

	@Override
	public <E extends ProBaseVO> Set<E> listar(E vo, String query) throws Exception {
		ProBaseRN appRn = getAppRN(vo);
		Set aux = null;
		try {
			iniciarTransacao();
			aux = appRn.listar(vo, query);
			limparTransacao();
			return aux;
		} catch (Exception e) {
			cancelarTransacao(e);

			throw e;
		}
	}

	@Override
	public <E extends ProBaseVO> E recuperaPorChaveNatural(E appBaseVO, String nomeDaChave) throws Exception {
		ProBaseRN appRn = getAppRN(appBaseVO);
		try {
			iniciarTransacao();
			appBaseVO = appRn.recuperaObjetoPorChaveNatural(appBaseVO, nomeDaChave);
			limparTransacao();
		} catch (Exception e) {
			throw e;
		}
		return appBaseVO;
	}

	@Override
	public Object executar(ProBaseVO t, String evento) throws Exception {
		boolean podeConfirmarTransacao = getConfirmarTransacao();
		return executaRegraDeNegocio(evento, new Object[] { t }, podeConfirmarTransacao);
	}

	protected boolean getConfirmarTransacao() {
		return false;
	}

	@Override
	public void validarCampo(ProBaseVO t, String campo) throws Exception {
		executaRegraDeNegocio(campo, new Object[] { t }, false);
	}

	@Override
	public Object recuperar(ProBaseVO t, String evento) throws Exception {
		return executaRegraDeNegocio(evento, new Object[] { t }, false);
	}

	@Override
	public String recuperaObjetoRest(ProBaseVO obj, String[] exclusaoJson) throws Exception {
		ProBaseRN appRn = getAppRN(obj);
		String result = "";
		try {
			iniciarTransacao();
			result = appRn.recuperaObjetoRest(obj, exclusaoJson);
			limparTransacao();
		} catch (Exception e) {
			throw e;
		} finally {
			appRn.getAppDAO(obj).cancelarTransacao(null);
		}
		return result;
	}
}