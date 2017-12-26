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

public class TelefoneBindConverter implements TypeConverter {
	@Override
	public Object coerceToBean(Object val, Component comp) {
		Class classe = null;
		if (val != null && !"".equals(val) && comp != null) {
			// adequação para adicionar o nono dígito nos telefones
			// criado para corrigir a máscara que coloca '_' no final de
			// telefones que possuem apenas 8 digitos
			String aux2 = (String) val;
			val = aux2.replace("_", "").replace("-", "").replace("(", "").replace(")", "").replace(" ", "");
			classe = getClass(comp);
			if (classe != null && (classe.getSimpleName().equals(String.class.getSimpleName()))) {
				String aux = (String) val;
				return aux;

			} else if (classe != null && (classe.getSimpleName().equals(Long.class.getSimpleName()))) {
				String aux = (String) val;
				return StringHelper.strToLong(aux);
			}

		}
		return val;

	}

	@SuppressWarnings("deprecation")
	@Override
	public Object coerceToUi(Object val, Component comp) {

		if (val != null && !"".equals(val) && comp != null) {// TODO:[sandalo.bessa]
																// Avaliar com a
																// versão 6
			if (val instanceof String) {
				String aux = (String) val;
				aux = aux.replace(".", "").replace("/", "").replace("-", "").replace("(", "").replace(")", "").replace(" ", "");
				if (aux.length() > 10) {
					return StringHelper.format("(**) *****-****", aux);
				} else {
					return StringHelper.format("(**) ****-****", aux);
				}

			} else if ((val instanceof Long)) {
				String aux = ((Long) val).toString();
				aux = StringHelper.preencheStringComCaracter(aux.toString(), '0', 10);
				if (aux.length() > 10) {
					return StringHelper.format("(**) *****-****", aux);
				} else {
					return StringHelper.format("(**) ****-****", aux);
				}
			}

		}
		return val;
	}

	private Class getClass(Component comp) {
		Class classe = null;
		try {
			if (comp instanceof TelefoneboxBind) {
				TelefoneboxBind telefoneboxBind = (TelefoneboxBind) comp;
				String nomeDoCampo = "";
				if (!StringUtils.isEmpty(telefoneboxBind.getNomeDoObjeto())) {
					String[] nodos = telefoneboxBind.getNomeDoObjeto().split("\\.");
					nomeDoCampo = nodos[nodos.length - 1];
				} else {
					nomeDoCampo = telefoneboxBind.getId();
				}
				nomeDoCampo = nomeDoCampo.replace("Arg", "");
				classe = Reflexao.recuperaTipoDeRetornoDoMetodoGet(nomeDoCampo, telefoneboxBind.getObjectPai());
			}
			return classe;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}
}
