/*    */ package br.gov.prodemge.ssc.interfaces.dto;
/*    */ 
/*    */ public class PermissaoEspecieDTO extends SscBaseDTO
/*    */ {
/*    */   private static final long serialVersionUID = -8556797739811482715L;
/*    */   private EspecieDTO especie;
/*    */   private RecursoOperacaoDTO recursoOperacao;
/*    */   
/*    */   public EspecieDTO getEspecie()
/*    */   {
/* 11 */     return this.especie;
/*    */   }
/*    */   
/*    */   public void setEspecie(EspecieDTO especie) {
/* 15 */     this.especie = especie;
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


/* Location:              C:\AmbienteDev\maven\.m2\repository\br\gov\prodemge\ssc\ssc-interfaces\1.2.20\ssc-interfaces-1.2.20\!\br\gov\prodemge\ssc\interfaces\dto\PermissaoEspecieDTO.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */