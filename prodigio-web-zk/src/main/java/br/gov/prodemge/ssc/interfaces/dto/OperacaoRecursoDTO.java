/*    */ package br.gov.prodemge.ssc.interfaces.dto;
/*    */ 
/*    */ import br.gov.prodemge.ssc.enumerations.Operacao;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class OperacaoRecursoDTO
/*    */   extends SscBaseDTO
/*    */ {
/*    */   private static final long serialVersionUID = 91527243911443055L;
/*    */   private RecursoOperacaoDTO recursos;
/*    */   private Operacao operacao;
/*    */   
/*    */   public RecursoOperacaoDTO getRecursos()
/*    */   {
/* 17 */     return this.recursos;
/*    */   }
/*    */   
/*    */   public void setRecursos(RecursoOperacaoDTO recursos) {
/* 21 */     this.recursos = recursos;
/*    */   }
/*    */   
/*    */   public Operacao getOperacao() {
/* 25 */     return this.operacao;
/*    */   }
/*    */   
/*    */   public void setOperacao(Operacao operacao) {
/* 29 */     this.operacao = operacao;
/*    */   }
/*    */ }


/* Location:              C:\AmbienteDev\maven\.m2\repository\br\gov\prodemge\ssc\ssc-interfaces\1.2.20\ssc-interfaces-1.2.20\!\br\gov\prodemge\ssc\interfaces\dto\OperacaoRecursoDTO.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */