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
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.gov.prodigio.comuns.IProBaseDAO;
import br.gov.prodigio.comuns.anotacoes.RegraDeNegocio;
import br.gov.prodigio.comuns.anotacoes.enumeracao.MomentoDeExecucao;
import br.gov.prodigio.comuns.constantes.Constantes;
import br.gov.prodigio.comuns.excecoes.ViolacaoDeRegraEx;
import br.gov.prodigio.comuns.utils.Reflexao;
import br.gov.prodigio.comuns.utils.ServiceLocatorDAO;
import br.gov.prodigio.comuns.utils.ServiceLocatorRN;
import br.gov.prodigio.entidades.ProBaseVO;
import br.gov.prodigio.entidades.ProVO;
import br.gov.prodigio.persistencia.ProBaseDAO;

/**
 * Classe responsável por realizar a invocação de regras de negócio das entidades de negócio. As Classes filhas da ProBaseRN deverão ter regras de negócio que serão invocados.
 */
public class ProBaseRN {
	protected List<Method> regrasDeNegocioAntes = null;
	protected List<Method> regrasDeNegocioApos = null;
	private static final Logger log = LoggerFactory.getLogger(ProBaseRN.class);
	private final ServiceLocatorRN serviceLocator = ServiceLocatorRN.getInstance();
	private IProBaseDAO appBaseDAO;

	public ProBaseRN() {
		try {
			appBaseDAO = (IProBaseDAO) Class.forName(ProBaseDAO.class.getName()).newInstance();
			regrasDeNegocioAntes = carregaRegrasDeNegocio(MomentoDeExecucao.ANTES, regrasDeNegocioAntes, this.getClass());
			regrasDeNegocioApos = carregaRegrasDeNegocio(MomentoDeExecucao.APOS, regrasDeNegocioApos, this.getClass());
		} catch (InstantiationException e) {
			log.error("Erro ao criar uma nova instância para o ProBaseDAO", e);
		} catch (IllegalAccessException e) {
			log.error("Erro ao criar uma nova instância para o ProBaseDAO", e);
		} catch (ClassNotFoundException e) {
			log.error("Erro ao criar uma nova instância para o ProBaseDAO", e);
		}
	}

	public void setAppBaseDAO(IProBaseDAO appBaseDAO) {
		this.appBaseDAO = appBaseDAO;
	}

	/**
	 * Método responsável por invocar regras de negócio antes e após a ação de gravar.
	 * 
	 * @param appBaseVO
	 *            Entidade de negócio que será gravada.
	 * @return Identificador da entidade que foi gravada.
	 */
	public Object gravar(ProBaseVO appBaseVO) throws Exception {
		appBaseVO = antesGravar(appBaseVO);
		Long id = (Long) getAppDAO(appBaseVO).gravar(appBaseVO);
		appBaseVO.setId(id);
		appBaseVO = aposGravar(appBaseVO);
		return id;
	}

	/**
	 * Método responsável por invocar regras de negócio antes e após a ação de editar.
	 * 
	 * @param appBaseVO
	 *            Entidade de negócio que será editada.
	 */
	public void editar(ProBaseVO appBaseVO) throws Exception {
		appBaseVO = antesEditar(appBaseVO);
		getAppDAO(appBaseVO).gravar(appBaseVO);
		appBaseVO = aposEditar(appBaseVO);
	}

	/**
	 * Método responsável por invocar regras de negócio antes e após a ação de excluir detalhe.
	 * 
	 * @param appBaseVO
	 *            Entidade de negócio que será excluída.
	 */
	public void excluirDetalhe(ProBaseVO appBaseVO) throws Exception {
		appBaseVO = antesExcluirDetalhe(appBaseVO);
		getAppDAO(appBaseVO).gravar(appBaseVO);
		appBaseVO = aposExcluirDetalhe(appBaseVO);
	}

	/**
	 * Método responsável por invocar regras de negócio antes e após a ação de aprovar.
	 * 
	 * @param appBaseVO
	 *            Entidade de negócio que será aprovada.
	 */
	public void aprovar(ProBaseVO appBaseVO) throws Exception {
		/*
		 * appBaseVO = antesAprovar(appBaseVO); appBaseVO.setSituacao(ProBaseVO.SITUACAO_DO_REGISTRO.APROVADO); getAppDAO(appBaseVO).gravar(appBaseVO); appBaseVO = aposAprovar(appBaseVO);
		 */
	}

	/**
	 * Método responsável por invocar regras de negócio antes e após a ação de concluir.
	 * 
	 * @param appBaseVO
	 *            Entidade de negócio que será concluída.
	 * @param parametros
	 *            Parâmetro enviados para a conclusão da entidade.
	 * @return Entidade de negócio com o status concluído.
	 */
	public Object concluir(ProBaseVO appBaseVO, Map parametros) throws Exception {
		appBaseVO = antesConcluir(appBaseVO);
		/*
		 * if (appBaseVO.getSituacao() != null && appBaseVO.getSituacao().equals( ProBaseVO.SITUACAO_DO_REGISTRO.CONCLUIDO)) { throw new Exception("O registro já esta concluído."); } appBaseVO.setSituacao(ProBaseVO.SITUACAO_DO_REGISTRO.CONCLUIDO);
		 */getAppDAO(appBaseVO).concluir(appBaseVO, parametros);
		appBaseVO = aposConcluir(appBaseVO);
		return appBaseVO;
	}

	/**
	 * Método responsável por invocar o processamento de regras de negócio da entidade passada como parâmetro antes de editar.
	 * 
	 * @param appBaseVO
	 *            Entidade de negócio que será editada.
	 * @return Entidade com as regras de negócio aplicadas.
	 */
	protected <T extends ProBaseVO> T antesEditar(ProBaseVO appBaseVO) throws Exception {
		return processarRegrasDeNegocio(appBaseVO, "editar", MomentoDeExecucao.ANTES, regrasDeNegocioAntes);
	}

	/**
	 * Método responsável por invocar o processamento de regras de negócio da entidade passada como parâmetro após de editar.
	 * 
	 * @param appBaseVO
	 *            Entidade de negócio editada.
	 * @return Entidade com as regras de negócio aplicadas.
	 */
	protected <T extends ProBaseVO> T aposEditar(ProBaseVO appBaseVO) throws Exception {
		return processarRegrasDeNegocio(appBaseVO, "editar", MomentoDeExecucao.APOS, regrasDeNegocioApos);
	}

	/**
	 * Método responsável por invocar o processamento de regras de negócio da entidade passada como parâmetro antes de gravar.
	 * 
	 * @param appBaseVO
	 *            Entidade de negócio que será gravada.
	 * @return Entidade com as regras de negócio aplicadas.
	 */
	protected <T extends ProBaseVO> T antesGravar(ProBaseVO appBaseVO) throws Exception {
		return processarRegrasDeNegocio(appBaseVO, "gravar", MomentoDeExecucao.ANTES, regrasDeNegocioAntes);
	}

	/**
	 * Método responsável por invocar o processamento de regras de negócio da entidade passada como parâmetro após gravar.
	 * 
	 * @param appBaseVO
	 *            Entidade de negócio gravada.
	 * @return Entidade com as regras de negócio aplicadas.
	 */
	protected <T extends ProBaseVO> T aposGravar(ProBaseVO appBaseVO) throws Exception {
		return processarRegrasDeNegocio(appBaseVO, "gravar", MomentoDeExecucao.APOS, regrasDeNegocioApos);
	}

	/**
	 * Método responsável por invocar o processamento de regras de negócio da entidade passada como parâmetro antes de aprovar.
	 * 
	 * @param appBaseVO
	 *            Entidade de negócio que será aprovada.
	 * @return Entidade com as regras de negócio aplicadas.
	 */
	protected <T extends ProBaseVO> T antesAprovar(ProBaseVO appBaseVO) throws Exception {
		return processarRegrasDeNegocio(appBaseVO, "aprovar", MomentoDeExecucao.ANTES, regrasDeNegocioAntes);
	}

	/**
	 * Método responsável por invocar o processamento de regras de negócio da entidade passada como parâmetro após aprovar.
	 * 
	 * @param appBaseVO
	 *            Entidade de negócio aprovada.
	 * @return Entidade com as regras de negócio aplicadas.
	 */
	protected <T extends ProBaseVO> T aposAprovar(ProBaseVO appBaseVO) throws Exception {
		return processarRegrasDeNegocio(appBaseVO, "aprovar", MomentoDeExecucao.APOS, regrasDeNegocioApos);
	}

