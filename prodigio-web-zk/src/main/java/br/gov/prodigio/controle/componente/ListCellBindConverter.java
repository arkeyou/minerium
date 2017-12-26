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

import java.text.ParseException;

import javax.swing.text.MaskFormatter;

import org.zkoss.zk.ui.Component;
import org.zkoss.zkplus.databind.TypeConverter;

import br.gov.prodigio.comuns.utils.StringHelper;

public class ListCellBindConverter implements TypeConverter {

	@Override
	public Object coerceToUi(Object val, Component comp) {
		ListCellBind cellBind = (ListCellBind) comp;
		String mask = cellBind.getMask();
		String label = "";

		if (StringHelper.isNotEmpty(mask)) {
			try {
				MaskFormatter mf = new MaskFormatter(mask);

			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return label;
	}

	@Override
	public Object coerceToBean(Object val, Component comp) {
		return val;
	}

/*	public static void main(String args[]) throws Exception {

		MaskFormatter mf = new MaskFormatter("(##)####-####");
		mf.setValueContainsLiteralCharacters(false);
		System.out.println(mf.valueToString("31995120799"));
	}
*/
}
