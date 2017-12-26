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
package br.gov.prodigio.comum.utils;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.gov.prodigio.comum.ContextParameters;
import br.gov.prodigio.comuns.IProFacade;
import br.gov.prodigio.entidades.ProBaseVO;

public class ContextUtils {

	private static final Logger log = LoggerFactory.getLogger(ContextUtils.class);

	public static void atualizaLookupsDaAplicacao(ServletContext context, Class<?>... classes) {
		try {
			IProFacade facade = (IProFacade) context.getAttribute(ContextParameters.INTERFACE_DE_NEGOCIO);
			Set set = new LinkedHashSet();
			for (Class c : classes) {
				Object o = c.newInstance();
				set = facade.listar((ProBaseVO) o);
				String lookup = "listaDe" + o.getClass().getSimpleName().substring(0, o.getClass().getSimpleName().length());
				context.removeAttribute(lookup);
				context.setAttribute(lookup, set);
				log.info("Atualizando lookup ...:{} tamanho: {}", lookup, set.size());
			}
		} catch (Exception e) {
			log.error("Erro ao atualizar lookups da aplicação.", e.getCause());
		}
	}
}