	/**
	 * Método responsável por invocar o processamento de regras de negócio da entidade passada como parâmetro após concluir.
	 * 
	 * @param appBaseVO
	 *            Entidade de negócio concluída.
	 * @return Entidade com as regras de negócio aplicadas.
	 */
	protected <T extends ProBaseVO> T aposConcluir(ProBaseVO appBaseVO) throws Exception {
		return processarRegrasDeNegocio(appBaseVO, "concluir", MomentoDeExecucao.APOS, regrasDeNegocioApos);
	}

	/**
	 * Método responsável por invocar o processamento de regras de negócio da entidade passada como parâmetro antes de concluir.
	 * 
	 * @param appBaseVO
	 *            Entidade de negócio que será concluída.
	 * @return Entidade com as regras de negócio aplicadas.
	 */
	protected <T extends ProBaseVO> T antesConcluir(ProBaseVO appBaseVO) throws Exception {
		return processarRegrasDeNegocio(appBaseVO, "concluir", MomentoDeExecucao.ANTES, regrasDeNegocioAntes);
	}

	/**
	 * Método responsável por invocar o processamento de regras de negócio da entidade passada como parâmetro antes de excluir detalhe.
	 * 
	 * @param appBaseVO
	 *            Entidade de negócio que terá o detalhe excluído.
	 * @return Entidade com as regras de negócio aplicadas.
	 */
	protected <T extends ProBaseVO> T antesExcluirDetalhe(ProBaseVO appBaseVO) throws Exception {
		return processarRegrasDeNegocio(appBaseVO, "excluirDetalhe", MomentoDeExecucao.ANTES, regrasDeNegocioAntes);
	}

	/**
	 * Método responsável por invocar o processamento de regras de negócio da entidade passada como parâmetro após excluir detalhe.
	 * 
	 * @param appBaseVO
	 *            Entidade de negócio que teve o detalhe excluído.
	 * @return Entidade com as regras de negócio aplicadas.
	 */
	protected <T extends ProBaseVO> T aposExcluirDetalhe(ProBaseVO appBaseVO) throws Exception {
		return processarRegrasDeNegocio(appBaseVO, "excluirDetalhe", MomentoDeExecucao.APOS, regrasDeNegocioApos);
	}

	/**
	 * Método responsável por invocar o processamento de regras de negócio da entidade passada como parâmetro antes de recuperar o objeto.
	 * 
	 * @param appBaseVO
	 *            Entidade de negócio será recuperada.
	 * @return Entidade com as regras de negócio aplicadas.
	 */
	protected <T extends ProBaseVO> T antesRecuperaObjeto(ProBaseVO appBaseVO) throws Exception {
		return processarRegrasDeNegocio(appBaseVO, "recuperaObjeto", MomentoDeExecucao.ANTES, regrasDeNegocioAntes);
	}

	/**
	 * Método responsável por invocar o processamento de regras de negócio da entidade passada como parâmetro depois de recuperar o objeto.
	 * 
	 * @param appBaseVO
	 *            Entidade de negócio recuperada.
	 * @return Entidade com as regras de negócio aplicadas.
	 */
	protected <T extends ProBaseVO> T aposRecuperaObjeto(ProBaseVO appBaseVO) throws Exception {
		return processarRegrasDeNegocio(appBaseVO, "recuperaObjeto", MomentoDeExecucao.APOS, regrasDeNegocioApos);
	}

