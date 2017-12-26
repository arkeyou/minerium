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

import java.util.Set;

import org.zkoss.zkplus.databind.BindingListModelSet;

import br.gov.prodigio.entidades.RecordDataSet;

public class PagingListModel extends BindingListModelSet {

	public PagingListModel(Set set, boolean live) {
		super(set, live);
	}

	@Override
	public int getSize() {
		RecordDataSet recordDataSet = (RecordDataSet) getInnerSet();
		return super.getSize();
	}

	@Override
	public Object getElementAt(int j) {
		return super.getElementAt(j);
	}

}
