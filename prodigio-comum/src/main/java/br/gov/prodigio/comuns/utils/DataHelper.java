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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.bind.DatatypeConverter;

/**
 * Classe utilitária para responsável por tratar datas
 * 
 * @author Glauco Gonçalves
 */
public final class DataHelper {
	private static Calendar dataNascimento = Calendar.getInstance();
	private static Calendar dataAtual = Calendar.getInstance();

	private DataHelper() {
	}

	public static Integer getIdade(Date data) {
		dataNascimento.setTime(data);
		Integer diferencaMes = dataAtual.get(Calendar.MONTH) - dataNascimento.get(Calendar.MONTH);
		Integer diferencaDia = dataAtual.get(Calendar.DAY_OF_MONTH) - dataNascimento.get(Calendar.DAY_OF_MONTH);
		Integer idade = (dataAtual.get(Calendar.YEAR) - dataNascimento.get(Calendar.YEAR));

		if (diferencaMes < 0 || (diferencaMes == 0 && diferencaDia < 0)) {
			idade--;
		}
		return idade;
	}

	/**
	 * Converte para String a data informada. Além disso, converte-a em horário GMT. Esta conversão para GMT foi necessária porque o tipo data esperado pela interface REST não tem suporte a fuso horário, sempre entendendo a data informada como sendo
	 * GMT.
	 * 
	 * @param data
	 * @return data em formato String
	 */
	public static String converteData(Date data) {

		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ss");
		// converte a data para GMT, antes de converter para String
		calendar.setTime(data);
		int offset = calendar.getTimeZone().getOffset(calendar.getTimeInMillis());
		calendar.setTimeInMillis(calendar.getTimeInMillis() - offset);

		return dateFormat.format(calendar.getTime());
	}

	public static String converteDataHora(Date data) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
		return dateFormat.format(data);
	}

	/**
	 * Acrescentar um dia na data para realizar busca entre datas. (Sugestão do Christian)
	 * 
	 * @param date
	 * 
	 * @return Date.
	 */
	public static Date acrescentarNDiasNaData(Date data, Integer acrescimo) {
		Calendar dataConvertida = Calendar.getInstance();
		dataConvertida.setTime(data);
		dataConvertida.add(Calendar.DAY_OF_MONTH, acrescimo);
		data = dataConvertida.getTime();
		return data;
	}

	public static boolean verificaValidade(Date dataInicio, Date dataFim) {
		Date dataAtual = new Date();
		return isDataInicioNulaAnteriorAtual(dataInicio, dataAtual) && isDataFimNulaPosteriorAtual(dataFim, dataAtual);
	}

	public static boolean verificaValidade(Date dataInicial, int diasValidade) {
		Date dataFinal = DataHelper.acrescentarNDiasNaData(dataInicial, diasValidade);
		return verificaValidade(dataInicial, dataFinal);
	}

	private static boolean isDataInicioNulaAnteriorAtual(Date dataInicio, Date dataAtual) {
		return dataInicio == null || dataInicio.before(dataAtual) || dataInicio.equals(dataAtual);
	}

	private static boolean isDataFimNulaPosteriorAtual(Date dataFim, Date dataAtual) {
		return dataFim == null || dataFim.after(dataAtual) || dataFim.equals(dataAtual);
	}

	public static Date getDataImutavel(Date data) {
		if (data != null) {
			return new Date(data.getTime());
		}
		return data;
	}

	/**
	 * Utilizar o método {@link #retornaDataAtual()} ou {@link #retornaDataHoraAtual()}
	 */
	@Deprecated
	public static Date hoje() {
		return GregorianCalendar.getInstance().getTime();
	}

	public static Date converteStringParaData(String stringData) {
		return DatatypeConverter.parseDateTime(stringData).getTime();
	}

	/**
	 * CALCULA DIFERENÇA ENTRE DUAS DATAS.
	 * 
	 * @param dataInicio
	 *            {@link Date} DATA INICIAL
	 * @param dataFim
	 *            {@link Date} DATA FINAL
	 * @param unidadeCronologica
	 *            {@link ChronoUnit} UNIDADE CRONOLOGICA
	 * @return {@link Long} DIFERENÇA ENTRE DUAS DATAS
	 */
	public static Long calculaDiferencaEntreDatas(Date dataInicio, Date dataFim, ChronoUnit unidadeCronologica) {
		if (dataInicio == null || dataFim == null || unidadeCronologica == null) {
			throw new NullPointerException("DataUtil.calculaDiferencaEntreDatas:: O valor de todos os parametros devem ser informados");
		}
		LocalDateTime ldInicio = retornaLocalDateTime(dataInicio);
		LocalDateTime ldFim = retornaLocalDateTime(dataFim);
		return unidadeCronologica.between(ldInicio, ldFim);
	}

	/**
	 * AVANÇA DATA DE ACORDO COM UMA DATA INICIAL.
	 * 
	 * @param dataInicial
	 *            {@link Date} DATA INICIAL
	 * @param quantidade
	 *            {@link Long} QUANTIDADE QUE SERÁ ADICIONADA A DATA INICIAL
	 * @param unidadeTemporal
	 *            {@link ChronoUnit} UNIDADE CRONOLOGICA
	 * 
	 * @return {@link Date} DATA APOS ADIÇÃO DE VALORES
	 */
	public static Date avancaDataDeAcordoComDataOrigem(Date dataInicial, Long quantidade, TemporalUnit unidadeTemporal) {
		return retornaDataFromLocalDateTime(retornaLocalDateTime(dataInicial).plus(quantidade, unidadeTemporal));
	}

	/**
	 * VOLTA DATA DE ACORDO COM DATA INICIAL.
	 * 
	 * @param dataInicial
	 *            {@link Date} DATA INICIAL
	 * @param quantidade
	 *            {@link Long} QUANTIDADE QUE SERÁ SUBTRAIDA A DATA INICIAL
	 * @param unidadeTemporal
	 *            {@link ChronoUnit} UNIDADE CRONOLOGICA
	 * 
	 * @return {@link Date} DATA APOS SUBTRAÇÃO DE VALORES
	 */
	public static Date voltarDataDeAcordoComDataOrigem(Date dataInicial, Long quantidade, TemporalUnit unidadeTemporal) {
		return retornaDataFromLocalDateTime(retornaLocalDateTime(dataInicial).minus(quantidade, unidadeTemporal));
	}

	/**
	 * @param dataFinal
	 * @return
	 */
	private static Date retornaDataFromLocalDate(LocalDate dataFinal) {
		return Date.from(dataFinal.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
	}

	/**
	 * @return {@link Date} DATA ATUAL
	 */
	public static Date retornaDataAtual() {
		return retornaDataFromLocalDate(LocalDate.now());
	}

	/**
	 * @return {@link Date} DATA HORA ATUAL
	 */
	public static Date retornaDataHoraAtual() {
		return retornaDataFromLocalDateTime(LocalDateTime.now());
	}

	/**
	 * @param data
	 * @param formato
	 * @return
	 * @throws ParseException
	 */
	public static Date retornaData(String data, String formato) throws ParseException {
		if (data == null || formato == null) {
			throw new NullPointerException("DataUtil.retornaData:: O valor dos parametros data e formato devem ser informados");
		}
		return new SimpleDateFormat(formato).parse(data);
	}

	/**
	 * CONVERTE DATA PARA UM FORMATO ESPERADO
	 * 
	 * @param data
	 *            {@link Date} DATA
	 * @param formato
	 *            FORMATO DE DATA ESPERADO
	 * @return {@link String} COM VALOR DA DATA
	 */
	public static String converteData(Date data, String formato) {
		if (data == null || formato == null) {
			throw new NullPointerException("DataUtil.converteData:: O valor dos parametros data e formato devem ser informados");
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(formato);
		return dateFormat.format(data);
	}

	/**
	 * RETORNA DIA DA SEMANA PARA A DATA INFORMADA.
	 * 
	 * @param data
	 *            {@link Date} DATA
	 * @return {@link DayOfWeek} DIA DA SEMANA
	 */
	public static DayOfWeek retornaDiaDaSemana(Date data) {
		return retornaLocalDateTime(data).getDayOfWeek();
	}

	private static Date retornaDataFromLocalDateTime(LocalDateTime dataFinal) {
		return Date.from(dataFinal.atZone(ZoneId.systemDefault()).toInstant());
	}

	private static LocalDateTime retornaLocalDateTime(Date data) {
		return getInstant(data).atZone(ZoneId.systemDefault()).toLocalDateTime();
	}

	private static Date tratarSqlDate(Date data) {
		Date retorno = data;
		if (data instanceof java.sql.Date) {
			retorno = new Date(data.getTime());
		}
		return retorno;
	}

	private static Instant getInstant(Date data) {
		return tratarSqlDate(data).toInstant();
	}

	public static String retornaPeriodoEntreDatas(Date dataInicio, Date dataFim, FormatoPeriodo formatoPeriodo) {
		Period periodo = Period.between(retornaLocalDateTime(dataInicio).toLocalDate(), retornaLocalDateTime(dataFim).toLocalDate());
		StringBuilder retorno = new StringBuilder("");
		String periodoAnos = periodo.getYears() + " ANO(S)";
		String periodoMes = periodo.getMonths() + " MES(ES)";
		String periodoDias = periodo.getDays() + " DIA(S)";

		switch (formatoPeriodo) {
		case ANO:
			retorno.append(periodoAnos);
			break;
		case ANO_MES:
			retorno.append(periodoAnos).append(" E ").append(periodoMes);
			break;
		case ANO_MES_DIA:
			retorno.append(periodoAnos).append(", ").append(periodoMes).append(" E ").append(periodoDias);
			break;
		default:
			retorno.append("INDEFINIDO");
			break;
		}

		return retorno.toString();
	}

	public enum FormatoPeriodo {
		ANO, ANO_MES, ANO_MES_DIA
	}

	public static Date retornaDataParaDateTime(long time) {
		return new Date(time);
	}
}
