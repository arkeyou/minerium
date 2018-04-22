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
package br.gov.prodigio.negocio.seguranca;

//import br.gov.prodemge.ssc.interfaces.IUsuario;
import br.gov.prodigio.comuns.anotacoes.RegraDeNegocio;
import br.gov.prodigio.negocio.ProBaseRN;

public class UsuarioRN extends ProBaseRN {
	/*@RegraDeNegocio(autor = "Sândalo Bessa", codigo = "333", ordem = 0, fluxo = "gravar")
	public void teste(IUsuario usuario) {
		System.out.println("Teste");
	}*/

	/*@RegraDeNegocio(autor = "Sândalo Bessa", codigo = "333", ordem = 0, fluxo = "validar")
	public void validarNome(IUsuario usuario) {
		if (usuario != null && usuario.getNome().equals("MARIA")) {
			throw new RuntimeException("De acordo com o cadastro de nomes, esse nome não é valido");
		}
	}*/

}
