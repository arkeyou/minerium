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

import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;
import org.zkoss.zul.Window;

import br.gov.prodigio.controle.ProCtr;

public class CodigoDeBarrasBind extends Div implements AfterCompose, FieldValidator {

	private static final long serialVersionUID = 4353376494066356771L;
	private Object objectPai;
	private Object rootObject;
	private String converter;
	private String identificador;
	private String atributoQueSeraVisualizado;
	private String nomeDoObjeto;
	private Object object;
	private Image imagem;
	private String width;
	private String height;
	private String formato = "QR_CODE"; // UPC_A | UPC_E | EAN_13 | EAN_8 | QR_CODE

	@SuppressWarnings("rawtypes")
	public void afterCompose() {
		imagem = new Image();
		imagem.setWidth(this.getWidth());
		imagem.setHeight(this.getHeight());
		Window window = ProCtr.findWindow(this);
		ProCtr ctr = (ProCtr) window.getAttribute(window.getId() + "$" + "composer");
		ctr.getProAnnotateDataBinderHelper().adicionaAnotacaoParaBarcodeImagem(this);
		this.appendChild(imagem);

	}

	public String getNomeDoObjeto() {
		return nomeDoObjeto;
	}

	public void setNomeDoObjeto(String nomeDoObjeto) {
		this.nomeDoObjeto = nomeDoObjeto;
	}

	@Override
	public Object getObject() {
		return this.object;
	}

	@Override
	public void setObject(Object o) {
		this.object = o;
	}

	public Object getObjectPai() {
		return objectPai;
	}

	public void setObjectPai(Object objectPai) {
		this.objectPai = objectPai;
	}

	

	public Image getImagem() {
		return imagem;
	}

	public void setImagem(Image imagem) {
		this.imagem = imagem;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	@Override
	public Object getRootObject() {
		return this.rootObject;
	}

	@Override
	public void setRootObject(Object rootObject) {
		this.rootObject = rootObject;
	}

	@Override
	public String getConverter() {
		return this.converter;
	}

	@Override
	public void setConverter(String converter) {
		this.converter = converter;
	}

	@Override
	public String getDependeDoComponente() {
		return null;
	}

	@Override
	public void setDependeDoComponente(String dependeDoComponent) {

	}

	@Override
	public void addComponentesDependentes(String nomeDoObjeto, Component field) {

	}

	@Override
	public Map<String, Component> recuperaComponentesDependentes() {
		return null;
	}

	@Override
	public void setIdentificador(String identificador) {
		this.identificador = identificador;

	}

	@Override
	public String getIdentificador() {
		return this.identificador;
	}

	public String getAtributoQueSeraVisualizado() {
		return atributoQueSeraVisualizado;
	}

	public void setAtributoQueSeraVisualizado(String atributoQueSeraVisualizado) {
		this.atributoQueSeraVisualizado = atributoQueSeraVisualizado;
	}

	public String getFormato() {
		return formato;
	}

	public void setFormato(String formato) {
		this.formato = formato;
	}

	@Override
	public Boolean getValidarRegra() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setValidarRegra(Boolean validarRegra) {
		// TODO Auto-generated method stub

	}

	@Override
	public Boolean getValidarCampo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setValidarCampo(Boolean validarCampo) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getValidarQuando() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setValidarQuando(String validarQuando) {
		// TODO Auto-generated method stub

	}

}
