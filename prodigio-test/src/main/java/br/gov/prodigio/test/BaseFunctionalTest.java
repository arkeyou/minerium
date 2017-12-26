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
package br.gov.prodigio.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import br.gov.prodigio.test.excecoes.FalhaNoLogoutException;
/*
 * 
 *Métodos relacionados a testes funcionais nas Intefaces Web baseadas no Prodígio.
 *
 * */
public abstract class BaseFunctionalTest {

	protected WebDriver driver;
	protected String baseUrl;
	protected StringBuffer verificationErrors = new StringBuffer();
	protected boolean acceptNextAlert = true;
	
	@Before
	 public void setUp() throws Exception {
	    driver = new FirefoxDriver();
	    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	 }
	
	@After
	public void tearDown() throws Exception {
		driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}
	
	//casos de teste
	protected abstract void CRUDTest() throws Exception;
	
	protected abstract void obrigatoriedadeDeCamposPesquisaTest() throws Exception;
	
	protected abstract void formatoETamanhoDeCamposPesquisaTest() throws Exception;
	
	protected abstract void obrigatoriedadeDeCamposCriacaoTest() throws Exception;
	
	protected abstract void formatoETamanhoDeCamposCriacaoTest() throws Exception;
	
	//metodos relacionados a acesso inicial e login/logout
	
	protected void acessoInicial() throws InterruptedException {
		driver.get(baseUrl);
	}
	
	protected void acessoInicial(String url) throws InterruptedException {
		driver.get(url);
	}
	
	protected abstract void logar() throws InterruptedException;
	
	protected void verificarLogin(String posicaoXPathBotaoLogout) throws InterruptedException {
		verificarPresencaBotaoLogout(posicaoXPathBotaoLogout);
	}
	
	protected void verificarPresencaBotaoLogout(String posicaoXPathBotaoLogout) throws InterruptedException{
		esperarEVerificarPresencaElemento(posicaoXPathBotaoLogout);
	}
	
	protected void clicarBotaoLogoutECancelar(String posicaoXPathBotaoLogout, String posicaoXPathBotaoCancelamentoLogout) throws InterruptedException {
		driver.findElement(By.xpath(posicaoXPathBotaoLogout)).click();
		driver.findElement(By.xpath(posicaoXPathBotaoCancelamentoLogout)).click();
		verificarLogin(posicaoXPathBotaoLogout);
	}
	
	protected abstract void deslogar() throws InterruptedException;
	
	protected void clicarBotaoLogoutEConfirmar(String posicaoXPathBotaoLogout, String posicaoXPathBotaoConfirmacaoLogout) throws InterruptedException{
		driver.findElement(By.xpath(posicaoXPathBotaoLogout)).click();
		driver.findElement(By.xpath(posicaoXPathBotaoConfirmacaoLogout)).click();
		verificarLogout(posicaoXPathBotaoLogout);
	}
	
	public void verificarLogout(String posicaoXPathBotaoLogout) throws InterruptedException{
		try{
			verificarPresencaBotaoLogout(posicaoXPathBotaoLogout);
			throw new FalhaNoLogoutException();
		}catch (InterruptedException e){
			//Ok verificou Logout pela ausencia do botao de logout
		}
	}
	
	//metodos relacionados a navegacao

	protected abstract void selecionaItemMenu() throws InterruptedException;
	
	protected void selecionaItemMenu(String posicaoXPathMenu, String posicaoXPathItemMenu) throws InterruptedException {
		esperarEVerificarPresencaElemento(posicaoXPathMenu);
		driver.findElement(By.xpath(posicaoXPathMenu)).click();
		driver.findElement(By.xpath(posicaoXPathMenu)).click();
		esperarEVerificarPresencaElemento(posicaoXPathItemMenu);
		driver.findElement(By.xpath(posicaoXPathItemMenu)).click();
	}
	
	protected void selecionaItemMenu(String posicaoXPathMenu, String posicaoXPathItemMenu, String posicaoXPathSubItemMenu) throws InterruptedException {
		//TODO testar
		esperarEVerificarPresencaElemento(posicaoXPathMenu);
		driver.findElement(By.xpath(posicaoXPathMenu)).click();
		driver.findElement(By.xpath(posicaoXPathMenu)).click();
		esperarEVerificarPresencaElemento(posicaoXPathItemMenu);
		driver.findElement(By.xpath(posicaoXPathItemMenu)).click();
		driver.findElement(By.xpath(posicaoXPathItemMenu)).click();
		esperarEVerificarPresencaElemento(posicaoXPathSubItemMenu);
		driver.findElement(By.xpath(posicaoXPathSubItemMenu)).click();
	}
	
	protected abstract void clicarBotaoVoltar() throws InterruptedException;
	
	public void clicarBotaoVoltar(String posicaoXPathBotaoVoltar) throws InterruptedException{
		esperarEVerificarPresencaElemento(posicaoXPathBotaoVoltar);
		driver.findElement(By.xpath(posicaoXPathBotaoVoltar)).click();
	}
	
	
	//metodos relacionados a preenchimento de campos

	protected void preencherCampoTextoSimples(String posicaoXPathCampo, String valor) throws InterruptedException{
		esperarEVerificarPresencaElemento(posicaoXPathCampo);
		driver.findElement(By.xpath(posicaoXPathCampo)).clear();
	    driver.findElement(By.xpath(posicaoXPathCampo)).sendKeys(valor);
	}
	
	protected void preencherCampoPopUpSelecao(String posicaoXPathCampo, String posicaoXPathBotaoBuscaDoCampo, String valor, String posicaoXPathValor) throws InterruptedException{
		esperarEVerificarPresencaElemento(posicaoXPathCampo);
		driver.findElement(By.xpath(posicaoXPathCampo)).clear();
	    driver.findElement(By.xpath(posicaoXPathCampo)).sendKeys(valor);
	    driver.findElement(By.xpath(posicaoXPathBotaoBuscaDoCampo)).click();
	    esperarEVerificarPresencaElemento(posicaoXPathValor);
		driver.findElement(By.xpath(posicaoXPathValor)).click();
	}
	
	protected void verificarValorCampo(String posicaoXPathCampo, String valorEsperado) throws InterruptedException{
		assertEquals(valorEsperado, driver.findElement(By.xpath(posicaoXPathCampo)).getText());
	}
	
	//metodos relacionados a gravação
	
	protected abstract void clicarBotaoNovo() throws InterruptedException;
	
	protected void clicarBotaoNovo(String posicaoXPathBotaoNovo) throws InterruptedException {
		esperarEVerificarPresencaElemento(posicaoXPathBotaoNovo);
		driver.findElement(By.xpath(posicaoXPathBotaoNovo)).click();
	}
	
	protected abstract void clicarBotaoSalvar() throws InterruptedException;
	
	protected void clicarBotaoSalvar (String posicaoXPathBotaoSalvar) throws InterruptedException{
		driver.findElement(By.xpath(posicaoXPathBotaoSalvar)).click();
	}
	
	protected abstract void verificarMsgGravacaoSucesso() throws InterruptedException;
	
	protected abstract void verificarMsgGravacaoSucesso(String posicaoXPathPopUpMSG) throws InterruptedException;
	
	protected abstract void verificarMsgFalhaGravacaoPorRegistroJaExistente() throws InterruptedException;
	
	protected abstract void verificarMsgFalhaGravacaoPorRegistroJaExistente(String posicaoXPathPopUpMSG, String posicaoXPathBotaoOKPopUpMSG) throws InterruptedException;
	
	
	//metodos relacionado a exclusao
	
	protected abstract void clicarBotaoExcluir() throws InterruptedException;
	
	protected void clicarBotaoExcluir(String posicaoXPathBotaoExcluir) throws InterruptedException{
		esperarEVerificarPresencaElemento(posicaoXPathBotaoExcluir);
		driver.findElement(By.xpath(posicaoXPathBotaoExcluir)).click();
	}
	
	protected abstract void clicarBotaoDaPopUpConfirmarExclusao() throws InterruptedException;
	
	protected void clicarBotaoDaPopUpConfirmarExclusao(String posicaoXPathBotaoDaPopUpConfirmarExclusao) throws InterruptedException {
		esperarEVerificarPresencaElemento(posicaoXPathBotaoDaPopUpConfirmarExclusao);
		driver.findElement(By.xpath(posicaoXPathBotaoDaPopUpConfirmarExclusao)).click();
	}
	
	protected abstract void verificarMsgExclusaoSucesso() throws InterruptedException;
	
	protected abstract void verificarMsgExclusaoSucesso(String posicaoXPathPopUpMSG) throws InterruptedException;
	
	protected abstract void clicarBotaoDaPopUpCancelarExclusao() throws InterruptedException;
		
	protected void clicarBotaoDaPopUpCancelarExclusao(String posicaoXPathBotaoDaPopUpCancelarExclusao) throws InterruptedException{
		esperarEVerificarPresencaElemento(posicaoXPathBotaoDaPopUpCancelarExclusao);
		driver.findElement(By.xpath(posicaoXPathBotaoDaPopUpCancelarExclusao)).click();
	}
	
	
	//metodos relacionados a pesquisa
	
	protected abstract void clicarBotaoLimpar() throws InterruptedException;
	
	protected void clicarBotaoLimpar(String posicaoXPathBotaoLimpar) throws InterruptedException{
		esperarEVerificarPresencaElemento(posicaoXPathBotaoLimpar);
		driver.findElement(By.xpath(posicaoXPathBotaoLimpar)).click();
	}
	
	protected abstract void verificarTelaPesquisaAposClicarBotaoLimpar() throws InterruptedException;
	
	
	protected abstract void clicarBotaoPesquisar() throws InterruptedException;
	
	protected void clicarBotaoPesquisar(String posicaoXPathBotaoPesquisar) throws InterruptedException {
		esperarEVerificarPresencaElemento(posicaoXPathBotaoPesquisar);
		driver.findElement(By.xpath(posicaoXPathBotaoPesquisar)).click();
	}
	
	protected void selecionarItemDaPaginaAtualDeItensDePesquisa(String posicaoXpathItem) {
		driver.findElement(By.xpath(posicaoXpathItem)).click();
	}
	
	protected void avancarPaginaResultadosDePesquisa(int numeroAvancos, String posicaoXPathIconeAvanco){
		//TODO testar
		for (int i = 0; i < numeroAvancos; i++) {
			driver.findElement(By.xpath(posicaoXPathIconeAvanco)).click();
		}
	}
	
	
	//métodos auxiliares
		protected final boolean isElementPresent(By by) {
			try {
				driver.findElement(by);
				return true;
			} catch (NoSuchElementException e) {
				return false;
			}
		}

		public void esperarEVerificarPresencaElemento(String posicaoXPath) throws InterruptedException{
			for (int second = 0;; second++) {
				if (second >= 60)
					fail("timeout");
				try {
					if (isElementPresent(By.xpath(posicaoXPath)))
						break;
				} catch (Exception e) {
				}
				Thread.sleep(1000);
			}
		}
		
		protected final boolean isAlertPresent() {
			try {
				driver.switchTo().alert();
				return true;
			} catch (NoAlertPresentException e) {
				return false;
			}
		}

		protected final String closeAlertAndGetItsText() {
			try {
				Alert alert = driver.switchTo().alert();
				String alertText = alert.getText();
				if (acceptNextAlert) {
					alert.accept();
				} else {
					alert.dismiss();
				}
				return alertText;
			} finally {
				acceptNextAlert = true;
			}
		}
		
		protected void verificarMsg(String msg, String posicaoXPathMSG) throws InterruptedException{
			esperarEVerificarPresencaElemento(posicaoXPathMSG);
			assertEquals(msg, driver.findElement(By.xpath(posicaoXPathMSG)).getText());
		}
	
}
