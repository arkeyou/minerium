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
package br.gov.prodigio.persistencia;

import java.io.File;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.gov.prodigio.entidades.ArquivoVO;
import br.gov.prodigio.entidades.ProBaseVO;

public class ArquivoDAO extends ProBaseDAO {
	private static final Logger log = LoggerFactory.getLogger(ArquivoDAO.class);

	@Override
	public ProBaseVO recuperaObjeto(ProBaseVO vo) throws Exception {
		return super.recuperaObjeto(vo);
	}

	@Override
	public Set<ProBaseVO> listarBaseadoNoExemplo(ProBaseVO exemplo, ProBaseVO exemplo2, int primeiroRegistro, int quantidadeDeRegistros, String... projecoes)  {
		ArquivoVO arquivoVO = (ArquivoVO) exemplo;
		montaFilhos(arquivoVO);
		final Set<ProBaseVO> set = new TreeSet();
		set.add(arquivoVO);
		return set; 
	}

	protected void montaFilhos(ArquivoVO arquivoVO) {
		final String pathname = arquivoVO.getCaminhoAbsoluto();
		File folder = new File(pathname);
		File[] listOfFiles = folder.listFiles();
		if (listOfFiles == null) {
			arquivoVO.setArquivosFilhos(new ArrayList<ArquivoVO>());
			return;
		}
		for (int i = 0; i < listOfFiles.length; i++) {
			final File file = listOfFiles[i];
			ArquivoVO arquivoVO2 = null;
			if (arquivoVO != null) {
				try {
					arquivoVO2 = arquivoVO.getClass().newInstance();
				} catch (InstantiationException e) {
					log.error("Erro ao montar arquivos filho");
				} catch (IllegalAccessException e) {
					log.error("Erro ao montar arquivos filho");
				}
			} else {
				arquivoVO2 = new ArquivoVO();
			}
			if (file.isFile()) {
				arquivoVO2.setDiretorio(false);
				log.debug("Nome do arquivo", file.getName());
			} else if (file.isDirectory()) {
				log.debug("Nome do arquivo", file.getName());
				arquivoVO2.setDiretorio(true);
			}
			arquivoVO2.setCaminhoAbsoluto(file.getAbsolutePath());
			arquivoVO2.setNome(file.getName());
			arquivoVO2.setUsuarioVO(arquivoVO.getUsuarioVO());
			arquivoVO2.setUnidadeAdministrativaVO(arquivoVO.getUnidadeAdministrativaVO());
			insereArquivoFilhoNoPai(arquivoVO, arquivoVO2);
		}
	}

	protected void insereArquivoFilhoNoPai(ArquivoVO arquivoVO, ArquivoVO arquivoVO2) {
		arquivoVO.addArquivo(arquivoVO2);
	}
}
