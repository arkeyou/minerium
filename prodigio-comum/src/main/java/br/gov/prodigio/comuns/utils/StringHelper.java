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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.Normalizer;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.swing.text.MaskFormatter;

import br.gov.prodigio.comuns.constantes.Constantes;

/**
 * Classe utilitária responsável por tratar strings
 * 
 * @author Sândalo Bessa
 */
public class StringHelper {

	public static final String CARACTERES_NAO_ALFABETICOS = "()[]><#:;^,.?!|&_`~@$%/\\=+-*\"\'";

	private static final String NON_THIN = "[^iIl1\\.,']";

	/**
	 * Metodo que recebe uma string e retorna apenas digitos
	 * 
	 * @param str
	 *            - String contendo digitos e outros caracteres
	 * @return Retorna apenas digitos.
	 */
	public static String retornaApenasDigitos(String str) {
		return str.replaceAll("[^0-9]", "");
	}

	/**
	 * Metodo que retira todas as ocorrencias de aspas simples de uma string
	 * 
	 * @param str
	 *            - String contendo aspas simples
	 * @return Retorna string sem aspas simples.
	 */
	public static String retornaSemApasSimples(String str) {
		return str.replaceAll("[']", "");
	}

	/**
	 * Metodo que retira todas as ocorrencias de barras para esquerda de uma string
	 * 
	 * @param str
	 *            - String contendo barras a esquerda
	 * @return Retorna string sem aspas simples.
	 */
	public static String retornaSemBarraPraEsquerda(String str) {
		return str.replaceAll("[\\\\]", "");
	}

