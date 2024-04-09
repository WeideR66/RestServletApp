package dao;

import dbconn.DBConnection;
import dbconn.DBConnectionImpl;
import entities.User;
import mapper.UserMapper;

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

            try (ResultSet userID = statement.getGeneratedKeys()) {
                if (userID.next()) {
                    user.setId(userID.getLong("id"));
                } else {
                    throw new SQLException("No User ID obtained.");
                }
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
        try (
                Connection connection = dbConnection.getConnection();
                Statement statement = connection.createStatement();
        ) {
            String sql = String.format("select u.id, u.name, u.surname, u.age, a.id as address_id, a.country, a.city, a.street, a.number, b.id as bankaccount_id, b.accountname, b.cash " +
                            "from users as u " +
                            "inner join addresses as a on u.id = a.user_id " +
                            "inner join bankAccounts as b on u.id = b.user_id " +
                            "where u.id = %d",
                    id);

            try (ResultSet result = statement.executeQuery(sql)) {
                return UserMapper.getUserObjectFromResultSet(result);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User updateUser(User user) {
        if (user.getId() == null) {
            return null;
        }
        try (
                Connection connection = dbConnection.getConnection();
                Statement statement = connection.createStatement();
        ) {
            String sql = String.format(
                    "update users " +
                            "set name = '%s'," +
                            "surname = '%s'," +
                            "age = %d " +
                            "where id = %d",
                    user.getName(),
                    user.getSurname(),
                    user.getAge(),
                    user.getId()
            );

            statement.execute(sql);

            addressDAO.updateAddress(user, statement);

            bankAccountDAO.updateBankAccounts(user, statement);

            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteUser(long id) {
        try (
                Connection connection = dbConnection.getConnection();
                Statement statement = connection.createStatement();
        ) {
            String sql = String.format("delete from users where id = %d", id);
            statement.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
