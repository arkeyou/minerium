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

/**
 * Classe que implementa o padrão de projeto Service Locator para recuperar classes de persistência
 **/
public class ServiceLocatorDAO {
	private static final Logger log = LoggerFactory.getLogger(ServiceLocatorDAO.class);
	public Map objetosDAO = new HashMap();
	private static ServiceLocatorDAO instance = null;

	protected ServiceLocatorDAO() {
	}

	private static synchronized void createInstance() {
		if (instance == null) {
			instance = new ServiceLocatorDAO();
		}
	}

	public static ServiceLocatorDAO getInstance() {
		if (instance == null) {
			createInstance();
		}
		return instance;
	}

	/**
	 * Método responsável por recuperar o objeto DAO de determinada entidade de negócio. Por exemplo: Passando uma classe ClienteVO o metodo recuperaria o objeto da classe ClienteDAO. Para que seja possível recuperar o objeto DAO a entidade de
	 * negócio deve estar na seguinte estrutura de pacote: xpto.entidades.ClienteVO. E a classe DAO deve estar na seguinte estrutura de pacote: xpto.persistencia.ClienteDAO
	 * 
	 * @param classVO
	 *            Entidade de negócio
	 * @return Entidade DAO relativa a entidade de negócio passada como parâmetro.
	 **/
	public Object recuperaObjetoDAO(Class classVO) {
		Object daoAux = null;
		daoAux = objetosDAO.get(classVO);
		if (daoAux != null) {
			return daoAux;
		}
		StringBuilder nomeClasseDAO = new StringBuilder(classVO.getName());
		nomeClasseDAO.replace(nomeClasseDAO.indexOf("entidades"), nomeClasseDAO.indexOf("entidades") + 9, "persistencia");
		nomeClasseDAO.replace(nomeClasseDAO.indexOf("VO"), nomeClasseDAO.indexOf("VO") + 3, "DAO");
		Class classDAO = null;
		try {
			log.debug("Instanciando classe de persistência {} para a entidade {} ", nomeClasseDAO, classVO);
			classDAO = Class.forName(nomeClasseDAO.toString());
		} catch (ClassNotFoundException e) {
			if (classVO.getSuperclass() != Object.class) {
				log.debug("Não foi possível criar a classe de persistência {} para a entidade {}. ", nomeClasseDAO, classVO);
				log.debug("Tentando a criação para a classe pai {}", classVO.getSuperclass());
				daoAux = recuperaObjetoDAO(classVO.getSuperclass());
				if (daoAux != null) {
					return daoAux;
				}
			} else {
				log.error("Erro ao tentar criar classe de negócio. A entidade deve estender as classes ProBaseVO ou ProVO", e);
			}
		}
		try {
			daoAux = classDAO.newInstance();
			log.info("Adicionando classe {} a lista de objetos de persistência", classDAO.getName());
			objetosDAO.put(classVO, daoAux);
		} catch (InstantiationException e) {
			log.error("Erro ao recuperar objeto DAO", e);
		} catch (IllegalAccessException e) {
			log.error("Erro ao recuperar objeto DAO", e);
		}
		return daoAux;
	}
}
