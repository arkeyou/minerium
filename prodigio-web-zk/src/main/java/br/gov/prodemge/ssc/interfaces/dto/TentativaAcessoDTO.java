/*    */ package br.gov.prodemge.ssc.interfaces.dto;
/*    */ 
/*    */ public class TentativaAcessoDTO extends SscBaseDTO
/*    */ {
/*    */   private static final long serialVersionUID = -5185208943994585540L;
/*    */   private String login;
/*    */   private Integer quantidadeTentativa;
/*    */   private UsuarioDTO usuario;
/*    */   
/*    */   public String getLogin() {
/* 11 */     return this.login;
/*    */   }
/*    */   
/*    */   public void setLogin(String login) {
/* 15 */     this.login = login;
/*    */   }
/*    */   
/*    */   public Integer getQuantidadeTentativa() {
/* 19 */     return this.quantidadeTentativa;
/*    */   }
/*    */   
/*    */   public UsuarioDTO getUsuario() {
/* 23 */     return this.usuario;
/*    */   }
/*    */   
/*    */   public void setUsuario(UsuarioDTO usuario) {
/* 27 */     this.usuario = usuario;
/*    */   }
/*    */   
/*    */   public void setQuantidadeTentativa(Integer quantidadeTentativa) {
/* 31 */     this.quantidadeTentativa = quantidadeTentativa;
/*    */   }
/*    */ }


/* Location:              C:\AmbienteDev\maven\.m2\repository\br\gov\prodemge\ssc\ssc-interfaces\1.2.20\ssc-interfaces-1.2.20\!\br\gov\prodemge\ssc\interfaces\dto\TentativaAcessoDTO.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */