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

import java.io.Serializable;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.SerializableEventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

import br.gov.prodigio.comuns.utils.Reflexao;

@SuppressWarnings("rawtypes")
public class AppListRender implements ListitemRenderer, Serializable {
	@SuppressWarnings({ "unchecked" })
	@Override
	public void render(Listitem listitem, Object o, int index) throws Exception {

		listitem.addEventListener(Events.ON_CLICK, new SerializableEventListener() {
			@Override
			public void onEvent(Event e) throws Exception {
				Bandboxbind bandboxbind = (Bandboxbind) e.getTarget().getParent().getParent().getParent();
				bandboxbind.close();
				bandboxbind.selecionarItem(e);

			}
		});
		listitem.addEventListener(Events.ON_OK, new SerializableEventListener() {
			@Override
			public void onEvent(Event e) throws Exception {
				Bandboxbind bandboxbind = (Bandboxbind) e.getTarget().getParent().getParent().getParent();
				bandboxbind.close();
				bandboxbind.selecionarItem(e);

			}
		});

		Bandboxbind bandboxbind = (Bandboxbind) listitem.getParent().getParent().getParent();
		if (bandboxbind.getLabelValorList() != null && !bandboxbind.getLabelValorList().equals("")) {
			String[] labelValores = bandboxbind.getLabelValorList().split(";");
			for (int i = 0; i < labelValores.length; i++) {
				String[] labelValor = labelValores[i].split(":");
				String valor = labelValor[1];
				Object object = null;
				if (valor.equals("toString")) {
					object = o;
				} else {
					object = Reflexao.recuperaValorDaPropriedade(valor, o);
				}
				if (object != null) {
					Listcell listcell = new Listcell();
					if (valor.equals("cpf")) {
						listcell.setLabel(new CpfBindConverter().coerceToUi(object.toString(), listcell).toString());
						listcell.setStyle("text-align:center");
					} else {
						listcell.setLabel(object.toString());
						listcell.setStyle("text-align:left");
					}
					listitem.appendChild(listcell);
					listitem.setValue(o);
				}
			}
		} else {
			Object object = null;
			object = o;
			if (object != null) {
				Listcell listcell = new Listcell();
				listcell.setLabel(object.toString());
				listcell.setStyle("text-align:left");
				listitem.appendChild(listcell);
				listitem.setValue(object);
			}
		}
	}
}
