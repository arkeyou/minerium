package br.gov.prodemge.ssc.interfaces;

import java.io.Serializable;

public abstract interface IUsuarioModulo
  extends Serializable
{
  public abstract IUsuario getUsuario();
  
  public abstract IModulo getModulo();
  
  public abstract Long getId();
}


/* Location:              C:\AmbienteDev\maven\.m2\repository\br\gov\prodemge\ssc\ssc-interfaces\1.2.20\ssc-interfaces-1.2.20\!\br\gov\prodemge\ssc\interfaces\IUsuarioModulo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */