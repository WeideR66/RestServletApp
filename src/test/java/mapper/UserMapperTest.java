package mapper;

import dao.UserDAO;
import dao.UserDAOImpl;
import dbconn.DBConnectionImpl;
import dbconn.TableDataCreation;
import entities.Address;
import entities.BankAccount;
import entities.User;
import org.junit.*;

import java.io.BufferedReader;
import java.sql.*;
import java.util.Arrays;

import static org.junit.Assert.*;

public class UserMapperTest {

    private UserMapper userMapper;

    @BeforeClass
    public static void setUp() throws Exception {
        try (
                Connection connection = DBConnectionImpl.createConnectionFactory().getConnection();
                Statement statement = connection.createStatement();
        ) {
            TableDataCreation.createUserTable(statement);
            TableDataCreation.createAddressTable(statement);
            TableDataCreation.createBankAccountsTable(statement);
            TableDataCreation.createTestUsers();
        }
    }

    @Before
    public void setUpLocal() throws Exception {
        userMapper = new UserMapper();
    }

    @Test
    public void whenGetResultSetOfUserFromDBThenMapperResultRightUser() throws Exception {
        User userInDB = new User(
                1L,
                "Вася",
                "Пупкин",
                21,
                new Address(
                        1L,
                        "Россия",
                        "Москва",
                        "Пушкина",
                        6
                ),
                Arrays.asList(
                        new BankAccount(1L, "Счет№1", 100),
                        new BankAccount(2L, "Счет№2", 5736),
                        new BankAccount(3L, "Счет№3", 41235)

                )
        );
        String sql = "select u.id, u.name, u.surname, u.age, a.id as address_id, " +
                "a.country, a.city, a.street, a.number, b.id as bankaccount_id, b.accountname, b.cash " +
                "from users as u " +
                "left join addresses as a on u.id = a.user_id " +
                "left join bankAccounts as b on u.id = b.user_id " +
                "where u.id = ?";
        try (
                Connection connection = DBConnectionImpl.createConnectionFactory().getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
                ) {
            statement.setLong(1, 1L);

            ResultSet resultSet = statement.executeQuery();
            User userFromMapper = userMapper.getUserObjectFromResultSet(resultSet);

            assertEquals(userInDB, userFromMapper);
        }
    }

    @Test
    public void whenUserIsNotInDBThenMapperReturnsNull() throws Exception {
        String sql = "select u.id, u.name, u.surname, u.age, a.id as address_id, " +
                "a.country, a.city, a.street, a.number, b.id as bankaccount_id, b.accountname, b.cash " +
                "from users as u " +
                "left join addresses as a on u.id = a.user_id " +
                "left join bankAccounts as b on u.id = b.user_id " +
                "where u.id = ?";
        try (
                Connection connection = DBConnectionImpl.createConnectionFactory().getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setLong(1, 123L);

            ResultSet resultSet = statement.executeQuery();
            User userFromMapper = userMapper.getUserObjectFromResultSet(resultSet);

            assertNull(userFromMapper);
        }
    }

    @Test
    public void whenUserDoNotHaveBankAccountsInDBThenMapperReturnsUserObjectWithNullBankAccountsField() throws Exception {
        String sql = "select u.id, u.name, u.surname, u.age, a.id as address_id, " +
                "a.country, a.city, a.street, a.number, b.id as bankaccount_id, b.accountname, b.cash " +
                "from users as u " +
                "left join addresses as a on u.id = a.user_id " +
                "left join bankAccounts as b on u.id = b.user_id " +
                "where u.id = ?";
        try (
                Connection connection = DBConnectionImpl.createConnectionFactory().getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setLong(1, 3L);

            ResultSet resultSet = statement.executeQuery();
            User userFromMapper = userMapper.getUserObjectFromResultSet(resultSet);

            assertNull(userFromMapper.getBankAccounts());
        }
    }

    @AfterClass
    public static void tearDown() throws Exception {
        try (
                Connection connection = DBConnectionImpl.createConnectionFactory().getConnection();
                Statement statement = connection.createStatement();
        ) {
            TableDataCreation.dropBankAccountsTable(statement);
            TableDataCreation.dropAddressTable(statement);
            TableDataCreation.dropUserTable(statement);
        }
    }
}