	/**
	 * Método responsável por invocar o processamento de regras da entidade passada como parâmetro. É responsavel também por verificar se essa entidade possui relacionamentos com outras entidades e invocar o processamento de regras de negócio para as
	 * entidades agregadas.
	 * 
	 * @param appBaseVO
	 *            Entidade de negócio
	 * @param fluxo
	 *            fluxo de persistência que irá atuar o método.
	 * @param momento
	 *            momento no fluxo que o método irá atuar. Exemplo: Antes ou Após.
	 * @return Entidade com as regras de negócio aplicadas.
	 */
	protected <T extends ProBaseVO> T processarRegrasDeNegocio(ProBaseVO appBaseVO, String fluxo, MomentoDeExecucao momento, List<Method> regrasDeNegocio) throws Exception {
		Method methods[] = appBaseVO.getClass().getMethods();
		appBaseVO = processarRegras(appBaseVO, fluxo, momento, regrasDeNegocio);
		for (Method getter : methods) {
			OneToMany oneToMany = Reflexao.findAnnotation(getter, OneToMany.class);
			ManyToOne manyToOne = Reflexao.findAnnotation(getter, ManyToOne.class);
			OneToOne oneToOne = Reflexao.findAnnotation(getter, OneToOne.class);
			CascadeType[] cascadeTypes = new CascadeType[] { CascadeType.ALL };
			if (oneToMany != null && Arrays.equals(oneToMany.cascade(), cascadeTypes)) {
				processaRegrasParaAgregados(appBaseVO, fluxo, momento, getter, regrasDeNegocio);
			} else if (manyToOne != null && Arrays.equals(manyToOne.cascade(), cascadeTypes)) {
				processaRegrasParaAgregados(appBaseVO, fluxo, momento, getter, regrasDeNegocio);
			} else if (oneToOne != null && Arrays.equals(oneToOne.cascade(), cascadeTypes)) {
				Object ob = (ProBaseVO) getter.invoke(appBaseVO);
				if (ob instanceof ProVO) {
					ProVO pai = (ProVO) appBaseVO;
					ProVO baseVO = (ProVO) ob;
					baseVO.setCdLoginMovimentacao(pai.getCdLoginMovimentacao());
					baseVO.setStatusDoRegistro(pai.getStatusDoRegistro());
					baseVO.setDsSituacao(pai.getDsSituacao());
					baseVO.setIpMovimentacao(pai.getIpMovimentacao());
					baseVO.setTsMovimentacao(pai.getTsMovimentacao());
				}
				processaRegrasParaAgregados(appBaseVO, fluxo, momento, getter, regrasDeNegocio);
			}
		}
		return (T) appBaseVO;
	}

	/**
	 * Método responsável por invocar o processamento de regras de negócio para entidades de negocio agregadas.
	 * 
	 * @param appBaseVO
	 *            Entidade de negócio principal.
	 * @param fluxo
	 *            Fluxo de persistência que a regra será processada.
	 * @param momento
	 *            Momento no fluxo que o método de regra de negócio irá atuar. Exemplo: Antes ou Após.
	 * @param getter
	 *            Método que possui a regra de negócio da entidade agregada.
	 * @param regrasDeNegocio
	 */
	private void processaRegrasParaAgregados(ProBaseVO appBaseVO, String fluxo, MomentoDeExecucao momento, Method getter, List<Method> regrasDeNegocio) throws IllegalAccessException, InvocationTargetException, Exception {
		Object agregado = Reflexao.recuperaValorDeMetodoGet(getter, appBaseVO);
		if (agregado instanceof Collection) {
			Collection coll = (Collection) agregado;
			for (Object object : coll) {
				ProBaseVO proBaseVODetalhe = (ProBaseVO) object;

				if (incluirAuditoriaNoDetalhe(fluxo)) {
					atribuiValoresDeAuditoriaNosObjetosFilhos(appBaseVO, proBaseVODetalhe);
				}

				ProBaseRN rnAgragado = getAppRN(proBaseVODetalhe);
				if (MomentoDeExecucao.ANTES.equals(momento))
					rnAgragado.processarRegras(proBaseVODetalhe, fluxo, momento, rnAgragado.getRegrasDeNegocioAntes());
				else
					rnAgragado.processarRegras(proBaseVODetalhe, fluxo, momento, rnAgragado.getRegrasDeNegocioApos());
			}
		} else if (agregado instanceof ProBaseVO) {
			if (getter.isAnnotationPresent(OneToOne.class)) {
				OneToOne oneToOne = getter.getAnnotation(OneToOne.class);
				if (Arrays.equals(oneToOne.cascade(), new CascadeType[] { CascadeType.ALL })) {
					final ProBaseVO agregado2 = (ProBaseVO) agregado;
					atribuiValoresDeAuditoriaNosObjetosFilhos(appBaseVO, agregado2);
					ProBaseRN rnAgragado = getAppRN(agregado2);
					if (MomentoDeExecucao.ANTES.equals(momento))
						rnAgragado.processarRegrasDeNegocio(agregado2, fluxo, momento, rnAgragado.getRegrasDeNegocioAntes());
					else
						rnAgragado.processarRegrasDeNegocio(agregado2, fluxo, momento, rnAgragado.getRegrasDeNegocioApos());
				}
			}
		}
	}

