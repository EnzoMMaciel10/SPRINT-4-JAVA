package br.com.fiap.apoia.domain.service;

import br.com.fiap.apoia.domain.model.Consulta;
import br.com.fiap.apoia.domain.model.Lembrete;
import br.com.fiap.apoia.domain.model.Paciente;
import br.com.fiap.apoia.infrastructure.persistence.repository.JdbcConsultaRepository;
import br.com.fiap.apoia.infrastructure.persistence.repository.JdbcLembreteRepository;
import br.com.fiap.apoia.infrastructure.persistence.repository.JdbcPacienteRepository;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;

public class ApoiaService {
    private final JdbcPacienteRepository pacienteRepo = new JdbcPacienteRepository();
    private final JdbcConsultaRepository consultaRepo = new JdbcConsultaRepository();
    private final JdbcLembreteRepository lembreteRepo = new JdbcLembreteRepository();

    // use \s como \\s dentro da string Java
    private final Pattern email = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
    private final Pattern phone = Pattern.compile("^[0-9]{10,13}$");


    public boolean validarContato(String contato) {
        return email.matcher(contato).matches() || phone.matcher(contato).matches();
    }

    public boolean isAcessivelParaDigital(Paciente p) {
        return p != null && (p.isAcessivelParaDigital() || email.matcher(p.getContato()).matches());
    }

    public Consulta confirmarPresenca(Long consultaId) {
        Consulta c = consultaRepo.find(consultaId).orElseThrow(() -> new RuntimeException("Consulta não encontrada"));
        c.setStatus("CONFIRMADA");
        consultaRepo.update(c.getId(), c);
        return c;
    }

    public Consulta marcarAusente(Long consultaId) {
        Consulta c = consultaRepo.find(consultaId).orElseThrow(() -> new RuntimeException("Consulta não encontrada"));
        c.setStatus("AUSENTE");
        consultaRepo.update(c.getId(), c);
        return c;
    }

    public boolean isProxima(LocalDateTime dataHora, long minutos) {
        long diff = java.time.Duration.between(java.time.LocalDateTime.now(), dataHora).toMinutes();
        return diff <= minutos && diff >= 0;
    }

    public List<Lembrete> gerarLembreteAutomatico(Long consultaId) {
        Consulta c = consultaRepo.find(consultaId).orElseThrow(() -> new RuntimeException("Consulta não encontrada"));
        java.time.LocalDateTime t24 = c.getDataHora().minusHours(24);
        java.time.LocalDateTime t2 = c.getDataHora().minusHours(2);
        java.time.LocalDateTime t15 = c.getDataHora().minusMinutes(15);
        lembreteRepo.create(new Lembrete(null, consultaId, "T24H", t24, false));
        lembreteRepo.create(new Lembrete(null, consultaId, "T2H", t2, false));
        lembreteRepo.create(new Lembrete(null, consultaId, "T15MIN", t15, false));
        return lembreteRepo.listByConsulta(consultaId);
    }

    public JdbcPacienteRepository pacienteRepo() { return pacienteRepo; }
    public JdbcConsultaRepository consultaRepo() { return consultaRepo; }
    public JdbcLembreteRepository lembreteRepo() { return lembreteRepo; }
}
