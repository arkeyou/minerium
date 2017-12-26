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

import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zkplus.databind.SelectedItemConverter;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;

import br.gov.prodigio.entidades.ProBaseVO;

public class ProSelectedItemConverter extends SelectedItemConverter {
	@Override
	public Object coerceToUi(Object argAppVO, Component argListbox) {
		Listitem listitem = null;
		Listbox listbox = (Listbox) argListbox;
		if (argAppVO == null && listbox != null && listbox.getItemCount() > 0) {
			listitem = listbox.getItemAtIndex(0);
			listitem.setSelected(true);
		} else {
			List list = (List) listbox.getModel();
			int i = 0;
			if (list != null) {
				for (Object object : list) {
					ProBaseVO voLista = (ProBaseVO) object;
					ProBaseVO voSelecionado = (ProBaseVO) argAppVO;
					if (voSelecionado == null)
						break;
					if (voLista.getId().equals(voSelecionado.getId())) {
						listitem = listbox.getItemAtIndex(i);
						break;
					}
					i++;
				}
			}
		}
		return listitem;
	}

	@Override
	public Object coerceToBean(Object arg0, Component arg1) {
		return super.coerceToBean(arg0, arg1);
	}

}
