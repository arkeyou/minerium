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

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.softwareag.entirex.aci.Broker;
import com.softwareag.entirex.aci.BrokerException;
import com.softwareag.entirex.aci.BrokerMessage;
import com.softwareag.entirex.aci.BrokerService;

public class BrokerServer implements Runnable {
	private static final Logger log = LoggerFactory.getLogger(BrokerServer.class);
	private final Broker broker;
	private final BrokerService servico;

	BrokerServer() {
		broker = new Broker("192.168.238.100:1970", Thread.currentThread().getName());
		servico = new BrokerService(broker, "FCAE", "BHMVSB", "SIAFIT1");
		servico.setDefaultWaittime("90s");
	}

	public static void main(String[] args) {
		BrokerServer target = new BrokerServer();
		Thread thread = new Thread(target);
		Thread thread2 = new Thread(target);
		Thread thread3 = new Thread(target);
		thread.start();
		thread2.start();
		thread3.start();
	}

	public void start() {
		try{
			servico.register();
		}catch(BrokerException e){
			log.error("Erro ao registrar", e);
		}
	}

	@Override
	public void run() {
		try{
			log.info("Servidor ACI: thread {}   registrada - {} ", Thread.currentThread().getName(), new Date());
			do{
				try{
					BrokerMessage brokerMessage = servico.receive();
					brokerMessage.reply(new BrokerMessage("000" + Thread.currentThread().getName() + " " + brokerMessage.toString()));
				}catch(BrokerException e){
					log.error("Erro ao executar serviço", e);
				}
			}while(true);
		}catch(Exception e){
			log.error("Erro", e);
		}finally{
			try{
				servico.deregister();
			}catch(BrokerException e){
				log.error("Erro", e);
			}
			broker.disconnect();
		}
		log.info("Servidor ACI: thread terminada - {} ", new Date());
	}
}