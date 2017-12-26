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
package br.gov.prodigio.controle;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.gov.prodigio.comum.ContextParameters;
import br.gov.prodigio.comuns.IProFacade;
import br.gov.prodigio.comuns.IProFacadeLocal;
import br.gov.prodigio.comuns.IProFacadeRemote;
import br.gov.prodigio.comuns.anotacoes.Lookup;
import br.gov.prodigio.comuns.utils.ProConfiguracao;
import br.gov.prodigio.entidades.ProBaseVO;

@WebListener
public class ProContextListener implements ServletContextListener {
	private static final Logger log = LoggerFactory.getLogger(ProContextListener.class);

	@Override
	public void contextInitialized(ServletContextEvent context) {
		log.info("Iniciando contexto da aplicação");
		Long agora = System.currentTimeMillis();
		ServletContext servletContext = context.getServletContext();
		inicializaVariaveisDeEscopoAplication(servletContext);
		servletContext.getAttribute(ContextParameters.INTERFACE_DE_NEGOCIO);
		inicializaInterfaceDeNegocio(servletContext);
		inicializaLookUpsDaplicacao(servletContext);
		log.info("Contexto iniciado em {}", (System.currentTimeMillis() - agora) + "ms");
	}

	@Override
	public void contextDestroyed(ServletContextEvent context) {
	}

	private void inicializaVariaveisDeEscopoAplication(ServletContext wapp) {
		log.info("Inicializando variáveis de escopo");
		wapp.setAttribute("px_preferred_locale", new Locale("pt", "BR"));
		try {
			String diretorioDaAplicacao = wapp.getRealPath("\\");

			log.info("Atribuindo diretório {} ao contexto", diretorioDaAplicacao);

			wapp.setAttribute(ContextParameters.DIRETORIO_DA_APLICACAO, diretorioDaAplicacao);
			wapp.setAttribute(ContextParameters.COMANDOS_VINCULADOS, wapp.getInitParameter(ContextParameters.COMANDOS_VINCULADOS));
		} catch (Exception e) {
			log.error("Erro ao iniciar as variáveis de escopo", e);
		}
	}

	public void inicializaInterfaceDeNegocio(ServletContext wapp) {
		log.info("Inicializando interface de  negócio");
		IProFacade ejb = null;
		try {
			ejb = recuperarInterfaceCustomizada(wapp);
		} catch (Exception e) {
			log.warn("Falha ao realizar lookup pelo arquivo  de configuração");
		}

		if (ejb == null) {
			try {
				ejb = recuperarInterfaceRemotaPorConvencao(wapp);
			} catch (Exception e) {
				log.warn("Falha ao realizar lookup de interface local por conveção");
			}
		}

		if (ejb == null) {
			try {
				ejb = recuperarInterfaceLocalPorConvencao(wapp);
			} catch (Exception e) {
				log.warn("Falha ao realizar lookup pelo arquivo  de configuração");
			}
		}

		if (ejb == null) {
			try {
				ejb = recuperarInterfacePorConfiguracao(wapp);
			} catch (Exception e) {
				new RuntimeException("Falha ao realizar lookup de interface remota por conveção - Ultima tentativa");
			}
		}
		wapp.setAttribute(ContextParameters.INTERFACE_DE_NEGOCIO, ejb);
	}

