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

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration.Dynamic;
import javax.servlet.annotation.WebListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebListener
public class ProZKContextListener implements ServletContextListener {
	private static final Logger log = LoggerFactory.getLogger(ProZKContextListener.class);

	@Override
	public void contextInitialized(ServletContextEvent context) {
		log.info("Iniciando contexto da aplicação");
		Long agora = System.currentTimeMillis();
		ServletContext servletContext = context.getServletContext();
		inicializaZK(servletContext);

		log.info("Contexto iniciado em {}", (System.currentTimeMillis() - agora) + "ms");
	}

	private void inicializaZK(ServletContext servletContext) {
		// Adicionando o HttpSessionListener do ZK
		log.info("Configurando o Zk");
		// Adicionando servlets do zk
		// The ZK loader for ZUML pages
		Dynamic zkHtmlLayoutServlet = servletContext.addServlet("zkLoader", "org.zkoss.zk.ui.http.DHtmlLayoutServlet");
		zkHtmlLayoutServlet.setInitParameter("update-uri", "/zkau");
		zkHtmlLayoutServlet.addMapping("*.zul");
		zkHtmlLayoutServlet.addMapping("*.zhtml");
		zkHtmlLayoutServlet.setLoadOnStartup(1);
		// The asynchronous update engine for ZK
		Dynamic zkHtmlUpdateServlet = servletContext.addServlet("auEngine", "org.zkoss.zk.au.http.DHtmlUpdateServlet");
		zkHtmlUpdateServlet.addMapping("/zkau/*");
	}

	@Override
	public void contextDestroyed(ServletContextEvent context) {
	}

}
