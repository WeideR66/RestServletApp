package dao;

import dbconn.DBConnection;
import dbconn.DBConnectionImpl;
import entities.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserDAOImpl implements UserDAO {

    private final DBConnection dbConnection;
    private final AddressDAO addressDAO;
    private final BankAccountDAO bankAccountDAO;

    public UserDAOImpl() {
        this.dbConnection = DBConnectionImpl.createFactory();
        this.addressDAO = new AddressDAOImpl();
        this.bankAccountDAO = new BankAccountDAOImpl();
    }

    @Override
    public User createUser(User user) {
        try (
                Connection connection = dbConnection.getConnection();
                Statement statement = connection.createStatement();
        ) {
            String sql = String.format("insert into users (name, surname, age)" +
                    "values ('%s', '%s', '%d')",
                    user.getName(),
                    user.getSurname(),
                    user.getAge());

            int affectedRow = statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            if (affectedRow == 0) {
                throw new SQLException("Failed User creation.");
            }

            ResultSet userID = statement.getGeneratedKeys();
            if (userID.next()) {
                user.setId(userID.getLong("id"));
            } else {
                throw new SQLException("No User ID obtained.");
            }

            addressDAO.createAddress(user, statement);

            bankAccountDAO.createBankAccounts(user, statement);

            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User getUserById(long id) {
        return null;
    }

    @Override
    public User updateUser(long id, User user) {
        return null;
    }

    @Override
    public boolean deleteUser(long id) {
        return false;
    }
}
