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

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Classe utilitária para resposável por tratar numeros
 * 
 * @author Glauco Gonçalves
 */
public final class NumberHelper {
	private NumberHelper() {
	}

	/**
	 * Metodo responsavel por tranformar BigDecimal em String de qualquer formato
	 * 
	 * @param value
	 *            do BigDecimal
	 * @param formato
	 *            que se deseja
	 * @return numero em formato String
	 */
	public static final String bigDecimalToStr(BigDecimal value, DecimalFormat moneyFormat) {

		if (value == null) {
			return null;
		}
		return moneyFormat.format(value);

	}

	/**
	 * COMPARA NUMEROS ONDE O NUMERO TEM QUE SER MENOR QUE A ORIGEM.
	 * 
	 * @param origem
	 *            instancia de {@linkplain Number}
	 * 
	 * @param numero
	 *            instancia de {@linkplain Number}
	 * @return
	 */
	public static boolean numeroMenorQueOrigem(Number origem, Number numero) {
		return compare(numero, origem) == Integer.valueOf("-1");
	}

	/**
	 * COMPARA NUMEROS ONDE O NUMERO TEM QUE SER MENOR OU IGUAL A ORIGEM.
	 * 
	 * @param origem
	 *            instancia de {@linkplain Number}
	 * @param numero
	 *            instancia de {@linkplain Number}
	 * @return
	 */
	public static boolean numeroMenorOuIgualAOrigem(Number origem, Number numero) {
		return numeroMenorQueOrigem(origem, numero) || numeroIgualAOrigem(origem, numero);
	}

	/**
	 * COMPARA NUMEROS ONDE O NUMERO TEM QUE SER MAIOR QUE A ORIGEM.
	 * 
	 * @param origem
	 *            instancia de {@linkplain Number}
	 * @param numero
	 *            instancia de {@linkplain Number}
	 * @return
	 */
	public static boolean numeroMaiorQueOrigem(Number origem, Number numero) {
		return compare(numero, origem) == Integer.valueOf("1");
	}

	/**
	 * COMPARA NUMEROS ONDE O NUMERO TEM QUE SER MAIOR OU IGUAL A ORIGEM.
	 * 
	 * @param origem
	 *            instancia de {@linkplain Number}
	 * @param numero
	 *            instancia de {@linkplain Number}
	 * @return
	 */
	public static boolean numeroMaiorOuIgualAOrigem(Number origem, Number numero) {
		return numeroIgualAOrigem(origem, numero) || numeroMaiorQueOrigem(origem, numero);
	}

	/**
	 * COMPARA NUMEROS ONDE O NUMERO TEM QUE SER IGUAL A ORIGEM.
	 * 
	 * @param origem
	 *            instancia de {@linkplain Number}
	 * @param numero
	 *            instancia de {@linkplain Number}
	 * @return
	 */
	public static boolean numeroIgualAOrigem(Number origem, Number numero) {
		return compare(origem, numero) == Integer.valueOf("0");
	}

	private static int compare(Number origem, Number numero) {
		if (origem == null || numero == null) {
			throw new IllegalArgumentException("Os campos origem e numero devem ser informados");
		}
		return new BigDecimal(origem.toString()).compareTo(new BigDecimal(numero.toString()));
	}

	/**
	 * VERIFICA SE UM NUMERO ESTA DENTRO DE UMA FAIXA DE VALORES.
	 * 
	 * @param numero
	 *            instancia de {@linkplain Number}
	 * @param inicioFaixa
	 *            instancia de {@linkplain Number}
	 * @param fimFaixa
	 *            instancia de {@linkplain Number}
	 * @return
	 */
	public static boolean numeroEstaEntreFaixa(Number numero, Number inicioFaixa, Number fimFaixa) {
		if (numero == null || inicioFaixa == null || fimFaixa == null) {
			throw new IllegalArgumentException("Os campos numero, inicioFaixa e fimFaixa devem ser informados");
		}
		return numeroMenorQueOrigem(fimFaixa, numero) && numeroMaiorQueOrigem(inicioFaixa, numero);
	}

	/**
	 * 
	 * @param number
	 * @param formato
	 * @return
	 */
	public static final String formatarNumero(Number number, String formato) {
		if (formato == null) {
			throw new IllegalArgumentException("O formato deve ser informado");
		}
		if (number != null) {
			return formatarNumero(number, new DecimalFormat(formato));
		}
		return null;
	}

	/**
	 * @param number
	 * @param formatter
	 * @return
	 */
	public static final String formatarNumero(Number number, DecimalFormat formatter) {
		if (formatter == null) {
			throw new IllegalArgumentException("O formatter deve ser informado");
		}
		if (number != null) {
			BigDecimal decimalNumber = new BigDecimal(number.toString());
			return formatter.format(decimalNumber);
		}
		return null;
	}

}
