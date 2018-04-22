/*    */ package br.gov.prodemge.ssc.interfaces.dto;
/*    */ 
/*    */ import br.gov.prodemge.ssc.enumerations.Operacao;
/*    */ 
/*    */ public class RecursoOperacaoDTO
/*    */   extends SscBaseDTO
/*    */ {
/*    */   private static final long serialVersionUID = -2132576906591950603L;
/*    */   private RecursoDTO recursoDTO;
/*    */   private Operacao operacao;
/*    */   private String descricao;
/*    */   
/*    */   public RecursoDTO getRecursoDTO()
/*    */   {
/* 15 */     return this.recursoDTO;
/*    */   }
/*    */   
/*    */   public void setRecursoDTO(RecursoDTO recurso) {
/* 19 */     this.recursoDTO = recurso;
/*    */   }
/*    */   
/*    */   public Operacao getOperacao() {
/* 23 */     return this.operacao;
/*    */   }
/*    */   
/*    */   public void setOperacao(Operacao operacao) {
/* 27 */     this.operacao = operacao;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getDescricao()
/*    */   {
/* 39 */     return this.descricao;
/*    */   }
/*    */   
/*    */   public void setDescricao(String descricao) {
/* 43 */     this.descricao = descricao;
/*    */   }
/*    */ }


/* Location:              C:\AmbienteDev\maven\.m2\repository\br\gov\prodemge\ssc\ssc-interfaces\1.2.20\ssc-interfaces-1.2.20\!\br\gov\prodemge\ssc\interfaces\dto\RecursoOperacaoDTO.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */