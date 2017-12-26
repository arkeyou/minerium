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
package br.gov.prodigio.mensagem;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.InitialContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnviaEmailSMTP implements Serializable {

	private static final String MAIL_SMTP_HOST = "mail.smtp.host";
	public final Logger log = LoggerFactory.getLogger(this.getClass());
	private static final long serialVersionUID = 8034414689019079420L;
	private Session sessao;
	private String assunto;
	private String mensagem;
	private static final String REMETENTE = "seg.corporativa@prodemge.gov.br";

	public EnviaEmailSMTP() {
		super();
	}

	public boolean enviarEmail(String destinatario) {
		sessao = configuraPropertiesObtemSession();
		try {
			MimeMessage message = new MimeMessage(sessao); // DEFINE DESTINATARIOS
			InternetAddress enderecoCliente = new InternetAddress(destinatario);
			Address[] addresses = new InternetAddress[] { enderecoCliente };
			Address remetente = new InternetAddress(REMETENTE);
			message.setRecipients(Message.RecipientType.TO, addresses);
			message.setFrom(remetente); // DEFINE ASSUNTO
			message.setSubject(assunto, "UTF-8"); // DEFINE CONTEUDO
			message.setContent(mensagem, "text/html; charset=utf-8");
			Transport.send(message);
		} catch (MessagingException e) {
			log.error("Falha na tentativa de envio da mensagem.", e);
		}
		return false;
	} // TODO [DESIDERIO] DEFINIR FORMA DE PREENCHER CORPO DO EMAIL.

	public void preencheCorpoEmail() {
	}

	private Session configuraPropertiesObtemSession() {
		Properties propriedades = System.getProperties();
		String mailHost = propriedades.getProperty(MAIL_SMTP_HOST);
		if (mailHost == null) {
			throw (new RuntimeException("Serviço de email não configurado!"));
		}
		InitialContext context;
		Session session = null;
		try {
			context = new InitialContext();
			session = (Session) context.lookup("java:jboss/mail/ProdemgeMail");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (session == null)
			session = Session.getDefaultInstance(propriedades, null);
		return session;
	}

	public String getAssunto() {
		return assunto;
	}

	public void setAssunto(String assunto) {
		this.assunto = assunto;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public static void main(String[] args) {

		String userID = "_hora_" + Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + "" + Calendar.getInstance().get(Calendar.MINUTE) + "" + Calendar.getInstance().get(Calendar.SECOND);
		System.out.println(userID);

		Date hora = new Date();
		String userID2 = "_hora_" + hora.getHours() + "" + hora.getMinutes() + "" + hora.getSeconds();
		System.out.println(userID2);
	}
}
