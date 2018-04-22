/*     */ package br.gov.prodemge.ssc.interfaces.dto;
/*     */ 
/*     */ import javax.xml.bind.annotation.XmlAccessType;
/*     */ import javax.xml.bind.annotation.XmlAccessorType;
/*     */ 
/*     */ @XmlAccessorType(XmlAccessType.FIELD)
/*     */ public class UnidadeDTO
/*     */   extends SscBaseDTO
/*     */ {
/*     */   private static final long serialVersionUID = 8502386268403603545L;
/*     */   private String nome;
/*     */   private String sigla;
/*     */   private String descricao;
/*     */   private String codigo;
/*     */   private String telefone;
/*     */   private String email;
/*     */   private String fax;
/*     */   private UnidadeDTO unidadePai;
/*     */   private Long codigoOrgao;
/*     */   private String statusRegistro;
/*     */   private String cnpj;
/*     */   
/*     */   public UnidadeDTO getUnidadePaiDTO()
/*     */   {
/*  25 */     return this.unidadePai;
/*     */   }
/*     */   
/*     */   public String getNome() {
/*  29 */     return this.nome;
/*     */   }
/*     */   
/*     */   public String getSigla() {
/*  33 */     return this.sigla;
/*     */   }
/*     */   
/*     */   public String getDescricao() {
/*  37 */     return this.descricao;
/*     */   }
/*     */   
/*     */   public String getCodigo() {
/*  41 */     return this.codigo;
/*     */   }
/*     */   
/*     */   public String getTelefone() {
/*  45 */     return this.telefone;
/*     */   }
/*     */   
/*     */   public String getEmail() {
/*  49 */     return this.email;
/*     */   }
/*     */   
/*     */   public String getFax() {
/*  53 */     return this.fax;
/*     */   }
/*     */   
/*     */   public void setNome(String nome) {
/*  57 */     this.nome = nome;
/*     */   }
/*     */   
/*     */   public void setSigla(String sigla) {
/*  61 */     this.sigla = sigla;
/*     */   }
/*     */   
/*     */   public void setDescricao(String descricao) {
/*  65 */     this.descricao = descricao;
/*     */   }
/*     */   
/*     */   public void setCodigo(String codigoUnidade) {
/*  69 */     this.codigo = codigoUnidade;
/*     */   }
/*     */   
/*     */   public void setTelefone(String telefone) {
/*  73 */     this.telefone = telefone;
/*     */   }
/*     */   
/*     */   public void setEmail(String email) {
/*  77 */     this.email = email;
/*     */   }
/*     */   
/*     */   public void setFax(String fax) {
/*  81 */     this.fax = fax;
/*     */   }
/*     */   
/*     */   public void setUnidadePaiDTO(UnidadeDTO unidadePai) {
/*  85 */     this.unidadePai = unidadePai;
/*     */   }
/*     */   
/*     */   public Long getCodigoOrgao() {
/*  89 */     return this.codigoOrgao;
/*     */   }
/*     */   
/*     */   public void setCodigoOrgao(Long codigoOrgao) {
/*  93 */     this.codigoOrgao = codigoOrgao;
/*     */   }
/*     */   
/*     */   public String getStatusRegistro() {
/*  97 */     return this.statusRegistro;
/*     */   }
/*     */   
/*     */   public void setStatusRegistro(String statusRegistro) {
/* 101 */     this.statusRegistro = statusRegistro;
/*     */   }
/*     */   
/*     */   public String getCnpj() {
/* 105 */     return this.cnpj;
/*     */   }
/*     */   
/*     */   public void setCnpj(String cnpj) {
/* 109 */     this.cnpj = cnpj;
/*     */   }
/*     */ }


/* Location:              C:\AmbienteDev\maven\.m2\repository\br\gov\prodemge\ssc\ssc-interfaces\1.2.20\ssc-interfaces-1.2.20\!\br\gov\prodemge\ssc\interfaces\dto\UnidadeDTO.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */