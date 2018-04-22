package br.gov.prodemge.ssc.interfaces;

import br.gov.prodemge.ssc.enumerations.TipoPapel;
import java.io.Serializable;

public abstract interface IPapelAssociacao
  extends Serializable
{
  public abstract IPapel getPapel();
  
  public abstract IModulo getModulo();
  
  public abstract TipoPapel getTipo();
}


/* Location:              C:\AmbienteDev\maven\.m2\repository\br\gov\prodemge\ssc\ssc-interfaces\1.2.20\ssc-interfaces-1.2.20\!\br\gov\prodemge\ssc\interfaces\IPapelAssociacao.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */