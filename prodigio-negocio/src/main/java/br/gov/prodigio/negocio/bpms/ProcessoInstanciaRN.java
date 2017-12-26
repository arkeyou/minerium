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
package br.gov.prodigio.negocio.bpms;

import br.gov.prodigio.negocio.ProBaseRN;

public class ProcessoInstanciaRN extends ProBaseRN {
/*
	@Override
	public IProBpmsDAO getAppDAO(ProBaseVO appBaseVO) {

		return (IProBpmsDAO) super.getAppDAO(appBaseVO);
	}

	public Long iniciarProcesso(ProcessoInstanciaVO processoInstancia) throws Exception {

		processoInstancia = antesIniciarProcesso(processoInstancia);
		Long id;
		try {
			id = getAppDAO(processoInstancia).iniciarProcesso(processoInstancia);
		} catch (ErroBpmsIniciarProcessoException e) {
			throw new ErroNegocioIniciarProcessoException(e);
		}
		processoInstancia = aposIniciarProcesso(processoInstancia);
		return id;

	}

	protected <T extends ProBaseVO> T antesIniciarProcesso(ProBaseVO appBaseVO) throws ErroNegocioIniciarProcessoException {
		try {
			return processarRegrasDeNegocio(appBaseVO, "iniciarProcesso", MomentoDeExecucao.ANTES);
		} catch (Exception e) {
			throw new ErroNegocioIniciarProcessoException(e);
		}
	}

	protected <T extends ProBaseVO> T aposIniciarProcesso(ProBaseVO appBaseVO) throws ErroNegocioIniciarProcessoException {
		try {
			return processarRegrasDeNegocio(appBaseVO, "iniciarProcesso", MomentoDeExecucao.APOS);
		} catch (Exception e) {
			throw new ErroNegocioIniciarProcessoException(e);
		}
	}*/

}
