/*    */ package br.gov.prodemge.ssc.enumerations;
/*    */ 
/*    */ public enum TipoDedo
/*    */ {
/*  5 */   minimoEsquerda(Integer.valueOf(1)),  anelarEsquerda(Integer.valueOf(2)),  medioEsquerda(Integer.valueOf(3)),  indicadorEsquerda(Integer.valueOf(4)),  polegarEsquerda(Integer.valueOf(5)), 
/*  6 */   minimoDireita(Integer.valueOf(6)),  anelarDireita(Integer.valueOf(7)),  medioDireita(Integer.valueOf(8)),  indicadorDireita(Integer.valueOf(9)),  polegarDireita(Integer.valueOf(10));
/*    */   
/*    */   private Integer valor;
/*    */   
/*    */   private TipoDedo(Integer valor) {
/* 11 */     this.valor = valor;
/*    */   }
/*    */   
/*    */   public Integer getValor() {
/* 15 */     return this.valor;
/*    */   }
/*    */   
/*    */   public void setValor(Integer valor) {
/* 19 */     this.valor = valor;
/*    */   }
/*    */ }


/* Location:              C:\AmbienteDev\maven\.m2\repository\br\gov\prodemge\ssc\ssc-interfaces\1.2.20\ssc-interfaces-1.2.20\!\br\gov\prodemge\ssc\enumerations\TipoDedo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */