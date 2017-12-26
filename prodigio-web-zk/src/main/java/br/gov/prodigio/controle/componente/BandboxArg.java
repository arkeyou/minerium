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
import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Window;

import br.gov.prodigio.controle.ProCtr;

public class BandboxArg extends Bandbox implements AfterCompose, FieldValidator {

	/**
     * 
     */
	private static final long serialVersionUID = 1140459631529658907L;

	private String widthList;
	private String heightList;
	private String nomeDoObjeto;
	private String atributoQueSeraVisualizado;
	private String labelValorList;
	private Listbox listbox;
	private Object object;
	private Object objectPai;
	private String metodoSelecao;
	private String metodoFiltro;
	private String dependeDoBandBox;
	private Map<String, BandboxArg> bandboxbindsDependentes = new HashMap<String, BandboxArg>();
	private Boolean validarRegra;

	private Boolean validarCampo;

	private Object rootObject;

	private String converter;

	private String identificador;

	private String validarQuando = "";

	public BandboxArg() {
		super();
	}

	@Override
	public void afterCompose() {
		this.setMold("rounded");
		Window window = ProCtr.findWindow(this);
		ProCtr ctr = (ProCtr) window.getAttribute(window.getId() + "$" + "composer");
		// ctr.adicionaElementosNoBandboxArg(this);
		GenericConstraint constraint = new GenericConstraint();
		this.setConstraint(constraint);
		/*
		 * Component parent = this.getParent(); while(!parent.getId().equals("manutencao")){ parent = parent.getParent(); } String aninhado = getDependeDoBandBox(); if(aninhado!=null&&!"".equals(aninhado)){ BandboxArg bandboxbindPai = (BandboxArg)
		 * recuperaBandboxParaAninhar(parent, aninhado); bandboxbindPai.getBandboxbindsDependentes().put(this.getNomeDoObjeto(), this); this.setAttribute(aninhado, bandboxbindPai, Component.COMPONENT_SCOPE); }
		 */
	}

	public String getWidthList() {
		return widthList;
	}

	public void setWidthList(String widthList) {
		this.widthList = widthList;
	}

	public String getHeightList() {
		return heightList;
	}

	public void setHeightList(String heightList) {
		this.heightList = heightList;
	}

	public String getLabelValorList() {
		return labelValorList;
	}

	public void setLabelValorList(String labelValorList) {
		this.labelValorList = labelValorList;
	}

	public String getNomeDoObjeto() {
		return nomeDoObjeto;
	}

	public void setNomeDoObjeto(String nomeDoObjeto) {
		this.nomeDoObjeto = nomeDoObjeto;
	}

	public Listbox getListbox() {
		return listbox;
	}

	public void setListbox(Listbox listbox) {
		this.listbox = listbox;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	@Override
	public void onChildAdded(Component child) {
		super.onChildAdded(child);
	}

	public Object getObjectPai() {
		return objectPai;
	}

	public void setObjectPai(Object objectPai) {
		this.objectPai = objectPai;
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

	private Component recuperaBandboxParaAninhar(Component com, String nomeDoObjeto) {
		List children = com.getChildren();
		Component band = null;
		for (Object object : children) {
			if (object instanceof BandboxArg) {
				if (((BandboxArg) object).getNomeDoObjeto().equals(nomeDoObjeto)) {
					band = (BandboxArg) object;
					break;
				}
			} else {
				band = recuperaBandboxParaAninhar((Component) object, nomeDoObjeto);
				if (band != null) {
					break;
				}
			}
		}
		return band;
	}

	public String getMetodoSelecao() {
		return metodoSelecao;
	}

	public String getMetodoFiltro() {
		return metodoFiltro;
	}

	public void setMetodoSelecao(String metodoSelecao) {
		this.metodoSelecao = metodoSelecao;
	}

	public void setMetodoFiltro(String metodoFiltro) {
		this.metodoFiltro = metodoFiltro;
	}

	public String getAtributoQueSeraVisualizado() {
		return atributoQueSeraVisualizado;
	}

	public void setAtributoQueSeraVisualizado(String atributoVisualizado) {
		this.atributoQueSeraVisualizado = atributoVisualizado;
	}

	public String getDependeDoBandBox() {
		return dependeDoBandBox;
	}

	public void setDependeDoBandBox(String dependeDoBandBox) {
		this.dependeDoBandBox = dependeDoBandBox;
	}

	public Map<String, BandboxArg> getBandboxbindsDependentes() {
		return bandboxbindsDependentes;
	}

	public void setBandboxbindsDependentes(Map<String, BandboxArg> bandboxbindsDependentes) {
		this.bandboxbindsDependentes = bandboxbindsDependentes;
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

	public String getValidarQuando() {
		return validarQuando;
	}

	public void setValidarQuando(String validarQuando) {
		this.validarQuando = validarQuando;
	}

}
