package br.com.fiap.apoia.infrastructure.persistence.repository;

import br.com.fiap.apoia.domain.model.Paciente;
import java.sql.*;
import java.util.*;

public class JdbcPacienteRepository extends JdbcRepository {

    public List<Paciente> findAll() {
        List<Paciente> list = new ArrayList<>();
        try (Connection c = ds().getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT ID,NOME,CONTATO,ACESSIVEL_DIGITAL,ATIVO FROM PACIENTE")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) { throw new RuntimeException(e); }
        return list;
    }

    public Optional<Paciente> findById(Long id) {
        try (Connection c = ds().getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT ID,NOME,CONTATO,ACESSIVEL_DIGITAL,ATIVO FROM PACIENTE WHERE ID=?")) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(map(rs));
            return Optional.empty();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public Paciente create(Paciente p) {
        try (Connection c = ds().getConnection();
             PreparedStatement ps = c.prepareStatement("INSERT INTO PACIENTE(NOME,CONTATO,ACESSIVEL_DIGITAL,ATIVO) VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getNome());
            ps.setString(2, p.getContato());
            ps.setBoolean(3, p.isAcessivelParaDigital());
            ps.setBoolean(4, p.isAtivo());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) p.setId(keys.getLong(1));
            return p;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public boolean update(Long id, Paciente p) {
        try (Connection c = ds().getConnection();
             PreparedStatement ps = c.prepareStatement("UPDATE PACIENTE SET NOME=?,CONTATO=?,ACESSIVEL_DIGITAL=?,ATIVO=? WHERE ID=?")) {
            ps.setString(1, p.getNome());
            ps.setString(2, p.getContato());
            ps.setBoolean(3, p.isAcessivelParaDigital());
            ps.setBoolean(4, p.isAtivo());
            ps.setLong(5, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public boolean delete(Long id) {
        try (Connection c = ds().getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM PACIENTE WHERE ID=?")) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    private Paciente map(ResultSet rs) throws SQLException {
        return new Paciente(
            rs.getLong("ID"),
            rs.getString("NOME"),
            rs.getString("CONTATO"),
            rs.getBoolean("ACESSIVEL_DIGITAL"),
            rs.getBoolean("ATIVO")
        );
    }
}
