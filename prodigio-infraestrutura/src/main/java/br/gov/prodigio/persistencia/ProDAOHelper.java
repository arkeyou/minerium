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
/** * Classe criada em 01/01/2010 * Este código pode ser usado apenas para fins não comerciais * @author Sândalo Bessa */
package br.gov.prodigio.persistencia;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.commons.lang.ArrayUtils;
import org.hibernate.collection.internal.AbstractPersistentCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.gov.prodigio.entidades.ProBaseVO;
import flexjson.BeanAnalyzer;
import flexjson.BeanProperty;
import flexjson.ChainedSet;
import flexjson.JSONContext;
import flexjson.JSONException;
import flexjson.JSONSerializer;
import flexjson.Path;
import flexjson.TypeContext;
import flexjson.transformer.IterableTransformer;
import flexjson.transformer.ObjectTransformer;
import flexjson.transformer.TransformerWrapper;

public class ProDAOHelper {
	private static final Logger log = LoggerFactory.getLogger(ProDAOHelper.class);

	private static final ThreadLocal<EntityManager> entityManagerThreadLocal = new ThreadLocal<EntityManager>();

	private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("transactions-optional");
	
	private static final String ERROR_RETURN = "";

	private static String getJNDIName() {
		String jndi = "";
		jndi = recuperaNomePorConvecao();

		return jndi;
	}

	private static String recuperaNomePorConvecao() {
		String jndi;
		String loader = ProDAOHelper.class.getClassLoader().toString();
		String nomeCompletoDaAplicacao = loader.split("deployment.")[1];
		String nomePrincipal = nomeCompletoDaAplicacao.contains(".ear:main") ? nomeCompletoDaAplicacao.split(".ear:main")[0] : nomeCompletoDaAplicacao.split(".war:main")[0];
		String nomeSecundario = nomePrincipal.replaceAll("-app-negocio", "").replaceAll("-web", "").replaceAll("-app-backend", "");
		jndi = "java:/emf/" + nomeSecundario;

		if (nomeSecundario.contains(".")) {
			String[] split = nomeSecundario.split("[.]");
			if (split != null) {
				nomeSecundario = split[0].substring(0, split[0].lastIndexOf("-"));
				jndi = "java:/emf/" + nomeSecundario;
			}
		} else {
			jndi = "java:/emf/" + nomeSecundario;
		}
		return jndi;
	}

	/**
	 * Método criado para retornar dados do contexto, informando de qual aplicação BPMS será enviado o e-mail autor valdeci.sousa data 28/07/2015
	 */

	public static String getContexto() {
		String loader = ProDAOHelper.class.getClassLoader().toString();
		String nomeCompletoDaAplicacao = loader.split("deployment.")[1];
		String nomePrincipal = nomeCompletoDaAplicacao.contains(".ear:main") ? nomeCompletoDaAplicacao.split(".ear:main")[0] : nomeCompletoDaAplicacao.split(".war:main")[0];
		Integer posicaoFinal = nomePrincipal.length();
		Integer posicaoApp = nomePrincipal.indexOf("-app");
		String parteRetirada = nomePrincipal.substring(posicaoApp, posicaoFinal);
		String nomeAplicacao = nomePrincipal.replaceAll(parteRetirada, "");

		return nomeAplicacao;
	}

