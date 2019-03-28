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

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.SerializableEventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zul.Button;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Window;

import br.gov.prodigio.controle.ProCtr;

/**
 * 
 * @author p062271
 *
 *         exemplo de uso:
 *
 *         ZUL: <buttonpdfbind label="gerar relatório" nomeRelatorio="relatorioTeste.pdf" metodoMontarExcel="metodoExcel" />
 * 
 *         Ctr: public byte[] metodoExcel() throws Exception { ITabelaBuilder builder = new TabelaExcelBuilder(2); builder.addLinhaDestaque("CABEÇALHO"); builder.abrirLinha().addColunasDestaque("Nome", "CPF").fecharLinha();
 *         builder.abrirLinha().addColuna("Pedro").addColuna("000.000.000-00").fecharLinha(); builder.addLinhaDestaque("RODAPÉ"); return builder.construir(); }
 *
 */

public class ButtonRelatorioExcelBind extends Button implements AfterCompose {

	private static final long serialVersionUID = 7644458046813596916L;
	public final Logger log = LoggerFactory.getLogger(ButtonRelatorioExcelBind.class);

	private String nomeRelatorio;
	private String metodoMontarExcel;

	public String getNomeRelatorio() {
		return nomeRelatorio;
	}

	public void setNomeRelatorio(String nomeRelatorio) {
		this.nomeRelatorio = nomeRelatorio;
	}

	public String getMetodoMontarExcel() {
		return metodoMontarExcel;
	}

	public void setMetodoMontarExcel(String metodoMontarExcel) {
		this.metodoMontarExcel = metodoMontarExcel;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void afterCompose() {
		Window window = ProCtr.findWindow(this);
		final ProCtr ctr = (ProCtr) window.getAttribute(window.getId() + "$" + "composer");
		this.addEventListener(Events.ON_CLICK, new SerializableEventListener() {
			@Override
			public void onEvent(Event e) throws Exception {
				ButtonRelatorioExcelBind buttonExcelBind = (ButtonRelatorioExcelBind) e.getTarget();
				String metodo = buttonExcelBind.getMetodoMontarExcel();

				if (metodo != null && !metodo.trim().isEmpty()) {
					Method metodoGerarExcel = ctr.getClass().getMethod(metodo, new Class[] {});
					Object retorno = metodoGerarExcel.invoke(ctr, new Object[] {});
					if (retorno instanceof byte[]) {
						byte[] relatorio = (byte[]) retorno;
						String nomeRelatorio = buttonExcelBind.getNomeRelatorio();
						if (nomeRelatorio != null && !nomeRelatorio.trim().isEmpty()) {
							Filedownload.save(relatorio, "application/vnd.ms-excel", nomeRelatorio);
						} else {
							Filedownload.save(relatorio, "application/vnd.ms-excel", "relatorio.xls");
						}
					} else {
						log.error("O retorno do método que monta o Excel deve ser byte[].");
					}
				} else {
					log.error("Atributo 'metodoMontarExcel' não foi preenchido.");
				}

			}
		});
	}
}
