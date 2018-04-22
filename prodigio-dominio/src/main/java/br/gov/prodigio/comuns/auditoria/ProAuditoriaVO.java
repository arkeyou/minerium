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
package br.gov.prodigio.comuns.auditoria;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

//import org.hibernate.envers.RevisionTimestamp;

@MappedSuperclass
public class ProAuditoriaVO implements Serializable {

	private static final long serialVersionUID = 4423672590551435863L;
	private Long id;
	private String cdLoginUsuario;
	private long timestamp;

	@Transient
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Transient
	public String getCdLoginUsuario() {
		return cdLoginUsuario;
	}

	public void setCdLoginUsuario(String cdLoginUsuario) {
		this.cdLoginUsuario = cdLoginUsuario;
	}

//	@RevisionTimestamp
	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

}