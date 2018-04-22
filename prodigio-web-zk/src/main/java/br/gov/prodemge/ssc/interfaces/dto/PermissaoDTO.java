/*    */ package br.gov.prodemge.ssc.interfaces.dto;
/*    */ 
/*    */ public class PermissaoDTO extends SscBaseDTO
/*    */ {
/*    */   private static final long serialVersionUID = 233417291201142341L;
/*    */   private PapelDTO papel;
/*    */   private RecursoOperacaoDTO recursoOperacao;
/*    */   
/*    */   public PapelDTO getPapel()
/*    */   {
/* 11 */     return this.papel;
/*    */   }
/*    */   
/*    */   public void setPapel(PapelDTO papel) {
/* 15 */     this.papel = papel;
/*    */   }
/*    */   
/*    */   public RecursoOperacaoDTO getRecursoOperacao() {
/* 19 */     return this.recursoOperacao;
/*    */   }
/*    */   
/*    */   public void setRecursoOperacao(RecursoOperacaoDTO recursoOperacao) {
/* 23 */     this.recursoOperacao = recursoOperacao;
/*    */   }
/*    */ }


/* Location:              C:\AmbienteDev\maven\.m2\repository\br\gov\prodemge\ssc\ssc-interfaces\1.2.20\ssc-interfaces-1.2.20\!\br\gov\prodemge\ssc\interfaces\dto\PermissaoDTO.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */