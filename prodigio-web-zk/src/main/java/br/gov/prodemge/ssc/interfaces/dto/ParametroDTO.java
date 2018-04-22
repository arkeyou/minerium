/*    */ package br.gov.prodemge.ssc.interfaces.dto;
/*    */ 
/*    */ 
/*    */ public class ParametroDTO
/*    */   extends SscBaseDTO
/*    */ {
/*    */   private static final long serialVersionUID = 4689400945183233053L;
/*    */   private String nome;
/*    */   private String valor;
/*    */   
/*    */   public String getNome()
/*    */   {
/* 13 */     return this.nome;
/*    */   }
/*    */   
/*    */   public void setNome(String nome) {
/* 17 */     this.nome = nome;
/*    */   }
/*    */   
/*    */   public String getValor() {
/* 21 */     return this.valor;
/*    */   }
/*    */   
/*    */   public void setValor(String valor) {
/* 25 */     this.valor = valor;
/*    */   }
/*    */ }


/* Location:              C:\AmbienteDev\maven\.m2\repository\br\gov\prodemge\ssc\ssc-interfaces\1.2.20\ssc-interfaces-1.2.20\!\br\gov\prodemge\ssc\interfaces\dto\ParametroDTO.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */