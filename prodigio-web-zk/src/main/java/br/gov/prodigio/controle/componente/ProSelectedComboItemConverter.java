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

import java.util.HashSet;
import java.util.Set;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zkplus.databind.BindingListModel;
import org.zkoss.zkplus.databind.SelectedComboitemConverter;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.SimpleListModel;

import br.gov.prodigio.entidades.ProBaseVO;
import br.gov.prodigio.visao.helper.ProSimpleListModel;

public class ProSelectedComboItemConverter extends SelectedComboitemConverter {
	@Override
	public Object coerceToBean(Object val, Component comp) {
		Combobox cbbox = (Combobox) comp;
		if (val != null) {
			ListModel model = cbbox.getModel();
			if (model instanceof ProSimpleListModel)
				return ((Comboitem) val).getValue();
			return model != null ? model.getElementAt(cbbox.getItems().indexOf(val)) : ((Comboitem) val).getValue();
		}
		return null;
	}

	/**
	 * @since 3.0.2
	 */
	public Object coerceToUi(Object val, Component comp) {
		final Combobox cbbox = (Combobox) comp;
		if (val != null) {
			final ListModel xmodel = cbbox.getModel();
			if (xmodel instanceof BindingListModel) {
				final BindingListModel model = (BindingListModel) xmodel;
				int index = model.indexOf(val);
				if (index >= 0) {
					final Comboitem item = (Comboitem) cbbox.getItemAtIndex(index);
					int selIndex = 0;
					try {
						selIndex = cbbox.getSelectedIndex();
					} catch (WrongValueException w) {
						selIndex = 0;
					}
					if (item != null && selIndex != index) { // bug 1647817,
						Set items = new HashSet();
						items.add(item);
						Events.postEvent(new SelectEvent("onSelect", cbbox, items, item));
					}
					return item;
				}
			} else if (xmodel instanceof SimpleListModel && val instanceof ProBaseVO) {
				cbbox.setValue(val.toString());
				return null;
			} else {
				throw new UiException("model of the databind combobox " + cbbox + " must be an instanceof of org.zkoss.zkplus.databind.BindingListModel." + xmodel);
			}
		}
		return null;
	}

}
