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

public interface ITabelaBuilder {

	public ITabelaBuilder criarTabela(int numeroDeColunas, String estiloDaTabela);

	public void definirConverter(IConverterTableBuilder converterTableBuilder);

	public void addEstiloColunaDestaque(String css, String value);

	public void addEstiloColuna(String css, String value);

	public ITabelaBuilder abrirLinha();

	public void fecharLinha();

	public void addLinha(Object cabecalho);

	public void addLinhaDestaque(Object cabecalho);

	public ITabelaBuilder addColuna(Object conteudo);

	public ITabelaBuilder addColunaDestaque(Object conteudo);

	public ITabelaBuilder addColuna(Object conteudo, int colspan, int rowspan);

	public ITabelaBuilder addColunaDestaque(Object conteudo, int colspan, int rowspan);

	public ITabelaBuilder addColunas(Object... conteudo);

	public ITabelaBuilder addColunasDestaque(Object... conteudo);

	public byte[] construir();

}
