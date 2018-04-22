package br.gov.prodemge.ssc.interfaces;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

public abstract interface IUsuarioUnidade
  extends Serializable
{
  public abstract IUsuario getUsuario();
  
  public abstract IUnidade getUnidade();
  
  public abstract Set<IUsuarioUnidadePapel> getUsuarioUnidadePapel();
  
  public abstract Date getDataInicio();
  
  public abstract Date getDataFim();
  
  public abstract Long getId();
  
  public abstract List<IPapel> getPapeis();
}


/* Location:              C:\AmbienteDev\maven\.m2\repository\br\gov\prodemge\ssc\ssc-interfaces\1.2.20\ssc-interfaces-1.2.20\!\br\gov\prodemge\ssc\interfaces\IUsuarioUnidade.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */