package br.gov.prodemge.ssc.interfaces;

import java.io.Serializable;

public abstract interface ISenha
  extends Serializable
{
  public abstract Long getId();
  
  public abstract String getSenhaAtual();
  
  public abstract IUsuario getUsuario();
  
  public abstract String getConfirmacaoSenhaAtual();
}


/* Location:              C:\AmbienteDev\maven\.m2\repository\br\gov\prodemge\ssc\ssc-interfaces\1.2.20\ssc-interfaces-1.2.20\!\br\gov\prodemge\ssc\interfaces\ISenha.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */