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

public class AtividadeInstanciaRN extends ProBaseRN {

	/**
	 * Recuperar a primeira atividade de interacao humana dispoível do processo.
	 * 
	 * O método foi construído com o propósito de recuperar a primeira atividade humana após criar um processo. Porém, o seu uso pode ser feito em outros pontos do processo que se deseja recuperar a atividade humana ativa do processo para um usuário
	 * logado no sistema. Levando em consideração o propósito inicial, o método não deve ser utilizado quando: 1- Antes da primeira atividade humana do processo houver atividades sistêmicas. 2- A primeira atividade humana do processo é
	 * multi-instância (paralelismo).
	 * 
	 * @param processo
	 *            Identificador do processo no BPM.
	 * @return Instancia da atividade de interacao humana do processo informado.
	 * @throws ProdigioBpmsNegocioException
	 */
/*	@SuppressWarnings("unchecked")
	public AtividadeInstanciaVO<ProcessoInstanciaVO> retornarPrimeiraAtividadeHumanaDoProcesso(ProcessoInstanciaVO processo) throws Exception {

		AtividadeInstanciaVO<ProcessoInstanciaVO> atividadeInstanciaVOArg = new AtividadeInstanciaVO<ProcessoInstanciaVO>();
		atividadeInstanciaVOArg.setUsuario(processo.getUsuario());
		atividadeInstanciaVOArg.setProcessoInstanciaVO(processo);

		Set<ProBaseVO> atividades;
		try {
			atividades = (Set<ProBaseVO>) listarBaseadoNoExemplo(atividadeInstanciaVOArg, atividadeInstanciaVOArg, 0, 1, "");
			if (atividades.size() > 0) {
				Iterator<ProBaseVO> i = atividades.iterator();
				atividadeInstanciaVOArg = (AtividadeInstanciaVO<ProcessoInstanciaVO>) i.next();
				atividadeInstanciaVOArg.setUsuario(processo.getUsuario());

				return recuperaObjeto(atividadeInstanciaVOArg);

			} else {
				return null;
			}
		} catch (Exception e) {
			throw e;
		}

	}*/

}
