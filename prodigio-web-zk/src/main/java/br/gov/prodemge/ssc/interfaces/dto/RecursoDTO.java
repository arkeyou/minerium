/*    */ package br.gov.prodemge.ssc.interfaces.dto;
/*    */ 
/*    */ import br.gov.prodemge.ssc.enumerations.TipoRecurso;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RecursoDTO
/*    */   extends SscBaseDTO
/*    */ {
/*    */   private static final long serialVersionUID = 7932032168580872195L;
/*    */   private TipoRecurso tipoRecurso;
/*    */   private ModuloDTO moduloDTO;
/*    */   private String url;
/*    */   private String chaveMenu;
/*    */   private String nome;
/*    */   private String descricao;
/*    */   private String codigoRecurso;
/*    */   
/*    */   public TipoRecurso getTipoRecurso()
/*    */   {
/* 24 */     return this.tipoRecurso;
/*    */   }
/*    */   
/*    */   public void setTipoRecurso(TipoRecurso tipoRecurso) {
/* 28 */     this.tipoRecurso = tipoRecurso;
/*    */   }
/*    */   
/*    */   public ModuloDTO getModuloDTO() {
/* 32 */     return this.moduloDTO;
/*    */   }
/*    */   
/*    */   public void setModuloDTO(ModuloDTO modulo) {
/* 36 */     this.moduloDTO = modulo;
/*    */   }
/*    */   
/*    */   public String getNome() {
/* 40 */     return this.nome;
/*    */   }
/*    */   
/*    */   public void setNome(String nome) {
/* 44 */     this.nome = nome;
/*    */   }
/*    */   
/*    */   public String getUrl() {
/* 48 */     return this.url;
/*    */   }
/*    */   
/*    */   public void setUrl(String url) {
/* 52 */     this.url = url;
/*    */   }
/*    */   
/*    */   public String getChaveMenu() {
/* 56 */     return this.chaveMenu;
/*    */   }
/*    */   
/*    */   public void setChaveMenu(String chaveMenu) {
/* 60 */     this.chaveMenu = chaveMenu;
/*    */   }
/*    */   
/*    */   public String getDescricao() {
/* 64 */     return this.descricao;
/*    */   }
/*    */   
/*    */   public void setDescricao(String descricao) {
/* 68 */     this.descricao = descricao;
/*    */   }
/*    */   
/*    */   public String getCodigoRecurso() {
/* 72 */     return this.codigoRecurso;
/*    */   }
/*    */   
/*    */   public void setCodigoRecurso(String codigoRecurso) {
/* 76 */     this.codigoRecurso = codigoRecurso;
/*    */   }
/*    */ }


/* Location:              C:\AmbienteDev\maven\.m2\repository\br\gov\prodemge\ssc\ssc-interfaces\1.2.20\ssc-interfaces-1.2.20\!\br\gov\prodemge\ssc\interfaces\dto\RecursoDTO.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */