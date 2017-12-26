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
package br.gov.prodigio.test;

import br.gov.prodigio.comuns.utils.Context;
import br.gov.prodigio.entidades.ProVO;

public class BeforeTestSetUp {
	
	public static void registrarAuditoria()
	{
		final String ATRIBUTO_USUARIO = "usuarioLogado";
		final String CPF_MOVIMENTAVAO_TEST = "71515034151";
		
		if(Context.getAttribute(ATRIBUTO_USUARIO) != null){
			ProVO appVO = new ProVO();
			appVO.setCdLoginMovimentacao(CPF_MOVIMENTAVAO_TEST);
			Context.setAttribute(ATRIBUTO_USUARIO, new ProVO());
		}
	}

}
