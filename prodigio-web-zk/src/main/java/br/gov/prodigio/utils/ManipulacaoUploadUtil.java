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
package br.gov.prodigio.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Window;

import br.gov.prodemge.ssc.comuns.constantes.Constantes;
import br.gov.prodemge.ssc.interfaces.IUnidade;
import br.gov.prodemge.ssc.interfaces.IUsuario;

public class ManipulacaoUploadUtil {

	private String caminho_base;
	public final Logger log = LoggerFactory.getLogger("ManipulacaoUploadUtil");
	public static final String PROPRIEDADE_CONTENT_TYPE = "tipoConteudo";
	public static final String PROPRIEDADE_NOME = "nomeArquivo";
	public static final String LOCALIZACAO_ARQUIVO = "descricaoLocalizacaoArquivo";
	public static final String PROPRIEDADE_DIRETORIO_ARQUIVO = "diretorio_arquivo";
	public static final String PROPRIEDADE_USUARIO_LOGADO = "cpfResponsavel";
	public static final String PROPRIEDADE_UNIDADE_USUARIO_LOGADO = "cdUnidadeAcessoResponsavel";
	private static ManipulacaoUploadUtil instance;

	static String dataFormatada() {
		SimpleDateFormat simpleDate = new SimpleDateFormat("dd-MM-YYYY");
		return simpleDate.format(new Date());
	}

	public String recuperarCaminhoBaseDiretorios() {
		try {
			String propriedade = System.getProperty(PROPRIEDADE_DIRETORIO_ARQUIVO);
			if (propriedade == null) {
				throw new NullPointerException();
			}
			return System.getProperty(PROPRIEDADE_DIRETORIO_ARQUIVO);
		} catch (NullPointerException e) {
			log.error("A propriedade " + PROPRIEDADE_DIRETORIO_ARQUIVO + " não foi informada no arquivo standalone.xml ! Esta propriedade é responsável por definir a pasta de armazenamento de uploads de arquivos!");
			e.printStackTrace();
			throw e;
		} catch (SecurityException e) {
			log.error("Ocorreu um erro de segurança ao acessar a propriedade : " + PROPRIEDADE_DIRETORIO_ARQUIVO + " no arquivo standalone.xml");
			e.printStackTrace();
			throw e;
		} catch (IllegalArgumentException e) {
			log.error("A propriedade " + PROPRIEDADE_DIRETORIO_ARQUIVO + " está vazia! Esta propriedade é responsável por definir a pasta de armazenamento de uploads de arquivos!");
			e.printStackTrace();
			throw e;
		}
	}