	private static String getJNDINameComVersao() {
		String loader = ProDAOHelper.class.getClassLoader().toString();
		String nomeCompletoDaAplicacao = loader.split("deployment.")[1];
		String nomePrincipal = nomeCompletoDaAplicacao.contains(".ear:main") ? nomeCompletoDaAplicacao.split(".ear:main")[0] : nomeCompletoDaAplicacao.split(".war:main")[0];
		String nomeSecundario = nomePrincipal.replaceAll("-app-negocio", "").replaceAll("-web", "");
		String jndi = "java:/emf/" + nomeSecundario;

		if (!nomeSecundario.contains(".")) {
			try {
				// Faz uma busca no jar de infraestrutura para descobrir a versão do sistema.
				// e procura o nome do EntityManagerFactory com essa versão
				Enumeration<URL> resources = ProDAOHelper.class.getClassLoader().getResources("/");
				while (resources.hasMoreElements()) {
					URL url = resources.nextElement();
					String path = url.getPath();
					if (path.contains(nomeSecundario + "-infraestrutura")) {
						jndi += path.split(nomeSecundario)[2].replaceAll("infraestrutura-", "").replaceAll(".jar/", "");
						break;
					}
				}
			} catch (IOException e) {
				log.error("Erro ao descobrir a versao do jar infraestrutura", e);
			}
		}

		return jndi;
	}

	public static EntityManager entityManager() {

		if (entityManagerThreadLocal.get() == null) {
			synchronized (entityManagerThreadLocal) {
				EntityManager em = getEntityManagerFactory().createEntityManager();
				entityManagerThreadLocal.set(em);
			}
		}
		return entityManagerThreadLocal.get();
	}

	public static EntityManagerFactory getEntityManagerFactory() {
		//Para EJB
		EntityManagerFactory toReturn = null;
		Context context;
		try {
			context = new javax.naming.InitialContext();
			toReturn = (EntityManagerFactory) context.lookup(getJNDIName());
		} catch (NamingException e) {
			try {
				context = new javax.naming.InitialContext();
				toReturn = (EntityManagerFactory) context.lookup(getJNDINameComVersao());
			} catch (NamingException e1) {
				log.error("Erro ao recuperar o EntityManagerFactory", e);
			}
		} catch (Exception e) {
			if (toReturn==null) {
				//Para JPA
				toReturn = emf;
			}
		}
		
		return toReturn;
	}

	public static void closeEntityManager() {
		EntityManager entityManager = entityManagerThreadLocal.get();
		if (entityManager != null && entityManager.isOpen()) {
			entityManager.close();
		}
		entityManagerThreadLocal.set(null);
	}

	public static String jsonSerializer(ProBaseVO serializeObject, String[] ignoreFields) {

		try {
			if (serializeObject.getClass().isAnnotationPresent(Entity.class)) {
				entityManager().detach(serializeObject);
			}
			JSONSerializer jsonSerializer = new JSONSerializer();
			String[] ignoreDefault = { "*.excluindoDetalhe", "*.tpOperacao", "*.titulo", "*.novo", "*.naoNovo", "*.tsMovimentacao", "*.nrVersao", /* "*.statusDoRegistro", */
			"*.dsSituacao", "*.imagem", "*.ipMovimentacao", "*.cdLoginMovimentacao", "*.dataCriacao", "*.class", "*.logomarca", "*.icone" };
			String[] ignoreAllFields = (String[]) ArrayUtils.addAll(ignoreDefault, ignoreFields);
			jsonSerializer.exclude(ignoreAllFields);
			jsonSerializer.transform(new ProTransformer(), ProBaseVO.class).transform(new ProCollectionTransformer(), Collection.class);
			return jsonSerializer.deepSerialize(serializeObject);
		} catch (Exception e) {
			log.error("Erro generico", e);
			return ERROR_RETURN;
		}

	}

	public static String jsonSerializerSemIgnoreDefault(ProBaseVO serializeObject, String[] ignoreFields) {
		try {
			if (serializeObject.getClass().isAnnotationPresent(Entity.class)) {
				entityManager().detach(serializeObject);
			}
			JSONSerializer jsonSerializer = new JSONSerializer();
			jsonSerializer.exclude(ignoreFields);
			jsonSerializer.transform(new ProTransformer(), ProBaseVO.class).transform(new ProCollectionTransformer(), Collection.class);
			return jsonSerializer.deepSerialize(serializeObject);
		} catch (Exception e) {
			log.error("Erro generico", e);
			return ERROR_RETURN;
		}

	}

