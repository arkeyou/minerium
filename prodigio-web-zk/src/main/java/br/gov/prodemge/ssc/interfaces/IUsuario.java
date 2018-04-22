package br.gov.prodemge.ssc.interfaces;

import br.gov.prodemge.ssc.interfaces.base.IUsuarioBase;
import java.util.List;
import java.util.Set;

public abstract interface IUsuario
  extends IUsuarioBase
{
  public abstract Long getId();
  
  public abstract String getEmail();
  
  public abstract String getTelefone();
  
  public abstract String getTokenLtpa2();
  
  public abstract String getCpf();
  
  public abstract String getEmailGestor();
  
  public abstract void setTokenLtpa2(String paramString);
  
  public abstract ISenha getSenha();
  
  public abstract Set<IUsuarioUnidade> getListaUsuarioUnidade();
  
  public abstract List<IPapel> getPapeis();
  
  public abstract List<IPapel> getPapeis(IUnidade paramIUnidade);
  
  public abstract boolean contextoAutenticado(String paramString);
  
  public abstract String getChaveRecuperacaoSenha();
  
  public abstract String getMatricula();
}


/* Location:              C:\AmbienteDev\maven\.m2\repository\br\gov\prodemge\ssc\ssc-interfaces\1.2.20\ssc-interfaces-1.2.20\!\br\gov\prodemge\ssc\interfaces\IUsuario.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */