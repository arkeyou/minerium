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
package br.gov.prodigio.comuns.constantes;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class Constantes {
	public static final String FILTRO_EXCLUSAO_LOGICA = "ST_REGISTRO IS NULL OR ST_REGISTRO <> 3 ";
	public static final Locale LOCAL_BRAZIL = new Locale("pt", "BR");
	public static final String PADRAO_REAL = new String("###,###,##0.00");
	public static final DecimalFormatSymbols SYMBOlO_REAL = new DecimalFormatSymbols(LOCAL_BRAZIL);
	public static final DecimalFormat REAL = new DecimalFormat(PADRAO_REAL, SYMBOlO_REAL);
	public static final int DuasCasasDecimais = 2;
	public static final int TresCasasDecimais = 3;
	public static final String EMAIL_PATTERN = "^[_A-Za-z0-9-._/]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	public static final int MAIORIDADE = 18;

	public static final String IMAGEM_DEFAULT = "~./imagem/669999.gif";

	public static final Integer PROFUNDIDADE_GRAFO_ENTIDADE = 3;

	public interface BOTOES {

		String APROVAR = "aprovar";
		String ABRIR = "abrir";
		String EDITAR = "editar";
		String CLONAR = "clonar";
		String CONCLUIR = "concluir";
		String EXCLUIR = "excluir";
		String IMPRIMIR = "imprimir";
		String PESQUISAR = "pesquisar";
		String SALVAR = "salvar";
		String NOVO = "novo";
		String PROXIMO = "proximo";
		String DIV_BOTOES = "barradebotoes";
		String VISUALIZAR_ARQUIVOS = "botaoVisualizarArquivos";
		String VISUALIZAR_ARQUIVOS_LABEL = "Visualizar Arquivos";
		String VISUALIZAR_ARQUIVOS_WIDTH = "125px";
		String VER_HISTORICO = "botaoVerHistorico";
		String VER_HISTORICO_LABEL = "Ver Histórico";
		String VER_HISTORICO_WIDTH = "100px";
		String VISUALIZAR_DEMANDA = "botaoVisualizarDemanda";
		String VISUALIZAR_DEMANDA_LABEL = "Visualizar Demanda";
		String VISUALIZAR_DEMANDA_WIDTH = "130px";
		String LIMPAR = "limpar";
		String LIMPAR_LABEL = "Limpar";
		String LIMPAR_WIDTH = "100px";
		String VOLTAR = "botaoVoltar";
		String VOLTAR_LABEL = "Voltar";
		String VOLTAR_WIDTH = "100px";
		String FECHAR = "botaoFechar";
		String FECHAR_LABEL = "Fechar";
		String FECHAR_WIDTH = "100px";

	}

	public interface FLUXO {
		String GRAVAR = "gravar";
		String EXCLUIR = "excluir";
		String EDITAR = "editar";
		String CONCLUIR = "concluir";
		String LISTAR_BASEADO_NO_EXEMPLO = "listarBaseadoNoExemplo";
		String LISTAR = "listar";
	}

	public static final String QUANTIDADE_UNIDADE_VINCULADA = "quantidadeDeUnidade";

}
