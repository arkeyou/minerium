package br.gov.prodemge.ssc.interfaces;

import br.gov.prodemge.ssc.enumerations.Operacao;
import java.io.Serializable;
import java.util.List;

public abstract interface IRecursoOperacao
  extends Serializable
{
  public abstract Operacao getOperacao();
  
  public abstract IRecurso getRecurso();
  
  public abstract Long getId();
  
  public abstract List<IPermissaoEspecie> getListaPermissaoEspecie();
}


/* Location:              C:\AmbienteDev\maven\.m2\repository\br\gov\prodemge\ssc\ssc-interfaces\1.2.20\ssc-interfaces-1.2.20\!\br\gov\prodemge\ssc\interfaces\IRecursoOperacao.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */