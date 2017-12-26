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

import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Window;

import br.gov.prodigio.controle.ProCtr;

public class ListheaderSort extends Listheader implements AfterCompose {

	private boolean inMemory = false;

	@Override
	public void afterCompose() {
		Window window = ProCtr.findWindow(this);
		ProCtr ctr = (ProCtr) window.getAttribute(window.getId() + "$" + "composer");
		ctr.adicionaComparadorParaCampos(this);
	}

	@Override
	public void onSort() {
		super.onSort();
	}

	@Override
	public boolean sort(boolean ascending) {
		if (!inMemory) {// vai ao banco
			Listbox listbox = getListbox();
			ListModel model = listbox.getModel();
			listbox.setModel((ListModel) null);// TODO : estratégia para evitar a paginação em memória feita pelo ZK, verificar como sobrescrever este comportamento de forma mais elegante
			boolean value = super.sort(ascending);
			listbox.setModel(model);// TODO : estratégia para evitar a paginação em memória feita pelo ZK, verificar como sobrescrever este comportamento de forma mais elegante
			Clients.showBusy("Aguarde...");
			Clients.evalJavaScript("zAu.send(new zk.Event(zk.Widget.$('$pesquisar'), 'onClick', {toServer:true}));");
			return value;
		} else {
			return super.sort(ascending);
		}
	}

	public boolean isInMemory() {
		return inMemory;
	}

	public void setInMemory(boolean inMemory) {
		this.inMemory = inMemory;
	}
}
