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

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.gov.prodigio.comuns.utils.Reflexao;

public class ComparadorGenerico implements Comparator, Serializable {
	private static final Logger log = LoggerFactory.getLogger(ComparadorGenerico.class);
	private boolean ascendente;
	private String atributo;

	public ComparadorGenerico(boolean asc, String campo) {
		ascendente = asc;
		atributo = campo;
	}

	public int compare(Object o1, Object o2) {
		Object v1 = null;
		Object v2 = null;
		try {
			v1 = Reflexao.recuperaValorDaPropriedade(atributo, o1);
			v2 = Reflexao.recuperaValorDaPropriedade(atributo, o2);
		} catch (IllegalAccessException e) {
			log.error("Erro ao comparar objeto ", e);
		} catch (InvocationTargetException e) {
			log.error("Erro ao comparar objeto ", e);
		} catch (Exception e) {
			log.error("Erro ao comparar objeto ", e);
		}
		if (ascendente) {
			if (v1 == null) {
				return -1;
			}
			if (v2 == null) {
				return 1;
			}
			return ((Comparable) v1).compareTo(v2);
		} else {
			if (v1 == null) {
				return 1;
			}
			if (v2 == null) {
				return -1;
			}
			return ((Comparable) v2).compareTo(v1);
		}
	}
}
