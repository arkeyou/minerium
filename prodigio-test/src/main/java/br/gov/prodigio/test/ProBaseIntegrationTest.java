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

import org.jboss.shrinkwrap.api.spec.WebArchive;

import br.gov.prodigio.comuns.utils.Context;
import br.gov.prodigio.entidades.ProVO;
import br.gov.prodigio.test.util.WarGenerator;

public abstract class ProBaseIntegrationTest {

	public void registrarAuditoria() {
		final String ATRIBUTO_USUARIO = "usuarioLogado";
		final String CPF_MOVIMENTAVAO_TEST = "ARQUILLIAN";

		if (Context.getAttribute(ATRIBUTO_USUARIO) == null) {
			ProVO appVO = new ProVO();
			appVO.setCdLoginMovimentacao(CPF_MOVIMENTAVAO_TEST);
			appVO.setIpMovimentacao(getIpMovimentacaoAuditoria());
			Context.setAttribute(ATRIBUTO_USUARIO, new ProVO());
		}
	}

	public static WebArchive instanciarAplicacao(String settingsPath, String prefixoAplicacao, String extensaoAplicacao, String[] pacotes) {
		WarGenerator warGenerator = new WarGenerator(settingsPath, prefixoAplicacao, extensaoAplicacao);
		return warGenerator.createWith(pacotes);
	}

	abstract public String getIpMovimentacaoAuditoria();
}
