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
    private final UserMapper userMapper;

    public UserDAOImpl() {
        this.dbConnection = DBConnectionImpl.createConnectionFactory();
        this.addressDAO = new AddressDAOImpl();
        this.bankAccountDAO = new BankAccountDAOImpl();
        this.userMapper = new UserMapper();
    }

    @Override
    public void createUser(User user) throws SQLException {
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
        }
    }

    @Override
    public User getUserById(long id) throws SQLException {
        try (
                Connection connection = dbConnection.getConnection();
                Statement statement = connection.createStatement();
        ) {
            String sql = String.format("select u.id, u.name, u.surname, u.age, a.id as address_id, " +
                            "a.country, a.city, a.street, a.number, b.id as bankaccount_id, b.accountname, b.cash " +
                            "from users as u " +
                            "left join addresses as a on u.id = a.user_id " +
                            "left join bankAccounts as b on u.id = b.user_id " +
                            "where u.id = %d",
                    id);

            try (ResultSet result = statement.executeQuery(sql)) {
                return userMapper.getUserObjectFromResultSet(result);
            }
        }
    }

    @Override
    public User updateUser(User user) throws SQLException {
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
        }
    }

    @Override
    public boolean deleteUser(long id) throws SQLException {
        if (getUserById(id) == null) {
            return false;
        }
        try (
                Connection connection = dbConnection.getConnection();
                Statement statement = connection.createStatement();
        ) {
            String sql = String.format("delete from users where id = %d", id);
            statement.execute(sql);
            return true;
        }
    }
}
