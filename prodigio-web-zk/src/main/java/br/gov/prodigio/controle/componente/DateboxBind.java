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
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Window;

import br.gov.prodigio.controle.ProCtr;

public class DateboxBind extends Datebox implements AfterCompose, FieldValidator {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7424727966318992050L;
	private Boolean validarRegra;
	private Boolean validarCampo;
	private String nomeDoObjeto;
	private Object object = null;
	private Object objectPai;
	private Object rootObject;
	private String converter;
	private String identificador;
	private String atributoQueSeraVisualizado;
	private String validarQuando = "";

	@Override
	public void afterCompose() {
		Window window = ProCtr.findWindow(this);
		ProCtr ctr = (ProCtr) window.getAttribute(window.getId() + "$" + "composer");
		ctr.getProAnnotateDataBinderHelper().adicionaAnotacaoParaCampoData(this);
		GenericConstraint.configuraConstraint(this);
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

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public Object getObjectPai() {
		return objectPai;
	}

	public void setObjectPai(Object objectPai) {
		this.objectPai = objectPai;
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

	public String getValidarQuando() {
		return validarQuando;
	}

	public void setValidarQuando(String validarQuando) {
		this.validarQuando = validarQuando;
	}
}
