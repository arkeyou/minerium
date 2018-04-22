package br.gov.prodemge.ssc.interfaces;

import br.gov.prodemge.ssc.interfaces.base.IUnidadeBase;
import java.util.Set;

public abstract interface IUnidade
  extends IUnidadeBase
{
  public abstract Long getId();
  
  public abstract String getNome();
  
  public abstract IUnidade getUnidadePai();
  
  public abstract Set<IUnidadePapel> getListaUnidadePapel();
  
  public abstract Set<IUnidadeEspecie> getListaUnidadeEspecie();
  
  public abstract String getSigla();
  
  public abstract String getDescricao();
  
  public abstract String getCodigo();
  
  public abstract String getTelefone();
  
  public abstract String getEmail();
  
  public abstract String getFax();
  
  public abstract Long getCodigoOrgao();
}


/* Location:              C:\AmbienteDev\maven\.m2\repository\br\gov\prodemge\ssc\ssc-interfaces\1.2.20\ssc-interfaces-1.2.20\!\br\gov\prodemge\ssc\interfaces\IUnidade.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */