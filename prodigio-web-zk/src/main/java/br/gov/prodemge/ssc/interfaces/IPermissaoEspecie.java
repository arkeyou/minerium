package br.gov.prodemge.ssc.interfaces;

import java.io.Serializable;

public abstract interface IPermissaoEspecie
  extends Serializable
{
  public abstract IRecursoOperacao getRecursoOperacao();
  
  public abstract IEspecie getEspecie();
}


/* Location:              C:\AmbienteDev\maven\.m2\repository\br\gov\prodemge\ssc\ssc-interfaces\1.2.20\ssc-interfaces-1.2.20\!\br\gov\prodemge\ssc\interfaces\IPermissaoEspecie.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */