package br.gov.prodemge.ssc.interfaces;

import java.io.Serializable;
import java.util.Set;

public abstract interface IUnidadePapel
  extends Serializable
{
  public abstract IUnidade getUnidade();
  
  public abstract IPapel getPapel();
  
  public abstract Set<IUsuarioUnidadePapel> getUsuariosUnidadesPapeis();
  
  public abstract Long getId();
}


/* Location:              C:\AmbienteDev\maven\.m2\repository\br\gov\prodemge\ssc\ssc-interfaces\1.2.20\ssc-interfaces-1.2.20\!\br\gov\prodemge\ssc\interfaces\IUnidadePapel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */