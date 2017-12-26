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

import java.util.HashMap;
import java.util.Map;

/**
 * Classe responsável por transportar dados entre camadas lógicas na mesma thread (requisição). No entanto, essa classe não deve ser utilizada (não funciona) se as camadas estiverem fisicamente separadas. Essa classe funciona de forma similar ao
 * escopo de requisição WEB, com a diferença de poder ser utilizada sem que exista um container web instanciado.
 * 
 * @author Sândalo Bessa
 */
public class Context {
	private static final ThreadLocal<Map<String, Object>> contexto = new ThreadLocal<Map<String, Object>>();

	/**
	 * @param atributo
	 *            nome do atributo que será inserido no contexto.
	 * @return Objeto inserido no contexto
	 */
	public static Object getAttribute(String atributo) {
		if (contexto.get() == null) {
			contexto.set(new HashMap());
		}
		Map<String, Object> map = contexto.get();
		return map.get(atributo);
	}

	/**
	 * @param atributo
	 *            nome do atributo que será inserido no contexto.
	 * @param Objeto
	 *            inserido no contexto
	 */
	public static void setAttribute(String atributo, Object objeto) {
		if (contexto.get() == null) {
			contexto.set(new HashMap());
		}
		Map map = contexto.get();
		map.put(atributo, objeto);
	}

}
