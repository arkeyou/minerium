package br.gov.prodemge.ssc.interfaces;

import br.gov.prodemge.ssc.enumerations.TipoPapel;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

public abstract interface IPapel
  extends Serializable
{
  public abstract IModulo getModulo();
  
  public abstract String getDescricao();
  
  public abstract String getNome();
  
  public abstract Date getDataInicio();
  
  public abstract Date getDataFim();
  
  public abstract List<IUnidadePapel> getListaUnidadePapel();
  
  public abstract TipoPapel getTipoPapel();
  
  public abstract IPapel getPapelPai();
  
  public abstract Long getId();
  
  public abstract Set<IPermissao> getListaPermissoes();
  
  public abstract boolean verificaValidade();
  
  public abstract String getCodigoPapel();
  
  public abstract Set<IPapelTag> getListaPapelTag();
}


/* Location:              C:\AmbienteDev\maven\.m2\repository\br\gov\prodemge\ssc\ssc-interfaces\1.2.20\ssc-interfaces-1.2.20\!\br\gov\prodemge\ssc\interfaces\IPapel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */