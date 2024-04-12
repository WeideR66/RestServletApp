package dao;

import dbconn.DBConnection;
import dbconn.DBConnectionImpl;
import dbconn.TableCreation;
import entities.Address;
import entities.BankAccount;
import entities.User;
import org.junit.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

import static org.junit.Assert.*;

public class UserDAOImplTest {

    private final DBConnection dbConnection = DBConnectionImpl.createFactory();
    private final UserDAO userDAO = new UserDAOImpl();
    private User user;

    @BeforeClass
    public static void setUp() throws Exception {
        try (
                Connection connection = DBConnectionImpl.createFactory().getConnection();
                Statement statement = connection.createStatement();
        ) {
            TableCreation.createUserTable(statement);
            TableCreation.createAddressTable(statement);
            TableCreation.createBankAccountsTable(statement);
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    @Before
    public void setUpLocal() throws Exception {
        user = new User(
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
    }

    @Test
    public void whenUseCreateUserThenUserAddInDB() {
        userDAO.createUser(user);

        User actualUser = userDAO.getUserById(user.getId());

        assertEquals(user, actualUser);
    }

    @Test
    public void returnNullIfUserDoesNotExist() {
        User user = userDAO.getUserById(31L);
        assertNull(user);
    }

    @Test
    public void whenChangeUserFieldAndExecuteUpdateUserDataInDBChange() {
        userDAO.createUser(user);
        user.setName("Андрей");
        user.getAddress().setStreet("Заборная");
        user.getBankAccounts().getFirst().setAccountName("УльтраСчет");

        userDAO.updateUser(user);
        User updatedUser = userDAO.getUserById(user.getId());

        assertEquals(user, updatedUser);
    }

    @Test
    public void whenUserIsNotChangedThenUpdateUserDoNotChangeValuesInDB() {
        userDAO.createUser(user);

        User pseudoUpdatedUser = userDAO.updateUser(user);

        assertEquals(user, pseudoUpdatedUser);
    }

    @Test
    public void whenUserIdIsNullTHenUpdateUserReturnNull() {
        User updatedUser = userDAO.updateUser(user);

        assertNull(updatedUser);
    }

    @Test
    public void ifUserInDBTHenDeleteUserWillRemoveUserFromDB() {
        userDAO.createUser(user);

        assertEquals(userDAO.getUserById(user.getId()), user);

        userDAO.deleteUser(user.getId());

        assertNull(userDAO.getUserById(user.getId()));
    }

    @AfterClass
    public static void tearDown() throws Exception {
        try (
                Connection connection = DBConnectionImpl.createFactory().getConnection();
                Statement statement = connection.createStatement();
        ) {
            TableCreation.dropBankAccountsTable(statement);
            TableCreation.dropAddressTable(statement);
            TableCreation.dropUserTable(statement);
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }
}