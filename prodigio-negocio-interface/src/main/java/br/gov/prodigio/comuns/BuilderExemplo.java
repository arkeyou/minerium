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
package br.gov.prodigio.comuns;

import java.io.Serializable;

import br.gov.prodigio.entidades.ProBaseVO;

public class BuilderExemplo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 184812676759489844L;
	ProBaseVO exemplo;
	ProBaseVO exemplo2;
	int primeiroRegistro;
	int quantidadeDeRegistros;
	String[] projecoes;

	public ProBaseVO getExemplo() {
		return exemplo;
	}

	public BuilderExemplo setExemplo(ProBaseVO exemplo) {
		this.exemplo = exemplo;
		return this;
	}

	public ProBaseVO getExemplo2() {
		return exemplo2;
	}

	public BuilderExemplo setExemplo2(ProBaseVO exemplo2) {
		this.exemplo2 = exemplo2;
		return this;
	}

	public int getPrimeiroRegistro() {
		return primeiroRegistro;
	}

	public BuilderExemplo setPrimeiroRegistro(int primeiroRegistro) {
		this.primeiroRegistro = primeiroRegistro;
		return this;
	}

	public int getQuantidadeDeRegistros() {
		return quantidadeDeRegistros;
	}

	public BuilderExemplo setQuantidadeDeRegistros(int quantidadeDeRegistros) {
		this.quantidadeDeRegistros = quantidadeDeRegistros;
		return this;
	}

	public String[] getProjecoes() {
		return projecoes;
	}

	public BuilderExemplo setProjecoes(String[] projecoes) {
		this.projecoes = projecoes;
		return this;
	}
}
