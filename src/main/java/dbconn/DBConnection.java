package dbconn;

import java.sql.Connection;
import java.sql.SQLException;

public interface DBConnection {
    Connection getConnection() throws SQLException;
}
