/*    */ package br.gov.prodemge.ssc.interfaces.dto;
/*    */ 
/*    */ public class UsuarioUnidadePapelDTO
/*    */   extends SscBaseDTO
/*    */ {
/*    */   private static final long serialVersionUID = 2454442517782503682L;
/*    */   private UsuarioUnidadeDTO usuarioUnidade;
/*    */   private UnidadePapelDTO unidadePapel;
/*    */   
/*    */   public UsuarioUnidadeDTO getUsuarioUnidade()
/*    */   {
/* 12 */     return this.usuarioUnidade;
/*    */   }
/*    */   
/*    */   public void setUsuarioUnidade(UsuarioUnidadeDTO usuarioUnidade) {
/* 16 */     this.usuarioUnidade = usuarioUnidade;
/*    */   }
/*    */   
/*    */   public UnidadePapelDTO getUnidadePapel() {
/* 20 */     return this.unidadePapel;
/*    */   }
/*    */   
/*    */   public void setUnidadePapel(UnidadePapelDTO unidadePapel) {
/* 24 */     this.unidadePapel = unidadePapel;
/*    */   }
/*    */ }


/* Location:              C:\AmbienteDev\maven\.m2\repository\br\gov\prodemge\ssc\ssc-interfaces\1.2.20\ssc-interfaces-1.2.20\!\br\gov\prodemge\ssc\interfaces\dto\UsuarioUnidadePapelDTO.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */