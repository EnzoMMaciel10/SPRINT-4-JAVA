package br.com.fiap.apoia.domain.model;

import java.time.LocalDateTime;

public class Consulta {
    private Long id;
    private Long pacienteId;
    private LocalDateTime dataHora;
    private String status;
    private String linkSala;

    public Consulta() {}
    public Consulta(Long id, Long pacienteId, LocalDateTime dataHora, String status, String linkSala) {
        this.id=id; this.pacienteId=pacienteId; this.dataHora=dataHora; this.status=status; this.linkSala=linkSala;
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getPacienteId() { return pacienteId; }
    public void setPacienteId(Long pacienteId) { this.pacienteId = pacienteId; }
    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getLinkSala() { return linkSala; }
    public void setLinkSala(String linkSala) { this.linkSala = linkSala; }
}
