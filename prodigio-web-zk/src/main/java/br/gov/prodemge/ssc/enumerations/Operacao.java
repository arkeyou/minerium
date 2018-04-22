/*    */ package br.gov.prodemge.ssc.enumerations;
/*    */ 
/*    */ public enum Operacao {
/*  4 */   ABRIR("Abrir"),  ALTERAR("Alterar"),  APROVAR("Aprovar"),  CLONAR("Clonar"),  CONCLUIR("Concluir"),  CRIAR("Criar"),  EDITAR("Editar"),  EXCLUIR("Excluir"),  FILTRAR("Filtrar"),  INCLUIR("Incluir"),  IMPRIMIR("Imprimir"),  NOVO("Novo"),  PESQUISAR(
/*  5 */     "Pesquisar"),  SALVAR("Salvar"),  VER("Ver"),  AVANCAR("Avançar"),  VOLTAR("Voltar"),  AUDITAR("Auditar");
/*    */   
/*    */   private String descricao;
/*    */   
/*    */   private Operacao(String descricao) {
/* 10 */     this.descricao = descricao;
/*    */   }
/*    */   
/*    */   public String getDescricao() {
/* 14 */     return this.descricao;
/*    */   }
/*    */   
/*    */   public void setDescricao(String descricao) {
/* 18 */     this.descricao = descricao;
/*    */   }
/*    */ }


/* Location:              C:\AmbienteDev\maven\.m2\repository\br\gov\prodemge\ssc\ssc-interfaces\1.2.20\ssc-interfaces-1.2.20\!\br\gov\prodemge\ssc\enumerations\Operacao.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */