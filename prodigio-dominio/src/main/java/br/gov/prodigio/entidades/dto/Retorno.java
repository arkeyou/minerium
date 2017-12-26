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
package br.gov.prodigio.entidades.dto;

import java.text.SimpleDateFormat;
import java.util.Date;


public class Retorno {

	private Integer codigoMensagem;
	private String mensagem;
	private Object objeto;
	private String dataServidor;
	
	public Retorno(){
		
	}

	public Retorno(Integer codigoMensagem, String mensagem, Object objeto) {
		this.codigoMensagem = codigoMensagem;
		this.mensagem = mensagem;
		this.objeto = objeto;
	}

	public Integer getCodigoMensagem() {
		return codigoMensagem;
	}

	public void setCodigoMensagem(Integer codigoMensagem) {
		this.codigoMensagem = codigoMensagem;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public Object getObjeto() {
		return objeto;
	}

	public void setObjeto(Object objeto) {
		this.objeto = objeto;
	}


	public String getDataServidor() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		dataServidor = format.format(new Date());
		return dataServidor;
	}


	public void setDataServidor(String dataServidor) {
		this.dataServidor = dataServidor;
	}		

}
