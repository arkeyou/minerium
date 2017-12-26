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
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Window;

import br.gov.prodigio.controle.ProCtr;

public class ProReportHelperView extends ProHelperView {
	@Override
	public void ajustaBotoesParaTelaSelecao(Window windowatual) {
		super.ajustaBotoesParaTelaSelecao(windowatual);
		Component relatorio = windowatual.getFellowIfAny("relatorio");
		relatorio.setVisible(true);
	}

	@Override
	public void configuraTelaDeEntrada(Component comp) throws Exception {
		super.configuraTelaDeEntrada(comp);
	}

	@Override
	public void ajustaLayoutParaManutencao(Window janela) {
	}

	@Override
	protected boolean temLogicaDeManutencao(Window tela) {
		return true;
	}

	@Override
	protected void montaBotoes(Div div, Window tela) {
		final ProCtr controller = getController(tela);
		super.montaBotoes(div, tela);
		div.setStyle("width: 50%;float: right;text-align: right;");
		Button relatorio = new Button();
		relatorio.setSclass("btn btn-default btn-relatorio");
		relatorio.setId("relatorio");
		relatorio.setLabel("Gerar relatório");
		relatorio.setWidth("100px");
		relatorio.setHeight("30px");
		relatorio.setVisible(true);
		relatorio.setParent(div);
		relatorio.addEventListener("onClick", new EventListener() {
			@Override
			public void onEvent(Event e) throws Exception {
				controller.exibirRelatorio();
			}
		});
		div.appendChild(relatorio);
	}
}
