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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.zkoss.zk.ui.Component;
import org.zkoss.zkplus.databind.ListModelConverter;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;

public class ProModelSetConverter extends ListModelConverter {
	@Override
	public Object coerceToBean(Object val, Component comp) {
		return super.coerceToBean(val, comp);
	}

	@Override
	public Object coerceToUi(Object val, Component comp) {
		Listbox destino = (Listbox) comp;
		Listboxdual listboxdual = (Listboxdual) destino.getParent();
		Set listaDetalhe = (Set) val;
		Listbox origem = listboxdual.getOrigem();
		List itensQueSeraoremovidos = new ArrayList();
		List<Listitem> listitems = origem.getItems();

		for (Listitem item : listitems) {
			if (listaDetalhe != null) {
				for (Object detalhe : listaDetalhe) {
					Object value = item.getValue();
					if (value != null && detalhe != null && detalhe.toString().equals(value.toString())) {
						itensQueSeraoremovidos.add(item);
						break;
					}
				}
			}
		}
		for (Object listitem : itensQueSeraoremovidos) {
			origem.removeChild((Component) listitem);
		}

		return super.coerceToUi(val, comp);
	}
}
