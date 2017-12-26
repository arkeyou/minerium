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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zul.Button;
import org.zkoss.zul.Window;

import br.gov.prodigio.controle.ProCtr;
import br.gov.prodigio.visao.helper.ProHelperView;

import com.itextpdf.text.Utilities;

public class ButtonRelatorioPDFBind extends Button implements AfterCompose {

	private static final long serialVersionUID = 7644458046813596916L;
	public final Logger log = LoggerFactory.getLogger(ButtonRelatorioPDFBind.class);

	private String nomeRelatorio;
	private String metodoMontarCabecalho;
	private String metodoMontarCorpo;
	private String metodoMontarRodape;
	private String margens;
	private float tamanhoCabecalho = 20;
	private float tamanhoRodape = 20;
	private byte[] conteudoCorpo;

	private float[] margensRelatorio = { 20, 20, 20, 20 };// esquerda / direita / superior / inferior
	private String orientacao;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void afterCompose() {
		Window window = ProCtr.findWindow(this);
		final ProCtr ctr = (ProCtr) window.getAttribute(window.getId() + "$" + "composer");
		this.addEventListener(Events.ON_CLICK, new EventListener() {
			@Override
			public void onEvent(Event e) throws Exception {
				ButtonRelatorioPDFBind buttonPDFBind = (ButtonRelatorioPDFBind) e.getTarget();
				String metodoMontarCorpo = buttonPDFBind.getMetodoMontarCorpo();
				if (metodoMontarCorpo != null && !metodoMontarCorpo.trim().isEmpty()) {
					byte[] recuperarCorpoDoRelatorio = recuperarCorpoDoRelatorio(ctr);
					if (recuperarCorpoDoRelatorio != null) {

						buttonPDFBind.setConteudoCorpo(recuperarCorpoDoRelatorio);
						ProHelperView helperView = ProHelperView.getInstance(ctr.getClass());
						byte[] pdf = helperView.converteHTMLParaPDF(buttonPDFBind, window);

						String nomeRelatorio = buttonPDFBind.getNomeRelatorio();
						if (nomeRelatorio != null && !nomeRelatorio.trim().isEmpty()) {
							ctr.download(pdf, null, nomeRelatorio);
						} else {
							ctr.download(pdf, null, "relatorio.pdf");
						}
					}
				} else {
					log.error("Atributo 'metodoMontarCorpo' não foi preenchido.");
				}

			}
		});
	}

	protected byte[] recuperarCorpoDoRelatorio(Object controller) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		Method metodoMontarCorpo = controller.getClass().getMethod(getMetodoMontarCorpo(), new Class[] {});
		byte[] htmlCorpo = (byte[]) metodoMontarCorpo.invoke(controller, new Object[] {});
		return htmlCorpo;
	}

	public byte[] getConteudoCorpo() {
		return conteudoCorpo;
	}

	public void setConteudoCorpo(byte[] conteudoCorpo) {
		this.conteudoCorpo = conteudoCorpo;
	}

	public String getMetodoMontarCabecalho() {
		return metodoMontarCabecalho;
	}

	public void setMetodoMontarCabecalho(String metodoMontarCabecalho) {
		this.metodoMontarCabecalho = metodoMontarCabecalho;
	}

	public String getMetodoMontarRodape() {
		return metodoMontarRodape;
	}

	public void setMetodoMontarRodape(String metodoMontarRodape) {
		this.metodoMontarRodape = metodoMontarRodape;
	}

	public String getOrientacao() {
		return orientacao;
	}

	public void setOrientacao(String orientacao) {
		this.orientacao = orientacao;
	}

	public String getNomeRelatorio() {
		return nomeRelatorio;
	}

	public void setNomeRelatorio(String nomeRelatorio) {
		this.nomeRelatorio = nomeRelatorio;
	}

	public String getMetodoMontarCorpo() {
		return metodoMontarCorpo;
	}

	public void setMetodoMontarCorpo(String metodoMontarCorpo) {
		this.metodoMontarCorpo = metodoMontarCorpo;
	}

	public float getTamanhoCabecalho() {
		return tamanhoCabecalho;
	}

	public void setTamanhoCabecalho(float tamanhoCabecalho) {
		this.tamanhoCabecalho = tamanhoCabecalho;
	}

	public float getTamanhoRodape() {
		return tamanhoRodape;
	}

	public void setTamanhoRodape(float tamanhoRodape) {
		this.tamanhoRodape = tamanhoRodape;
	}

	public String getMargens() {
		return margens;
	}

	public void setMargens(String margens) {
		if (StringUtils.isNotBlank(margens)) {
			String[] split = margens.split(",");
			for (int i = 0; i < 4; i++) {
				margensRelatorio[i] = Float.parseFloat(split[i]);
			}
		}
		this.margens = margens;
	}

	public float[] getMargensRelatorio() {
		return margensRelatorio;
	}

	public void setMargensRelatorio(float[] margensRelatorio) {
		this.margensRelatorio = margensRelatorio;
	}

	public float getMargemEsquerdaEmPontos() {
		return Utilities.millimetersToPoints(this.getMargensRelatorio()[0]);
	}

	public float getMargemDireitaEmPontos() {
		return Utilities.millimetersToPoints(this.getMargensRelatorio()[1]);
	}

	public float getMargemSuperiorEmPontos() {
		return Utilities.millimetersToPoints(this.getMargensRelatorio()[2]);
	}

	public float getMargemSuperiorComCabecalhoEmPontos() {
		if (StringUtils.isNotBlank(this.getMetodoMontarCabecalho())) {
			return getMargemSuperiorEmPontos() + getTamanhoCabecalhoEmPontos();
		} else {
			return Utilities.millimetersToPoints(this.getMargensRelatorio()[2]);
		}
	}

	public float getMargemInferiorEmPontos() {
		return Utilities.millimetersToPoints(this.getMargensRelatorio()[3]);
	}

	public float getMargemInferiorComRodapeEmPontos() {
		if (StringUtils.isNotBlank(this.getMetodoMontarRodape())) {
			return getMargemInferiorEmPontos() + getTamanhoRodapeEmPontos();
		} else {
			return Utilities.millimetersToPoints(this.getMargensRelatorio()[3]);
		}
	}

	public float getTamanhoCabecalhoEmPontos() {
		return Utilities.millimetersToPoints(this.getTamanhoCabecalho());
	}

	public float getTamanhoRodapeEmPontos() {
		return Utilities.millimetersToPoints(this.getTamanhoRodape());
	}
}
