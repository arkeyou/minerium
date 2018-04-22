package br.gov.prodemge.ssc.interfaces;

import br.gov.prodemge.ssc.enumerations.TipoRecurso;
import java.io.Serializable;
import java.util.Set;

public abstract interface IRecurso
  extends Serializable
{
  public abstract String getUrl();
  
  public abstract String getChaveMenu();
  
  public abstract TipoRecurso getTipoRecurso();
  
  public abstract IModulo getModulo();
  
  public abstract String getNome();
  
  public abstract Set<IRecursoOperacao> getRecursoOperacoes();
  
  public abstract Long getId();
  
  public abstract String getCodigoRecurso();
}


/* Location:              C:\AmbienteDev\maven\.m2\repository\br\gov\prodemge\ssc\ssc-interfaces\1.2.20\ssc-interfaces-1.2.20\!\br\gov\prodemge\ssc\interfaces\IRecurso.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */