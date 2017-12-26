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
package br.gov.prodigio.persistencia.mainframe;

import java.lang.reflect.Field;
import java.util.List;

import br.gov.prodigio.comuns.exception.BrokerException;

public interface IMainFrameDAOHelper {

	public abstract void preencheListaComCamposAnotadosComReceive(Object obj, List<Field> lista) throws Exception;

	public abstract void preencheCamposDoObjetoComValoresRecebidos(List<Field> fields, Object obj, StringBuffer linha) throws Exception;

	public abstract void atribuiValor(Object value, Field field, Object obj) throws Exception;

	public abstract void preencheListaComCamposAnotadosComSend(Object obj, List<Field> lista) throws Exception;

	public abstract void preencheCamposDoObjetoComValoresAEnviar(List<Field> fields, Object obj, StringBuffer result) throws Exception;

	public abstract Object sendReceive(Object objeto, int posicaoInicial, String... codigos) throws BrokerException, Exception;

	public abstract Object sendReceive(Object objeto) throws BrokerException, Exception;

	public abstract Object sendReceive(Object objeto, int posicaoInicial) throws BrokerException, Exception;

}