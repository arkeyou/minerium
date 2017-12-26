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

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CLASSE UTILITARIA QUE PERMITE RECUPERAR FACHADAS EJB.
 * 
 * @author p061992
 * 
 */
public class FacadeHelper {

	private static final Logger log = LoggerFactory.getLogger(FacadeHelper.class);

	/**
	 * RECUPERA FACHADA EJB REMOTA ENTRE PROJETOS NO MESMO CONTAINER.
	 * 
	 * @param nomeLookup
	 *            LOOKUP EJB QUE SERA RECUPERADO
	 * @return EJB REMOTO
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getFachadaRemotaGenerica(String nomeLookup) {
		T ejb = null;
		Context context;
		log.info("Tentando fazer o lookup de EJB para interface remota ...");
		try {
			Hashtable<String, Object> props = new Hashtable<String, Object>();
			props.put("jboss.naming.client.ejb.context", true);
			props.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			context = new InitialContext(props);
			ejb = (T) context.lookup(nomeLookup);
		} catch (NamingException e) {
			throw new RuntimeException("Falha ao instanciar contexto de nomes.", e);

		} catch (Exception e) {
			log.error("Falha ao realizar lookup EJB para: '{}', Erro: {}", nomeLookup, e.getMessage());
			throw new RuntimeException("Falha ao tentar localizar EJB para o nome JNDI: '" + nomeLookup + "'.", e);
		}
		return ejb;
	}

	/**
	 * RECUPERA FACHADA EJB REMOTA.
	 * 
	 * @param nomeApp
	 *            NOME DA APLICACAO
	 * @param nomeModulo
	 *            NOME DO MODULO
	 * @param nomeEjb
	 *            NOME DO EJB
	 * @param classe
	 *            INTERFACE / CLASSE DO EJB
	 *
	 * @return EJB REMOTO
	 */
	public static <T> T getFachadaRemotaGenerica(String nomeApp, String nomeModulo, String nomeEjb, Class<T> classe) {
		String facadeImpl = classe.getName();
		String lookupJndiName = "ejb:" + nomeApp + "/" + nomeModulo + "/" + nomeEjb + "!" + facadeImpl;
		return getFachadaRemotaGenerica(lookupJndiName);
	}

	/**
	 * RECUPERA FACHADA EJB LOCAL.
	 * 
	 * @param nomeApp
	 *            NOME DA APLICACAO
	 * @param nomeModulo
	 *            NOME DO MODULO
	 * @param nomeEjb
	 *            NOME DO EJB
	 * @param classe
	 *            INTERFACE EJB
	 *
	 * @return EJB LOCAL
	 */
	public static <T> T getFachadaLocalGenerica(String nomeApp, String nomeModulo, String nomeEjb, Class<T> classe) {
		String facadeImpl = classe.getName();
		String lookupJndiName = "ejb:" + nomeApp + "/" + nomeModulo + "/" + nomeEjb + "!" + facadeImpl;
		return getFachadaLocalGenerica(lookupJndiName);
	}

	/**
	 * RECUPERA FACHADA EJB LOCAL.
	 * 
	 * @param nomeLookup
	 *            LOOKUP EJB QUE SERA RECUPERADO
	 * @return EJB LOCAL
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getFachadaLocalGenerica(String nomeLookup) {
		T ejb = null;
		Context context;
		log.info("Tentando fazer o lookup de EJB para interface local ...");
		try {
			context = new InitialContext();
			ejb = (T) context.lookup(nomeLookup);
		} catch (NamingException e) {
			throw new RuntimeException("Falha ao instanciar contexto de nomes.", e);
		} catch (Exception e) {
			log.error("Falha ao realizar lookup EJB para: '{}', Erro: {}", nomeLookup, e.getMessage());
			throw new RuntimeException("Falha ao tentar localizar EJB para o nome JNDI: '" + nomeLookup + "'.", e);
		}
		return ejb;
	}

	/**
	 * RECUPERA FACHADA EJB REMOTA ENTRE PROJETOS EM CONTAINERS DISTINTOS.
	 * 
	 * @param nomeLookup
	 *            LOOKUP QUE SERA RECUPERADO
	 * @param providerUrl
	 *            URL DO EJB REMOTO
	 * @param useClientEjbContext
	 * @param useSaslPolicyNoPlainText
	 *            SE USA POLITICA DE SASL
	 * @param securityPrincipal
	 *            USUARIO
	 * @param securityCredentials
	 *            SENHA DO CLIENTE EJB
	 * @return EJB REMOTO
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getFachadaRemotaGenerica(String nomeLookup, String providerUrl, boolean useClientEjbContext, boolean useSaslPolicyNoPlainText, String securityPrincipal, String securityCredentials) {
		T ejb = null;
		Context context;
		Hashtable<String, Object> propriedades = new Hashtable<String, Object>();
		propriedades.put(Context.PROVIDER_URL, providerUrl);
		propriedades.put("jboss.naming.client.ejb.context", useClientEjbContext);
		propriedades.put("jboss.naming.client.connect.options.org.xnio.Options.SASL_POLICY_NOPLAINTEXT", useSaslPolicyNoPlainText);
		propriedades.put(Context.SECURITY_PRINCIPAL, securityPrincipal);
		propriedades.put(Context.SECURITY_CREDENTIALS, securityCredentials);
		log.info("Tentando fazer o lookup de EJB para interface remota ...");
		try {
			context = new InitialContext(propriedades);
			ejb = (T) context.lookup(nomeLookup);
		} catch (NamingException e) {
			throw new RuntimeException("Falha ao instanciar contexto de nomes.", e);

		} catch (Exception e) {
			log.error("Falha ao realizar lookup EJB para: '{}', Erro: {}", nomeLookup, e.getMessage());
			throw new RuntimeException("Falha ao tentar localizar EJB para o nome JNDI: '" + nomeLookup + "'.", e);
		}
		return ejb;
	}

	/**
	 * @param nomeLookup
	 * @param propriedades
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> T getFachadaRemotaGenerica(String nomeLookup, Hashtable propriedades) {
		T ejb = null;
		Context context;

		log.info("Tentando fazer o lookup de EJB para interface remota ...");
		try {
			context = new InitialContext(propriedades);
			ejb = (T) context.lookup(nomeLookup);
		} catch (NamingException e) {
			throw new RuntimeException("Falha ao instanciar contexto de nomes.", e);

		} catch (Exception e) {
			log.error("Falha ao realizar lookup EJB para: '{}', Erro: {}", nomeLookup, e.getMessage());
			throw new RuntimeException("Falha ao tentar localizar EJB para o nome JNDI: '" + nomeLookup + "'.", e);
		}
		return ejb;
	}

}
