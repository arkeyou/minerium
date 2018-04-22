/*    */ package br.gov.prodemge.ssc.interfaces.dto;
/*    */ 
/*    */ public class SenhaDTO
/*    */   extends SscBaseDTO
/*    */ {
/*    */   private static final long serialVersionUID = -5966884390574090336L;
/*    */   private String senhaAtual;
/*    */   private String confirmacaoSenhaAtual;
/*    */   private String novaSenha;
/*    */   
/*    */   public String getSenhaAtual()
/*    */   {
/* 13 */     return this.senhaAtual;
/*    */   }
/*    */   
/*    */   public void setSenhaAtual(String senhaAtual) {
/* 17 */     this.senhaAtual = senhaAtual;
/*    */   }
/*    */   
/*    */   public String getConfirmacaoSenhaAtual() {
/* 21 */     return this.confirmacaoSenhaAtual;
/*    */   }
/*    */   
/*    */   public void setConfirmacaoSenhaAtual(String confirmacaoSenhaAtual) {
/* 25 */     this.confirmacaoSenhaAtual = confirmacaoSenhaAtual;
/*    */   }
/*    */   
/*    */   public String getNovaSenha() {
/* 29 */     return this.novaSenha;
/*    */   }
/*    */   
/*    */   public void setNovaSenha(String novaSenha) {
/* 33 */     this.novaSenha = novaSenha;
/*    */   }
/*    */ }


/* Location:              C:\AmbienteDev\maven\.m2\repository\br\gov\prodemge\ssc\ssc-interfaces\1.2.20\ssc-interfaces-1.2.20\!\br\gov\prodemge\ssc\interfaces\dto\SenhaDTO.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */