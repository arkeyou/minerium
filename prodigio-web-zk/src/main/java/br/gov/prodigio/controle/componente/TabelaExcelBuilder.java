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

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import jxl.CellView;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TabelaExcelBuilder implements ITabelaBuilder {

	public final Logger log = LoggerFactory.getLogger(this.getClass());
	private int numeroDeColunas = 0;
	private int indiceColulaAtual = 0;
	private int indiceLinhaAtual = 0;
	private int qtdeRowspan = 0;
	private int indiceColunaRowspanDe = 0;
	private int indiceColunaRowspanAte = 0;
	private WritableSheet planilha;
	WritableWorkbook excel;
	ByteArrayOutputStream arquivoDeSaida = new ByteArrayOutputStream();
	private IConverterTableBuilder conversor = new ConverterTableBuilder();

	public TabelaExcelBuilder(int numeroDeColunas) {
		if (numeroDeColunas < 1) {
			throw new IllegalArgumentException("Número de colunas inválido: " + numeroDeColunas);
		}
		try {
			this.numeroDeColunas = numeroDeColunas;

			excel = Workbook.createWorkbook(arquivoDeSaida);
			this.planilha = excel.createSheet("Planilha", 0);
		} catch (IOException e) {
			log.error("Método criarTabela: ", e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public void addLinhaDestaque(Object conteudo) {
		addLinhaGenerico(conteudo, true);
	}

	@Override
	public void addLinha(Object conteudo) {
		addLinhaGenerico(conteudo, false);
	}

	public void addLinhaGenerico(Object conteudo, boolean destaque) {
		try {
			Label label = getLabel(conteudo, destaque);
			planilha.mergeCells(indiceColulaAtual, indiceLinhaAtual, numeroDeColunas - 1, indiceLinhaAtual);
			planilha.addCell(label);
			indiceLinhaAtual++;
		} catch (RowsExceededException e) {
			log.error("Método addLinhaCabecalho: ", e);
			throw new RuntimeException("Número de linhas excedido");
		} catch (WriteException e) {
			log.error("Método addLinhaCabecalho: ", e);
			throw new RuntimeException("Erro ao escrever coluna");
		}
	}

	public WritableCellFormat getFormatacaoDestaque() {
		try {
			WritableCellFormat fonteArial10Negrito = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD));
			WritableCellFormat formatacaoColuna = new WritableCellFormat(fonteArial10Negrito);
			formatacaoColuna.setAlignment(Alignment.CENTRE);
			formatacaoColuna.setVerticalAlignment(VerticalAlignment.CENTRE);
			formatacaoColuna.setBackground(Colour.GRAY_25);
			return formatacaoColuna;
		} catch (WriteException e) {
			log.error("Erro método getFormatacaoDestaque: ", e);
			throw new IllegalArgumentException(e);
		}
	}

	@Override
	public ITabelaBuilder addColuna(Object conteudo) {
		return addColuna(conteudo, 1, 1);
	}

	@Override
	public ITabelaBuilder addColunas(Object... conteudo) {
		for (Object conteudoColuna : conteudo) {
			addColuna(conteudoColuna);
		}
		return this;
	}

	@Override
	public byte[] construir() {
		try {
			ajustarLarguraColunasExcel(this.numeroDeColunas, excel);
			excel.write();
			excel.close();
			return arquivoDeSaida.toByteArray();
		} catch (Exception e) {
			log.error("Método construir: ", e);
			throw new RuntimeException("Erro ao construir o relatório Excel");
		}
	}

	public void ajustarLarguraColunasExcel(int numeroDeColunas, WritableWorkbook workbook) {
		for (WritableSheet sheet : workbook.getSheets()) {
			for (int x = 0; x < numeroDeColunas; x++) {
				CellView cf = new CellView();
				cf.setAutosize(true);
				sheet.setColumnView(x, cf);
			}
		}
	}

	@Override
	public ITabelaBuilder addColunaDestaque(Object conteudo) {
		return addColunaGenerico(conteudo, 1, 1, true);
	}

	@Override
	public ITabelaBuilder addColuna(Object conteudo, int colspan, int rowspan) {
		return addColunaGenerico(conteudo, colspan, rowspan, false);
	}

	private Label getLabel(Object conteudo, boolean destaque) {
		if (destaque) {
			return new Label(indiceColulaAtual, indiceLinhaAtual, conversor.converter(conteudo), getFormatacaoDestaque());
		} else {
			return new Label(indiceColulaAtual, indiceLinhaAtual, conversor.converter(conteudo));
		}
	}

	public ITabelaBuilder addColunaGenerico(Object conteudo, int colspan, int rowspan, boolean destaque) {

		int indiceColspan = 0;
		Label label = null;
		try {
			if (rowspan > 1 && colspan > 1) {
				label = getLabel(conteudo, destaque);
				if (colspan == numeroDeColunas) {
					planilha.mergeCells(indiceColulaAtual, indiceLinhaAtual, (indiceColulaAtual += colspan - 1), (indiceLinhaAtual += rowspan - 1));
					qtdeRowspan = 0;
				} else {
					qtdeRowspan = rowspan;
					indiceColunaRowspanDe = indiceColulaAtual;
					indiceColunaRowspanAte = indiceColulaAtual + colspan - 1;
					planilha.mergeCells(indiceColulaAtual, indiceLinhaAtual, (indiceColulaAtual + colspan - 1), (indiceLinhaAtual + rowspan - 1));
				}

			} else if (rowspan > 1) {
				qtdeRowspan = rowspan;
				indiceColunaRowspanDe = indiceColulaAtual;
				indiceColunaRowspanAte = indiceColulaAtual;
				label = getLabel(conteudo, destaque);
				planilha.mergeCells(indiceColulaAtual, indiceLinhaAtual, indiceColulaAtual, (indiceLinhaAtual + rowspan - 1));

			} else if (colspan > 1) {
				indiceColspan = indiceColulaAtual + colspan;
				if (qtdeRowspan > 0) {
					while (colunaAtualNoIntervaloDoColspan()) {
						indiceColulaAtual++;
					}
					label = getLabel(conteudo, destaque);
					planilha.mergeCells(indiceColulaAtual, indiceLinhaAtual, indiceColspan - 1, indiceLinhaAtual);
				} else {
					label = getLabel(conteudo, destaque);
					planilha.mergeCells(indiceColulaAtual, indiceLinhaAtual, indiceColspan - 1, indiceLinhaAtual);
				}

			} else {
				if (qtdeRowspan > 0) {
					while (colunaAtualNoIntervaloDoColspan()) {
						indiceColulaAtual++;
					}
				}
				label = getLabel(conteudo, destaque);
			}
			planilha.addCell(label);
			if (indiceColulaAtual >= numeroDeColunas) {
				throw new RuntimeException("Número de colunas excedido");
			}

			if (colspan > 1) {
				indiceColulaAtual = indiceColulaAtual + indiceColspan;
			} else {
				indiceColulaAtual++;
			}

			return this;
		} catch (RowsExceededException e) {
			log.error("Método addColuna: ", e);
			throw new RuntimeException("Número de linhas excedido");
		} catch (WriteException e) {
			log.error("Método addColuna: ", e);
			throw new RuntimeException("Erro ao escrever coluna");
		}

	}

	private boolean colunaAtualNoIntervaloDoColspan() {
		return indiceColulaAtual >= indiceColunaRowspanDe && indiceColulaAtual <= indiceColunaRowspanAte;
	}

	@Override
	public ITabelaBuilder abrirLinha() {
		indiceColulaAtual = 0;
		return this;
	}

	@Override
	public void fecharLinha() {
		indiceLinhaAtual++;
		indiceColulaAtual = 0;
		qtdeRowspan--;

	}

	@Override
	public ITabelaBuilder criarTabela(int numeroDeColunas, String estiloDaTabela) {
		return new TabelaExcelBuilder(numeroDeColunas);
	}

	@Override
	public ITabelaBuilder addColunasDestaque(Object... conteudo) {
		for (Object conteudoColuna : conteudo) {
			addColunaDestaque(conteudoColuna);
		}
		return this;
	}

	@Override
	public ITabelaBuilder addColunaDestaque(Object conteudo, int colspan, int rowspan) {
		return addColunaGenerico(conteudo, colspan, rowspan, true);
	}

	@Override
	public void addEstiloColunaDestaque(String css, String value) {
		log.info("Método 'addEstiloColunaDestaque' indisponível no gerador de excel");

	}

	@Override
	public void addEstiloColuna(String css, String value) {
		log.info("Método 'addEstiloColuna' indisponível no gerador de excel");
	}

	@Override
	public void definirConverter(IConverterTableBuilder converterTableBuilder) {
		setConversor(converterTableBuilder);

	}

	public IConverterTableBuilder getConversor() {
		return conversor;
	}

	public void setConversor(IConverterTableBuilder conversor) {
		this.conversor = conversor;
	}

}
