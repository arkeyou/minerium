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


import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zul.Auxheader;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Layout;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.Window;

import br.gov.prodigio.comuns.utils.StringHelper;
import br.gov.prodigio.controle.ProCtr;


/**
 * 
 * Classe que representa a coluna do cabeçalho do filtro automático usado no listboxDet. Olhar também a classe {@link AuxheadFilter}
 * 
 * 
 * @author p057693
 *
 */
public class AuxheaderFilter extends Auxheader implements AfterCompose{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2943775515742595623L;
	
	protected String campoFiltrado;
	protected String orientacao;
	protected Textbox  inputTextBox;
	protected String inputTextSClass;
	protected Layout layout;
	

	@Override
	public void afterCompose() {		
		final Window window = ProCtr.findWindow(this);
		
		if(StringHelper.isNotEmpty(campoFiltrado)){
			getChildren().clear();			
			if(orientacao!=null && "vertical".equals(orientacao.toLowerCase())){
				layout = new Vlayout();
			}else{
				layout = new Hlayout();
			}
				
			inputTextBox = new Textbox();
			
			if (((AuxheadFilter) getParent()).isBuscaAutomatica()) {
				inputTextBox.setInstant(true);
		
				inputTextBox.addEventListener("onChange", new EventListener<Event>() {
					public void onEvent(Event event) throws Exception {
						ProCtr ctr = (ProCtr) window.getAttribute(window.getId() + "$" + "composer");
						ctr.getBinder().loadComponent(((AuxheadFilter) getParent()).getDetailListBox());
					}
				});			
			}
			
			if (StringHelper.isEmpty(inputTextSClass)) {
				inputTextSClass = "campoFiltro";
			}
			
			inputTextBox.setSclass(inputTextSClass);
			layout.appendChild(inputTextBox);
			
			appendChild(layout);												
		}						
	}


	/**
	 * Representa um atributo do objeto utilizado na lista do detalhe que será usado como criterio. Deve ser do tipo String.
	 * 
	 * @return
	 */
	public String getCampoFiltrado() {
		return campoFiltrado;
	}

	/**
	 * Representa um atributo do objeto utilizado na lista do detalhe que será usado como criterio. Deve ser do tipo String.
	 * 
	 * @param campoFiltrado Ex: 'papel.modulo.nome'
	 */
	public void setCampoFiltrado(String campoFiltrado) {
		this.campoFiltrado = campoFiltrado;
	}

	
	/**
	 * Os valores possíveis sao: "vertical" ou "horizontal"
	 * 
	 * 
	 * @return
	 */
	public String getOrientacao() {
		return orientacao;
	}

	/**
	 * Os valores possíveis sao: "vertical" ou "horizontal"
	 * 
	 * @param orientacao
	 */
	public void setOrientacao(String orientacao) {
		this.orientacao = orientacao;
	}

	/**
	 * 
	 * Componente Textbox de onde será extraído o valor para ser usado no filtro 
	 * 
	 * @return
	 */
	public Textbox getInputTextBox() {
		return inputTextBox;
	}


	public void setInputTextBox(Textbox inputTextBox) {
		this.inputTextBox = inputTextBox;
	}


	public Layout getLayout() {
		return layout;
	}


	public void setLayout(Layout layout) {
		this.layout = layout;
	}


	/**
	 * 
	 * Classe CSS utilizada no TextBox
	 * 
	 * @return
	 */
	public String getInputTextsClass() {
		return inputTextSClass;
	}


	/**
	 * 
	 * Classe CSS utilizada no TextBox
	 * 
	 * @param inputTextsClass
	 */
	public void setInputTextsClass(String inputTextsClass) {
		this.inputTextSClass = inputTextsClass;
	}
	
	
	
	
	
	

}
