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


import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zul.Div;
import org.zkoss.zul.Html;
import org.zkoss.zul.Image;
import org.zkoss.zul.Popup;

import br.gov.prodigio.visao.helper.ProHelperView;

public class Help extends Div implements AfterCompose{
	private final String WIDTH = "100%";
	private final String ALIGN = "right";
	private final String STYLE_IMAGE = "cursor:pointer";
	private final String SRC_IMAGE = "/imagens/ajuda.png";
	private final String ID_POPUP = "title";
	private final String POPUP_WIDTH = "30%";
	private final String ZCLASS_POPUP = ".";
	
	private String content;
	private String srcImage;
	private String idPopUp;
	private String style;
	

	@Override
	public void afterCompose() {
		String width = getWidth();
		if(width==null||width.equals("")){
			setWidth(WIDTH);
		}
		
		String align = getAlign();
		if(align==null||align.equals("")){
			setAlign(ALIGN);
		}

		Image image = new Image();
		image.setStyle(style);
		image.setSrc(srcImage);
		
		if(style==null||style.equals("")){
			image.setStyle(STYLE_IMAGE);
		}
		
		if(srcImage==null||srcImage.equals("")){
			image.setSrc(SRC_IMAGE);
		}
				
		appendChild(image);
		image.setParent(this);
		
		Popup popup = new Popup();
		popup.setAttribute("id", idPopUp);
		image.setPopup(idPopUp);
		
		if(idPopUp==null||idPopUp.equals("")){
			popup.setAttribute("id", ID_POPUP);
			image.setPopup(ID_POPUP);
		}
		
		popup.setWidth(POPUP_WIDTH);
		popup.setZclass(ZCLASS_POPUP);
		
		appendChild(popup);
		popup.setParent(this);
		
		final String texto = "<html xmlns='native'>${"+content+"}</html>";
		
		image.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
            @Override
            public void onEvent(Event event) throws Exception {
            	Image image = (Image) event.getTarget();
            	Popup popup = (Popup) image.getParent().getLastChild();
	            popup.open(image,  "after_pointer");
            }
        });
		
		EventListener evtnm = new EventListener() {
			public void onEvent(Event e) throws Exception {
				Executions.getCurrent().createComponentsDirectly(texto, "zhtml",e.getTarget().getLastChild(), null);
			}
		};
		this.addEventListener("onCreate", evtnm);

	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}


	public String getSrcImage() {
		return srcImage;
	}


	public void setSrcImage(String srcImage) {
		this.srcImage = srcImage;
	}


	public String getIdPopUp() {
		return idPopUp;
	}


	public void setIdPopUp(String idPopUp) {
		this.idPopUp = idPopUp;
	}


	public String getStyle() {
		return style;
	}


	public void setStyle(String style) {
		this.style = style;

	}
	
	
	

}
