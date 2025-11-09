package br.com.fiap.apoia.infrastructure;

import java.sql.*;
import java.util.Properties;
import javax.sql.DataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class Database {
    private static HikariDataSource ds;

    public static void init() {
        try {
            Properties p = new Properties();
            p.load(Database.class.getClassLoader().getResourceAsStream("application.properties"));
            String driver = p.getProperty("db.driver");
            String url = p.getProperty("db.url");
            String user = p.getProperty("db.user");
            String pass = p.getProperty("db.pass");
            Class.forName(driver);
            HikariConfig cfg = new HikariConfig();
            cfg.setJdbcUrl(url);
            cfg.setUsername(user);
            cfg.setPassword(pass);
            cfg.setMaximumPoolSize(5);
            ds = new HikariDataSource(cfg);
            bootstrap();
        } catch (Exception e) {
            throw new RuntimeException("Failed to init database: " + e.getMessage(), e);
        }
    }

    public static DataSource dataSource() {
        return ds;
    }

    private static void bootstrap() throws SQLException {
        try (Connection c = ds.getConnection()) {
            c.createStatement().execute(
                "CREATE TABLE IF NOT EXISTS PACIENTE (" +
                "ID IDENTITY PRIMARY KEY," +
                "NOME VARCHAR(120) NOT NULL," +
                "CONTATO VARCHAR(120) NOT NULL," +
                "ACESSIVEL_DIGITAL BOOLEAN NOT NULL," +
                "ATIVO BOOLEAN NOT NULL" +
                ");"
            );
            c.createStatement().execute(
                "CREATE TABLE IF NOT EXISTS CONSULTA (" +
                "ID IDENTITY PRIMARY KEY," +
                "PACIENTE_ID BIGINT NOT NULL," +
                "DATA_HORA TIMESTAMP NOT NULL," +
                "STATUS VARCHAR(20) NOT NULL," +
                "LINK_SALA VARCHAR(255) NOT NULL," +
                "FOREIGN KEY (PACIENTE_ID) REFERENCES PACIENTE(ID)" +
                ");"
            );
            c.createStatement().execute(
                "CREATE TABLE IF NOT EXISTS LEMBRETE (" +
                "ID IDENTITY PRIMARY KEY," +
                "CONSULTA_ID BIGINT NOT NULL," +
                "TIPO VARCHAR(20) NOT NULL," +
                "AGENDAMENTO TIMESTAMP NOT NULL," +
                "ENVIADO BOOLEAN NOT NULL," +
                "FOREIGN KEY (CONSULTA_ID) REFERENCES CONSULTA(ID)" +
                ");"
            );

            ResultSet rs = c.createStatement().executeQuery("SELECT COUNT(*) FROM PACIENTE");
            rs.next();
            if (rs.getInt(1) == 0) {
                c.createStatement().execute("INSERT INTO PACIENTE(NOME,CONTATO,ACESSIVEL_DIGITAL,ATIVO) VALUES " +
                        "('Dona Maria','11999999999',FALSE,TRUE)," +
                        "('Seu José','jose@example.com',TRUE,TRUE)");
                c.createStatement().execute("INSERT INTO CONSULTA(PACIENTE_ID,DATA_HORA,STATUS,LINK_SALA) VALUES " +
                        "(1, DATEADD('MINUTE', 180, CURRENT_TIMESTAMP),'AGENDADA','https://sala.ex/1')," +
                        "(2, DATEADD('DAY', 1, CURRENT_TIMESTAMP),'AGENDADA','https://sala.ex/2')");
            }
        }
    }
}
