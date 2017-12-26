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

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@MappedSuperclass
public class ProVO extends ProBaseVO implements Comparable<ProVO> {

	private static final long serialVersionUID = 2872638127758612957L;

	private String dsSituacao = null;
	private byte[] imagem;

	public interface SITUACAO_DO_REGISTRO {
		public String EM_EDICAO = "EM_EDICAO";
		public String CONCLUIDO = "CONCLUIDO";
		public String APROVADO = "APROVADO";
	}

	@Column(name="CD_LOGIN_MOVIMENTACAO",length = 11, nullable = false)
	public String getCdLoginMovimentacao() {
		return super.getCdLoginMovimentacao();
	}



	@Column(name="IP_MOVIMENTACAO",length = 40, nullable = false)
	public String getIpMovimentacao() {
		return super.getIpMovimentacao();
	}


	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="TS_MOVIMENTACAO", nullable = false)
	public Date getTsMovimentacao() {
		return super.getTsMovimentacao();
	}



	@Column(name="TP_OPERACAO", length = 1, nullable = false)
	public String getTpOperacao() {
		return super.getTpOperacao();
	}

	
	@Column(name="DS_SITUACAO",length = 50, nullable = false)
	public String getDsSituacao() {
		return dsSituacao;
	}
	

	public void setDsSituacao(String dsSituacao) {
		this.dsSituacao = dsSituacao;
	}

	@Transient
	public byte[] getImagem() {
		return imagem;
	}

	public void setImagem(byte[] imagem) {
		this.imagem = imagem;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		if (obj instanceof ProVO) {
			if (this.getId() != null && obj != null) {
				return this.getId().equals(((ProVO) obj).getId());
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		if (getId() != null)
			return getId().intValue();
		return super.hashCode();
	}

	public int compareTo(ProVO o) {
		if (getId() != null && o != null && o.getId() != null)
			return getId().compareTo(o.getId());
		return -1;
	}
	
	

}