	public static String jsonSerializerSemIgnore(ProBaseVO serializeObject) {
		try {

			JSONSerializer jsonSerializer = new JSONSerializer();

			jsonSerializer.transform(new ProTransformer(), ProBaseVO.class).transform(new ProCollectionTransformer(), Collection.class);
			return jsonSerializer.deepSerialize(serializeObject);
		} catch (Exception e) {
			log.error("Erro generico", e);
			return ERROR_RETURN;
		}
	}

	public static String jsonSerializer(ProBaseVO serializeObject) {
		String[] ignoreFields = {};
		return jsonSerializer(serializeObject, ignoreFields);
	}

	/**
	 * Transformer
	 */
	static class ProCollectionTransformer extends IterableTransformer {
		@SuppressWarnings({ "rawtypes", "unchecked" })
		@Override
		public void transform(Object object) {
			Collection valor = (Collection) object;
			if (object instanceof Set) {
				object = new HashSet(valor);
			} else if (object instanceof List) {
				object = new ArrayList(valor);
			}
			super.transform(object);
		}
	}

	/**
	 * Transformer criado para eliminar serialização de campos nulos, vazios e coleções lazy
	 */
	static class ProTransformer extends ObjectTransformer {
		@Override
		public void transform(Object object) {
			JSONContext context = getContext();
			Path path = context.getPath();
			ChainedSet visits = context.getVisits();
			try {
				if (!visits.contains(object)) {
					context.setVisits(new ChainedSet(visits));
					context.getVisits().add(object);
					BeanAnalyzer analyzer = BeanAnalyzer.analyze(resolveClass(object));
					TypeContext typeContext = context.writeOpenObject();
					for (BeanProperty prop : analyzer.getProperties()) {
						String name = prop.getName();
						path.enqueue(name);
						if (context.isIncluded(prop) && prop.isReadable()) {

							Object value;
							try {
								value = prop.getValue(object);
								// LEMBRAR QUE SE ALTERAR A VERSAO DO HIBERNATE ESTA IMPLEMENTAÇÃO SERÁ IMPACTADA
								if (value instanceof AbstractPersistentCollection) {
									if (!((AbstractPersistentCollection) value).wasInitialized()) {
										value = null;
									}

								}
							} catch (Exception e) {
								value = null;
							}
							// NAO DEIXA SERIALIZAR SE O VALOR FOR NULO OU VAZIO
							if (isNotNullOrEmpty(value)) {
								if (!context.getVisits().contains(value)) {

									TransformerWrapper transformer = (TransformerWrapper) context.getTransformer(prop, value);

									if (!transformer.isInline()) {
										if (!typeContext.isFirst())
											context.writeComma();
										typeContext.increment();
										context.writeName(prop.getJsonName());
									}
									typeContext.setPropertyName(prop.getJsonName());

									transformer.transform(value);
								}
							}
						}
						path.pop();
					}
					context.writeCloseObject();
					context.setVisits((ChainedSet) context.getVisits().getParent());

				} else {
					TypeContext parentTypeContext = getContext().peekTypeContext();
					if (parentTypeContext != null) {
						parentTypeContext.decrement();
					}
				}
			} catch (JSONException e) {
				throw e;
			} catch (Exception e) {
				throw new JSONException(String.format("%s: Error while trying to deepSerialize.", path), e);
			}
		}

		/**
		 * Verifica se o objeto é nulo ou uma lista vazia.
		 * 
		 * @param value
		 *            valor que será validado
		 * @return true or false
		 */
		@SuppressWarnings({ "rawtypes", "unchecked" })
		private boolean isNotNullOrEmpty(Object value) {
			boolean retorno = value != null;
			if (retorno) {
				if (value instanceof Collection) {
					Collection collection = (Collection) value;
					retorno = !collection.isEmpty();
					if (collection instanceof List) {
						value = new ArrayList(collection);
					} else if (collection instanceof Set) {
						value = new HashSet(collection);
					}
				}
			}
			return retorno;
		}
	}
}