	private IProFacade recuperarInterfaceLocalPorConvencao(ServletContext wapp) {
		IProFacade ejb = null;
		Context context;

		try {
			context = new InitialContext();
		} catch (NamingException e) {
			throw new RuntimeException("Falha  ao instanciar contexto de nomes.", e);
		}
		// Nome da aplicacao sem o "web" e qualquer coisa depois do "web"
		String token_web_rest = recuperaNomeToken(wapp);
		int lastIndexOf = wapp.getContextPath().lastIndexOf(token_web_rest);
		String nomeAppBase = wapp.getContextPath().substring(0, lastIndexOf).replaceAll("/", "");

		String nomeApp = nomeAppBase + "app-negocio";
		String nomeModulo = wapp.getServletContextName().replace(token_web_rest, "negocio");
		String nomeEjb = nomeAppBase + "ejb";
		String facadeImpl = IProFacadeLocal.class.getName();
		String lookupGlobal = "java:global/" + nomeApp + "/" + nomeModulo + "/" + nomeEjb + "!" + facadeImpl;
		try {
			ejb = (IProFacade) context.lookup(lookupGlobal);
			log.info("Lookup da interface local realizado com sucesso par '{}': {}", lookupGlobal, ejb);
		} catch (Exception e1) {
			log.warn("Falha ao realizar lookup pela interface local: {} - Mensagem de erro: {}", lookupGlobal, e1);
			try {
				log.warn("Falha ao realizar  lookup sem cosiderar versão maven: {} - Mensagem de erro: {}", lookupGlobal, e1);
				facadeImpl = IProFacadeLocal.class.getName();
				lookupGlobal = "java:global/" + nomeModulo.replace("negocio", "app-negocio") + "/" + nomeModulo + "/" + nomeEjb + "!" + facadeImpl;
				ejb = (IProFacade) context.lookup(lookupGlobal);
				log.info("Lookup da interface remota realizado com sucesso par '{}': {}", lookupGlobal, ejb);
			} catch (Exception e2) {
				throw new RuntimeException("Erro ao recuperar interface remota.", e1);
			}
		}
		return ejb;
	}

	private String recuperaNomeToken(ServletContext wapp) {
		String token_web_rest = "";
		if (wapp.getContextPath().contains("web")) {
			token_web_rest = "web";
		} else if (wapp.getContextPath().contains("rest")) {
			token_web_rest = "rest";
		} else if (wapp.getContextPath().contains("frontend")) {
			token_web_rest = "frontend";
		}
		return token_web_rest;
	}

	private IProFacade recuperarInterfaceRemotaPorConvencao(ServletContext wapp) {
		IProFacade ejb = null;

		Context context;

		try {
			context = new InitialContext();
		} catch (NamingException e) {
			throw new RuntimeException("Falha  ao instanciar contexto de nomes.", e);
		}
		// Nome da aplicacao sem o "web" e qualquer coisa depois do "web"

		String token_web_rest = recuperaNomeToken(wapp);

		String nomeAppBase = wapp.getContextPath().substring(0, wapp.getContextPath().lastIndexOf(token_web_rest)).replaceAll("/", "");
		String nomeApp = nomeAppBase + "app-negocio";
		String nomeModulo = wapp.getServletContextName().replace(token_web_rest, "negocio");
		String nomeEjb = nomeAppBase + "ejb";
		String facadeImpl = IProFacadeRemote.class.getName();
		String lookupGlobal = "java:global/" + nomeApp + "/" + nomeModulo + "/" + nomeEjb + "!" + facadeImpl;
		try {
			ejb = (IProFacade) context.lookup(lookupGlobal);
			log.info("Lookup da interface local realizado com sucesso par '{}': {}", lookupGlobal, ejb);
		} catch (Exception e1) {
			log.warn("Falha ao realizar lookup pela interface local: {} - Mensagem de erro: {}", lookupGlobal, e1);
			try {
				log.warn("Falha ao realizar  lookup sem cosiderar versão maven: {} - Mensagem de erro: {}", lookupGlobal, e1);
				facadeImpl = IProFacadeRemote.class.getName();
				lookupGlobal = "java:global/" + nomeModulo.replace("negocio", "app-negocio") + "/" + nomeModulo + "/" + nomeEjb + "!" + facadeImpl;
				ejb = (IProFacade) context.lookup(lookupGlobal);
				log.info("Lookup da interface remota realizado com sucesso par '{}': {}", lookupGlobal, ejb);
			} catch (Exception e2) {
				throw new RuntimeException("Erro ao recuperar interface remota.", e1);
			}
		}
		return ejb;
	}

