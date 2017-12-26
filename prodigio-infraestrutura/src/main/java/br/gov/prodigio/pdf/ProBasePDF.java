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
package br.gov.prodigio.pdf;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.List;

import br.gov.prodigio.comuns.IProDataPDF;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

public class ProBasePDF {

	public OutputStream gerar(IProDataPDF dado) throws DocumentException {
		Document document = new Document();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PdfWriter.getInstance(document, baos);
		document.addTitle(dado.getTitulo());
		document.addAuthor(dado.getAutor());
		document.addCreator(dado.getCriador());
		document.addKeywords(dado.getPalavrasChaves());
		document.addSubject(dado.getAssunto());
		document.addCreationDate();
		document.open();
		document.close();
		return baos;
	}

	public OutputStream gerar(IProDataPDF dado, List<String> linhas) throws DocumentException {
		Document document = null;
		if ("paisagem".equals(dado.getOrientacao()))
			document = new Document(PageSize.LETTER.rotate());
		else
			document = new Document();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PdfWriter.getInstance(document, baos);
		document.addTitle(dado.getTitulo());
		document.addAuthor(dado.getAutor());
		document.addCreator(dado.getCriador());
		document.addKeywords(dado.getPalavrasChaves());
		document.addSubject(dado.getAssunto());
		document.addCreationDate();
		document.open();
		for (String linha : linhas) {
			Paragraph paragraph = new Paragraph();
			Chunk chunk = new Chunk();
			Font font = new Font();
			font.setSize(dado.getFonte());
			font.setFamily(FontFactory.COURIER);
			chunk.setFont(font);
			chunk.append(linha + "");
			paragraph.setLeading(10F);
			paragraph.add(chunk);
			document.add(paragraph);
		}
		if (linhas.size() == 0) {
			Paragraph paragraph = new Paragraph();
			Chunk chunk = new Chunk();
			Font font = new Font();
			font.setSize(30);
			font.setFamily(FontFactory.COURIER);
			chunk.setFont(font);
			chunk.append("Erro!  Arquivo inconsistente!");
			paragraph.setLeading(10F);
			paragraph.add(chunk);
			document.add(paragraph);
		}

		document.close();
		return baos;
	}

}