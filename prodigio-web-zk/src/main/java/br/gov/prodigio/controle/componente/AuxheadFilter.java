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
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zul.Auxhead;
import org.zkoss.zul.Button;
import org.zkoss.zul.Window;

import br.gov.prodigio.controle.ProCtr;

/**
 * 
 * Classe responsável pelo filtro automático no cabeçalho de um listboxdet.
 * 
 * Trabalha em conjunto com a classe {@link AuxheaderFilter} 
 * 
 * 
 * 
 * 
 * @author p057693
 *
 */
public class AuxheadFilter extends Auxhead implements AfterCompose {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6115661358179894223L;
	protected String labelButtonSearch;
	private ListboxDet detailListBox;
	protected boolean buscaAutomatica = true; 

	@Override
	public void afterCompose() {
		final Window window = ProCtr.findWindow(this);
		Component lastChild = getLastChild();
				
		if (getParent() instanceof ListboxDet) {
			setDetailListBox((ListboxDet) getParent());
		}
		
		if (!isBuscaAutomatica()) {
			Button botaoFiltrar = new Button();
			if(labelButtonSearch!=null){			
				botaoFiltrar.setLabel(getLabelButtonSearch());
			}else{
				botaoFiltrar.setLabel("Filtrar");
			}
			botaoFiltrar.addEventListener("onClick", new EventListener<Event>() {
				public void onEvent(Event event) throws Exception {
					ProCtr ctr = (ProCtr) window.getAttribute(window.getId() + "$" + "composer");
					ctr.getBinder().loadComponent(getParent());
				}
			});
			
			botaoFiltrar.setSclass("btn btn-default btn-search");
			botaoFiltrar.setStyle("margin: 4px 8px");
			
			if(lastChild instanceof AuxheaderFilter){			
				((AuxheaderFilter)lastChild).getLayout().appendChild(botaoFiltrar);
			}else{			
				lastChild.appendChild(botaoFiltrar);
			}
		}

	}

	public String getLabelButtonSearch() {
		return labelButtonSearch;
	}

	public void setLabelButtonSearch(String labelButtonSearch) {
		this.labelButtonSearch = labelButtonSearch;
	}

	public ListboxDet getDetailListBox() {
		return detailListBox;
	}

	public void setDetailListBox(ListboxDet detailListBox) {
		this.detailListBox = detailListBox;
	}

	public boolean isBuscaAutomatica() {
		return buscaAutomatica;
	}

	public void setBuscaAutomatica(boolean buscaAutomatica) {
		this.buscaAutomatica = buscaAutomatica;
	}
	
}