	private IProFacade recuperarInterfacePorConfiguracao(ServletContext wapp) {
		IProFacade ejb = null;
		Context context;
		ProConfiguracao config = new ProConfiguracao();
		log.info("Tentando fazer o lookup de EJB pela interface LOCAL...");
		String token_web_rest = recuperaNomeToken(wapp);
		// Nome da aplicacao sem o "web" e qualquer coisa depois do "web"
		String nomeAppBase = wapp.getContextPath().substring(0, wapp.getContextPath().lastIndexOf(token_web_rest) - 1).replaceAll("/", "");

		try {
			context = new InitialContext();
		} catch (Exception e) {
			throw new RuntimeException("Falha ao instanciar contexto de nomes.", e);
		}

		String lookupJndiNameLocal1 = config.getPropriedade(nomeAppBase + ".ejbFacadeJndiLookupLocal1");
		log.info("Tentando nome JNDI: {}", lookupJndiNameLocal1);
		try {
			ejb = (IProFacade) context.lookup(lookupJndiNameLocal1);
			log.info("Lookup EJB realizado com sucesso para '{}', EJB: {}", lookupJndiNameLocal1, ejb);
		} catch (Exception e1) {
			log.warn("Falha ao realizar lookup EJB para: '{}', Erro: {}", lookupJndiNameLocal1, e1.getMessage());

			String lookupJndiNameLocal2 = config.getPropriedade(nomeAppBase + ".ejbFacadeJndiLookupLocal2");
			log.info("Tentando nome JNDI: {}", lookupJndiNameLocal2);

			try {
				ejb = (IProFacade) context.lookup(lookupJndiNameLocal2);
				log.info("Lookup EJB realizado com sucesso para '{}', EJB: {}", lookupJndiNameLocal2, ejb);
			} catch (Exception e2) {
				log.warn("Falha ao realizar lookup EJB para: '{}', Erro: {}", lookupJndiNameLocal2, e2.getMessage());
				log.info("Tentando fazer o lookup de EJB pela interface REMOTA...");

				try {
					Hashtable props = new Hashtable();
					props.put("jboss.naming.client.ejb.context", true);
					props.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
					context = new InitialContext(props);
				} catch (Exception e3) {
					throw new RuntimeException("Falha ao instanciar contexto de nomes.", e3);
				}

				String lookupJndiNameRemote1 = config.getPropriedade(nomeAppBase + ".ejbFacadeJndiLookupRemote1");
				log.info("Tentando nome JNDI: {}", lookupJndiNameRemote1);

				try {
					ejb = (IProFacade) context.lookup(lookupJndiNameRemote1);
					log.info("Lookup EJB realizado com sucesso para '{}', EJB: {}", lookupJndiNameRemote1, ejb);
				} catch (Exception e4) {
					log.warn("Falha ao realizar lookup EJB para: '{}', Erro: {}", lookupJndiNameRemote1, e4.getMessage());

					String lookupJndiNameRemote2 = config.getPropriedade(nomeAppBase + ".ejbFacadeJndiLookupRemote2");
					log.info("Tentando nome JNDI: {}", lookupJndiNameRemote2);

					try {
						ejb = (IProFacade) context.lookup(lookupJndiNameRemote2);
						log.info("Lookup EJB realizado com sucesso para '{}', EJB: {}", lookupJndiNameRemote2, ejb);
					} catch (Exception e5) {
						log.warn("Falha ao realizar lookup EJB para: '{}', Erro: {}", lookupJndiNameRemote2, e5.getMessage());
						throw new RuntimeException("Falha ao tentar localizar EJB. TODAS tentativas fracassaram para os nomes JNDI: '" + lookupJndiNameLocal1 + "', '" + lookupJndiNameLocal2 + "', '" + lookupJndiNameRemote1 + "', '"
								+ lookupJndiNameRemote2 + "'.", e5);
					}
				}
			}
		}
		return ejb;
	}

	private IProFacade recuperarInterfaceCustomizada(ServletContext wapp) {
		IProFacade ejb = null;
		Context context;
		ProConfiguracao config = new ProConfiguracao();
		log.info("Tentando fazer o lookup de EJB pela interface customizada...");
		String token_web_rest = recuperaNomeToken(wapp);
		// Nome da aplicacao sem o "web" e qualquer coisa depois do "web"
		String nomeAppBase = wapp.getContextPath().substring(0, wapp.getContextPath().lastIndexOf(token_web_rest) - 1).replaceAll("/", "");

		try {
			context = new InitialContext();
		} catch (Exception e) {
			throw new RuntimeException("Falha ao instanciar contexto de nomes.", e);
		}

		String facadeJndiLookupCustom = config.getPropriedade(nomeAppBase + ".ejbFacadeJndiLookupCustom");
		if (facadeJndiLookupCustom == null || "".endsWith(facadeJndiLookupCustom)) {
			log.info("Não há uma implementação de fachada customizada");
		} else {
			log.info("Tentando nome JNDI: {}", facadeJndiLookupCustom);
			try {
				ejb = (IProFacade) context.lookup(facadeJndiLookupCustom);
				log.info("Lookup EJB realizado com sucesso para '{}', EJB: {}", facadeJndiLookupCustom, ejb);
			} catch (Exception e1) {
				log.warn("Falha ao realizar lookup EJB para: '{}', Erro: {}", facadeJndiLookupCustom, e1.getMessage());
			}
		}
		return ejb;
	}

