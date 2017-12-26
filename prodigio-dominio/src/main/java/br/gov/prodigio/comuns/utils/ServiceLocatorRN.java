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

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServiceLocatorRN {
	private static final Logger log = LoggerFactory.getLogger(ServiceLocatorRN.class);

	public Map objetosDeNegocioMap = new HashMap();
	public Map objetosPDFView = new HashMap();
	private static ServiceLocatorRN instance = null;

	protected ServiceLocatorRN() {
	}

	private static synchronized void createInstance() {
		if (instance == null) {
			instance = new ServiceLocatorRN();
		}
	}

	public static ServiceLocatorRN getInstance() {
		if (instance == null) {
			createInstance();
		}
		return instance;
	}

	public Object getObject(Object vo) {
		Object objetoDeServico = objetosDeNegocioMap.get(vo.getClass());
		if (objetoDeServico == null) {
			objetoDeServico = recuperaObjetoRN(vo.getClass());
			objetosDeNegocioMap.put(vo.getClass(), objetoDeServico);
		}
		return objetoDeServico;
	}

	public Object recuperaObjetoRN(Class classVO) {
		Object objetoAux = null;
		objetoAux = objetosDeNegocioMap.get(classVO);
		if (objetoAux != null) {
			return objetoAux;
		}
		StringBuilder nomeClasse = new StringBuilder(classVO.getName());
		nomeClasse.replace(nomeClasse.indexOf("entidades"), nomeClasse.indexOf("entidades") + 9, "negocio");
		nomeClasse.replace(nomeClasse.indexOf("VO"), nomeClasse.indexOf("VO") + 3, "RN");
		Class classDoObjeto = null;
		try {
			log.debug("Instanciando classe de negócio {} para a entidade {} ", nomeClasse, classVO);
			classDoObjeto = Class.forName(nomeClasse.toString());
		} catch (ClassNotFoundException e) {

			if (classVO.getSuperclass() != Object.class) {
				log.debug("Não foi possível criar a classe de negócio {} para a entidade {}. ", nomeClasse, classVO);
				log.debug("Tentando a criação para a classe pai {}", classVO.getSuperclass());
				objetoAux = recuperaObjetoRN(classVO.getSuperclass());
				if (objetoAux != null) {
					return objetoAux;
				}
			} else {
				log.warn("Erro ao tentar criar classe de negócio. A entidade deve estender as classes ProBaseVO ou ProVO", e);
			}
		}
		try {
			objetoAux = classDoObjeto.newInstance();
			log.info("Adicionando classe {} a lista de objetos de negócio", classDoObjeto.getName());
			objetosDeNegocioMap.put(classVO, objetoAux);
		} catch (InstantiationException e) {
			log.error("Erro ao recuperar objeto de regra de negócio", e);
		} catch (IllegalAccessException e) {
			log.error("Erro ao recuperar objeto de regra de negócio", e);
		}
		return objetoAux;
	}

	public Object recuperaObjetoPDFVIEW(Class classVO) {
		Object viewAux = null;
		viewAux = objetosPDFView.get(classVO);
		if (viewAux != null) {
			return viewAux;
		}
		StringBuilder nomePacoteClasseVO = new StringBuilder(classVO.getPackage().getName());
		StringBuilder nomeClassePDFView = new StringBuilder(nomePacoteClasseVO + ".visao." + classVO.getSimpleName());
		nomeClassePDFView.replace(nomeClassePDFView.indexOf("VO"), nomeClassePDFView.indexOf("VO") + 3, "PDFVW");
		Class classPDFVIEW = null;
		try {
			log.debug("Instanciando classe de visão {} para a entidade {} ", nomeClassePDFView, classVO);
			classPDFVIEW = Class.forName(nomeClassePDFView.toString());
		} catch (ClassNotFoundException e) {
			if (classVO.getSuperclass() != Object.class) {
				log.debug("Não foi possível criar a classe de visão {} para a entidade {}. ", nomeClassePDFView, classVO);
				log.debug("Tentando a criação para a classe pai {}", classVO.getSuperclass());
				viewAux = recuperaObjetoPDFVIEW(classVO.getSuperclass());
				if (viewAux != null) {
					return viewAux;
				}
			} else {
				log.error("Erro ao tentar criar classe de visão. A entidade deve estender as classes ProBaseVO ou ProVO", e);
			}
		}
		try {
			viewAux = classPDFVIEW.newInstance();
			log.info("Adicionando classe {} a lista de objetos de visão", classPDFVIEW.getName());
			objetosPDFView.put(classVO, viewAux);
		} catch (InstantiationException e) {
			log.error("Erro ao recuperar objeto de visão", e);
		} catch (IllegalAccessException e) {
			log.error("Erro ao recuperar objeto de visão", e);
		}
		return viewAux;
	}

}
