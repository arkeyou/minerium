package br.gov.prodemge.ssc.interfaces;

import java.io.Serializable;
import java.util.Set;

public abstract interface IModulo
  extends Serializable
{
  public abstract Set<IRecurso> getRecursos();
  
  public abstract Long getId();
  
  public abstract String getNome();
  
  public abstract String getSigla();
  
  public abstract String getDescricao();
  
  public abstract String getLink();
  
  public abstract String getContexto();
  
  public abstract IModulo getAplicacaoPai();
  
  public abstract String getParametrosGerais();
}


/* Location:              C:\AmbienteDev\maven\.m2\repository\br\gov\prodemge\ssc\ssc-interfaces\1.2.20\ssc-interfaces-1.2.20\!\br\gov\prodemge\ssc\interfaces\IModulo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */