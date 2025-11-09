package br.com.fiap.apoia.infrastructure.persistence.repository;

import br.com.fiap.apoia.domain.model.Consulta;
import java.sql.*;
import java.util.*;

public class JdbcConsultaRepository extends JdbcRepository {

    public List<Consulta> list() {
        List<Consulta> list = new ArrayList<>();
        try (Connection c = ds().getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT ID,PACIENTE_ID,DATA_HORA,STATUS,LINK_SALA FROM CONSULTA ORDER BY DATA_HORA DESC")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) { throw new RuntimeException(e); }
        return list;
    }

    public Optional<Consulta> find(Long id) {
        try (Connection c = ds().getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT ID,PACIENTE_ID,DATA_HORA,STATUS,LINK_SALA FROM CONSULTA WHERE ID=?")) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(map(rs));
            return Optional.empty();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public Consulta create(Consulta x) {
        try (Connection c = ds().getConnection();
             PreparedStatement ps = c.prepareStatement(
                "INSERT INTO CONSULTA(PACIENTE_ID,DATA_HORA,STATUS,LINK_SALA) VALUES (?,?,?,?)",
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, x.getPacienteId());
            ps.setTimestamp(2, Timestamp.valueOf(x.getDataHora()));
            ps.setString(3, x.getStatus());
            ps.setString(4, x.getLinkSala());
            ps.executeUpdate();
            ResultSet k = ps.getGeneratedKeys();
            if (k.next()) x.setId(k.getLong(1));
            return x;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public boolean update(Long id, Consulta x) {
        try (Connection c = ds().getConnection();
             PreparedStatement ps = c.prepareStatement(
                "UPDATE CONSULTA SET PACIENTE_ID=?,DATA_HORA=?,STATUS=?,LINK_SALA=? WHERE ID=?")) {
            ps.setLong(1, x.getPacienteId());
            ps.setTimestamp(2, Timestamp.valueOf(x.getDataHora()));
            ps.setString(3, x.getStatus());
            ps.setString(4, x.getLinkSala());
            ps.setLong(5, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public boolean delete(Long id) {
        try (Connection c = ds().getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM CONSULTA WHERE ID=?")) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    private Consulta map(ResultSet rs) throws SQLException {
        return new Consulta(
            rs.getLong("ID"),
            rs.getLong("PACIENTE_ID"),
            rs.getTimestamp("DATA_HORA").toLocalDateTime(),
            rs.getString("STATUS"),
            rs.getString("LINK_SALA")
        );
    }
}
