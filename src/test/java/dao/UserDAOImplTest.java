package dao;

import dbconn.DBConnection;
import dbconn.DBConnectionImpl;
import entities.Address;
import entities.BankAccount;
import entities.User;
import org.junit.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

import static org.junit.Assert.*;

public class UserDAOImplTest {

    private final DBConnection dbConnection = DBConnectionImpl.createFactory();
    private final UserDAO userDAO = new UserDAOImpl();

    @BeforeClass
    public static void setUp() throws Exception {
        try (
                Connection connection = DBConnectionImpl.createFactory().getConnection();
                Statement statement = connection.createStatement();
        ) {
            createUserTable(statement);
            createAddressTable(statement);
            createBankAccountsTable(statement);
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    @Test
    public void whenUseCreateUserThenUserAddInDB() {
        User user = new User(
                null,
                "Вася",
                "Пупкин",
                22,
                new Address(null, "Россия", "Москва", "Пушкина", 123),
                Arrays.asList(
                        new BankAccount(null, "Счет1", 12345),
                        new BankAccount(null, "Счет2", 1234567)
                )
        );
        User expectedUser = userDAO.createUser(user);

        try (
                Connection connection = dbConnection.getConnection();
                Statement statement = connection.createStatement();
        ) {
            String sql = String.format("select u.id, u.name, u.surname, u.age, a.country, a.city, a.street, a.number, b.accountname, b.cash " +
                            "from users as u " +
                            "inner join addresses as a on u.id = a.user_id " +
                            "inner join bankAccounts as b on u.id = b.user_id " +
                            "where u.id = %d",
                    user.getId());

            ResultSet result = statement.executeQuery(sql);
            result.next();
            assertEquals(expectedUser.getId(), Long.valueOf(result.getLong("id")));
            assertEquals(expectedUser.getName(), result.getString("name"));
            assertEquals(expectedUser.getSurname(), result.getString("surname"));
            assertEquals(expectedUser.getAge(), Integer.valueOf(result.getInt("age")));
            assertEquals(expectedUser.getAddress().getCountry(), result.getString("country"));
            assertEquals(expectedUser.getAddress().getCity(), result.getString("city"));
            assertEquals(expectedUser.getAddress().getStreet(), result.getString("street"));
            assertEquals(expectedUser.getAddress().getNum(), Integer.valueOf(result.getInt("number")));
            assertEquals(expectedUser.getBankAccounts().getFirst().getAccountName(), result.getString("accountname"));
            assertEquals(expectedUser.getBankAccounts().getFirst().getCash(), Integer.valueOf(result.getInt("cash")));
            System.out.println(user);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterClass
    public static void tearDown() throws Exception {
        try (
                Connection connection = DBConnectionImpl.createFactory().getConnection();
                Statement statement = connection.createStatement();
        ) {
            dropBankAccountsTable(statement);
            dropAddressTable(statement);
            dropUserTable(statement);
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    private static void createUserTable(Statement statement) throws SQLException {
        statement.execute("create table if not exists users (" +
                "id bigserial primary key," +
                "name varchar(40)," +
                "surname varchar(40)," +
                "age integer)");
        System.out.println("Table Users crated");
    }

    private static void dropUserTable(Statement statement) throws SQLException {
        statement.execute("drop table if exists users");
        System.out.println("Table Users dropped");
    }

    private static void createAddressTable(Statement statement) throws SQLException {
        statement.execute("create table if not exists addresses (" +
                "id bigserial primary key," +
                "country varchar(40)," +
                "city varchar(40)," +
                "street varchar(40)," +
                "number integer," +
                "user_id bigint," +
                "foreign key (user_id) references users (id))");
        System.out.println("Table Address created");
    }

    private static void dropAddressTable(Statement statement) throws SQLException {
        statement.execute("drop table if exists addresses");
        System.out.println("Table Address dropped");
    }

    private static void createBankAccountsTable(Statement statement) throws SQLException {
        statement.execute("create table if not exists bankAccounts (" +
                "id bigserial primary key," +
                "accountName varchar(40)," +
                "cash integer," +
                "user_id bigint," +
                "foreign key (user_id) references users(id))");
        System.out.println("Table bankAccounts created");
    }

    private static void dropBankAccountsTable(Statement statement) throws SQLException {
        statement.execute("drop table if exists bankAccounts");
        System.out.println("Table bankAccounts dropped");
    }
}