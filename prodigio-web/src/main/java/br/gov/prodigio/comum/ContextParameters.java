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
package br.gov.prodigio.comum;

import java.io.File;

public interface ContextParameters {
	
	public static final String DIRETORIO_DA_APLICACAO = "diretorioDaAplicacao";
	public static final String COMANDOS_VINCULADOS = "comandosVinculadosAdseg";
	public static final String PREFIX_ENUM = "listaDe";
	public static final String ENTIDADES_CAMINHO = "WEB-INF"+File.separator+"classes"+File.separator+"br"+File.separator+"com"+File.separator+"prodigio"+File.separator+"entidades";
	public static final String ENUMERACAO_CAMINHO = "WEB-INF"+File.separator+"classes"+File.separator+"br"+File.separator+"com"+File.separator+"prodigio"+File.separator+"entidades"+File.separator+"enumeracao";
	public static final String INTERFACE_DE_NEGOCIO = "interfaceDeNegocio";
	public static final String ENTIDADES_CAMINHO_LIB = "WEB-INF"+File.separator+"lib";
	public static final String ENTIDADES_PRODIGIO_LIB = "prodigio-interfaces.jar";
	public static final String PACOTE_ENTIDADES_APP = "br.gov.prodemge.grp.contabil.entidades";
	public static final String PACOTE_ENTIDADES_PRODIGIO = "br.gov.prodigio.entidades";
}
