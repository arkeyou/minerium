/*    */ package br.gov.prodemge.ssc.interfaces.dto;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.Date;
/*    */ import javax.xml.bind.annotation.XmlAccessType;
/*    */ import javax.xml.bind.annotation.XmlAccessorType;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @XmlAccessorType(XmlAccessType.FIELD)
/*    */ public class PapelDTO
/*    */   extends SscBaseDTO
/*    */ {
/*    */   private static final long serialVersionUID = -6890162814654017442L;
/*    */   public ModuloDTO modulo;
/*    */   private String nome;
/*    */   private String descricao;
/*    */   private Date dataInicio;
/*    */   private Date dataFim;
/*    */   private PapelDTO papelPai;
/*    */   private String codigoPapel;
/*    */   private Collection<TagDTO> tags;
/*    */   
/*    */   public String getCodigoPapel()
/*    */   {
/* 38 */     return this.codigoPapel;
/*    */   }
/*    */   
/*    */   public void setCodigoPapel(String codigoPapel) {
/* 42 */     this.codigoPapel = codigoPapel;
/*    */   }
/*    */   
/*    */   public String getNome() {
/* 46 */     return this.nome;
/*    */   }
/*    */   
/*    */   public void setNome(String nome) {
/* 50 */     this.nome = nome;
/*    */   }
/*    */   
/*    */   public String getDescricao() {
/* 54 */     return this.descricao;
/*    */   }
/*    */   
/*    */   public void setDescricao(String descricao) {
/* 58 */     this.descricao = descricao;
/*    */   }
/*    */   
/*    */   public Date getDataInicio() {
/* 62 */     return this.dataInicio;
/*    */   }
/*    */   
/*    */   public void setDataInicio(Date dataInicio) {
/* 66 */     this.dataInicio = dataInicio;
/*    */   }
/*    */   
/*    */   public Date getDataFim() {
/* 70 */     return this.dataFim;
/*    */   }
/*    */   
/*    */   public void setDataFim(Date dataFim) {
/* 74 */     this.dataFim = dataFim;
/*    */   }
/*    */   
/*    */   public ModuloDTO getModuloDTO() {
/* 78 */     return this.modulo;
/*    */   }
/*    */   
/*    */   public void setModuloDTO(ModuloDTO moduloDTO) {
/* 82 */     this.modulo = moduloDTO;
/*    */   }
/*    */   
/*    */   public PapelDTO getPapelPaiDTO() {
/* 86 */     return this.papelPai;
/*    */   }
/*    */   
/*    */   public void setPapelPaiDTO(PapelDTO papelPai) {
/* 90 */     this.papelPai = papelPai;
/*    */   }
/*    */   
/*    */   public Collection<TagDTO> getTags() {
/* 94 */     return this.tags;
/*    */   }
/*    */   
/*    */   public void setTags(Collection<TagDTO> tags) {
/* 98 */     this.tags = tags;
/*    */   }
/*    */ }


/* Location:              C:\AmbienteDev\maven\.m2\repository\br\gov\prodemge\ssc\ssc-interfaces\1.2.20\ssc-interfaces-1.2.20\!\br\gov\prodemge\ssc\interfaces\dto\PapelDTO.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */