package dbconn;

import java.sql.SQLException;
import java.sql.Statement;

public class TableCreation {
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
}
