package br.gov.prodemge.ssc.interfaces;

import java.io.Serializable;
import java.util.Set;

public abstract interface IEspecie
  extends Serializable
{
  public abstract Long getId();
  
  public abstract String getCodigoEspecie();
  
  public abstract String getNome();
  
  public abstract String getDescricao();
  
  public abstract Set<IUnidadeEspecie> getUnidadeEspecie();
  
  public abstract Set<IPermissaoEspecie> getListaPermissoesEspecie();
  
  public abstract Set<IEspecieTag> getListaEspecieTag();
}


/* Location:              C:\AmbienteDev\maven\.m2\repository\br\gov\prodemge\ssc\ssc-interfaces\1.2.20\ssc-interfaces-1.2.20\!\br\gov\prodemge\ssc\interfaces\IEspecie.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */