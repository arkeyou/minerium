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
package br.gov.prodigio.comuns.excecoes;

import java.util.HashMap;
import java.util.Map;

import br.gov.prodigio.comuns.exception.ProBaseException;

public class ViolacaoDeRegraEx extends ProBaseException {

	private static final long serialVersionUID = -681184136758849271L;
	private String mensagem = "";
	private Map<String, String> campos = new HashMap<>();

	public ViolacaoDeRegraEx() {
		super();
	}

	public ViolacaoDeRegraEx(String msg) {
		super(msg);
		mensagem = msg;
	}

	public ViolacaoDeRegraEx(Map<String, String> campos) {
		super("Erros!");
		this.campos = campos;

	}

	@Override
	public String getMessage() {
		return mensagem;
	}

	public Map<String, String> getMensagensParaCamposEspecificos() {
		return campos;
	}
}
