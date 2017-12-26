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
/**
 * 
 */
package br.gov.prodigio.entidades;

import java.util.List;

/**
 * @author p051679
 *
 */
public interface TreeNodo {
	List<?> nodosFilhos = null;

	public List<TreeNodo> getNodosFilhos();

	/**
	 * @return
	 */
	public boolean isDiretorio();

	/**
	 * @return
	 */
	public String getNome();

	/**
	 * @return
	 */
	public String getTitulo();
}