	protected boolean incluirAuditoriaNoDetalhe(String fluxo) {
		return fluxo.equals(Constantes.FLUXO.GRAVAR) || fluxo.equals(Constantes.FLUXO.CONCLUIR);
	}

	/**
	 * Método responsável por definir auditoria da entidade agregada com os dados de auditoria da entidade principal.
	 * 
	 * @param appBaseVO
	 *            Entidade de negócio principal.
	 * @param proBaseVODetalhe
	 *            Entidadde de negocio agregada.
	 */
	protected void atribuiValoresDeAuditoriaNosObjetosFilhos(ProBaseVO appBaseVO, ProBaseVO proBaseVODetalhe) {
		proBaseVODetalhe.setCdLoginMovimentacao(appBaseVO.getCdLoginMovimentacao());
		proBaseVODetalhe.setTsMovimentacao(appBaseVO.getTsMovimentacao());
		proBaseVODetalhe.setTpOperacao(appBaseVO.getTpOperacao());
		proBaseVODetalhe.setIpMovimentacao(appBaseVO.getIpMovimentacao());
	}

	/**
	 * Método responsável por invocar métodos com anotações de regra de negócio na classe RN da entidade de negócio passada como parâmetro.
	 * 
	 * @param appBaseVO
	 *            Entidade de negócio.
	 * @param fluxo
	 *            Fluxo que será executada a regra. Exemplo: gravar, editar.
	 * @param momento
	 *            Momento em que será executada a regra de negócio. Exemplo: Antes ou Após.
	 * @return Entidade de negócio com as regras de negócio executadas.
	 **/
	protected <T extends ProBaseVO> T processarRegras(ProBaseVO appBaseVO, String fluxo, MomentoDeExecucao momento, List<Method> regrasDeNegocio) throws Exception {
		Object classeDeNegocio = ServiceLocatorRN.getInstance().recuperaObjetoRN(appBaseVO.getClass());

		ProBaseVO aux = appBaseVO;
		for (Method method : regrasDeNegocio) {
			RegraDeNegocio regraDeNegocio = method.getAnnotation(RegraDeNegocio.class);
			try {
				if (regraDeNegocio.fluxo().equals(fluxo)) {
					if (!regraDeNegocio.intrusiva()) {
						method.invoke(classeDeNegocio, aux);
					} else {
						aux = (ProBaseVO) method.invoke(this, aux);
					}
				}
			} catch (Exception e) {
				if (e instanceof InvocationTargetException) {
					final InvocationTargetException invocationTargetException = (InvocationTargetException) e;
					final Throwable targetException = invocationTargetException.getTargetException();
					if (targetException instanceof ViolacaoDeRegraEx) {
						throw (ViolacaoDeRegraEx) targetException;
					}
					throw new Exception("Erro no fluxo:  " + fluxo + ", no momento da execução da regra: " + method.getName() + ". Autor: " + regraDeNegocio.autor() + ". Causa do erro:" + targetException.getMessage(), e);
				}
				throw new Exception("Erro no fluxo: " + fluxo + ", no momento da execução da regra: " + method.getName() + ". Autor: " + regraDeNegocio.autor() + ". Causa do erro:" + e.getMessage(), e);
			}
		}
		return (T) aux;
	}

	/**
	 * Método responsável por invocar métodos com anotações de regra de negócio na classe RN da entidade de negócio passada como parâmetro.
	 * 
	 * @param appBaseVO
	 *            Entidade de negócio.
	 * @param fluxo
	 *            Fluxo que será executada a regra. Exemplo: gravar, editar.
	 * @param momento
	 *            Momento em que será executada a regra de negócio. Exemplo: Antes ou Após.
	 * @return Entidade de negócio com as regras de negócio executadas.
	 **/
	protected <T extends ProBaseVO> T processarRegrasPorCodigoDaRegra(ProBaseVO appBaseVO, String codigo, List<Method> regrasDeNegocio) throws Exception {
		Object classeDeNegocio = ServiceLocatorRN.getInstance().recuperaObjetoRN(appBaseVO.getClass());

		ProBaseVO aux = appBaseVO;
		boolean executou = false;
		for (Method method : regrasDeNegocio) {
			RegraDeNegocio regraDeNegocio = method.getAnnotation(RegraDeNegocio.class);
			try {
				if (regraDeNegocio.codigo().equals(codigo)) {
					if (!regraDeNegocio.intrusiva()) {
						method.invoke(classeDeNegocio, aux);
						executou = true;
					} else {
						aux = (ProBaseVO) method.invoke(this, aux);
						executou = true;
					}
				}
			} catch (Exception e) {
				if (e instanceof InvocationTargetException) {
					final InvocationTargetException invocationTargetException = (InvocationTargetException) e;
					final Throwable targetException = invocationTargetException.getTargetException();
					if (targetException instanceof ViolacaoDeRegraEx) {
						throw (ViolacaoDeRegraEx) targetException;
					}
					throw new Exception("Erro no fluxo:  " + codigo + ", no momento da execução da regra: " + method.getName() + ". Autor: " + regraDeNegocio.autor() + ". Causa do erro:" + targetException.getMessage(), e);
				}
				throw new Exception("Erro no fluxo: " + codigo + ", no momento da execução da regra: " + method.getName() + ". Autor: " + regraDeNegocio.autor() + ". Causa do erro:" + e.getMessage(), e);
			}
		}
		if (!executou) {
			throw new ViolacaoDeRegraEx("A regra de código: " + codigo + " ainda não foi implementada.");
		}
		return (T) aux;
	}

