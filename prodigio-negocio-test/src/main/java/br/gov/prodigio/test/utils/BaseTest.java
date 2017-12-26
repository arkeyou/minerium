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
package br.gov.prodigio.test.utils;


import br.gov.prodigio.comuns.utils.Context;
import br.gov.prodigio.entidades.ProVO;

public class BaseTest {

    protected static final String SENHA = "zxczxc";
    protected static final String CONTEXTO_ADMIN = "ssc-admin-web";
    protected static final String CONTEXTO_NAO_VINCULADO = "grp-contabil-web";
    protected static final String IP_MOVIMENTACAO = "000.000.00.00";
    protected static final String CPF_USUARIO = "71515034151";
    protected static final String SENHA_ERRADA = "SenhaErrada";
    protected static final String CPF_INEXISTENTE = "99988877755";


    public static void registrarAuditoria()
    {
        final String ATRIBUTO_USUARIO = "usuarioLogado";
        final String CPF_MOVIMENTAVAO_TEST = "ARQUILLIAN";

        if(Context.getAttribute(ATRIBUTO_USUARIO) == null){
            ProVO appVO = new ProVO();
            appVO.setCdLoginMovimentacao(CPF_MOVIMENTAVAO_TEST);
            appVO.setIpMovimentacao(IP_MOVIMENTACAO);
            Context.setAttribute(ATRIBUTO_USUARIO, new ProVO());
        }
    }



}
