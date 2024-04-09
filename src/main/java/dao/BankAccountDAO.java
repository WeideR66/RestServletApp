package dao;

import entities.User;

import java.sql.SQLException;
import java.sql.Statement;

public interface BankAccountDAO {
    void createBankAccounts(User user, Statement statement) throws SQLException;
    void updateBankAccounts(User user, Statement statement) throws SQLException;
}
