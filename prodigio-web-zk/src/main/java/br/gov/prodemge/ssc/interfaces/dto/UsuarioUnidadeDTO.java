/*    */ package br.gov.prodemge.ssc.interfaces.dto;
/*    */ 
/*    */ import java.util.Date;
/*    */ import java.util.Set;
/*    */ 
/*    */ public class UsuarioUnidadeDTO extends SscBaseDTO
/*    */ {
/*    */   private static final long serialVersionUID = -6016633776343613426L;
/*    */   private Date dataInicio;
/*    */   private Date dataFim;
/*    */   private UsuarioDTO usuario;
/*    */   private UnidadeDTO unidade;
/*    */   private Set<UsuarioUnidadePapelDTO> usuarioUnidadePapel;
/*    */   
/*    */   public UsuarioDTO getUsuario()
/*    */   {
/* 17 */     return this.usuario;
/*    */   }
/*    */   
/*    */   public UnidadeDTO getUnidade() {
/* 21 */     return this.unidade;
/*    */   }
/*    */   
/*    */   public Set<UsuarioUnidadePapelDTO> getUsuarioUnidadePapel() {
/* 25 */     return this.usuarioUnidadePapel;
/*    */   }
/*    */   
/*    */   public Date getDataInicio() {
/* 29 */     return this.dataInicio;
/*    */   }
/*    */   
/*    */   public Date getDataFim() {
/* 33 */     return this.dataFim;
/*    */   }
/*    */   
/*    */   public void setUsuario(UsuarioDTO usuario) {
/* 37 */     this.usuario = usuario;
/*    */   }
/*    */   
/*    */   public void setUnidade(UnidadeDTO unidade) {
/* 41 */     this.unidade = unidade;
/*    */   }
/*    */   
/*    */   public void setUsuarioUnidadePapel(Set<UsuarioUnidadePapelDTO> usuarioUnidadePapel) {
/* 45 */     this.usuarioUnidadePapel = usuarioUnidadePapel;
/*    */   }
/*    */   
/*    */   public void setDataInicio(Date dataInicio) {
/* 49 */     this.dataInicio = dataInicio;
/*    */   }
/*    */   
/*    */   public void setDataFim(Date dataFim) {
/* 53 */     this.dataFim = dataFim;
/*    */   }
/*    */ }


/* Location:              C:\AmbienteDev\maven\.m2\repository\br\gov\prodemge\ssc\ssc-interfaces\1.2.20\ssc-interfaces-1.2.20\!\br\gov\prodemge\ssc\interfaces\dto\UsuarioUnidadeDTO.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */