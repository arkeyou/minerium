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
package br.gov.prodigio.visao.helper;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Window;

public class ProTabularHelperView extends ProHelperView {
	@Override
	public void ajustaBotoesParaTelaSelecao(Window windowatual) {
		super.ajustaBotoesParaTelaSelecao(windowatual);
		Component salvar = windowatual.getFellowIfAny("salvar");
		Component excluir = windowatual.getFellowIfAny("excluir");
		salvar.setVisible(false);
		excluir.setVisible(false);
	}

	@Override
	public void ajustaLayoutParaManutencao(Window janela) {
	}

	@Override
	protected boolean temLogicaDeManutencao(Window tela) {
		return true;
	}
}
