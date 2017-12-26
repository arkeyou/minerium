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
package br.gov.prodigio.comuns.validacoes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.gov.prodigio.comuns.constantes.Constantes;
import br.gov.prodigio.comuns.utils.StringHelper;

public class Validacao {
	private static final Logger log = LoggerFactory.getLogger(Validacao.class);

	static public boolean email(final String hex) {
		Pattern pattern = Pattern.compile(Constantes.EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(hex);
		return matcher.matches();

	}

	// 02998301000181
	static public boolean CNPJ(String str_cnpj) {
		str_cnpj = StringHelper.retornaApenasDigitos(str_cnpj);
		if (str_cnpj.length() != 14) {
			return false;
		}
		int soma = 0, aux, dig;
		String cnpj_calc = str_cnpj.substring(0, 12);

		char[] chr_cnpj = str_cnpj.toCharArray();

		/* Primeira parte */
		for (int i = 0; i < 4; i++) {
			if (chr_cnpj[i] - 48 >= 0 && chr_cnpj[i] - 48 <= 9) {
				soma += (chr_cnpj[i] - 48) * (6 - (i + 1));
			}
		}
		for (int i = 0; i < 8; i++) {
			if (chr_cnpj[i + 4] - 48 >= 0 && chr_cnpj[i + 4] - 48 <= 9) {
				soma += (chr_cnpj[i + 4] - 48) * (10 - (i + 1));
			}
		}
		dig = 11 - (soma % 11);

		cnpj_calc += (dig == 10 || dig == 11) ? "0" : Integer.toString(dig);

		/* Segunda parte */
		soma = 0;
		for (int i = 0; i < 5; i++) {
			if (chr_cnpj[i] - 48 >= 0 && chr_cnpj[i] - 48 <= 9) {
				soma += (chr_cnpj[i] - 48) * (7 - (i + 1));
			}
		}
		for (int i = 0; i < 8; i++) {
			if (chr_cnpj[i + 5] - 48 >= 0 && chr_cnpj[i + 5] - 48 <= 9) {
				soma += (chr_cnpj[i + 5] - 48) * (10 - (i + 1));
			}
		}
		dig = 11 - (soma % 11);
		cnpj_calc += (dig == 10 || dig == 11) ? "0" : Integer.toString(dig);

		return str_cnpj.equals(cnpj_calc);
	}

	public static boolean CPF(String s_aux) {
		// ------- Rotina para CPF
		s_aux = StringHelper.retornaApenasDigitos(s_aux);
		if (s_aux.length() == 11) {
			int d1, d2;
			int digito1, digito2, resto;
			int digitoCPF;
			String nDigResult;
			d1 = d2 = 0;
			digito1 = digito2 = resto = 0;
			for (int n_Count = 1; n_Count < s_aux.length() - 1; n_Count++) {
				digitoCPF = Integer.valueOf(s_aux.substring(n_Count - 1, n_Count)).intValue();
				// --------- Multiplique a ultima casa por 2 a seguinte por 3 a
				// seguinte por 4 e assim por diante.
				d1 = d1 + (11 - n_Count) * digitoCPF;
				// --------- Para o segundo digito repita o procedimento
				// incluindo o primeiro digito calculado no passo anterior.
				d2 = d2 + (12 - n_Count) * digitoCPF;
			}
			;
			// --------- Primeiro resto da divisão por 11.
			resto = (d1 % 11);
			// --------- Se o resultado for 0 ou 1 o digito é 0 caso contário o
			// digito é 11 menos o resultado anterior.
			if (resto < 2) {
				digito1 = 0;
			} else {
				digito1 = 11 - resto;
			}
			d2 += 2 * digito1;
			// --------- Segundo resto da divisão por 11.
			resto = (d2 % 11);
			// --------- Se o resultado for 0 ou 1 o digito é 0 caso contrário o
			// digito é 11 menos o resultado anterior.
			if (resto < 2) {
				digito2 = 0;
			} else {
				digito2 = 11 - resto;
			}
			// --------- Digito verificador do CPF que está sendo validado.
			String nDigVerific = s_aux.substring(s_aux.length() - 2, s_aux.length());
			// --------- Concatenando o primeiro resto com o segundo.
			nDigResult = String.valueOf(digito1) + String.valueOf(digito2);
			// --------- Comparar o digito verificador do cpf com o primeiro
			// resto + o segundo resto.
			return nDigVerific.equals(nDigResult);
		}
		return false;
	}

	public static void main(String[] args) {
		String str = "123/asdas/asdfsdf/4566";
		log.debug(str.replaceAll("[^0-9]", ""));

		log.debug(CNPJ("04.902.979/0001-44") ? "OK" : "Incorreto");
		log.debug(CNPJ("02998301000182") ? "OK" : "Incorreto");

		log.debug(CPF("68265099687") ? "OK" : "Incorreto");
		log.debug(CPF("62865099687") ? "OK" : "Incorreto");

	}
}
