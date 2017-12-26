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

public enum StatusDoRegistro {
	ATIVO("ATIVO", "/imagens/stock_lock_open.png"), BLOQUEADO("BLOQUEADO", "/imagens/stock_lock.png"), INATIVO(
			"INATIVO", "/imagens/stock_lock.png"), EXCLUIDO("EXCLUIDO", "/imagens/stock_lock.png");
	String descricao;
	String imagem;

	StatusDoRegistro(String descricao, String imagem) {
		this.descricao = descricao;
		this.imagem = imagem;
	}

	@Override
	public String toString() {
		return descricao;
	}

	public String getImagem() {
		return imagem;
	}

	public void setImagem(String imagem) {
		this.imagem = imagem;
	}
}
