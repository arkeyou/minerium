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
package br.gov.prodigio.comuns.utils;

import java.text.MessageFormat;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.DefaultConfigurationBuilder;

/**
 * Classe responsável por recuperar configurações, em diversas fontes de dados, utilizando o Apache Commons Configuration para o acesso. As fontes de dados devem ser configuradas no arquivo {@link ProConfiguracao#arquivoConfiguracao} e podem ser
 * Properties, XMLs, configurações de Sistema e etc.
 * 
 * @author (Thiago Fonseca)
 */
public class ProConfiguracao {

	/**
	 * Objeto, do Commons Configuration, que possui a composição de todos arquivos de configuração, previamente definidos no {@link ProConfiguracao#arquivoConfiguracao}.
	 */
	private Configuration configuracao;
	/**
	 * Nome do arquivo, onde estão definidos os arquivos que possuem as configurações a serem recuperadas. Este arquivo deve estar no diretório de resources para ser localizado.
	 */
	private final String arquivoConfiguracao = "proConfiguracao.xml";

	/**
	 * Recupera objeto, do Commons Configuration, que possui a composição de todos arquivos de configuração.
	 * 
	 * @return Arquivo de configuração.
	 */
	public Configuration getConfiguracao() {
		try {
			if (this.configuracao == null) {
				DefaultConfigurationBuilder configurationBuilder;
				configurationBuilder = new DefaultConfigurationBuilder(this.arquivoConfiguracao);
				this.configuracao = configurationBuilder.getConfiguration();
			}
		} catch (ConfigurationException e) {
			throw new RuntimeException("Falha de configuracao do sistema.", e);
		}

		return this.configuracao;
	}

	/**
	 * Recupera o valor da propriedade, através do nome.
	 * 
	 * @param nomePropriedade
	 *            - Propriedade a ser pesquisada.
	 * @return Valor da propriedade localizada.
	 */
	public String getPropriedade(String nomePropriedade) {
		return this.getConfiguracao().getString(nomePropriedade);
	}

	/**
	 * Recupera o valor da propriedade, substituindo as tags com os parâmetros informados.
	 * 
	 * @param nomePropriedade
	 *            - Propriedade a ser pesquisada.
	 * @param parametros
	 *            - Parâmetros que serão formatados no valor da propriedade.
	 * @return Valor da propriedade localizada com os parâmetros substituidos.
	 */
	public String getPropriedade(String nomePropriedade, Object... parametros) {
		String valorPropriedade = null;
		String formatoPropriedade = this.getPropriedade(nomePropriedade);

		if (formatoPropriedade != null) {
			valorPropriedade = MessageFormat.format(formatoPropriedade, parametros);
		}

		return valorPropriedade;
	}

}