	private IProFacade recuperaIterfaceDeNegocio(ServletContext wapp) {
		IProFacade facade = (IProFacade) wapp.getAttribute(ContextParameters.INTERFACE_DE_NEGOCIO);
		return facade;
	}

	private void inicializaLookUpsDaplicacao(ServletContext wapp) {
		log.info("Inicializando lookups da aplicação");
		String diretorioDaAplicacao = wapp.getRealPath(File.separator);
		File dir = new File(diretorioDaAplicacao + "" + ContextParameters.ENTIDADES_CAMINHO);
		log.info("antes iniciar lookups no diretório", dir.getAbsolutePath());
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		File dirJar = new File(diretorioDaAplicacao + "" + ContextParameters.ENTIDADES_CAMINHO_LIB);
		inicializaLookupsDaAplicacao(wapp, dirJar, cl);
	}

	private void inicializaLookupsDoDiretorio(ServletContext wapp, File dir, ClassLoader cl) {
		Class c = null;
		File[] children = dir.listFiles();
		log.info("Inicializando lookups do diretório: ", dir.getAbsolutePath());
		if (children != null) {
			for (File fileOrDir : children) {
				try {
					if (fileOrDir.isFile()) {
						if (ehClasseDeDominio(fileOrDir)) {
							String path = fileOrDir.getPath();
							String caminhoBase = getConvencaoDoPathDoDominio();
							int inicioDoNomeDaClasse = path.indexOf(caminhoBase);
							int fimDoNomeDaClasse = path.length() - 6;
							String nomeDaClasse = path.substring(inicioDoNomeDaClasse, fimDoNomeDaClasse).replace(File.separator, ".");
							c = cl.loadClass(nomeDaClasse);
							Lookup lookup = (Lookup) c.getAnnotation(Lookup.class);
							if (lookup != null || c.isEnum()) {
								try {
									if (lookup != null) {
										Set set = new LinkedHashSet();
										Object o = c.newInstance();
										set = recuperaIterfaceDeNegocio(wapp).listar((ProBaseVO) o);
										if (lookup.namedQuery() == null || "".equals(lookup.namedQuery())) {
											wapp.setAttribute("listaDe" + o.getClass().getSimpleName().substring(0, o.getClass().getSimpleName().length()), set);
										} else {
											wapp.setAttribute(lookup.namedQuery(), set);
										}
										log.info("Inicializando lookup ...:{} tamanho: {}", lookup.namedQuery(), set.size());
									} else if (c.isEnum()) {
										Method m;
										m = c.getMethod("values", null);
										Object[] o;
										o = (Object[]) m.invoke(null, new Object[] {});
										List oList = new ArrayList();
										oList.addAll(Arrays.asList(o));
										wapp.setAttribute(ContextParameters.PREFIX_ENUM + c.getSimpleName(), oList);

										log.info("Inicializando enum...: lista{} tamanho: {}", c.getSimpleName(), oList.size());
									}

								} catch (Exception e) {
									log.warn("Erro ao inicializar lookup ", e.getMessage());
								}
							}
						}
					} else if (fileOrDir.isDirectory()) {
						inicializaLookupsDoDiretorio(wapp, fileOrDir, cl);
					}
				} catch (SecurityException e1) {
					log.warn("Erro ao inicializar lookup", e1.getMessage());
				} catch (ClassNotFoundException e) {
					log.warn("Erro ao inicializar lookup", e.getMessage());
				}
			}
		}
	}

