package br.com.fiap.apoia.infrastructure.persistence.repository;

import br.com.fiap.apoia.domain.model.Lembrete;
import java.sql.*;
import java.util.*;

public class JdbcLembreteRepository extends JdbcRepository {

    public java.util.List<Lembrete> listByConsulta(Long consultaId) {
        java.util.List<Lembrete> list = new java.util.ArrayList<>();
        try (Connection c = ds().getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT ID,CONSULTA_ID,TIPO,AGENDAMENTO,ENVIADO FROM LEMBRETE WHERE CONSULTA_ID=? ORDER BY AGENDAMENTO")) {
            ps.setLong(1, consultaId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) { throw new RuntimeException(e); }
        return list;
    }

    public Lembrete create(Lembrete x) {
        try (Connection c = ds().getConnection();
             PreparedStatement ps = c.prepareStatement(
                "INSERT INTO LEMBRETE(CONSULTA_ID,TIPO,AGENDAMENTO,ENVIADO) VALUES (?,?,?,?)",
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, x.getConsultaId());
            ps.setString(2, x.getTipo());
            ps.setTimestamp(3, Timestamp.valueOf(x.getAgendamento()));
            ps.setBoolean(4, x.isEnviado());
            ps.executeUpdate();
            ResultSet k = ps.getGeneratedKeys();
            if (k.next()) x.setId(k.getLong(1));
            return x;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public boolean markSent(Long id) {
        try (Connection c = ds().getConnection();
             PreparedStatement ps = c.prepareStatement("UPDATE LEMBRETE SET ENVIADO=TRUE WHERE ID=?")) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    private Lembrete map(ResultSet rs) throws SQLException {
        return new Lembrete(
            rs.getLong("ID"),
            rs.getLong("CONSULTA_ID"),
            rs.getString("TIPO"),
            rs.getTimestamp("AGENDAMENTO").toLocalDateTime(),
            rs.getBoolean("ENVIADO")
        );
    }
}
