/*    */ package br.gov.prodemge.ssc.interfaces.dto;
/*    */ 
/*    */ public class UsuarioModuloDTO extends SscBaseDTO
/*    */ {
/*    */   private static final long serialVersionUID = 2200891843431716644L;
/*    */   private UsuarioDTO usuario;
/*    */   private ModuloDTO modulo;
/*    */   
/*    */   public UsuarioDTO getUsuario()
/*    */   {
/* 11 */     return this.usuario;
/*    */   }
/*    */   
/*    */   public void setUsuario(UsuarioDTO usuario) {
/* 15 */     this.usuario = usuario;
/*    */   }
/*    */   
/*    */   public ModuloDTO getModulo() {
/* 19 */     return this.modulo;
/*    */   }
/*    */   
/*    */   public void setModulo(ModuloDTO modulo) {
/* 23 */     this.modulo = modulo;
/*    */   }
/*    */ }


/* Location:              C:\AmbienteDev\maven\.m2\repository\br\gov\prodemge\ssc\ssc-interfaces\1.2.20\ssc-interfaces-1.2.20\!\br\gov\prodemge\ssc\interfaces\dto\UsuarioModuloDTO.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */