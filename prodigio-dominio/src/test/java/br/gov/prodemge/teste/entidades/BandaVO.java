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
package br.gov.prodemge.teste.entidades;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import br.gov.prodigio.entidades.ProVO;

@Entity
public class BandaVO extends ProVO {

	private static final long serialVersionUID = 1444706160500213524L;

	private PaisVO origem;
	private String nome;
	private String descricao;
	private String formacao;
	private Date fundacao;
	private byte[] logo;

	private Integer numeroIntegrantes;

	private EstiloMusicalVO estiloMusical;
	private List<IntegranteVO> integrantes = new ArrayList<IntegranteVO>();
	private Set<AlbumVO> discografia;

	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return super.getId();
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Transient
	public String getFormacao() {
		return formacao;
	}

	public void setFormacao(String formacao) {
		this.formacao = formacao;
	}

	@ManyToOne
	@JoinColumn(name = "ID_ESTILO_MUSICAL")
	public EstiloMusicalVO getEstiloMusical() {
		return estiloMusical;
	}

	public void setEstiloMusical(EstiloMusicalVO estiloMusical) {
		this.estiloMusical = estiloMusical;
	}

	@OneToMany(mappedBy = "banda", targetEntity = IntegranteVO.class)
	public List<IntegranteVO> getIntegrantes() {
		return integrantes;
	}

	public void setIntegrantes(List<IntegranteVO> integrantes) {
		this.integrantes = integrantes;
	}

	public byte[] getLogo() {
		return logo;
	}

	public void setLogo(byte[] logo) {
		this.logo = logo;
	}

	public Date getFundacao() {
		return fundacao;
	}

	public void setFundacao(Date fundacao) {
		this.fundacao = fundacao;
	}

	public Integer getNumeroIntegrantes() {
		return numeroIntegrantes;
	}

	public void setNumeroIntegrantes(Integer numeroIntegrantes) {
		this.numeroIntegrantes = numeroIntegrantes;
	}

	@OneToMany(mappedBy = "banda", targetEntity = AlbumVO.class)
	public Set<AlbumVO> getDiscografia() {
		return discografia;
	}

	public void setDiscografia(Set<AlbumVO> discografia) {
		this.discografia = discografia;
	}

	@ManyToOne
	@JoinColumn(name = "ID_PAIS")
	public PaisVO getOrigem() {
		return origem;
	}

	public void setOrigem(PaisVO origem) {
		this.origem = origem;
	}

}
