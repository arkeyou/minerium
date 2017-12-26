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

import org.apache.commons.lang.StringUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zkplus.databind.TypeConverter;

import br.gov.prodigio.comuns.utils.Reflexao;

public class PlacaBindConverter implements TypeConverter {

	@Override
	public Object coerceToBean(Object val, Component comp) {
		Class classe = null;
		if (val != null && !"".equals(val) && comp != null) {
			String aux = (String) val;
			aux = aux.replace(".", "").replace("/", "").replace("-", "").replace("(", "").replace(")", "").replace(" ", "").replace("_", "");
			if ("".equals(aux)) {
				return null;
			}

			classe = getClass(comp);
			if (classe != null && (classe.getSimpleName().equals(String.class.getSimpleName()))) {
				return aux;

			}
		}
		return null;

	}

	@Override
	public Object coerceToUi(Object val, Component comp) {

		if (val != null && !"".equals(val) && comp != null) {
			if (val instanceof String && ((String) val).length() > 6) {
				String aux = (String) val;
				aux = aux.replace(".", "").replace("/", "").replace("-", "").replace("(", "").replace(")", "").replace(" ", "").replace("_", "");
				return aux.substring(0, 3) + "-" + aux.substring(3, 7);
			}
		}
		return val;
	}

	private Class getClass(Component comp) {
		Class classe = null;
		try {
			if (comp instanceof PlacaboxBind) {
				PlacaboxBind placaboxBind = (PlacaboxBind) comp;
				String nomeDoCampo = "";
				if (!StringUtils.isEmpty(placaboxBind.getNomeDoObjeto())) {
					String[] nodos = placaboxBind.getNomeDoObjeto().split("\\.");
					nomeDoCampo = nodos[nodos.length - 1];
				} else {
					nomeDoCampo = placaboxBind.getId();
				}
				nomeDoCampo = nomeDoCampo.replace("Arg", "");
				classe = Reflexao.recuperaTipoDeRetornoDoMetodoGet(nomeDoCampo, placaboxBind.getObjectPai());
			}
			return classe;

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}
}
