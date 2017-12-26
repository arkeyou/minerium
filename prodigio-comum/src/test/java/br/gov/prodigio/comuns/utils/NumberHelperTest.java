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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Test;

public class NumberHelperTest {

	@Test
	public void testNumeroMenorQueOrigem() {
		Integer origem = Integer.valueOf("5");
		BigDecimal numero = new BigDecimal("4.999999999999999");
		assertTrue(NumberHelper.numeroMenorQueOrigem(origem, numero));
	}

	@Test
	public void testNumeroMenorOuIgualAOrigem() {
		Integer origem = Integer.valueOf("5");
		assertTrue(NumberHelper.numeroMenorOuIgualAOrigem(origem, new BigDecimal("4.999999999999999")));
		assertTrue(NumberHelper.numeroMenorOuIgualAOrigem(origem, origem));
	}

	@Test
	public void testNumeroMaiorQueOrigem() {
		Integer origem = Integer.valueOf("5");
		Double numero = Double.valueOf("5.5");
		assertTrue(NumberHelper.numeroMaiorQueOrigem(origem, numero));
	}

	@Test
	public void testNumeroMaiorOuIgualAOrigem() {
		Integer origem = Integer.valueOf("5");
		Double numero = Double.valueOf("5.5");
		assertTrue(NumberHelper.numeroMaiorOuIgualAOrigem(origem, numero));
		assertTrue(NumberHelper.numeroMaiorOuIgualAOrigem(origem, origem));
	}

	@Test
	public void testNumeroIgualAOrigem() {
		Integer origem = Integer.valueOf("5");
		Double numero = Double.valueOf("5.5");
		assertTrue(NumberHelper.numeroIgualAOrigem(origem, origem));
		assertFalse(NumberHelper.numeroIgualAOrigem(origem, numero));
	}

	@Test
	public void testNumeroEstaEntreFaixa() {
		Integer inicioFaixa = Integer.valueOf("5");
		Double numero = Double.valueOf("5.49999999");
		BigDecimal fimFaixa = new BigDecimal("5.50");
		assertTrue(NumberHelper.numeroEstaEntreFaixa(numero, inicioFaixa, fimFaixa));
		assertFalse(NumberHelper.numeroEstaEntreFaixa(numero, inicioFaixa, numero));
	}

	@Test
	public void testFormatarNumero() {
		Object[] formatarNumero = new Object[] { NumberHelper.formatarNumero(Double.valueOf("0.330999999999"), "R$ #,##0.00") };
		Object[] esperado = new Object[] { "R$ 0,33" };
		assertArrayEquals(esperado, formatarNumero);
	}
}
