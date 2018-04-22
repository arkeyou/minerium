/*    */ package br.gov.prodemge.ssc.interfaces.dto;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ 
/*    */ public class SscBaseDTO implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = -2883000057836445036L;
/*    */   private Long id;
/*    */   private String cdLoginMovimentacao;
/*    */   private String ipMovimentacao;
/*    */   
/*    */   public Long getId() {
/* 13 */     return this.id;
/*    */   }
/*    */   
/*    */   public void setId(Long id) {
/* 17 */     this.id = id;
/*    */   }
/*    */   
/*    */   public String getCdLoginMovimentacao() {
/* 21 */     return this.cdLoginMovimentacao;
/*    */   }
/*    */   
/*    */   public void setCdLoginMovimentacao(String cdLoginMovimentacao) {
/* 25 */     this.cdLoginMovimentacao = cdLoginMovimentacao;
/*    */   }
/*    */   
/*    */   public String getIpMovimentacao() {
/* 29 */     return this.ipMovimentacao;
/*    */   }
/*    */   
/*    */   public void setIpMovimentacao(String ipMovimentacao) {
/* 33 */     this.ipMovimentacao = ipMovimentacao;
/*    */   }
/*    */ }


/* Location:              C:\AmbienteDev\maven\.m2\repository\br\gov\prodemge\ssc\ssc-interfaces\1.2.20\ssc-interfaces-1.2.20\!\br\gov\prodemge\ssc\interfaces\dto\SscBaseDTO.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */