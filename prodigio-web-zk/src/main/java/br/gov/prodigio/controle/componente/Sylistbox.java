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
package br.gov.prodigio.controle.componente;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Window;

import br.gov.prodigio.controle.ProCtr;

public class Sylistbox extends Listbox implements AfterCompose {

	public static final String LISTA_SELECAO = "listaSelecao";
	private boolean selecionavel = true;

	public String getCrud() {
		return crud;
	}

	public void setCrud(String crud) {
		this.crud = crud;
	}

	private String crud = "";
	/**
	 * 
	 */
	private static final long serialVersionUID = 8062240472154901922L;

	@Override
	public void afterCompose() {
		this.setId(LISTA_SELECAO);
		Window window = ProCtr.findWindow(this);
		window.setAttribute(LISTA_SELECAO, this);
		ProCtr ctr = (ProCtr) window.getAttribute(window.getId() + "$" + "composer");
		if(!getCrud().equals("false")){
			ctr.getProAnnotateDataBinderHelper().adicionaAnotacaoParaLista(this);
		}
	}

	@Override
	public void onChildAdded(Component child) {
		final Window window = ProCtr.findWindow(this);
		final ProCtr ctr = (ProCtr) window.getAttribute(window.getId() + "$" + "composer");
		ctr.getProAnnotateDataBinderHelper().adicionaAnotacaoParaItensDaLista(child);
		super.onChildAdded(child);
	}

	public boolean isSelecionavel() {
		return selecionavel;
	}

	public void setSelecionavel(boolean selecionavel) {
		this.selecionavel = selecionavel;
	}

	@Override
	public void onInitRender() {
		super.onInitRender();
		Window window = ProCtr.findWindow(this);
		ProCtr ctr = (ProCtr) window.getAttribute(window.getId() + "$" + "composer");
		ctr.getBinder().loadComponent(this);
	}
}
