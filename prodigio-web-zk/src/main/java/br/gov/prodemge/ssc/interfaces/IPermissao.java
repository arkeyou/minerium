package br.gov.prodemge.ssc.interfaces;

import java.io.Serializable;

public abstract interface IPermissao
  extends Serializable
{
  public abstract IRecursoOperacao getRecursoOperacao();
  
  public abstract IPapel getPapel();
  
  public abstract Long getId();
}


/* Location:              C:\AmbienteDev\maven\.m2\repository\br\gov\prodemge\ssc\ssc-interfaces\1.2.20\ssc-interfaces-1.2.20\!\br\gov\prodemge\ssc\interfaces\IPermissao.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */