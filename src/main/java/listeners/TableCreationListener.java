package listeners;

import dbconn.DBConnectionImpl;
import dbconn.TableDataCreation;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@WebListener
public class TableCreationListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try (
                Connection connection = DBConnectionImpl.createConnectionFactory().getConnection();
                Statement statement = connection.createStatement();
        ) {
            TableDataCreation.createUserTable(statement);
            TableDataCreation.createAddressTable(statement);
            TableDataCreation.createBankAccountsTable(statement);
            TableDataCreation.createTestUsers();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try (
                Connection connection = DBConnectionImpl.createConnectionFactory().getConnection();
                Statement statement = connection.createStatement();
        ) {
            TableDataCreation.dropBankAccountsTable(statement);
            TableDataCreation.dropAddressTable(statement);
            TableDataCreation.dropUserTable(statement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