	protected boolean ehClasseDeDominio(File fileOrDir) {
		String path = fileOrDir.getPath();
		return path.contains(getConvencaoDoPathDoDominio()) && (path.contains("entidades") || path.contains("enums")) && fileOrDir.getName().indexOf(".class") != -1;
	}

	protected String getConvencaoDoPathDoDominio() {
		return new StringBuilder().append("br").append(File.separator).append("gov").append(File.separator).append("prodemge").toString();
	}

	private void carregaLookups(ServletContext wapp, File file, ClassLoader cl) {
		Class c = null;
		if (file.toURI().toString().endsWith(".jar")) {
			JarFile jf = null;
			try {
				jf = new JarFile(file);
				Enumeration<JarEntry> en = jf.entries();
				while (en.hasMoreElements()) {
					JarEntry entry = en.nextElement();
					if (entry.getName().endsWith(".class") && entry.getName().contains("/entidades/")) {
						c = cl.loadClass(entry.getName().replaceAll(".class", "").replaceAll("/", "."));
						Lookup lookup = (Lookup) c.getAnnotation(Lookup.class);
						if (lookup != null) {
							Set set = new LinkedHashSet();
							Object o = c.newInstance();
							try {
								set = recuperaIterfaceDeNegocio(wapp).listar((ProBaseVO) o);
							} catch (Exception e) {
								log.warn("!!!!!!!!!!!!!!!!!!!!! Não carregou lookup para a classe...:{}", o.getClass().getName());
								continue;
							}
							if (lookup.namedQuery() == null || "".equals(lookup.namedQuery())) {
								wapp.setAttribute("listaDe" + o.getClass().getSimpleName().substring(0, o.getClass().getSimpleName().length()), set);
							} else {
								wapp.setAttribute(lookup.namedQuery(), set);
							}
							log.info("Inicializando lookup ...:{} tamanho: {}", lookup.namedQuery(), set.size());
						}
					} else if (entry.getName().endsWith(".class") && entry.getName().contains("/enums/")) {
						c = cl.loadClass(entry.getName().replaceAll("\\.class", "").replaceAll("/", "."));
						if (c.isEnum()) {
							Method m;
							m = c.getMethod("values", null);
							Object[] o;
							o = (Object[]) m.invoke(null, new Object[] {});
							List oList = new ArrayList();
							oList.addAll(Arrays.asList(o));
							wapp.setAttribute(ContextParameters.PREFIX_ENUM + c.getSimpleName(), oList);
							log.info("Inicializando enum...: lista{} tamanho: {}", c.getSimpleName(), oList.size());
						}
					}
				}
				jf.close();
			} catch (InstantiationException e) {
				log.error("Erro ao carregar lookups ", e);
			} catch (IllegalAccessException e) {
				log.error("Erro ao carregar lookups ", e);
			} catch (InvocationTargetException e) {
				log.error("Erro ao carregar lookups ", e);
			} catch (NoSuchMethodException e) {
				log.error("Erro ao carregar lookups ", e);
			} catch (ClassNotFoundException e) {
				log.error("Erro ao carregar lookups ", e);
			} catch (IOException e) {
				log.error("Erro ao carregar lookups ", e);
			}
		}
	}

	private void inicializaLookupsDaBase(ServletContext wapp, File dir, ClassLoader cl) {
		// Carrega entidades da arquitetura de base
		carregaLookups(wapp, dir, cl);
	}

	private void inicializaLookupsDaAplicacao(ServletContext wapp, File dir, ClassLoader cl) {
		File[] children = dir.listFiles();
		log.info("Início da varredura do diretorio: {}", dir.getAbsolutePath());
		if (children != null) {
			for (File fileOrDir : children) {
				try {
					if (fileOrDir.isFile()) {
						if (fileOrDir.getName().indexOf("-interfaces") != -1 || fileOrDir.getName().indexOf("-dominio") != -1 || fileOrDir.getName().indexOf("-domain") != -1) {
							log.debug("Carregando lookups de {}", fileOrDir.getName());
							carregaLookups(wapp, fileOrDir, cl);
						}
					} else if (fileOrDir.isDirectory()) {
						inicializaLookupsDoDiretorio(wapp, fileOrDir, cl);
					}
				} catch (SecurityException e1) {
					log.error("Erro ao inicializar lookups", e1);
				}
			}
		}
	}

}
