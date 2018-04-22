/*    */ package br.gov.prodemge.ssc.interfaces.dto;
/*    */ 
/*    */ 
/*    */ public class UnidadeEspecieDTO
/*    */   extends SscBaseDTO
/*    */ {
/*    */   private static final long serialVersionUID = 876348316704405676L;
/*    */   private UnidadeDTO unidade;
/*    */   private EspecieDTO especie;
/*    */   
/*    */   public UnidadeDTO getUnidade()
/*    */   {
/* 13 */     return this.unidade;
/*    */   }
/*    */   
/*    */   public void setUnidade(UnidadeDTO unidade) {
/* 17 */     this.unidade = unidade;
/*    */   }
/*    */   
/*    */   public EspecieDTO getEspecie()
/*    */   {
/* 22 */     return this.especie;
/*    */   }
/*    */   
/*    */   public void setEspecie(EspecieDTO especie) {
/* 26 */     this.especie = especie;
/*    */   }
/*    */ }


/* Location:              C:\AmbienteDev\maven\.m2\repository\br\gov\prodemge\ssc\ssc-interfaces\1.2.20\ssc-interfaces-1.2.20\!\br\gov\prodemge\ssc\interfaces\dto\UnidadeEspecieDTO.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */