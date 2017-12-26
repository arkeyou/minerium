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

import java.util.ArrayList;
import java.util.List;

import br.gov.prodemge.ssc.interfaces.base.IUnidadeBase;
import br.gov.prodemge.ssc.interfaces.base.IUsuarioBase;

public class ArquivoVO extends ProBaseVO {
	private boolean isArquivo = false;
	private String nome = "";
	private String diretorio = "";
	private String caminhoAbsoluto = "";
	private String caminhoVirtual = "";
	private byte[] binario = null;
	private IUsuarioBase usuarioVO;
	private IUnidadeBase unidadeAdministrativaVO;
	private List<ArquivoVO> arquivosFilhos = null;
	private boolean filhosCarregados;

	public void addArquivo(ArquivoVO arquivoVO) {
		if (arquivosFilhos == null) {
			arquivosFilhos = new ArrayList<ArquivoVO>();
		}
		arquivosFilhos.add(arquivoVO);
		filhosCarregados = true;
	}

	public boolean isDiretorio() {
		return isArquivo;
	}

	public void setDiretorio(boolean isArquivo) {
		this.isArquivo = isArquivo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String name) {
		this.nome = name;
	}

	public List<? extends ArquivoVO> getArquivosFilhos() {
		return arquivosFilhos;
	}

	public void setArquivosFilhos(List<ArquivoVO> arquivosFilhos) {
		if (arquivosFilhos == null) {
			filhosCarregados = false;
		} else {
			filhosCarregados = true;
		}
		this.arquivosFilhos = arquivosFilhos;
	}

	public String getDiretorio() {
		return diretorio;
	}

	public void setDiretorio(String diretorio) {
		this.diretorio = diretorio;
	}

	public String getCaminhoAbsoluto() {
		return caminhoAbsoluto;
	}

	public void setCaminhoAbsoluto(String caminhoAbsoluto) {
		this.caminhoAbsoluto = caminhoAbsoluto;
	}

	public String getCaminhoVirtual() {
		return caminhoVirtual;
	}

	public void setCaminhoVirtual(String caminhoVirtual) {
		this.caminhoVirtual = caminhoVirtual;
	}

	public IUsuarioBase getUsuarioVO() {
		return usuarioVO;
	}

	public void setUsuarioVO(IUsuarioBase usuarioVO) {
		this.usuarioVO = usuarioVO;
	}

	public IUnidadeBase getUnidadeAdministrativaVO() {
		return unidadeAdministrativaVO;
	}

	public void setUnidadeAdministrativaVO(IUnidadeBase unidadeAdministrativaVO) {
		this.unidadeAdministrativaVO = unidadeAdministrativaVO;
	}

	public boolean isFilhosCarregados() {
		return filhosCarregados;
	}

	public void setFilhosCarregados(boolean filhosCarregados) {
		this.filhosCarregados = filhosCarregados;
	}

	public byte[] getBinario() {
		return binario;
	}

	public void setBinario(byte[] binario) {
		this.binario = binario;
	}
}
