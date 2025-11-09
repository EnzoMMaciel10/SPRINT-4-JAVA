package br.com.fiap.apoia.infrastructure.persistence.repository;

import br.com.fiap.apoia.infrastructure.Database;
import javax.sql.DataSource;

public abstract class JdbcRepository {
    protected DataSource ds() { return Database.dataSource(); }
}
