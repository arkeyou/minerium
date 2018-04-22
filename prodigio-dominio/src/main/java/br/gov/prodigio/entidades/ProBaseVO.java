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

package br.gov.prodigio.entidades;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;

@MappedSuperclass
public class ProBaseVO implements Serializable {

	private static final long serialVersionUID = -6878025554640409904L;
	private Long id;
	private String cdLoginMovimentacao;
	private String ipMovimentacao;
	private Date tsMovimentacao;
	private String tpOperacao;
	private int nrVersao;
	private boolean excluindoDetalhe = false;
	private String titulo;
	private StatusDoRegistro statusDoRegistro;
	private Map<String,Object> atributosDeContexto = new HashMap<String, Object>();

	@Transient
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Transient
	public String getCdLoginMovimentacao() {
		return cdLoginMovimentacao;
	}

	public void setCdLoginMovimentacao(String cdLoginMovimentacao) {
		this.cdLoginMovimentacao = cdLoginMovimentacao;
	}

	@Transient
	public String getIpMovimentacao() {
		return ipMovimentacao;
	}

	public void setIpMovimentacao(String ipMovimentacao) {
		this.ipMovimentacao = ipMovimentacao;
	}

	@Transient
	public Date getTsMovimentacao() {
		return tsMovimentacao;
	}

	public void setTsMovimentacao(Date tsMovimentacao) {
		this.tsMovimentacao = tsMovimentacao;
	}

	@Transient
	public String getTpOperacao() {
		return tpOperacao;
	}

	public void setTpOperacao(String tpOperacao) {
		this.tpOperacao = tpOperacao;
	}

	@Version
	@Column(name="NR_VERSAO")
	public int getNrVersao() {
		return nrVersao;
	}

	public void setNrVersao(int nrVersao) {
		this.nrVersao = nrVersao;
	}

	@Transient
	public boolean isExcluindoDetalhe() {
		return excluindoDetalhe;
	}

	public void setExcluindoDetalhe(boolean excluindoDetalhe) {
		this.excluindoDetalhe = excluindoDetalhe;
	}

	@Transient
	public String getTitulo() {
		return titulo + "";
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	@Enumerated(EnumType.ORDINAL)
	@Column(name = "ST_REGISTRO")
	public StatusDoRegistro getStatusDoRegistro() {
		return statusDoRegistro;
	}

	public void setStatusDoRegistro(StatusDoRegistro statusDoRegistro) {
		this.statusDoRegistro = statusDoRegistro;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		if (obj.getClass().equals(this.getClass())) {
			if (this.getId() != null && ((ProBaseVO) obj) != null) {
				return this.getId().equals(((ProBaseVO) obj).getId());
			}
		}
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		if (getId() != null)
			return getId().intValue();
		return super.hashCode();
	}
	
	@Transient
	public void addAtributo(String key, Object value){
		this.atributosDeContexto.put(key, value);
	}

	@Transient
	public Object getAtributo(String key){
		return this.atributosDeContexto.get(key);
	}
	
	@Transient
	public  Map<String,Object> getAtributosDeContexto(){
		return Collections.unmodifiableMap(this.atributosDeContexto);
	}
	
}
