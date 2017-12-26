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

import java.util.LinkedList;
import java.util.List;

import org.zkoss.zul.ListModel;
import org.zkoss.zul.SimpleListModel;

public class ProSimpleListModel extends SimpleListModel {

	public ProSimpleListModel(String[] data) {
		super(data);
	}

	public ProSimpleListModel(List data) {
		super(data);
	}

	public ListModel getSubModel(Object value, int nRows) {
		final String idx = value == null ? "" : objectToString(value);
		final LinkedList data = new LinkedList();
		for (int i = 0; i < getSize(); i++) {
			if (entryMatchesText(getElementAt(i).toString(), idx) && idx.length() >= 3) {
				data.add(getElementAt(i));
			}
		}
		return new ProSimpleListModel(data);
	}

	public boolean entryMatchesText(String entry, String text) {
		return entry.startsWith(text.toUpperCase()) || entry.startsWith(text.toLowerCase());
	}

}
