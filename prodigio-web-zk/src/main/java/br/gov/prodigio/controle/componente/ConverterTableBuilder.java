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
package br.gov.prodigio.controle.componente;

import java.math.BigDecimal;
import java.util.Date;

import br.gov.prodigio.comuns.constantes.Constantes;
import br.gov.prodigio.comuns.utils.DataHelper;
import br.gov.prodigio.comuns.utils.NumberHelper;

public class ConverterTableBuilder implements IConverterTableBuilder {

	@Override
	public String converter(Object valor) {
		if (valor == null) {
			return "";
		}
		if (valor instanceof Boolean) {
			if ((Boolean) valor) {
				return "Sim";
			}
			return "Não";
		}

		if (valor instanceof Date) {
			return DataHelper.converteDataHora((Date) valor);
		}

		if (valor instanceof BigDecimal) {
			return NumberHelper.bigDecimalToStr((BigDecimal) valor, Constantes.REAL);
		}
		return valor.toString();
	}

	public static void main(String[] args) {
		ConverterTableBuilder a = new ConverterTableBuilder();
		System.out.println(a.converter(null));
		System.out.println(a.converter(Boolean.TRUE));
		System.out.println(a.converter(new Date()));
		System.out.println(a.converter(new BigDecimal(10)));
	}
}
