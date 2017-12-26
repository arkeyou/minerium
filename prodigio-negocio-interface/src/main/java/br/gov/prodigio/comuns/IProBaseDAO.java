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

import java.util.List;
import java.util.Map;
import java.util.Set;

import br.gov.prodigio.entidades.ProBaseVO;

public interface IProBaseDAO {

	/**
	 * Persiste o objeto no banco de dados
	 * 
	 * Se o id do objeto for nulo, adiciona ele no banco de dados. Se o id não for nulo, atualiza a entidade correspondente no banco de dados com as propriedades desta entidade.
	 * 
	 * @param objeto
	 *            a ser salvo
	 * @return id da entidade persistida
	 * @throws Exception
	 */
	public abstract Object gravar(Object objeto) throws Exception;

	/**
	 * Atualiza determinados atributos de um ou mais objetos
	 * 
	 * @param classe
	 *            dos objetos a seream atualizados
	 * @param mapaDeAtributosValores
	 *            atributos que seram atualizados
	 * 
	 * @param ids
	 *            identificador dos objetos que seram atualizados
	 * @throws Exception
	 */
	public abstract void updateEmLote(Class classe, Map<String, Object> mapaDeAtributosValores, List<Long> ids) throws Exception;

	public abstract void concluir(Object objeto, Map parametros) throws Exception;

	/**
	 * @description Remove o objeto especificado do banco de dados
	 * 
	 * @param objeto
	 *            objeto a ser removido
	 * @throws Exception
	 * @version 1.0
	 */
	public abstract void excluir(Object objeto) throws Exception;

	public abstract Set<ProBaseVO> listar(ProBaseVO vo) throws Exception;

	public abstract Set<ProBaseVO> listar(ProBaseVO appBaseVO, String quaryNamed) throws Exception;

	public abstract <E extends ProBaseVO> E recuperaObjetoPorChaveNatural(E vo, String nomeDaChave) throws Exception;

	public abstract <E extends ProBaseVO> E recuperaObjeto(E vo) throws Exception;

	public abstract <E extends ProBaseVO> E recuperaObjetoLazy(E vo) throws Exception;

	public abstract <E extends ProBaseVO> String recuperaObjetoRest(E vo, String[] exclusaoJson) throws Exception;

	public abstract Set<ProBaseVO> listarBaseadoNoExemplo(ProBaseVO exemplo, ProBaseVO exemplo2, int primeiroRegistro, int quantidadeDeRegistros, String... projecoes) throws Exception;

	public abstract Set<ProBaseVO> listarBaseadoNoExemplo(ProBaseVO exemplo, ProBaseVO exemplo2, ProBaseVO pojo, String... projecoes) throws Exception;

	public void iniciarTransacao() throws Exception;

	void confirmarTransacao() throws Exception;

	void limparTransacao();

	void cancelarTransacao(Throwable e) throws Exception;

}
