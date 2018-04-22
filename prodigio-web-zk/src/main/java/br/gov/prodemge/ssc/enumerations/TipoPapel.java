/*    */ package br.gov.prodemge.ssc.enumerations;
/*    */ 
/*    */ public enum TipoPapel {
/*  4 */   AUDITOR(
/*    */   
/*    */ 
/*  7 */     "Auditor"), 
/*    */   
/*  9 */   ADMINISTRADORSEGURANCA(
/*    */   
/*    */ 
/* 12 */     "Administrador Segurança"), 
/*    */   
/* 14 */   GESTORMODULO(
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 20 */     "Gestor de Segurança"), 
/*    */   
/* 22 */   ADMINISTRADORMODULO(
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 33 */     "Administrador"), 
/*    */   
/* 35 */   USUARIO(
/*    */   
/*    */ 
/*    */ 
/* 39 */     "Usuário");
/*    */   
/*    */   private String descricao;
/*    */   
/*    */   private TipoPapel(String descricao) {
/* 44 */     this.descricao = descricao;
/*    */   }
/*    */   
/*    */   public String getDescricao() {
/* 48 */     return this.descricao;
/*    */   }
/*    */   
/*    */   public void setDescricao(String descricao) {
/* 52 */     this.descricao = descricao;
/*    */   }
/*    */ }


/* Location:              C:\AmbienteDev\maven\.m2\repository\br\gov\prodemge\ssc\ssc-interfaces\1.2.20\ssc-interfaces-1.2.20\!\br\gov\prodemge\ssc\enumerations\TipoPapel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */