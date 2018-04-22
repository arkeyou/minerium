/*    */ package br.gov.prodemge.ssc.interfaces.dto;
/*    */ 
/*    */ import javax.xml.bind.annotation.XmlAccessType;
/*    */ import javax.xml.bind.annotation.XmlAccessorType;
/*    */ 
/*    */ @XmlAccessorType(XmlAccessType.FIELD)
/*    */ public class TagDTO
/*    */   extends SscBaseDTO
/*    */ {
/*    */   private static final long serialVersionUID = 2026844104722990064L;
/*    */   private String descricao;
/*    */   private ModuloDTO modulo;
/*    */   
/*    */   public String getDescricao()
/*    */   {
/* 16 */     return this.descricao;
/*    */   }
/*    */   
/*    */   public void setDescricao(String descricao) {
/* 20 */     this.descricao = descricao;
/*    */   }
/*    */   
/*    */   public ModuloDTO getModulo() {
/* 24 */     return this.modulo;
/*    */   }
/*    */   
/*    */   public void setModulo(ModuloDTO modulo) {
/* 28 */     this.modulo = modulo;
/*    */   }
/*    */ }


/* Location:              C:\AmbienteDev\maven\.m2\repository\br\gov\prodemge\ssc\ssc-interfaces\1.2.20\ssc-interfaces-1.2.20\!\br\gov\prodemge\ssc\interfaces\dto\TagDTO.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */