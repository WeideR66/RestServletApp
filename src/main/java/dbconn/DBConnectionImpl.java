package dbconn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectionImpl implements DBConnection {
    private final String url;
    private final String username;
    private final String password;

    private DBConnectionImpl() {
        this.url = "jdbc:postgresql://localhost:5432/testDB";
        this.username = "admin";
        this.password = "1234567890";
    }

    private DBConnectionImpl(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public static DBConnectionImpl createFactory() {
        return new DBConnectionImpl();
    }

    public static DBConnectionImpl createFactory(String url, String username, String password) {
        return new DBConnectionImpl(url, username, password);
    }

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(this.url, this.username, this.password);
    }
}
