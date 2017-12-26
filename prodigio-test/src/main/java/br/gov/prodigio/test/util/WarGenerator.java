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
package br.gov.prodigio.test.util;

import java.io.File;
import java.io.IOException;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.ScopeType;

import br.gov.prodigio.test.ProBaseIntegrationTest;


public class WarGenerator 
{

    private WebArchive webArchive;
    protected String prefixoAplicacao = "ssc-admin";
    protected String pacoteAplicacao = "webapp";
    protected String pom = "pom.xml";
    protected String extensaoAplicacao = ".war";
    protected String resourcesPath = "src/main/resources";
    protected String webinfPath = "src/main/webapp/WEB-INF";
    protected String settingsPath;
    
    public WarGenerator(String settingsPath, String prefixoAplicacao,String extensaoAplicacao) {
    	this.extensaoAplicacao = extensaoAplicacao;
    	this.prefixoAplicacao = prefixoAplicacao;
    	this.settingsPath = settingsPath;
	}

//    public WebArchive createWith(Class<?>... classes)
//    {
//        if(classes == null)
//        	throw new IllegalArgumentException("Para criar um WAR é preciso ao menos uma classe!");
//    	
//    	webArchive = ShrinkWrap
//                .create(WebArchive.class, createAppNameWithVersion())
////                .addClasses(ProRN.class,ProBaseIntegrationTest.class)
//                .addClasses(classes);
//                
//
//        addPomDependencies();
//        addFileFrom(getResourcesPath());
//        addFileFrom(getWebinfPath());
//
//        return webArchive;
//    }


    public WebArchive createWith(String... pacotes)
    {
        if(pacotes == null)
            throw new IllegalArgumentException("Para criar um WAR é preciso ao menos uma classe!");

        webArchive = ShrinkWrap
                .create(WebArchive.class, createAppNameWithVersion())
                .addPackages(true,pacotes)
                .addClass(ProBaseIntegrationTest.class);


        addPomDependencies(getSettingsPath());
        addFileFrom(getResourcesPath());
        addFileFrom(getWebinfPath());

        return webArchive;
    }

    private String createAppNameWithVersion()
    {
        return getPrefixoAplicacao() +"-"+ProjectVersion.getValue()+getExtensaoAplicacao();
    }

	private void addPomDependencies(String settingsPath)
    {
        printAbsolutPath();

        if(settingsPath==null || "".equals(settingsPath.trim()))
        	settingsPath = "C:/Prodemge/maven/apache-maven-3.0.5/conf/settings.xml";
        
		webArchive.addAsLibraries(
            Maven.configureResolver().workOffline()
            		.fromFile(settingsPath)            		            		            	
                    .loadPomFromFile(getPom())
                    .importDependencies(ScopeType.RUNTIME, ScopeType.COMPILE,ScopeType.PROVIDED,ScopeType.TEST)
                    .resolve()
                    .withTransitivity()
                    .asFile()
        );
    }

    private void addFileFrom(String targetPath)
    {
        File[] files = collectFiles(targetPath);
        
        if(files!=null)
        for (File file : files)
        {
            if (file.isDirectory()){        
            	addResourceOrWebInf(targetPath, file);
            	addFileFrom(file.getPath());
            }
            else{
            	addResourceOrWebInf(targetPath, file);
            	
            }
        }
    }

    private void addResourceOrWebInf(String targetPath, File file)
    {
    	if(targetPath.contains(getPacoteAplicacao()))
                webArchive.addAsWebInfResource(file);
        else
            if(file.getName().contains("persistence"))
                webArchive.addAsResource(file,"META-INF/persistence.xml");
            else
                webArchive.addAsResource(file);
    }

    private File[] collectFiles(String targetPath)
    {
        return new File(targetPath).listFiles();
    }

    private void printAbsolutPath(){
        try {
            System.out.println(new File(new File(".").getAbsolutePath()).getCanonicalPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // GETTERS AND SETTERS

	public String getPrefixoAplicacao() {
		return prefixoAplicacao;
	}

	public void setPrefixoAplicacao(String prefixoAplicacao) {
		this.prefixoAplicacao = prefixoAplicacao;
	}

	public String getPacoteAplicacao() {
		return pacoteAplicacao;
	}

	public void setPacoteAplicacao(String pacoteAplicacao) {
		this.pacoteAplicacao = pacoteAplicacao;
	}

	public String getPom() {
		return pom;
	}

	public void setPom(String pom) {
		this.pom = pom;
	}

	public String getExtensaoAplicacao() {
		return extensaoAplicacao;
	}

	public void setExtensaoAplicacao(String extensaoAplicacao) {
		this.extensaoAplicacao = extensaoAplicacao;
	}

	public String getResourcesPath() {
		return resourcesPath;
	}

	public void setResourcesPath(String resourcesPath) {
		this.resourcesPath = resourcesPath;
	}

	public String getWebinfPath() {
		return webinfPath;
	}

	public void setWebinfPath(String webinfPath) {
		this.webinfPath = webinfPath;
	}

	public String getSettingsPath() {
		return settingsPath;
	}

	public void setSettingsPath(String settingsPath) {
		this.settingsPath = settingsPath;
	}
    
	
    
    
}