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
package br.gov.prodigio.persistencia.mainframe;

import java.util.Calendar;

import com.softwareag.entirex.aci.Broker;
import com.softwareag.entirex.aci.BrokerException;
import com.softwareag.entirex.aci.BrokerMessage;
import com.softwareag.entirex.aci.BrokerService;

/** --------------------------------------------------------------------------------------------------- **/
/** BrokerClient - Cliente genérico para Entire Broker (Consist)                                        **/
/** Parâmetros de configuração:                                                                         **/
/** - brokerID: identificação da instância do Broker; default="ETB158".                                 **/
/** - portNumber: porta utilizada; default="1971".                                                      **/
/** - serverClass: classe de identificação do serviço.                                                  **/
/** - serverName: nome do servidor; default="BHMVSB".                                                   **/
/** - serviceName: nome do serviço.                                                                     **/
/**   Obs.: O serviço identificado pelos 3 parâmetros acima deve aparecer no arquivo de configuração do **/
/**         Entire Broker.                                                                              **/
/** - sendBuffer: cadeia de parâmetros a ser processada pelo serviço.                                   **/
/**   Obs.: As 3 primeiras posições deste string identificam o subprograma NATURAL a ser executado.     **/
/** - receiveBuffer: cadeia de resposta enviada pelo programa servidor.                                 **/
/** --------------------------------------------------------------------------------------------------- **/
public class BrokerClient {

	private static final int MAX_RECEIVE_LENGTH = 20000;
	private String brokerID;
	private String portNumber;
	private String timeout = "90S";
	private String serverClass = "";
	private String serverName;
	private String serviceName = "";
	protected Broker broker = null;
	protected BrokerService bService = null;
	private String userID;

	protected BrokerClient() {
		Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

		userID = this.hashCode() + "_hora_" + Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + "" + Calendar.getInstance().get(Calendar.MINUTE) + "" + Calendar.getInstance().get(Calendar.SECOND);
	}

	protected BrokerClient(String brokerID, String portNumber, String timeout, String serverClass, String serverName) {
		this.brokerID = brokerID;
		this.portNumber = portNumber;
		this.timeout = timeout;
		this.serverClass = serverClass;
		this.serverName = serverName;
		userID = this.hashCode() + "_hora_" + Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + "" + Calendar.getInstance().get(Calendar.MINUTE) + "" + Calendar.getInstance().get(Calendar.SECOND);
		configure();
	}

	public void configure() {
		broker = new Broker(brokerID + ":" + portNumber, userID, "token");

		bService = new BrokerService(broker, serverClass, serverName, serviceName);
		bService.setMaxReceiveLen(MAX_RECEIVE_LENGTH);
	}

	public String callMainFrame(String sendBuffer) {
		String receiveBuffer = "";
		try {
			BrokerMessage bRequest = new BrokerMessage();
			bRequest.setMessage(sendBuffer);
			BrokerMessage bReply = bService.sendReceive(bRequest, getTimeout());
			receiveBuffer = bReply.toString();
		} catch (BrokerException bE) {
			receiveBuffer = bE.getMessage();
		}
		return receiveBuffer;
	}

	public String toString() {

		return "BROKER_ID " + brokerID + ", SERVICE_NAME " + serviceName + ", SERVER_NAME " + serverName + ", SERVER_CLASS " + serverClass;
	}

	private void desconectar() {
		broker.disconnect();
	}

	protected void setBrokerID(String brokerID) {
		this.brokerID = brokerID;
	}

	protected String getBrokerID() {
		return this.brokerID;
	}

	protected void setPortNumber(String portNumber) {
		this.portNumber = portNumber;
	}

	protected String getPortNumber() {
		return this.portNumber;
	}

	protected void setServerClass(String serverClass) {
		this.serverClass = serverClass;
	}

	protected void setServerName(String serverName) {
		this.serverName = serverName;
	}

	protected void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	protected void setTimeout(String timeout) {
		this.timeout = timeout;
	}

	private String getTimeout() {
		return timeout;
	}

	@Override
	public void finalize() {
		desconectar();
	}

	public static void main(String[] args) {
		BrokerClient bCliente = new BrokerClient();

		bCliente.setBrokerID("ETB156");
		bCliente.setPortNumber("1973");
		bCliente.setServiceName("DESENV");
		bCliente.setTimeout("90S");
		bCliente.setServerClass("USOGERAL");
		bCliente.setServerName("BHMVSB");

		bCliente.configure();
		String retorno = bCliente.callMainFrame("NFCAEWE61910001 2012P033263");

		System.out.println(retorno);
	}
}