	private List<Method> carregaRegrasDeNegocio(MomentoDeExecucao momento, List<Method> regrasDeNegocio, Class classeDeNegocio) {
		Method[] methods;
		methods = classeDeNegocio.getMethods();

		if (regrasDeNegocio == null) {
			regrasDeNegocio = new LinkedList<Method>();
			for (Method method : methods) {
				RegraDeNegocio regraDeNegocio = method.getAnnotation(RegraDeNegocio.class);
				if (regraDeNegocio != null && regraDeNegocio.marcadaParaExecutar()) {
					MomentoDeExecucao momentoDeExecucao = regraDeNegocio.momentoDeExecucao();
					if (momentoDeExecucao.equals(momento)) {
						regrasDeNegocio.add(method);
					}
				}
			}

			Collections.sort(regrasDeNegocio, new Comparator<Method>() {
				@Override
				public int compare(Method m1, Method m2) {
					RegraDeNegocio regraDeNegocio1 = m1.getAnnotation(RegraDeNegocio.class);
					RegraDeNegocio regraDeNegocio2 = m2.getAnnotation(RegraDeNegocio.class);
					Integer i1 = new Integer(regraDeNegocio1.ordem());
					Integer i2 = new Integer(regraDeNegocio2.ordem());
					return i1.compareTo(i2);
				}
			});
		}
		return regrasDeNegocio;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.IAppRN#persiste(T)
	 */
	/**
	 * Método responsável por invocar o método de excluir do DAO.
	 * 
	 * @param AppBaseVO
	 *            Entidade de negocio que será excluída.
	 **/
	void excluir(ProBaseVO appBaseVO) throws Exception {
		appBaseVO = antesExcluir(appBaseVO);
		getAppDAO(appBaseVO).excluir(appBaseVO);
		appBaseVO = aposExcluir(appBaseVO);
	}

	/**
	 * Método responsável por invocar o processamento de regras de negócio da entidade passada como parâmetro antes de excluir.
	 * 
	 * @param appBaseVO
	 *            Entidade de negócio que será excluída.
	 * @return Entidade com as regras de negócio aplicadas.
	 */
	protected ProBaseVO antesExcluir(ProBaseVO appBaseVO) throws Exception {
		return processarRegrasDeNegocio(appBaseVO, "excluir", MomentoDeExecucao.ANTES, regrasDeNegocioAntes);
	}

	/**
	 * Método responsável por invocar o processamento de regras de negócio da entidade passada como parâmetro depois de excluir.
	 * 
	 * @param appBaseVO
	 *            Entidade de negócio que foi excluída.
	 * @return Entidade com as regras de negócio aplicadas.
	 */
	protected ProBaseVO aposExcluir(ProBaseVO appBaseVO) throws Exception {
		return processarRegrasDeNegocio(appBaseVO, "excluir", MomentoDeExecucao.APOS, regrasDeNegocioApos);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.IAppRN#getLista(T)
	 */
	/**
	 * Método responsável por invocar o método listar do DAO.
	 * 
	 * @param t
	 *            Entidade de negócio
	 * @return Listagem baseada no objeto passado como parâmetro.
	 **/
	public Set<? extends ProBaseVO> listar(ProBaseVO t) throws Exception {
		return getAppDAO(t).listar(t);
	}

	/**
	 * Método responsável por invocar o método listar do DAO de acordo com uma Named Query passada como parâmetro.
	 * 
	 * @param t
	 *            Entidade de negócio
	 * @param namedQuery
	 *            Named Query que será utilizada pelo método para realizar a listagem.
	 * @return Listagem baseada no objeto passado como parâmetro.
	 **/
	public Set<? extends ProBaseVO> listar(ProBaseVO t, String namedQuery) throws Exception {
		return getAppDAO(t).listar(t, namedQuery);
	}

	/**
	 * Método responsável por recuperar a classe DAO da entidade de negócio passada como parâmetro.
	 * 
	 * @param appBaseVO
	 *            Entidade de negócio passada para recuperação da classe DAO.
	 * @return DAO relativo a classe da entidade passada como parâmetro.
	 **/
	public IProBaseDAO getAppDAO(ProBaseVO appBaseVO) {
		IProBaseDAO appDAO = null;
		if (appBaseVO == null) {
			appBaseVO = new ProBaseVO();
		}
		appDAO = (IProBaseDAO) ServiceLocatorDAO.getInstance().recuperaObjetoDAO(appBaseVO.getClass());
		return appDAO;
	}

	/**
	 * Método responsável por recuperar a classe RN da entidade de negócio passada como parâmetro.
	 * 
	 * @param AppBaseVO
	 *            Entidade de negócio passada para recuperação da classe RN.
	 * @return RN relativa a classe da entidade passada como parâmetro.
	 **/
	public ProBaseRN getAppRN(ProBaseVO AppBaseVO) {
		ProBaseRN appRN = (ProBaseRN) getServiceLocator().recuperaObjetoRN(AppBaseVO.getClass());
		return appRN;
	}

	/**
	 * Método responsável por invocar o método para recuperar a entidade de negócio passada como parâmetro. Além disso, invocar o processamento de regras de negócio após a recuperação da entidade de negócio.
	 * 
	 * @param appBaseVO
	 *            Entidade de negócio a ser recuperada.
	 * @return Entidade de negócio com os atributos recuperados.
	 **/
	public <E extends ProBaseVO> E recuperaObjeto(E appBaseVO) throws Exception {
		appBaseVO = getAppDAO(appBaseVO).recuperaObjeto(appBaseVO);
		if (appBaseVO != null) {
			appBaseVO = aposRecuperaObjeto(appBaseVO);
		}
		return appBaseVO;
	}

	public String recuperaObjetoRest(ProBaseVO obj, String[] exclusaoJson) throws Exception {
		String result = getAppDAO(obj).recuperaObjetoRest(obj, exclusaoJson);
		return result;
	}

	public <E extends ProBaseVO> E recuperaObjetoLazy(E appBaseVO) throws Exception {
		appBaseVO = getAppDAO(appBaseVO).recuperaObjetoLazy(appBaseVO);
		if (appBaseVO != null) {
			appBaseVO = aposRecuperaObjeto(appBaseVO);
		}
		return appBaseVO;
	}

	/**
	 * Método responsável por invocar o método para recuperar a entidade de negócio passada como parâmetro pela chave natural.
	 * 
	 * @param AppBaseVO
	 *            Entidade de negócio a ser recuperada.
	 * @param nomeDaChave
	 *            Nome da chave natural da entidade de negócio.
	 * @return Entidade de negócio com os atributos recuperados.
	 **/
	public <E extends ProBaseVO> E recuperaObjetoPorChaveNatural(E appBaseVO, String nomeDaChave) throws Exception {
		return getAppDAO(appBaseVO).recuperaObjetoPorChaveNatural(appBaseVO, nomeDaChave);
	}

	/**
	 * Método responsável por invocar o método do DAO para realizar a listagem baseado no exemplo que é passado como parâmetro.
	 * 
	 * @param example
	 *            Entidade de exemplo para o método de listagem.
	 * @param example2
	 *            Entidade
	 * @param primeiroRegistro
	 *            Posição que irá começar a listagem.
	 * @param quantidadeDeRegistros
	 *            Quantidade de registros que a listagem ira trazer.
	 * @param projecoes
	 *            Atributos da entidade que listagem irá recuperar.
	 * @return Listagem baseada nos parâmetros passados para o método.
	 * @throws Exception
	 **/
	public Set<? extends ProBaseVO> listarBaseadoNoExemplo(ProBaseVO example, ProBaseVO example2, int primeiroRegistro, int quantidadeDeRegistros, String... projecoes) {
		Set<ProBaseVO> listarBaseadoNoExemplo = new HashSet<ProBaseVO>();
		try {
			antesListarBaseadoNoExemplo(example, example2, null, primeiroRegistro, quantidadeDeRegistros, projecoes);
			listarBaseadoNoExemplo = getAppDAO(example).listarBaseadoNoExemplo(example, example2, primeiroRegistro, quantidadeDeRegistros, projecoes);
			aposListarBaseadoNoExemplo(listarBaseadoNoExemplo, example, example2, null, primeiroRegistro, quantidadeDeRegistros, projecoes);
			return listarBaseadoNoExemplo;
		} catch (ViolacaoDeRegraEx e) {
			throw e;
		} catch (Exception e) {
			log.error("Erro na execução do método listarBaseadoNoExemplo", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Método responsável por invocar o método do DAO para realizar a listagem baseado no exemplo que é passado como parâmetro.
	 * 
	 * @param example
	 *            Entidade de exemplo para o método de listagem.
	 * @param example2
	 * @param pojo
	 * @param projecoes
	 *            Atributos da entidade que listagem irá recuperar.
	 * @return Listagem baseada nos parâmetros passados para o método.
	 **/
	public Set<? extends ProBaseVO> listarBaseadoNoExemplo(ProBaseVO example, ProBaseVO example2, ProBaseVO pojo, String... projecoes) throws Exception {
		return getAppDAO(example).listarBaseadoNoExemplo(example, example2, pojo, projecoes);
	}

	/**
	 * Método responsável por retornar uma instancia de ServiceLocatorRN
	 * 
	 * @return Instancia de ServiceLocatorRN
	 **/
	public ServiceLocatorRN getServiceLocator() {
		return serviceLocator;
	}

	public List<Method> getRegrasDeNegocioAntes() {
		return regrasDeNegocioAntes;
	}

	public List<Method> getRegrasDeNegocioApos() {
		return regrasDeNegocioApos;
	}

	/**
	 * Método responsável por invocar o processamento de regras de negócio da entidade passada como parâmetro antes de listar baseado no exemplo.
	 * 
	 * @param appBaseVO
	 *            Entidade de negócio que será editada.
	 * @return Entidade com as regras de negócio aplicadas.
	 */
	protected <T extends ProBaseVO> T antesListarBaseadoNoExemplo(ProBaseVO example, ProBaseVO example2, ProBaseVO pojo, int primeiroRegistro, int quantidadeDeRegistros, String... projecoes) throws Exception {
		example.addAtributo("example2", example2);
		example.addAtributo("pojo", pojo);
		example.addAtributo("primeiroRegistro", primeiroRegistro);
		example.addAtributo("quantidadeDeRegistros", quantidadeDeRegistros);
		example.addAtributo("projecoes", projecoes);
		return processarRegrasDeNegocio(example, "listarBaseadoNoExemplo", MomentoDeExecucao.ANTES, regrasDeNegocioAntes);
	}

	/**
	 * Método responsável por invocar o processamento de regras de negócio da entidade passada como parâmetro após listar baseado no exemplo.
	 * 
	 * @param appBaseVO
	 *            Entidade de negócio editada.
	 * @return Entidade com as regras de negócio aplicadas.
	 */
	protected <T extends ProBaseVO> T aposListarBaseadoNoExemplo(Set<ProBaseVO> listarBaseadoNoExemplo, ProBaseVO example, ProBaseVO example2, ProBaseVO pojo, int primeiroRegistro, int quantidadeDeRegistros, String... projecoes) throws Exception {
		example.addAtributo("retornoListarBaseadoNoExemplo", listarBaseadoNoExemplo);
		example.addAtributo("example2", example2);
		example.addAtributo("pojo", pojo);
		example.addAtributo("primeiroRegistro", primeiroRegistro);
		example.addAtributo("quantidadeDeRegistros", quantidadeDeRegistros);
		example.addAtributo("projecoes", projecoes);
		return processarRegrasDeNegocio(example, "listarBaseadoNoExemplo", MomentoDeExecucao.APOS, regrasDeNegocioApos);
	}

}