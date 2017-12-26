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
import org.zkoss.zkplus.databind.RadiogroupSelectedItemConverter;
import org.zkoss.zul.Radio;

public class ProRadiogroupArgSelectItemConverter extends RadiogroupSelectedItemConverter {
	@Override
	public Object coerceToUi(Object arg0, Component arg1) {
		if (arg0 != null) {
			Class classe = arg0.getClass();
			if (classe.isEnum()) {
				Enum e = (Enum) arg0;
				RadiogroupArg radiogroupArg = (RadiogroupArg) arg1;
				List children = radiogroupArg.getChildren();
				for (Object object : children) {
					if (object instanceof Radio) {
						Radio radio = (Radio) object;
						if (radio.getValue().equals(e.name() + "")) {
							radio.setChecked(true);
							return e.name();
						}
					}
				}
			}
		}
		return null;
	}

	@Override
	public Object coerceToBean(Object arg0, Component arg1) {
		RadiogroupArg radiogroupArg = (RadiogroupArg) arg1;
		Radio radioSelecionado = radiogroupArg.getSelectedItem();
		if (radioSelecionado != null) {
			return radioSelecionado.getAttribute("objeto");
		}
		return arg0;
	}
}
