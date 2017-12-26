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
package br.gov.prodemge.test;

import java.io.InputStream;
import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.hsqldb.HsqldbDataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.hibernate.HibernateException;
import org.hibernate.internal.SessionImpl;
import org.junit.AfterClass;
import org.junit.BeforeClass;

public class AbstractDbUnitJpaTest {

	private static EntityManagerFactory entityManagerFactory;
	private static IDatabaseConnection connection;
	private static IDataSet dataset;
	private static EntityManager entityManager;

	private AbstractDbUnitJpaTest() {
	}

	/**
	 * Will load test-dataset.xml before each test case
	 * 
	 * @throws DatabaseUnitException
	 * @throws HibernateException
	 */
	@BeforeClass
	public static void initEntityManager() throws HibernateException, DatabaseUnitException {
		entityManagerFactory = Persistence.createEntityManagerFactory("persistence-test");
		entityManager = entityManagerFactory.createEntityManager();
		connection = new DatabaseConnection(((SessionImpl) (entityManager.getDelegate())).connection());
		connection.getConfig().setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new HsqldbDataTypeFactory());

		FlatXmlDataSetBuilder flatXmlDataSetBuilder = new FlatXmlDataSetBuilder();
		flatXmlDataSetBuilder.setColumnSensing(true);
		InputStream dataSet = Thread.currentThread().getContextClassLoader().getResourceAsStream("test-data.xml");
		dataset = flatXmlDataSetBuilder.build(dataSet);

		try {
			DatabaseOperation.CLEAN_INSERT.execute(connection, dataset);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@AfterClass
	public static void closeEntityManager() {
		entityManager.close();
		entityManagerFactory.close();
	}

	/**
	 * Will clean the dataBase before each test
	 * 
	 * @throws SQLException
	 * @throws DatabaseUnitException
	 */
	// @Before
	public void cleanDB() throws DatabaseUnitException, SQLException {
		DatabaseOperation.CLEAN_INSERT.execute(connection, dataset);
	}

	public static EntityManager getInstance() {
		try {
			if (entityManager == null)
				initEntityManager();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return entityManager;
	}
}
