/*    */ package br.gov.prodemge.ssc.interfaces.dto;
/*    */ 
/*    */ 
/*    */ public class EspecieDTO
/*    */   extends SscBaseDTO
/*    */ {
/*    */   private static final long serialVersionUID = 5429544137831683205L;
/*    */   
/*    */   private String nome;
/*    */   private String descricao;
/*    */   private String codigoEspecie;
/*    */   private ModuloDTO modulo;
/*    */   
/*    */   public String getNome()
/*    */   {
/* 16 */     return this.nome;
/*    */   }
/*    */   
/*    */   public void setNome(String nome) {
/* 20 */     this.nome = nome;
/*    */   }
/*    */   
/*    */   public String getDescricao() {
/* 24 */     return this.descricao;
/*    */   }
/*    */   
/*    */   public void setDescricao(String descricao) {
/* 28 */     this.descricao = descricao;
/*    */   }
/*    */   
/*    */   public String getCodigoEspecie() {
/* 32 */     return this.codigoEspecie;
/*    */   }
/*    */   
/*    */   public void setCodigoEspecie(String codigoEspecie) {
/* 36 */     this.codigoEspecie = codigoEspecie;
/*    */   }
/*    */   
/*    */   public ModuloDTO getModuloDTO() {
/* 40 */     return this.modulo;
/*    */   }
/*    */   
/*    */   public void setModuloDTO(ModuloDTO modulo) {
/* 44 */     this.modulo = modulo;
/*    */   }
/*    */ }


/* Location:              C:\AmbienteDev\maven\.m2\repository\br\gov\prodemge\ssc\ssc-interfaces\1.2.20\ssc-interfaces-1.2.20\!\br\gov\prodemge\ssc\interfaces\dto\EspecieDTO.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */