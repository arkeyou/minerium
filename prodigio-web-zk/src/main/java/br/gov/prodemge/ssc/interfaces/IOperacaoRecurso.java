package br.gov.prodemge.ssc.interfaces;

import br.gov.prodemge.ssc.enumerations.Operacao;
import java.io.Serializable;

public abstract interface IOperacaoRecurso
  extends Serializable
{
  public abstract IRecursoOperacao getRecursos();
  
  public abstract Operacao getOperacao();
}


/* Location:              C:\AmbienteDev\maven\.m2\repository\br\gov\prodemge\ssc\ssc-interfaces\1.2.20\ssc-interfaces-1.2.20\!\br\gov\prodemge\ssc\interfaces\IOperacaoRecurso.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */