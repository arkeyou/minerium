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
import br.gov.prodigio.comuns.utils.StringHelper;

public class CpfBindConverter implements TypeConverter {

	@Override
	public Object coerceToBean(Object val, Component comp) {
		Class classe = null;
		if (val != null && !"".equals(val) && comp != null) {

			classe = getClass(comp);
			if (classe != null && (classe.getSimpleName().equals(String.class.getSimpleName()))) {
				String aux = (String) val;
				return aux.replace(".", "").replace("/", "").replace("-", "").replace("(", "").replace(")", "").replace(" ", "");

			} else if (classe != null && (classe.getSimpleName().equals(Long.class.getSimpleName()))) {
				String aux = (String) val;
				return StringHelper.strToLong(aux);
			}

		}
		return val;

	}

	@Override
	public Object coerceToUi(Object val, Component comp) {

		if (val != null && !"".equals(val) && comp != null) {
			if (val instanceof String) {
				String aux = (String) val;
				aux = aux.replace(".", "").replace("/", "").replace("-", "").replace("(", "").replace(")", "").replace(" ", "");
				return StringHelper.format("###.###.###-##", aux);

			} else if ((val instanceof Long)) {
				String aux = ((Long) val).toString();
				aux = StringHelper.preencheStringComCaracter(aux, '0', 11);
				return StringHelper.format("###.###.###-##", aux);
			}

		}
		return val;
	}

	private Class getClass(Component comp) {
		Class classe = null;
		try {
			if (comp instanceof CpfboxBind) {
				CpfboxBind cpfboxBind = (CpfboxBind) comp;
				String nomeDoCampo = "";
				if (!StringUtils.isEmpty(cpfboxBind.getNomeDoObjeto())) {
					String[] nodos = cpfboxBind.getNomeDoObjeto().split("\\.");
					nomeDoCampo = nodos[nodos.length - 1];
				} else {
					nomeDoCampo = cpfboxBind.getId();
				}
				nomeDoCampo = nomeDoCampo.replace("Arg", "");
				classe = Reflexao.recuperaTipoDeRetornoDoMetodoGet(nomeDoCampo, cpfboxBind.getObjectPai());
			}
			return classe;

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}
}
