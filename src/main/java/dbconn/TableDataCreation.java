package dbconn;

import dao.UserDAO;
import dao.UserDAOImpl;
import entities.Address;
import entities.BankAccount;
import entities.User;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

public class TableDataCreation {
    public static void createUserTable(Statement statement) throws SQLException {
        statement.execute("create table if not exists users (" +
                "id bigserial primary key," +
                "name varchar(40) not null," +
                "surname varchar(40) not null," +
                "age integer not null)");
        System.out.println("Table Users created");
    }

    public static void dropUserTable(Statement statement) throws SQLException {
        statement.execute("drop table if exists users");
        System.out.println("Table Users dropped");
    }

    public static void createAddressTable(Statement statement) throws SQLException {
        statement.execute("create table if not exists addresses (" +
                "id bigserial primary key," +
                "country varchar(40) not null," +
                "city varchar(40) not null," +
                "street varchar(40) not null," +
                "number integer not null," +
                "user_id bigint," +
                "foreign key (user_id) references users (id) on delete cascade)");
        System.out.println("Table Address created");
    }

    public static void dropAddressTable(Statement statement) throws SQLException {
        statement.execute("drop table if exists addresses");
        System.out.println("Table Address dropped");
    }

    public static void createBankAccountsTable(Statement statement) throws SQLException {
        statement.execute("create table if not exists bankAccounts (" +
                "id bigserial primary key," +
                "accountName varchar(40) not null," +
                "cash integer not null," +
                "user_id bigint," +
                "foreign key (user_id) references users(id) on delete cascade)");
        System.out.println("Table bankAccounts created");
    }

    public static void dropBankAccountsTable(Statement statement) throws SQLException {
        statement.execute("drop table if exists bankAccounts");
        System.out.println("Table bankAccounts dropped");
    }

    public static void createTestUsers() {
        UserDAO userDAO = new UserDAOImpl();
        User user1 = new User(
                null,
                "Вася",
                "Пупкин",
                21,
                new Address(
                        null,
                        "Россия",
                        "Москва",
                        "Пушкина",
                        6
                ),
                Arrays.asList(
                        new BankAccount(null, "Счет№1", 100),
                        new BankAccount(null, "Счет№2", 5736),
                        new BankAccount(null, "Счет№3", 41235)
                )
        );
        User user2 = new User(
                null,
                "Андрей",
                "Тушенкин",
                22,
                new Address(
                        null,
                        "Россия",
                        "Москва",
                        "Тургенева",
                        54
                ),
                Arrays.asList(
                        new BankAccount(null, "Золотой Счет", 123455),
                        new BankAccount(null, "Платиновый Счет", 173542)
                )
        );
        User user3 = new User(
                null,
                "Игорь",
                "Бесчетный",
                23,
                new Address(
                        null,
                        "Россия",
                        "Омск",
                        "Банковская",
                        89
                ),
                null
        );

        try {
            userDAO.createUser(user1);
            userDAO.createUser(user2);
            userDAO.createUser(user3);
            System.out.println("Test Users created");
        } catch (SQLException ignored) {

        }
    }
}