	public void armazenarArquivo(Media media, String nomeFormatado) throws Exception {
		BufferedInputStream in = null;
		BufferedOutputStream out = null;
		try {

			log.info("Upload iniciado... " + media.getName() + " Data Final: " + new Date());
			if (media.isBinary()) {
				in = new BufferedInputStream(media.getStreamData());
			} else {
				in = new BufferedInputStream(new ByteArrayInputStream(media.getStringData().getBytes()));
			}

			String estruturaDiretorios = criarEstruturaDiretorios();
			File baseDir = new File(getCaminho_base() + estruturaDiretorios);

			if (!baseDir.exists()) {
				baseDir.mkdirs();
			}

			out = new BufferedOutputStream(new FileOutputStream(new File(getCaminho_base() + estruturaDiretorios + nomeFormatado)));

			byte buffer[] = new byte[1024 * 1024 * 10]; // 10 megas de buffer
			int ch = in.read(buffer);
			while (ch != -1) {
				out.write(buffer, 0, ch);
				ch = in.read(buffer);
			}

			log.info("sucessed upload :" + media.getName() + " Data Final: " + new Date());

		} catch (NullPointerException e) {
			log.error("Propriedade NULA ao associar o nome e caminho do arquivo para armazenamento de arquivos na estrutura de diretórios!");
			e.printStackTrace();
			throw e;
		} catch (SecurityException e) {
			log.error("Permissão negada para criar o arquivo na estrutura de diretórios! caminho: " + getCaminho_base());
			e.printStackTrace();
			throw e;
		} catch (FileNotFoundException e) {
			log.error("Arquivo não pode ser criado na estrutura de diretórios, caminho: " + getCaminho_base());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			log.error("Um erro inesperado ocorreu ao criar arquivo na estrutura de diretórios! caminho: " + getCaminho_base());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				if (out != null) {
					out.close();
				}

				if (in != null) {
					in.close();
				}

			} catch (IOException e) {
				log.error("Um erro inesperado ocorreu ao criar arquivo na estrutura de diretórios! caminho: " + getCaminho_base());
				e.printStackTrace();
				throw e;
			}
		}

	}

	public boolean deletarArquivoArmazenado(String nomeArquivo, String localizacaoArquivo) {
		try {

			log.info("Deletando o arquivo: " + nomeArquivo + " no local : " + localizacaoArquivo);
			File arquivoAserDeletado = new File(localizacaoArquivo + nomeArquivo);

			if (arquivoAserDeletado.delete()) {
				log.info("Arquivo deletado com sucesso.");
			}

			return true;

		} catch (NullPointerException e) {
			log.error("Erro ao deletar o arquivo na estrutura de diretórios. Propriedade NULA ao associar o nome e caminho do arquivo para deleção de arquivo na estrutura de arquivos");
			e.printStackTrace();
			throw e;
		} catch (SecurityException s) {
			log.error("Erro ao deletar o arquivo na estrutura de diretórios. Permissão negada para deletar o arquivo na estrutura de diretórios! caminho: " + nomeArquivo + localizacaoArquivo);
			s.printStackTrace();
			throw s;
		}

	}

	public void downloadArquivo(String nomeArquivo, String localizacaoArquivo, String contentType) throws Exception {

		if (nomeArquivo != null && localizacaoArquivo != null && contentType != null) {
			try {
				FileInputStream file = new FileInputStream(new File((localizacaoArquivo + nomeArquivo)));
				org.zkoss.zul.Filedownload.save(file, contentType, getNomeArquivoFormatado(nomeArquivo));

			} catch (FileNotFoundException e) {
				log.error("Arquivo nao encontrado ao tentar fazer o download: caminho:" + localizacaoArquivo + nomeArquivo);
				e.printStackTrace();
				throw e;
			} catch (NullPointerException e) {
				log.error("Propriedade NULA ao associar o nome e caminho do arquivo para download");
				e.printStackTrace();
				throw e;
			} catch (IllegalArgumentException e) {
				log.error("Propriedade NULA ao associar o nome do arquivo no momento de formatar o nome a ser exibido no download do arquivo");
				e.printStackTrace();
				throw e;
			}
		}
	}

	private String getNomeArquivoFormatado(String nomeArquivo) {
		if (nomeArquivo != null) {
			return (nomeArquivo.substring(nomeArquivo.indexOf("--") + 2));
		} else {
			throw new IllegalArgumentException();
		}

	}

	public String recuparCpfUsuarioSessaoUpload(Window window) {

		if (window == null) {
		}

		IUsuario usuario = (IUsuario) window.getAttribute(Constantes.USUARIO_AUTENTICADO, Component.SESSION_SCOPE);

		if (usuario == null) {
			throw new IllegalArgumentException("Não foi possível recuperar o usuario logado ao realizar o upload de arquivo");
		}

		return usuario.getCpf() != null ? usuario.getCpf() : "";

	}

	public String recuparCodigoUnidadeUsuarioSessaoUpload(Window window) {

		if (window == null) {
			throw new IllegalArgumentException("Não foi possível recuperar a unidade do usuario logado ao realizar o upload de arquivo. Parâmeto passado para o método ManipulacaoUploadUtil.recuparUsuarioSessaoUpload(Window window) NULO");
		}

		IUnidade unidade = (IUnidade) window.getAttribute(Constantes.UNIDADE_AUTENTICADA, Component.SESSION_SCOPE);

		if (unidade == null) {
			throw new IllegalArgumentException("Não foi possível recuperar a unidade do usuario logado ao realizar o upload de arquivo");
		}

		return unidade.getCodigo() != null ? unidade.getCodigo() : "";
	}

	public static String formataNomeArquivo() {
		String dia = String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
		String mes = String.valueOf(Calendar.getInstance().get(Calendar.MONTH));
		String ano = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
		String hora = String.valueOf(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
		String minuto = String.valueOf(Calendar.getInstance().get(Calendar.MINUTE));
		String segundo = String.valueOf(Calendar.getInstance().get(Calendar.SECOND));

		return dia + "-" + mes + "-" + ano + "_" + hora + "-" + minuto + "-" + segundo + "--";
	}

	public static ManipulacaoUploadUtil getInstance() {
		if (instance == null) {
			instance = new ManipulacaoUploadUtil();
		}

		return instance;
	}

	public String criarEstruturaDiretorios() {
		return dataFormatada() + "//";
	}

	public String getCaminho_base() {
		this.caminho_base = recuperarCaminhoBaseDiretorios();
		return caminho_base;
	}

	public void setCaminho_base(String caminho_base) {
		this.caminho_base = caminho_base;
	}

}
