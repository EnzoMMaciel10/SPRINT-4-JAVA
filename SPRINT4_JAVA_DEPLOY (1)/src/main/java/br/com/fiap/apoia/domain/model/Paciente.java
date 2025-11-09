package br.com.fiap.apoia.domain.model;

public class Paciente {
    private Long id;
    private String nome;
    private String contato;
    private boolean acessivelParaDigital;
    private boolean ativo;

    public Paciente() {}
    public Paciente(Long id, String nome, String contato, boolean acessivelParaDigital, boolean ativo) {
        this.id = id; this.nome = nome; this.contato = contato; this.acessivelParaDigital = acessivelParaDigital; this.ativo = ativo;
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getContato() { return contato; }
    public void setContato(String contato) { this.contato = contato; }
    public boolean isAcessivelParaDigital() { return acessivelParaDigital; }
    public void setAcessivelParaDigital(boolean v) { this.acessivelParaDigital = v; }
    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
}
