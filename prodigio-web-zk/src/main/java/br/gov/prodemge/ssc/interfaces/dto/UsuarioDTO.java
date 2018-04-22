/*     */ package br.gov.prodemge.ssc.interfaces.dto;
/*     */ 
/*     */ import javax.xml.bind.annotation.XmlAccessType;
/*     */ import javax.xml.bind.annotation.XmlAccessorType;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @XmlAccessorType(XmlAccessType.FIELD)
/*     */ public class UsuarioDTO
/*     */   extends SscBaseDTO
/*     */ {
/*     */   private static final long serialVersionUID = 5901877653975549366L;
/*     */   private String login;
/*     */   private String nome;
/*     */   private String email;
/*     */   private String emailGestor;
/*     */   private String emailSecundario;
/*     */   private String telefone;
/*     */   private String celular;
/*     */   private String registroGeral;
/*     */   private String cpf;
/*     */   private String ultimoNome;
/*     */   private String situacao;
/*     */   private String LinkRecuperacaoSenha;
/*     */   private String matricula;
/*     */   private Boolean flagUsuarioServidor;
/*     */   private String linkDeRedirecionamento;
/*     */   
/*     */   public String getLogin()
/*     */   {
/*  51 */     return this.login;
/*     */   }
/*     */   
/*     */   public String getCpf() {
/*  55 */     return this.cpf;
/*     */   }
/*     */   
/*     */   public String getNome() {
/*  59 */     return this.nome;
/*     */   }
/*     */   
/*     */   public String getEmail() {
/*  63 */     return this.email;
/*     */   }
/*     */   
/*     */   public String getTelefone() {
/*  67 */     return this.telefone;
/*     */   }
/*     */   
/*     */   public void setLogin(String login) {
/*  71 */     this.login = login;
/*     */   }
/*     */   
/*     */   public void setCpf(String cpf) {
/*  75 */     this.cpf = cpf;
/*     */   }
/*     */   
/*     */   public void setNome(String nome) {
/*  79 */     this.nome = nome;
/*     */   }
/*     */   
/*     */   public void setEmail(String email) {
/*  83 */     this.email = email;
/*     */   }
/*     */   
/*     */   public void setTelefone(String telefone) {
/*  87 */     this.telefone = telefone;
/*     */   }
/*     */   
/*     */   public String getLinkRecuperacaoSenha() {
/*  91 */     return this.LinkRecuperacaoSenha;
/*     */   }
/*     */   
/*     */   public void setLinkRecuperacaoSenha(String linkRecuperacaoSenha) {
/*  95 */     this.LinkRecuperacaoSenha = linkRecuperacaoSenha;
/*     */   }
/*     */   
/*     */   public String getUltimoNome() {
/*  99 */     return this.ultimoNome;
/*     */   }
/*     */   
/*     */   public void setUltimoNome(String ultimoNome) {
/* 103 */     this.ultimoNome = ultimoNome;
/*     */   }
/*     */   
/*     */   public String getSituacao() {
/* 107 */     return this.situacao;
/*     */   }
/*     */   
/*     */   public void setSituacao(String situacao) {
/* 111 */     this.situacao = situacao;
/*     */   }
/*     */   
/*     */   public String getMatricula() {
/* 115 */     return this.matricula;
/*     */   }
/*     */   
/*     */   public void setMatricula(String matricula) {
/* 119 */     this.matricula = matricula;
/*     */   }
/*     */   
/*     */   public Boolean getFlagUsuarioServidor() {
/* 123 */     return this.flagUsuarioServidor;
/*     */   }
/*     */   
/*     */   public void setFlagUsuarioServidor(Boolean flagUsuarioServidor) {
/* 127 */     this.flagUsuarioServidor = flagUsuarioServidor;
/*     */   }
/*     */   
/*     */   public String getEmailGestor() {
/* 131 */     return this.emailGestor;
/*     */   }
/*     */   
/*     */   public void setEmailGestor(String emailGestor) {
/* 135 */     this.emailGestor = emailGestor;
/*     */   }
/*     */   
/*     */   public String getEmailSecundario() {
/* 139 */     return this.emailSecundario;
/*     */   }
/*     */   
/*     */   public void setEmailSecundario(String emailSecundario) {
/* 143 */     this.emailSecundario = emailSecundario;
/*     */   }
/*     */   
/*     */   public String getCelular() {
/* 147 */     return this.celular;
/*     */   }
/*     */   
/*     */   public void setCelular(String celular) {
/* 151 */     this.celular = celular;
/*     */   }
/*     */   
/*     */   public String getRegistroGeral() {
/* 155 */     return this.registroGeral;
/*     */   }
/*     */   
/*     */   public void setRegistroGeral(String registroGeral) {
/* 159 */     this.registroGeral = registroGeral;
/*     */   }
/*     */   
/*     */   public String getLinkDeRedirecionamento() {
/* 163 */     return this.linkDeRedirecionamento;
/*     */   }
/*     */   
/*     */   public void setLinkDeRedirecionamento(String linkDeRedirecionamento) {
/* 167 */     this.linkDeRedirecionamento = linkDeRedirecionamento;
/*     */   }
/*     */ }


/* Location:              C:\AmbienteDev\maven\.m2\repository\br\gov\prodemge\ssc\ssc-interfaces\1.2.20\ssc-interfaces-1.2.20\!\br\gov\prodemge\ssc\interfaces\dto\UsuarioDTO.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */