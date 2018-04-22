package br.gov.prodemge.ssc.interfaces;

import java.io.Serializable;
import java.util.Date;

public abstract interface IUsuarioUnidadePapel
  extends Serializable
{
  public abstract IUsuarioUnidade getUsuarioUnidade();
  
  public abstract IUnidadePapel getUnidadePapel();
  
  public abstract Long getId();
  
  public abstract boolean verificaValidade();
  
  public abstract Date getDataInicio();
  
  public abstract Date getDataFim();
}


/* Location:              C:\AmbienteDev\maven\.m2\repository\br\gov\prodemge\ssc\ssc-interfaces\1.2.20\ssc-interfaces-1.2.20\!\br\gov\prodemge\ssc\interfaces\IUsuarioUnidadePapel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */