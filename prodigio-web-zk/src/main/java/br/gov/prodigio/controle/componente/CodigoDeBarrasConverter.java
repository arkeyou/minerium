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
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.zkoss.image.AImage;
import org.zkoss.zk.ui.Component;
import org.zkoss.zkplus.databind.TypeConverter;
import org.zkoss.zul.Image;

import br.gov.prodigio.comuns.utils.Reflexao;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.CodaBarWriter;
import com.google.zxing.oned.Code39Writer;
import com.google.zxing.oned.Code93Writer;
import com.google.zxing.oned.EAN13Writer;
import com.google.zxing.oned.EAN8Writer;
import com.google.zxing.qrcode.QRCodeWriter;

@SuppressWarnings("deprecation")
public class CodigoDeBarrasConverter implements TypeConverter {
	static AImage imgInvalida;
	static {
		try {
			String base64ImagemInvalida = "/9j/4AAQSkZJRgABAQAAAQABAAD//gA6Q1JFQVRPUjogZ2QtanBlZyB2MS4wICh1c2luZyBJSkcgSlBFRyB2ODApLCBxdWFsaXR5ID0gMQr/2wBDAP//////////////////////////////////////////////////////////////////////////////////////2wBDAf//////////////////////////////////////////////////////////////////////////////////////wAARCAAEAAQDASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEAAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSExBhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwB9FFFAH//Z";
			imgInvalida = new AImage("imagemInvalida", Base64.getDecoder().decode(base64ImagemInvalida.getBytes(StandardCharsets.UTF_8)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Object coerceToUi(Object val, Component comp) {
		CodigoDeBarrasBind codigoDeBarrasBind = (CodigoDeBarrasBind) comp.getParent();
		Object objetoPai = codigoDeBarrasBind.getObjectPai();
		try {
			if (objetoPai != null) {
				String[] propriedades = codigoDeBarrasBind.getNomeDoObjeto().split("\\.");
				String atributoDoCodigoDeBarras = propriedades[propriedades.length - 1];
				String codigoDeBarrasString = (String) Reflexao.recuperaValorDaPropriedade(atributoDoCodigoDeBarras, objetoPai);
				if (codigoDeBarrasString != null) {
					byte[] imagemCodigoDeBarras = gerarCodigoDeBarras(codigoDeBarrasString, codigoDeBarrasBind.getFormato());
					AImage img = new AImage("codigo", imagemCodigoDeBarras);
					((Image) comp).setContent(img);
					return img.getByteData();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			((Image) comp).setContent(imgInvalida);
		}
		return imgInvalida;
	}

	private byte[] gerarCodigoDeBarras(String codigoDeBarrasString, String formato) throws WriterException, IOException {

		BitMatrix bitMatrix;
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();

		if ("UPC_A".equals(formato)) { // 12 digitos
			bitMatrix = new Code39Writer().encode(codigoDeBarrasString, BarcodeFormat.CODE_39, 150, 80, null);
			MatrixToImageWriter.writeToStream(bitMatrix, "png", bytes);

		} else if ("UPC_E".equals(formato)) {
			bitMatrix = new Code93Writer().encode(codigoDeBarrasString, BarcodeFormat.CODE_93, 150, 80, null);
			MatrixToImageWriter.writeToStream(bitMatrix, "png", bytes);

		} else if ("EAN_13".equals(formato)) { // 13 digitos
			bitMatrix = new EAN13Writer().encode(codigoDeBarrasString, BarcodeFormat.EAN_13, 150, 80, null);
			MatrixToImageWriter.writeToStream(bitMatrix, "png", bytes);

		} else if ("CODABAR".equals(formato)) {
			bitMatrix = new CodaBarWriter().encode(codigoDeBarrasString, BarcodeFormat.CODABAR, 150, 80, null);
			MatrixToImageWriter.writeToStream(bitMatrix, "png", bytes);

		} else if ("EAN_8".equals(formato)) { // 8 digitos
			bitMatrix = new EAN8Writer().encode(codigoDeBarrasString, BarcodeFormat.EAN_8, 150, 80, null);
			MatrixToImageWriter.writeToStream(bitMatrix, "png", bytes);

		} else if ("QR_CODE".equals(formato)) {
			bitMatrix = new QRCodeWriter().encode(codigoDeBarrasString, BarcodeFormat.QR_CODE, 200, 200);
			MatrixToImageWriter.writeToStream(bitMatrix, "png", bytes);
		} else {
			throw new IllegalArgumentException("Formato do código de barras não encontrado. Usar: UPC_A | UPC_E | EAN_13 | EAN_8 | QR_CODE");
		}
		return bytes.toByteArray();
	}

	@Override
	public Object coerceToBean(Object val, Component comp) {
		return null;
	}

}
