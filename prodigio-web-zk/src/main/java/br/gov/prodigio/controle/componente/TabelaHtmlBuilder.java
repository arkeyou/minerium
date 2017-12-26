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

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TabelaHtmlBuilder implements ITabelaBuilder {

	public final Logger log = LoggerFactory.getLogger(this.getClass());
	private StringBuilder html = new StringBuilder();
	private boolean linhaAtiva = false;
	private Map<String, String> estiloColunaRotulo = new HashMap<>();
	private Map<String, String> estiloColuna = new HashMap<>();
	private int numeroColunas = 2;
	private IConverterTableBuilder conversor = new ConverterTableBuilder();

	public TabelaHtmlBuilder(int numeroDeColunas) {
		if (numeroDeColunas < 1) {
			throw new IllegalArgumentException("Número de colunas inválido: " + numeroDeColunas);
		}
		this.numeroColunas = numeroDeColunas;
		html = new StringBuilder();
		estiloColunaRotulo = new HashMap<>();
		estiloColuna = new HashMap<>();
		html.append("<table border=\"1\" style=\"width:100%;  border-spacing:0px; \">");
		estiloColunaRotulo.put("background-color", "#DCDCDC");
		estiloColunaRotulo.put("border", "solid 1px solid");
		estiloColuna.put("border", "solid 1px solid");
	}

	@Override
	public ITabelaBuilder criarTabela(int numeroDeColunas, String estiloDaTabela) {
		if (numeroDeColunas < 1) {
			throw new IllegalArgumentException("Número de colunas inválido: " + numeroDeColunas);
		}
		html = new StringBuilder();
		estiloColunaRotulo = new HashMap<>();
		estiloColuna = new HashMap<>();
		if (estiloDaTabela == null || estiloDaTabela.isEmpty()) {
			html.append("<table border=\"1\" style=\"width:100%;border:solid 1px solid; border-collapse: collapse;\">");
			estiloColunaRotulo.put("background-color", "#DCDCDC");
			estiloColunaRotulo.put("border", "solid 1px solid");
			estiloColuna.put("border", "solid 1px solid");
		} else {
			html.append("<table style=\" " + estiloDaTabela + " \">");
		}
		this.numeroColunas = numeroDeColunas;
		return this;
	}

	public TabelaHtmlBuilder(int numeroDeColunas, String estiloDaTabela, Map<String, String> estiloColunaRotulo, Map<String, String> estiloColuna) {
		if (numeroDeColunas < 1) {
			throw new IllegalArgumentException("Número de colunas inválido: " + numeroDeColunas);
		}
		this.numeroColunas = numeroDeColunas;
		this.html.append("<table style=\" " + estiloDaTabela + " \">");
		this.estiloColuna = estiloColuna;
		this.estiloColunaRotulo = estiloColunaRotulo;

	}

	public IConverterTableBuilder getConversor() {
		return conversor;
	}

	public void setConversor(IConverterTableBuilder conversor) {
		this.conversor = conversor;
	}

	@Override
	public byte[] construir() {
		fecharLinha();
		return this.html.append("</table>").toString().getBytes(StandardCharsets.UTF_8);
	}

	@Override
	public void addEstiloColunaDestaque(String css, String value) {
		this.estiloColunaRotulo.put(css, value);
	}

	@Override
	public void addEstiloColuna(String css, String value) {
		this.estiloColuna.put(css, value);
	}

	@Override
	public void addLinhaDestaque(Object cabecalho) {
		abrirLinha().criarColuna(cabecalho, this.numeroColunas, estiloColunaRotulo() + ";text-align:center;font-weight: bold;", 1);
	}

	@Override
	public void addLinha(Object cabecalho) {
		abrirLinha().criarColuna(cabecalho, this.numeroColunas, estiloColuna(), 1);
	}

	public void addLinhaCabecalhoSemEstilo(String cabecalho, int colspan) {
		fecharLinha();
		this.html.append("\n<tr>");
		this.html.append("\n\t<td align=\"center\" colspan=\"").append(colspan).append("\"><b>");
		this.html.append(cabecalho);
		this.html.append("\n\t</b></td>");
		this.html.append("\n</tr>");
	}

	private String estiloMapParaString(Map<String, String> cssMap) {
		StringBuilder style = new StringBuilder();
		for (Entry<String, String> nomeValor : cssMap.entrySet()) {
			style.append(nomeValor.getKey());
			style.append(":");
			style.append(nomeValor.getValue());
			style.append(";");
		}
		return style.toString();
	}

	private String estiloColunaRotulo() {
		return estiloMapParaString(this.estiloColunaRotulo);
	}

	private String estiloColuna() {
		return estiloMapParaString(this.estiloColuna);
	}

	private String estilo(String styleCompleto) {
		return " style=\"" + styleCompleto + "\" ";
	}

	public void addLinha(String rotulo, String valor) {
		fecharLinha();
		abrirLinha().addColunaDestaque(rotulo).addColuna(valor);
	}

	@Override
	public TabelaHtmlBuilder abrirLinha() {
		fecharLinha();
		this.html.append("\n<tr>");
		this.linhaAtiva = true;
		return this;
	}

	@Override
	public ITabelaBuilder addColunaDestaque(Object valor, int colspan, int rowspan) {
		return criarColuna(valor, colspan, estiloColunaRotulo(), rowspan);

	}

	@Override
	public ITabelaBuilder addColunaDestaque(Object valor) {
		return criarColuna(valor, 1, estiloColunaRotulo(), 1);
	}

	@Override
	public ITabelaBuilder addColuna(Object valor, int colspan, int rowspan) {
		return criarColuna(valor, colspan, estiloColuna(), rowspan);
	}

	@Override
	public ITabelaBuilder addColuna(Object valor) {
		return criarColuna(valor, 1, estiloColuna(), 1);
	}

	@Override
	public ITabelaBuilder addColunas(Object... valores) {
		for (Object valor : valores) {
			addColuna(valor);
		}
		return this;
	}

	@Override
	public ITabelaBuilder addColunasDestaque(Object... valores) {
		for (Object valor : valores) {
			addColunaDestaque(valor);
		}
		return this;
	}

	private TabelaHtmlBuilder criarColuna(Object valor, int colspan, String style, int rowspan) {
		this.html.append("\n<td ");

		if (rowspan > 1) {
			this.html.append(" rowspan=\"").append(rowspan).append("\" ");
		}
		if (colspan > 1) {
			this.html.append(" colspan=\"").append(colspan).append("\"");
		}
		this.html.append(estilo(style));
		this.html.append(">");
		this.html.append(conversor.converter(valor));
		this.html.append("\n</td>");
		return this;
	}

	@Override
	public String toString() {
		return this.html.toString();
	}

	@Override
	public void fecharLinha() {
		if (linhaAtiva) {
			this.html.append("\n</tr>");
			this.linhaAtiva = false;
		}

	}

	public static void main(String[] args) {
		ITabelaBuilder builder = new TabelaHtmlBuilder(3);
		builder.addLinhaDestaque("CABEÇALHO");
		builder.abrirLinha().addColunas("COLUNA SIMPLES 1", "COLUNA SIMPLES 2", "COLUNA SIMPLES 3").fecharLinha();
		builder.abrirLinha().addColunaDestaque("COLUNA 1 ROTULO").addColuna("COLUNA SIMPLES 2").addColuna("COLUNA SIMPLES 3").fecharLinha();
		builder.abrirLinha().addColunaDestaque("COLUNA 1 ROTULO ROWSPAN2 ", 1, 2).addColuna("COLUNA SIMPLES 2").addColuna("COLUNA SIMPLES 3").fecharLinha();
		builder.abrirLinha().addColunas("COLUNA SIMPLES 2", "COLUNA SIMPLES 3").fecharLinha();
		builder.addLinhaDestaque("LINHA ROTULO COLSPAN 3");
		builder.abrirLinha().addColunas("COLUNA SIMPLES 1", "COLUNA SIMPLES 2", "COLUNA SIMPLES 3").fecharLinha();
		builder.addLinhaDestaque("RODAPÉ");
		System.out.println(new String(builder.construir()));
	}

	@Override
	public void definirConverter(IConverterTableBuilder converterTableBuilder) {
		setConversor(converterTableBuilder);
	}
}
