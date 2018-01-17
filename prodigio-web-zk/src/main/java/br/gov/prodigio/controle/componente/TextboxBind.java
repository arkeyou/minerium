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

import java.util.HashMap;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import br.gov.prodigio.controle.ProCtr;

public class TextboxBind extends Textbox implements AfterCompose, FieldValidator {

	private static final long serialVersionUID = 7424727966318992050L;
	private String mask;
	private Boolean validarRegra;
	private Boolean validarCampo;
	private String nomeDoObjeto;
	private Object object = null;
	private Boolean upper = true;
	private Boolean paste = true;
	private String validarQuando = "";

	@Override
	public void afterCompose() {
		Window window = ProCtr.findWindow(this);
		ProCtr ctr = (ProCtr) window.getAttribute(window.getId() + "$" + "composer");
		ctr.getProAnnotateDataBinderHelper().adicionaAnotacaoParaCampoTexto(this);
		GenericConstraint.configuraConstraint(this);
		this.setSclass("form-control");
	}

	public Boolean getPaste() {
		return paste;
	}

	public void setPaste(Boolean paste) {
		this.paste = paste;
	}

	public String getMask() {
		return mask;
	}

	public void setMask(String mask) {
		this.mask = mask;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.gov.prodigio.controle.componente.FieldValidator#getValidarRegra()
	 */
	@Override
	public Boolean getValidarRegra() {
		return validarRegra;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.gov.prodigio.controle.componente.FieldValidator#setValidarRegra(java.lang.Boolean)
	 */
	@Override
	public void setValidarRegra(Boolean validarRegra) {
		this.validarRegra = validarRegra;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.gov.prodigio.controle.componente.FieldValidator#getValidarCampo()
	 */
	@Override
	public Boolean getValidarCampo() {
		return validarCampo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.gov.prodigio.controle.componente.FieldValidator#setValidarCampo(java.lang.Boolean)
	 */
	@Override
	public void setValidarCampo(Boolean validarCampo) {
		this.validarCampo = validarCampo;
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

	private Object objectPai;
	private Object rootObject;
	private String converter;
	private String dependeDoComponente;
	private Map<String, Component> componentsDependentes = null;
	private String identificador;
	private String atributoQueSeraVisualizado;

	public Boolean getUpper() {
		return upper;
	}

	public void setUpper(Boolean upper) {
		this.upper = upper;
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

	public String getDependeDoComponente() {
		return dependeDoComponente;
	}

	public void setDependeDoComponente(String dependeDoComponent) {
		this.dependeDoComponente = dependeDoComponent;
	}

	public Map<String, Component> recuperaComponentesDependentes() {
		return componentsDependentes;
	}

	public void addComponentesDependentes(String nomeComponent, Component component) {
		if (this.componentsDependentes == null) {
			this.componentsDependentes = new HashMap<String, Component>();
		}
		this.componentsDependentes.put(nomeComponent, component);
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

	public String getValidarQuando() {
		return validarQuando;
	}

	public void setValidarQuando(String validarQuando) {
		this.validarQuando = validarQuando;
	}

}
