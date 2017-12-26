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
/**
 * 
 */
package br.gov.prodigio.controle.componente;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.zkoss.lang.Library;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zkplus.databind.BindingListModel;
import org.zkoss.zkplus.databind.TypeConverter;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;

/**
 * @author p051679
 *
 */
public class SylistboxItemConverter implements TypeConverter, java.io.Serializable {
	private static final long serialVersionUID = 200808191439L;
	
	public Object coerceToUi(Object val, Component comp) { //load
		Listbox lbx = (Listbox) comp;
	  	if (val != null) {
	  		final ListModel xmodel = lbx.getModel();
	  		if (xmodel instanceof BindingListModel) {
	  			final BindingListModel model = (BindingListModel) xmodel;
	  			int index = model.indexOf(val);
	  			if (index >= 0) {
	    			final Listitem item = (Listitem) lbx.getItemAtIndex(index);
	    			//Bug #2728704: Listbox with databinding generates onSelect w/o user action
	    			//Shall not fire event by spec. For backward compatibility(still want to
	    			//fire onSelect event as usual), user can specifies in zk.xml
	    			//<library-property>
	    			//  <name>org.zkoss.zkplus.databind.onSelectWhenLoad</name>
	    			//  <value>true</value>
	    			//</library-property>
	    			//then data binder will still fire the onSelect event as usual.
	    			if (SylistboxItemConverter.isOnSelectWhenLoad()) {
		    			final int selIndex = lbx.getSelectedIndex();
		    			
						//We need this to support load-when:onSelect when first load 
						//the page in (so it is called only once).
		  				if (item != null && selIndex != index) { // bug 1647817, avoid endless-loop
		    				Set items = new HashSet();
		    				items.add(item);
		    				//bug #2140491
		    				Executions.getCurrent().setAttribute("zkoss.zkplus.databind.ON_SELECT"+lbx.getUuid(), Boolean.TRUE);
		    				Events.postEvent(new SelectEvent("onSelect", lbx, items, item));
		    			}
	    			}
	  				return item;
	  			}
	  		} else if (xmodel == null) { //no model case, assume Listitem.value to be used with selectedItem
	  			//iterate to find the selected item assume the value (select mold)
	  			for (final Iterator it = lbx.getItems().iterator(); it.hasNext();) {
	  				final Listitem li = (Listitem) it.next();
	  				if (val.equals(li.getValue())) {
	  					return li;
	  				}
	  			}
	  		} else {
	  			throw new UiException("model of the databind listbox "+lbx+" must be an instanceof of org.zkoss.zkplus.databind.BindingListModel." + xmodel);
	  		}
	  	}
	  	return null;
	}
	private static Boolean _onSelectWhenLoad;
	/*package*/ static boolean isOnSelectWhenLoad() {
		if (_onSelectWhenLoad == null) {
			final String str = Library.getProperty("org.zkoss.zkplus.databind.onSelectWhenLoad", "false");
			_onSelectWhenLoad = Boolean.valueOf("true".equals(str));
		}
		return _onSelectWhenLoad.booleanValue();
	}
	public Object coerceToBean(Object val, Component comp) { //save
	  	final Listbox lbx = (Listbox) comp;
		if (Executions.getCurrent().getAttribute("zkoss.zkplus.databind.ON_SELECT"+lbx.getUuid()) != null) {
			//bug #2140491
			//triggered by coerceToUi(), ignore this
			Executions.getCurrent().removeAttribute("zkoss.zkplus.databind.ON_SELECT"+lbx.getUuid());
			return TypeConverter.IGNORE;
		}
	  	if (val != null) {
	  		final ListModel model = lbx.getModel();
	  		//no model case, assume Listitem.value to be used with selectedItem
	 			return model != null ? model.getElementAt(((Listitem) val).getIndex()) : ((Listitem) val).getValue();
	  	}
	 	return null;
	}
}