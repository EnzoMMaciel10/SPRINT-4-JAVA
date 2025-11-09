package br.com.fiap.apoia.domain.model;

import java.time.LocalDateTime;

public class Lembrete {
    private Long id;
    private Long consultaId;
    private String tipo;
    private LocalDateTime agendamento;
    private boolean enviado;

    public Lembrete() {}
    public Lembrete(Long id, Long consultaId, String tipo, LocalDateTime agendamento, boolean enviado) {
        this.id=id; this.consultaId=consultaId; this.tipo=tipo; this.agendamento=agendamento; this.enviado=enviado;
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getConsultaId() { return consultaId; }
    public void setConsultaId(Long consultaId) { this.consultaId = consultaId; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public LocalDateTime getAgendamento() { return agendamento; }
    public void setAgendamento(LocalDateTime agendamento) { this.agendamento = agendamento; }
    public boolean isEnviado() { return enviado; }
    public void setEnviado(boolean enviado) { this.enviado = enviado; }
}