	/**
	 * Metodo que retorna um BigDecimal a partir de uma string
	 * 
	 * @param str
	 *            - String contendo o valor
	 * @return Retorna um BigDecimal.
	 */
	public static BigDecimal strToBigDecimal(String strValor) {
		DecimalFormat df = Constantes.REAL;
		df.setParseBigDecimal(true);
		df.setMinimumFractionDigits(2);
		try {
			return (BigDecimal) df.parse(strValor);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * Metodo que retorna um Long a partir de uma string
	 * 
	 * @param str
	 *            - String contendo o valor
	 * @return Retorna um Long.
	 */
	public static Long strToLong(String strValor) {

		strValor = strValor.replace(".", "").replace("/", "").replace("-", "").replace("(", "").replace(")", "").replace(" ", "");
		return Long.parseLong(strValor);
	}

	/**
	 * Metodo que retorna um Double a partir de uma string
	 * 
	 * @param strValor
	 *            - String contendo o valor
	 * @return Retorna um Double.
	 */
	public static Double strToDouble(String strValor) {
		strValor = strValor.replace(".", "").replace("/", "").replace("-", "").replace("(", "").replace(")", "").replace(" ", "");
		return Double.parseDouble(strValor);
	}

	/**
	 * Metodo que retorna um Integer a partir de uma string
	 * 
	 * @param strValor
	 *            - String contendo o valor
	 * @return Retorna um Integer.
	 */
	public static Integer strToInt(String strValor) {

		strValor = strValor.replace(".", "").replace("/", "").replace("-", "").replace("(", "").replace(")", "").replace(" ", "");
		return Integer.parseInt(strValor);

	}

	/**
	 * Metodo que retorna um cpf no formato 000.000.000/00
	 * 
	 * @param valor
	 *            - Long contendo o valor
	 * @return Retorna o cpf formatado.
	 */
	public static String longToCpf(Long valor) {
		if (valor != null && !"".equals(valor) && !valor.equals("null")) {
			String valor2 = preencheStringComCaracter(valor.toString(), '0', 11);
			return valor2.substring(0, 3) + "." + valor2.substring(3, 6) + "." + valor2.substring(6, 9) + "/" + valor2.substring(9, 11);
		} else {
			return null;
		}

	}

	/**
	 * Acrescenta a máscara, separando os níveis, do código contábil. Apenas segui o que já existe, embora acredito que não seja o melhor lugar para esse método
	 * 
	 * @param codigoConta
	 *            - Código sem a formatação.
	 * @param nivelSimplesMaximo
	 *            - Indica a quantidade de níveis simples. (Padrão tem sido o valor = 5)
	 * @return Código com a formatação.
	 */
	public static String acrescentarMascara(String codigoConta, int nivelSimplesMaximo) {
		StringBuilder codigoComMascara = new StringBuilder();
		boolean posicaoImpar = true;
		char caracterDivisaoNiveis = '.';

		for (int i = 0; i < codigoConta.length(); i++) {

			codigoComMascara.append(codigoConta.charAt(i));

			if (i < nivelSimplesMaximo || !posicaoImpar) {
				codigoComMascara.append(caracterDivisaoNiveis);
				posicaoImpar = true;
			} else {
				posicaoImpar = false;
			}
		}

		if (codigoComMascara.length() > 0 && codigoComMascara.charAt(codigoComMascara.length() - 1) == caracterDivisaoNiveis) {
			return codigoComMascara.substring(0, codigoComMascara.length() - 1);
		}

		return codigoComMascara.toString();
	}

	/**
	 * Metodo que retorna um CEP no formato 00000-000 a partir de um long
	 * 
	 * @param valor
	 *            - Long contendo o valor
	 * @return Retorna o CEP formatado.
	 */
	public static String longToCep(Long valor) {
		if (valor != null && !"".equals(valor) && !valor.equals("null")) {
			String valor2 = preencheStringComCaracter(valor.toString(), '0', 8);
			return valor2.substring(0, 5) + "-" + valor2.substring(5, 8);
		} else {
			return null;
		}

	}

	/**
	 * Metodo que retorna um CNPJ no formato 00.000.000/0000-00 a partir de um long
	 * 
	 * @param valor
	 *            - Long contendo o valor
	 * @return Retorna o CNPJ formatado.
	 */
	public static String longToCnpj(Long valor) {
		if (valor != null && !"".equals(valor) && !valor.equals("null")) {
			String valor2 = preencheStringComCaracter(valor.toString(), '0', 14);
			return valor2.substring(0, 2) + "." + valor2.substring(2, 5) + "." + valor2.substring(5, 8) + "/" + valor2.substring(8, 12) + "-" + valor2.substring(12, 14);
		} else {
			return null;
		}

	}

	/**
	 * Metodo que retorna um Telefone no formato (00)5555-5555 a partir de um long
	 * 
	 * @param valor
	 *            - Long contendo o valor
	 * @return Retorna o telefone formatado.
	 */
	public static String longToTelefone(Long valor) {
		if (valor != null && !"".equals(valor) && !valor.equals("null")) {
			String valor2 = preencheStringComCaracter(valor.toString(), '0', 10);
			return "(" + valor2.substring(0, 2) + ")" + valor2.substring(2, 6) + "-" + valor2.substring(6, 10);
		} else {
			return null;
		}

	}

	/**
	 * Metodo que preenche o inicio de uma determinada string com um determinado caracter até que se obtenha um tamanho desejado para a string.
	 * 
	 * @param texto
	 *            - Texto no qual se deseja adicionar um determinado caracter até que atingir um certo tamanho.
	 * @param caracter
	 *            - Caracter com oqual se deseja preencher a string até atingir um tamanho desejado.
	 * @param tamanho
	 *            - Tamanho final da string
	 * @return Retorna string com caracters preenchidos.
	 */
	public static String preencheStringComCaracter(String texto, char caracter, int tamanho) {

		String textoRetorno = "";
		byte contador = 0;
		if (texto.length() < tamanho) {
			for (contador = 1; contador <= (tamanho - texto.length()); contador++) {
				textoRetorno += caracter;
			}
			return (textoRetorno + texto);
		} else {
			return texto;
		}
	}

	/**
	 * Metodo que verifica se uma string esta vazia.
	 * 
	 * @param str
	 *            - A string que se deseja validar
	 * 
	 * @return Retorna true ou false.
	 */
	public static boolean isEmpty(String str) {
		return str == null || str.trim().length() == 0;
	}

	/**
	 * Metodo que verifica se uma string não esta vazia.
	 * 
	 * @param str
	 *            - A string que se deseja validar
	 * 
	 * @return Retorna true ou false.
	 */
	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	/**
	 * Metodo não deve ser utilizado, será retirado em breve
	 * 
	 * @deprecated
	 */
	@Deprecated
	public static String format(String pattern, Object value) {/* TODO - Refatorar este método para não depender to pacote javax.swing. */
		MaskFormatter mask;
		try {
			mask = new MaskFormatter(pattern);
			mask.setValueContainsLiteralCharacters(false);
			return mask.valueToString(value);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	public static boolean verificaExistenciaRepeticaoSequencialDeCaracter(String texto) {
		if (StringHelper.isNotEmpty(texto)) {
			int tam = texto.length();
			String caracter;
			for (int i = 1; i < tam; i++) {
				caracter = texto.substring(i - 1, i);
				if (texto.substring(i).startsWith(caracter)) {
					return true;
				}
			}
		}
		return false;
	}

	public static int contaNumeroOcorrenciasCaracter(String texto, char caracterProcurado) {
		int num = 0;
		for (int i = 0; i < texto.length(); i++) {
			if (texto.charAt(i) == caracterProcurado) {
				num++;
			}
		}
		return num;
	}

	public static boolean verificaLimiteRepeticoesCaracter(String texto) {
		if (StringHelper.isNotEmpty(texto)) {
			String caracterProcurado;
			while (texto.length() > 1) {
				caracterProcurado = texto.substring(0, 1);
				if (texto.substring(1).contains(caracterProcurado) && 1 /* caracterprocurado */+ contaNumeroOcorrenciasCaracter(texto.substring(1), caracterProcurado.charAt(0)) > 2) {
					return false;
				}
				texto = texto.replaceAll(caracterProcurado, "");
			}
		}
		return true;
	}

	public static String removeAcento(String texto) {
		texto = Normalizer.normalize(texto, Normalizer.Form.NFD);
		texto = texto.replaceAll("[^\\p{ASCII}]", "");

		return texto;
	}

	public static String concatenarStrings(Collection<String> palavras, String stringJuncao) {
		String retorno = "";
		if (CollectionHelper.isNotEmpty(palavras)) {
			retorno = palavras.stream().filter(StringHelper::isNotEmpty).collect(Collectors.joining(stringJuncao == null ? "" : stringJuncao));
		}
		return retorno;
	}

	public static boolean validarPattern(String palavra, String pattern) {
		if (pattern == null) {
			throw new IllegalArgumentException("O parametro pattern não foi informado");
		}
		return palavra != null && palavra.matches(pattern);
	}

	/**
	 * TRANSFORMA TEXTO EM LISTA DE ACORDO COM O SEPARADOR PASSADO.
	 * 
	 * @param texto
	 *            texto que sera tratado
	 * @param separador
	 *            separador para split de texto (caso seja nulo considera espaco em branco)
	 * @return {@link List} de {@link String}
	 */
	public static List<String> transformarTextoEmLista(String texto, String separador) {
		List<String> retorno = Collections.emptyList();
		if (StringHelper.isNotEmpty(texto)) {
			String regex = StringHelper.isEmpty(separador) ? " " : separador;
			List<String> itens = Arrays.asList(texto.split(regex));
			retorno = itens.stream().filter(StringHelper::isNotEmpty).collect(Collectors.toList());
		}
		return retorno;
	}

	/**
	 * RETORNA CONTEUDO DE UM ARQUIVO DO CLASSLOADER COMO TEXTO.
	 * 
	 * @param filePath
	 *            referencia do arquivo
	 * @return {@link String} do conteudo do arquivo
	 */
	public static String retornarConteudoDeArquivoResource(String filePath) {
		if (StringHelper.isEmpty(filePath)) {
			throw new IllegalArgumentException("O parametro filePath não foi informado");
		}
		try {
			InputStream streamResource = StringHelper.class.getClassLoader().getResourceAsStream(filePath);
			List<String> linhasCabecalho = new BufferedReader(new InputStreamReader(streamResource)).lines().collect(Collectors.toList());
			if (CollectionHelper.isEmpty(linhasCabecalho)) {
				return "";
			}
			return concatenarStrings(linhasCabecalho, "");
		} catch (Exception e) {
			throw new RuntimeException("Não foi possivel ler o arquivo", e);
		}

	}

	public static String replaceConteudoPorItemMapaParametros(String texto, Map<String, String> itensMapaParametros) {
		if (StringHelper.isEmpty("texto") || itensMapaParametros == null) {
			throw new IllegalArgumentException("Os parametros texto ");
		}
		String padraoChaveInicio = "#\\{";
		String padraoChaveFim = "\\}";
		return itensMapaParametros.entrySet().stream().reduce(texto, (textoAlterado, entry) -> replaceConteudo(textoAlterado, padraoChaveInicio.concat(entry.getKey()).concat(padraoChaveFim), entry.getValue()), (s1, s2) -> null);
	}

	public static String replaceConteudoPorItemMapaParametros(String texto, Map<String, String> itensMapaParametros, String padraoChaveInicio, String padraoChaveFim) {
		if (StringHelper.isEmpty("texto") || itensMapaParametros == null) {
			throw new IllegalArgumentException("Os parametros texto ");
		}

		return itensMapaParametros.entrySet().stream()
				.reduce(texto, (textoAlterado, entry) -> replaceConteudo(textoAlterado, concatenarStrings(Arrays.asList(padraoChaveInicio, entry.getKey(), padraoChaveFim), ""), entry.getValue()), (s1, s2) -> null);
	}

	public static String replaceConteudo(String texto, String key, String value) {
		return texto.replaceAll(key, value);
	}

	public static Set<String> retornarPalavrasEntre(String text, String after, String before) {
		Set<String> retorno = new HashSet<String>();
		Pattern pattern = Pattern.compile(after.concat("(.+?)").concat(before));
		Matcher matcher = pattern.matcher(text);
		while (matcher.find()) {
			retorno.add(matcher.group(Integer.valueOf("1")));
		}
		return retorno;
	}

	/**
	 * RETORNA CONTEUDO DE UM ARQUIVO DO CLASSLOADER COMO TEXTO.
	 * 
	 * @param filePath
	 *            referencia do arquivo
	 * @return {@link String} do conteudo do arquivo
	 */
	public static String retornarConteudoDeArquivoInterno(String filePath) {
		if (StringHelper.isEmpty(filePath)) {
			throw new IllegalArgumentException("O parametro filePath não foi informado");
		}
		try (InputStream streamResource = StringHelper.class.getClassLoader().getResourceAsStream(filePath); BufferedReader buffer = new BufferedReader(new InputStreamReader(streamResource));) {

			List<String> linhasCabecalho = buffer.lines().collect(Collectors.toList());
			if (CollectionHelper.isEmpty(linhasCabecalho)) {
				return "";
			}
			return StringHelper.concatenarStrings(linhasCabecalho, "");
		} catch (Exception e) {
			throw new RuntimeException("Não foi possivel ler o arquivo", e);
		}
	}
}
