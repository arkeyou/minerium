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

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.image.AImage;
import org.zkoss.zk.ui.Component;
import org.zkoss.zkplus.databind.TypeConverter;
import org.zkoss.zul.Image;

import br.gov.prodigio.comuns.constantes.Constantes;

public class ImageUploadConverter implements TypeConverter {
	private static final Logger log = LoggerFactory.getLogger(ImageUploadConverter.class);

	@Override
	public Object coerceToBean(Object val, Component comp) {
		ImageUpload imagemBind = (ImageUpload) comp;
		return val/*imagemBind.getContent().getByteData()*/;
	}

	@Override
	public Object coerceToUi(Object val, Component comp) {
		try {
			if (val != null) {
				AImage img = new AImage("p.jpg", ((byte[]) val));
				((Image) comp).setContent(img);
			} else {
				AImage img2 = new AImage(comp.getDesktop().getWebApp().getResource(Constantes.IMAGEM_DEFAULT));
				return img2.getByteData();
			}
		} catch (IOException e) {
			log.error("Erro ao valor do objeto para tela ", e);
		}
		return val;
	}

}
