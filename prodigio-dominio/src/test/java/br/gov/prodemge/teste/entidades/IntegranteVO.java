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

import java.util.List;

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
public class IntegranteVO extends ProVO {

	private static final long serialVersionUID = 1444706160500213524L;

	private String nome;
	private String descricao;
	private byte[] imagem;
	private BandaVO banda;

	private String pseudonimo;

	private List<InstrumentoVO> instrumentos;

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
	public String getPseudonimo() {
		return pseudonimo;
	}

	public void setPseudonimo(String pseudonimo) {
		this.pseudonimo = pseudonimo;
	}

	@OneToMany(mappedBy = "integrante", targetEntity = InstrumentoVO.class)
	public List<InstrumentoVO> getInstrumentos() {
		return instrumentos;
	}

	public void setInstrumentos(List<InstrumentoVO> instrumentos) {
		this.instrumentos = instrumentos;
	}

	public byte[] getImagem() {
		return imagem;
	}

	public void setImagem(byte[] imagem) {
		this.imagem = imagem;
	}

	@ManyToOne(targetEntity = BandaVO.class)
	@JoinColumn(name = "id_banda")
	public BandaVO getBanda() {
		return banda;
	}

	public void setBanda(BandaVO banda) {
		this.banda = banda;
	}

}
