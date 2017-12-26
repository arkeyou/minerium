/*
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
 *   
 */
package br.gov.prodigio.comuns.utils;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.junit.Test;

public class DataHelperTest {

	private static final String DD_MM_YYYY_HH_MM_SS = "dd/MM/yyyy HH:mm:ss";
	private static final String DD_MM_YYYY = "dd/MM/yyyy";

	@Test
	public void testCalculaDiferencaEntreDatasDias() {
		try {
			assertTrue("falha de data ", Long.valueOf("3653").equals(DataHelper.calculaDiferencaEntreDatas(DataHelper.retornaData("01/01/2000", DD_MM_YYYY), DataHelper.retornaData("01/01/2010", DD_MM_YYYY), ChronoUnit.DAYS)));
		} catch (ParseException e) {
			fail("falha de parse");
		}
	}

	@Test
	public void testCalculaDiferencaEntreDatasDecadas() {
		try {
			assertTrue("falha de data ", Long.valueOf("1").equals(DataHelper.calculaDiferencaEntreDatas(DataHelper.retornaData("01/01/2000", DD_MM_YYYY), DataHelper.retornaData("01/01/2016", DD_MM_YYYY), ChronoUnit.DECADES)));
		} catch (ParseException e) {
			fail("falha de parse");
		}
	}

	@Test
	public void testCalculaDiferencaEntreDatasHoras() {
		try {
			assertTrue("falha de data ",
					Long.valueOf("35").equals(DataHelper.calculaDiferencaEntreDatas(DataHelper.retornaData("01/01/2016 00:00:00 ", DD_MM_YYYY_HH_MM_SS), DataHelper.retornaData("02/01/2016 11:00:00", DD_MM_YYYY_HH_MM_SS), ChronoUnit.HOURS)));
		} catch (ParseException e) {
			fail("falha de parse");
		}
	}

	@Test
	public void testCalculaDiferencaEntreDatasSegundos() {
		try {
			assertTrue("falha de data ",
					Long.valueOf("35").equals(DataHelper.calculaDiferencaEntreDatas(DataHelper.retornaData("01/01/2016 00:00:00 ", DD_MM_YYYY_HH_MM_SS), DataHelper.retornaData("01/01/2016 00:00:35", DD_MM_YYYY_HH_MM_SS), ChronoUnit.SECONDS)));
		} catch (ParseException e) {
			fail("falha de parse");
		}
	}

	@Test
	public void testAvancaDataDeAcordoComDataOrigemDias() {
		try {
			Date dataInicial = DataHelper.retornaData("01/01/2016", DD_MM_YYYY);
			Date dataComparar = DataHelper.retornaData("02/01/2016", DD_MM_YYYY);
			Date dataCalculada = DataHelper.avancaDataDeAcordoComDataOrigem(dataInicial, Long.valueOf("1"), ChronoUnit.DAYS);
			assertTrue(isDatasIguais(dataCalculada, dataComparar));
		} catch (ParseException e) {
			fail("falha de parse");
		}
	}

	@Test
	public void testAvancaDataDeAcordoComDataOrigemHoras() {
		try {
			Date dataInicial = DataHelper.retornaData("01/01/2016 00:00:00", DD_MM_YYYY_HH_MM_SS);
			Date dataComparar = DataHelper.retornaData("01/01/2016 05:00:00", DD_MM_YYYY_HH_MM_SS);
			Date dataCalculada = DataHelper.avancaDataDeAcordoComDataOrigem(dataInicial, Long.valueOf("5"), ChronoUnit.HOURS);
			assertTrue(isDatasIguais(dataCalculada, dataComparar));
		} catch (ParseException e) {
			fail("falha de parse");
		}
	}

	@Test
	public void testAvancaDataDeAcordoComDataOrigemMinutos() {
		try {
			Date dataInicial = DataHelper.retornaData("01/01/2016 00:00:00", DD_MM_YYYY_HH_MM_SS);
			Date dataComparar = DataHelper.retornaData("01/01/2016 01:01:00", DD_MM_YYYY_HH_MM_SS);
			Date dataCalculada = DataHelper.avancaDataDeAcordoComDataOrigem(dataInicial, Long.valueOf("61"), ChronoUnit.MINUTES);
			assertTrue(isDatasIguais(dataCalculada, dataComparar));
		} catch (ParseException e) {
			fail("falha de parse");
		}
	}

	@Test
	public void testVoltarDataDeAcordoComDataOrigemDias() {
		try {
			Date dataInicial = DataHelper.retornaData("02/01/2016", DD_MM_YYYY);
			Date dataComparar = DataHelper.retornaData("01/01/2016", DD_MM_YYYY);
			Date dataCalculada = DataHelper.voltarDataDeAcordoComDataOrigem(dataInicial, Long.valueOf("1"), ChronoUnit.DAYS);
			assertTrue(isDatasIguais(dataCalculada, dataComparar));
		} catch (ParseException e) {
			fail("falha de parse");
		}
	}

	@Test
	public void testVoltarDataDeAcordoComDataOrigemHoras() {
		try {
			Date dataInicial = DataHelper.retornaData("01/01/2016 05:00:00", DD_MM_YYYY_HH_MM_SS);
			Date dataComparar = DataHelper.retornaData("01/01/2016 00:00:00", DD_MM_YYYY_HH_MM_SS);
			Date dataCalculada = DataHelper.voltarDataDeAcordoComDataOrigem(dataInicial, Long.valueOf("5"), ChronoUnit.HOURS);
			assertTrue(isDatasIguais(dataCalculada, dataComparar));
		} catch (ParseException e) {
			fail("falha de parse");
		}
	}

	@Test
	public void testVoltarDataDeAcordoComDataOrigemMinutos() {
		try {
			Date dataComparar = DataHelper.retornaData("01/01/2016 00:00:00", DD_MM_YYYY_HH_MM_SS);
			Date dataInicial = DataHelper.retornaData("01/01/2016 01:01:00", DD_MM_YYYY_HH_MM_SS);
			Date dataCalculada = DataHelper.voltarDataDeAcordoComDataOrigem(dataInicial, Long.valueOf("61"), ChronoUnit.MINUTES);
			assertTrue(isDatasIguais(dataCalculada, dataComparar));
		} catch (ParseException e) {
			fail("falha de parse");
		}
	}

	private boolean isDatasIguais(Date dataArg1, Date dataArg2) {
		return dataArg1.compareTo(dataArg2) == 0;
	}

}
