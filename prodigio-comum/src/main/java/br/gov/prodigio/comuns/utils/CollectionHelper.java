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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class CollectionHelper {

	private CollectionHelper() {
	}

	/**
	 * 
	 * Transforma qualquer grupo de coleções em coleções do tipo HashSet e ArrayList
	 * 
	 * @param objeto
	 *            - Coleção a ser atualizada
	 * @return Uma coleção somente com classe do tipo ArrayList e HashSet
	 */
	public static Object atualizaCollection(Collection<?> objeto) {
		if (objeto != null) {
			if (objeto instanceof Set) {
				Set<?> hashSet = new HashSet<Object>(objeto);
				return hashSet;
			} else if (objeto instanceof List) {
				List<?> arrayList = new ArrayList<Object>(objeto);
				return arrayList;
			}
		}
		return null;
	}

	/**
	 * Verifica se a coleção é vazia tratando nulidade
	 * 
	 * @param collection
	 * @return true para vazia e false para não vazia
	 */
	public static boolean isEmpty(Collection<?> collection) {
		return collection == null || collection.isEmpty();
	}

	/**
	 * 
	 * Verifica se a coleção não é vazia tratando nulidade
	 * 
	 * @param collection
	 * @return true para não vazia e false para vazia
	 */
	public static boolean isNotEmpty(Collection<?> collection) {
		return !isEmpty(collection);
	}

}
