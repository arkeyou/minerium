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

import java.util.Map;
import java.util.Set;

import br.gov.prodigio.entidades.ProBaseVO;

public interface IProFacade {

	public static final String NOME_BEAN = "ProFacadeImp";
	public static final String NOME_BEAN_LOCAL = "ejb/ProFace/local";

	public abstract void excluir(ProBaseVO t) throws Exception;

	public abstract void excluirDetalhe(ProBaseVO t) throws Exception;

	public abstract Object gravar(ProBaseVO t) throws Exception;

	public abstract void editar(ProBaseVO t) throws Exception;

	public abstract void aprovar(ProBaseVO t) throws Exception;

	public abstract Object concluir(ProBaseVO t, Map parametros) throws Exception;

	public abstract Object executar(ProBaseVO t, String evento) throws Exception;

	public abstract Object recuperar(ProBaseVO t, String evento) throws Exception;

	public abstract void validarCampo(ProBaseVO t, String campo) throws Exception;

	public abstract <E extends ProBaseVO> Set<E> listar(E app) throws Exception;

	public abstract <E extends ProBaseVO> Set<E> listar(E app, String query) throws Exception;

	public abstract <E extends ProBaseVO> E recuperaObjeto(E AppBaseVO) throws Exception;

	public abstract <E extends ProBaseVO> E recuperaPorChaveNatural(E AppBaseVO, String nomeDaChave) throws Exception;

	public <E extends ProBaseVO> Set<E> listarBaseadoNoExemplo(E example, E example2, int primeiroRegistro, int quantidadeDeRegistros, String... projecoes) throws Exception;

	public <E extends ProBaseVO> Set<E> listarBaseadoNoExemplo(E example, E example2, E pojo, String... projecoes) throws Exception;

	/**
	 * Recupera objetos com retorno em rest.
	 * 
	 * @param obj
	 *            Objeto que será pesquisado, (<i><b>"O id do objeto deve estar preenchido"</b></i>).
	 * @param exclusaoJson
	 *            Campos que serão excluidos
	 * @return objeto pesquisado no formato rest.
	 * @throws Exception
	 */
	public String recuperaObjetoRest(ProBaseVO obj, String[] exclusaoJson) throws Exception;

}