/*    */ package br.gov.prodemge.ssc.enumerations;
/*    */ 
/*    */ public enum TipoRecurso {
/*  4 */   CASODEUSO("Caso De Uso"),  OUTRO("Outro");
/*    */   
/*    */   private String descricao;
/*    */   
/*    */   private TipoRecurso(String descricao) {
/*  9 */     this.descricao = descricao;
/*    */   }
/*    */   
/*    */   public String getDescricao() {
/* 13 */     return this.descricao;
/*    */   }
/*    */   
/*    */   public void setDescricao(String descricao) {
/* 17 */     this.descricao = descricao;
/*    */   }
/*    */ }


/* Location:              C:\AmbienteDev\maven\.m2\repository\br\gov\prodemge\ssc\ssc-interfaces\1.2.20\ssc-interfaces-1.2.20\!\br\gov\prodemge\ssc\enumerations\TipoRecurso.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */