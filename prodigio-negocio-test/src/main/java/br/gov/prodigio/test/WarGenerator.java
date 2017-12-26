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

import java.io.File;
import java.io.IOException;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.ScopeType;

import br.gov.prodigio.negocio.ProRN;
import br.gov.prodigio.test.utils.BaseTest;
import br.gov.prodigio.test.utils.ProjectVersion;
import br.gov.prodigio.test.utils.WarConfig;


public class WarGenerator 
{

    private static WebArchive webArchive;

    public static WebArchive createWith(Class<?>... classes)
    {
        if(classes == null)
        	throw new IllegalArgumentException("Para criar um WAR é preciso ao menos uma classe!");
    	
    	webArchive = ShrinkWrap
                .create(WebArchive.class, createAppNameWithVersion())
                .addClasses(ProRN.class,BaseTest.class)
                .addClasses(classes);
                

        addPomDependencies();
        addFileFrom(WarConfig.RESOURCES_PATH);
        addFileFrom(WarConfig.WEBFILES_PATH);

        return webArchive;
    }


    public static WebArchive createWith(String... pacotes)
    {
        if(pacotes == null)
            throw new IllegalArgumentException("Para criar um WAR é preciso ao menos uma classe!");

        webArchive = ShrinkWrap
                .create(WebArchive.class, createAppNameWithVersion())
                .addPackages(true,pacotes)
                .addClass(BaseTest.class);


        addPomDependencies();
        addFileFrom(WarConfig.RESOURCES_PATH);
        addFileFrom(WarConfig.WEBFILES_PATH);

        return webArchive;
    }

    private static String createAppNameWithVersion()
    {
        return WarConfig.DEPLOYMENT_APPLICATION +"-"+ProjectVersion.getValue()+WarConfig.ARCHIVE_TYPE;
    }

    @SuppressWarnings("deprecation")
	private static void addPomDependencies()
    {
        printAbsolutPath();

        webArchive.addAsLibraries(
            Maven.configureResolver().workOffline()
                    .loadPomFromFile(WarConfig.POM)
                    .importDependencies(ScopeType.RUNTIME, ScopeType.COMPILE,ScopeType.PROVIDED,ScopeType.TEST)
                    .resolve()
                    .withTransitivity()
                    .asFile()
        );
    }

    private static void addFileFrom(String targetPath)
    {
        File[] files = collectFiles(targetPath);
        
        if(files!=null)
        for (File file : files)
        {
            if (file.isDirectory())
                addFileFrom(file.getPath());
            else
                addResourceOrWebInf(targetPath, file);
        }
    }

    private static void addResourceOrWebInf(String targetPath, File file)
    {
    	if(targetPath.contains(WarConfig.WEBAPP_FOLDER))
                webArchive.addAsWebInfResource(file);
        else
            if(file.getName().contains("persistence"))
                webArchive.addAsResource(file,"META-INF/persistence.xml");
            else
                webArchive.addAsResource(file);
    }

    private static File[] collectFiles(String targetPath)
    {
        return new File(targetPath).listFiles();
    }

    private static void printAbsolutPath(){
        try {
            System.out.println(new File(new File(".").getAbsolutePath()).getCanonicalPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}