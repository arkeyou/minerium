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

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.gov.prodigio.comuns.IProBaseDAO;
import br.gov.prodigio.comuns.utils.ServiceLocatorRN;
import br.gov.prodigio.entidades.ProBaseVO;
import br.gov.prodigio.entidades.ProVO;
import br.gov.prodigio.entidades.ProVO.SITUACAO_DO_REGISTRO;

public class ProRN extends ProBaseRN {
	public final Logger log = LoggerFactory.getLogger(this.getClass());

	public ProRN() {
		try {
			appBaseDAO = (IProBaseDAO) Class.forName("br.gov.prodigio.persistencia.ProBaseDAO").newInstance();
		} catch (InstantiationException e) {
			log.error("Erro ao criar uma nova instância para o ProBaseDAO", e);
		} catch (IllegalAccessException e) {
			log.error("Erro ao criar uma nova instância para o ProBaseDAO", e);
		} catch (ClassNotFoundException e) {
			log.error("Erro ao criar uma nova instância para o ProBaseDAO", e);
		}
	}

	private ServiceLocatorRN serviceLocator = ServiceLocatorRN.getInstance();

	private IProBaseDAO appBaseDAO = null;

	@Override
	public void setAppBaseDAO(IProBaseDAO appBaseDAO) {
		this.appBaseDAO = appBaseDAO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.IAppRN#persiste(T)
	 */
	@Override
	public Object gravar(ProBaseVO appBaseVO) throws Exception {
		((ProVO) appBaseVO).setDsSituacao(SITUACAO_DO_REGISTRO.EM_EDICAO);
		appBaseVO = antesGravar(appBaseVO);
		Object obj = getAppDAO(appBaseVO).gravar(appBaseVO);
		appBaseVO = aposGravar(appBaseVO);

		return obj;

	}

	@Override
	public void editar(ProBaseVO proBaseVO) throws Exception {
		ProVO proVO = (ProVO) proBaseVO;
		proVO.setDsSituacao(SITUACAO_DO_REGISTRO.EM_EDICAO);
		proVO = antesEditar(proVO);
		getAppDAO(proVO).gravar(proVO);
		proVO = aposEditar(proVO);
	}

	@Override
	public void excluirDetalhe(ProBaseVO appBaseVO) throws Exception {
		appBaseVO = antesExcluirDetalhe(appBaseVO);
		getAppDAO(appBaseVO).gravar(appBaseVO);
		appBaseVO = aposExcluirDetalhe(appBaseVO);
	}

	@Override
	public void aprovar(ProBaseVO appBaseVO) throws Exception {
		appBaseVO = antesAprovar(appBaseVO);
		if (((ProVO) appBaseVO).getDsSituacao() != null && ((ProVO) appBaseVO).getDsSituacao().equals(ProVO.SITUACAO_DO_REGISTRO.APROVADO)) {
			throw new Exception("O registro já esta aprovado.");
		}
		((ProVO) appBaseVO).setDsSituacao(ProVO.SITUACAO_DO_REGISTRO.APROVADO);
		getAppDAO(appBaseVO).gravar(appBaseVO);
		appBaseVO = aposAprovar(appBaseVO);
	}

	@Override
	public Object concluir(ProBaseVO appBaseVO, Map parametros) throws Exception {
		ProVO proVO = (ProVO) appBaseVO;
		proVO = antesConcluir(appBaseVO);
		if (proVO.getDsSituacao() != null && proVO.getDsSituacao().equals(SITUACAO_DO_REGISTRO.CONCLUIDO)) {
			throw new Exception("O registro já esta concluído.");
		}
		proVO.setDsSituacao(SITUACAO_DO_REGISTRO.CONCLUIDO);
		getAppDAO(appBaseVO).concluir(proVO, parametros);
		proVO = aposConcluir(proVO);
		return proVO;
	}

}