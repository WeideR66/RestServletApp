package dbconn;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnectionImpl implements DBConnection {
    private final String url;
    private final String username;
    private final String password;

    private DBConnectionImpl() {
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            Properties dbProperties = new Properties();
            dbProperties.load(loader.getResourceAsStream("db.properties"));
            this.url = dbProperties.getProperty("url", "jdbc:postgresql://localhost:5432/testDB");
            this.username = dbProperties.getProperty("username", "admin");
            this.password = dbProperties.getProperty("password", "1234567890");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private DBConnectionImpl(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public static DBConnectionImpl createConnectionFactory() {
        return new DBConnectionImpl();
    }

    public static DBConnectionImpl createConnectionFactory(String url, String username, String password) {
        return new DBConnectionImpl(url, username, password);
    }

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(this.url, this.username, this.password);
    }
}
