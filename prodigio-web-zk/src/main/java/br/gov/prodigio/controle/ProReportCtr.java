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
package br.gov.prodigio.controle;

import java.util.Arrays;

import org.zkoss.zul.ListModelList;



import br.gov.prodigio.comum.ReportType;
import br.gov.prodigio.entidades.ProBaseVO;

public class ProReportCtr<ENTITY extends ProBaseVO> extends ProCtr<ENTITY> {
    ReportType reportType = null;
     
    private ListModelList<ReportType> reportTypesModel = new ListModelList<ReportType>(
            Arrays.asList(
                    new ReportType("PDF", "pdf"), 
                    new ReportType("HTML", "html"), 
                    new ReportType("Word (RTF)", "rtf"), 
                    new ReportType("Excel", "xls"), 
                    new ReportType("Excel (JXL)", "jxl"), 
                    new ReportType("CSV", "csv"), 
                    new ReportType("OpenOffice (ODT)", "odt")));
    
    public ReportType getReportType() {
        return reportType;
    }
 
    public void setReportType(ReportType reportType) {
        this.reportType = reportType;
    }

	public ListModelList<ReportType> getReportTypesModel() {
		return reportTypesModel;
	}

	public void setReportTypesModel(ListModelList<ReportType> reportTypesModel) {
		this.reportTypesModel = reportTypesModel;
	}
    

}
