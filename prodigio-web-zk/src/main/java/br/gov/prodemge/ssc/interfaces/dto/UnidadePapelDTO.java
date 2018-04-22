/*    */ package br.gov.prodemge.ssc.interfaces.dto;
/*    */ 
/*    */ 
/*    */ public class UnidadePapelDTO
/*    */   extends SscBaseDTO
/*    */ {
/*    */   private static final long serialVersionUID = 6789337771492588562L;
/*    */   
/*    */   private UnidadeDTO unidade;
/*    */   
/*    */   private PapelDTO papel;
/*    */   
/*    */   public UnidadeDTO getUnidade()
/*    */   {
/* 15 */     return this.unidade;
/*    */   }
/*    */   
/*    */   public void setUnidade(UnidadeDTO unidade) {
/* 19 */     this.unidade = unidade;
/*    */   }
/*    */   
/*    */   public PapelDTO getPapel()
/*    */   {
/* 24 */     return this.papel;
/*    */   }
/*    */   
/*    */   public void setPapel(PapelDTO papel)
/*    */   {
/* 29 */     this.papel = papel;
/*    */   }
/*    */ }


/* Location:              C:\AmbienteDev\maven\.m2\repository\br\gov\prodemge\ssc\ssc-interfaces\1.2.20\ssc-interfaces-1.2.20\!\br\gov\prodemge\ssc\interfaces\dto\UnidadePapelDTO.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */