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
package br.gov.prodigio.servicoweb;

//import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.gov.prodigio.comum.ContextParameters;
import br.gov.prodigio.comuns.IProFacade;

public class ProServicoWeb {

	private static final Logger log = LoggerFactory.getLogger(ProServicoWeb.class);

//	@Resource
	WebServiceContext wsContext;


	@Context
	ServletContext context;

	public IProFacade getFachadaDeNegocio() {
		log.debug("Carregando fachada de negocio");
		//ServletContext context = (ServletContext) wsContext.getMessageContext().get(MessageContext.SERVLET_CONTEXT);
		return (IProFacade) getContext().getAttribute(ContextParameters.INTERFACE_DE_NEGOCIO);
	}

	public ServletContext getContext() {
		// QUANDO E USADO JAX-RS O CONTEXTO É PREENCHIDO.
		if (context != null) {
			return context;
		}
		// QUANDO É USADO JAX-WS O CONTEXTO É DEFINIDO DE OUTRA FORMA.
		return (ServletContext) wsContext.getMessageContext().get(MessageContext.SERVLET_CONTEXT);
	}

	public WebServiceContext getWsContext() {
		return wsContext;
	}
}
