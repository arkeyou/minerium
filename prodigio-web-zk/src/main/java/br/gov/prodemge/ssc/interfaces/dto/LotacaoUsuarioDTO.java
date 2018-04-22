/*    */ package br.gov.prodemge.ssc.interfaces.dto;
/*    */ 
/*    */ 
/*    */ public class LotacaoUsuarioDTO
/*    */   extends SscBaseDTO
/*    */ {
/*    */   private static final long serialVersionUID = -2892181967759904553L;
/*    */   private String codigoCliente;
/*    */   private String descricao;
/*    */   
/*    */   public String getCodigoCliente()
/*    */   {
/* 13 */     return this.codigoCliente;
/*    */   }
/*    */   
/*    */   public void setCodigoCliente(String codigoCliente) {
/* 17 */     this.codigoCliente = codigoCliente;
/*    */   }
/*    */   
/*    */   public String getDescricao() {
/* 21 */     return this.descricao;
/*    */   }
/*    */   
/*    */   public void setDescricao(String descricao) {
/* 25 */     this.descricao = descricao;
/*    */   }
/*    */ }


/* Location:              C:\AmbienteDev\maven\.m2\repository\br\gov\prodemge\ssc\ssc-interfaces\1.2.20\ssc-interfaces-1.2.20\!\br\gov\prodemge\ssc\interfaces\dto\LotacaoUsuarioDTO.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */