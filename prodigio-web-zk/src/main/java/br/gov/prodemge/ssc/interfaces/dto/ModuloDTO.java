/*    */ package br.gov.prodemge.ssc.interfaces.dto;
/*    */ 
/*    */ 
/*    */ public class ModuloDTO
/*    */   extends SscBaseDTO
/*    */ {
/*    */   private static final long serialVersionUID = -2439793863088926797L;
/*    */   
/*    */   private String nome;
/*    */   
/*    */   private String sigla;
/*    */   
/*    */   private String descricao;
/*    */   
/*    */   private String link;
/*    */   
/*    */   private String contexto;
/*    */   
/*    */   private ModuloDTO aplicacaoPai;
/*    */   
/*    */   public String getNome()
/*    */   {
/* 23 */     return this.nome;
/*    */   }
/*    */   
/*    */   public void setNome(String nome) {
/* 27 */     this.nome = nome;
/*    */   }
/*    */   
/*    */   public String getSigla() {
/* 31 */     return this.sigla;
/*    */   }
/*    */   
/*    */   public void setSigla(String sigla) {
/* 35 */     this.sigla = sigla;
/*    */   }
/*    */   
/*    */   public String getDescricao() {
/* 39 */     return this.descricao;
/*    */   }
/*    */   
/*    */   public void setDescricao(String descricao) {
/* 43 */     this.descricao = descricao;
/*    */   }
/*    */   
/*    */   public String getLink() {
/* 47 */     return this.link;
/*    */   }
/*    */   
/*    */   public void setLink(String link) {
/* 51 */     this.link = link;
/*    */   }
/*    */   
/*    */   public String getContexto() {
/* 55 */     return this.contexto;
/*    */   }
/*    */   
/*    */   public void setContexto(String contexto) {
/* 59 */     this.contexto = contexto;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public ModuloDTO getAplicacaoPai()
/*    */   {
/* 71 */     return this.aplicacaoPai;
/*    */   }
/*    */   
/*    */   public void setAplicacaoPaiDTO(ModuloDTO aplicacaoPai) {
/* 75 */     this.aplicacaoPai = aplicacaoPai;
/*    */   }
/*    */ }


/* Location:              C:\AmbienteDev\maven\.m2\repository\br\gov\prodemge\ssc\ssc-interfaces\1.2.20\ssc-interfaces-1.2.20\!\br\gov\prodemge\ssc\interfaces\dto\ModuloDTO.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */