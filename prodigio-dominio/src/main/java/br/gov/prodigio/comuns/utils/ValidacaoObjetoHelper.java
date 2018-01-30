package br.gov.prodigio.comuns.utils;

import java.util.Arrays;

public final class ValidacaoObjetoHelper {
	private static final String MSG_ERRO_COMUM = "Os parâmetros \"objeto\" e \"campos\" são obrigatórios";

	private ValidacaoObjetoHelper() {
	}

	/**
	 * VERIFICAR SE CAMPO NAO ESTA PREENCHIDO.
	 * 
	 * @param objeto
	 *            objeto raiz
	 * @param nomeCampo
	 *            nome do campo recuperado a ser validado
	 * @return
	 */
	public static boolean isCampoNaoPreenchido(Object objeto, String nomeCampo) {
		if (objeto == null || nomeCampo == null) {
			throw new IllegalArgumentException("Os parâmetros \"objeto\" e \"nomeCampo\" são obrigatórios");
		}
		try {
			Object valorPropriedade = Reflexao.recuperaValorDaPropriedade(nomeCampo, objeto);
			if (valorPropriedade instanceof String) {
				return StringHelper.isEmpty((String) valorPropriedade);
			}
			return valorPropriedade == null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static boolean isCampoPreenchido(Object objeto, String nomeCampo) {
		return !isCampoNaoPreenchido(objeto, nomeCampo);
	}

	/**
	 * VERIFICAR SE TODOS OS CAMPOS ESTAO PREENCHIDOS.
	 * 
	 * @param objeto
	 *            objeto raiz
	 * @param campos
	 *            campos que serao validados
	 * @return
	 */
	public static boolean isTodosOsCamposPreenchidos(Object objeto, String... campos) {
		if (objeto == null || campos == null) {
			throw new IllegalArgumentException(MSG_ERRO_COMUM);
		}
		return Arrays.asList(campos).stream().allMatch(nomeCampo -> isCampoPreenchido(objeto, nomeCampo));
	}

	/**
	 * VERIFICAR SE NENHUM CAMPO ESTA PREENCHIDOS.
	 * 
	 * @param objeto
	 *            objeto raiz
	 * @param campos
	 *            campos que serao validados
	 * @return
	 */
	public static boolean isNenhumCampoPreenchido(Object objeto, String... campos) {
		return !isAlgumCampoPreenchido(objeto, campos);
	}

	/**
	 * VERIFICAR SE EXISTE ALGUM OS CAMPO PREENCHIDO.
	 * 
	 * @param objeto
	 *            objeto raiz
	 * @param campos
	 *            campos que serao validados
	 * @return
	 */
	public static boolean isAlgumCampoPreenchido(Object objeto, String... campos) {
		if (objeto == null || campos == null) {
			throw new IllegalArgumentException(MSG_ERRO_COMUM);
		}
		return Arrays.asList(campos).stream().anyMatch(nomeCampo -> isCampoPreenchido(objeto, nomeCampo));
	}

	/**
	 * VERIFICAR SE EXISTE ALGUM OS CAMPO NAO PREENCHIDO.
	 * 
	 * @param objeto
	 *            objeto raiz
	 * @param campos
	 *            campos que serao validados
	 * @return
	 */
	public static boolean isAlgumCampoNaoPreenchido(Object objeto, String... campos) {
		if (objeto == null || campos == null) {
			throw new IllegalArgumentException(MSG_ERRO_COMUM);
		}
		return Arrays.asList(campos).stream().anyMatch(nomeCampo -> isCampoNaoPreenchido(objeto, nomeCampo));
	}

}
