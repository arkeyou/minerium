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
package br.gov.prodigio.comuns.utils;

import br.gov.prodigio.entidades.ProBaseVO;

public class EntityHelper {

	public static boolean isNull(ProBaseVO obj) {
		return obj == null;
	}

	public static boolean isNotNull(ProBaseVO obj) {
		return !(obj == null);
	}

	public static boolean isEmpty(ProBaseVO obj) {
		return isNull(obj) || obj.getId() == null;
	}

	/**
	 * Checa se a String não é vazia e não é nula.</p>
	 */
	public static boolean isNotEmpty(ProBaseVO obj) {
		return !isEmpty(obj);
	}

}
