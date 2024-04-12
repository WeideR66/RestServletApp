package dao;

import entities.BankAccount;
import entities.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BankAccountDAOImpl implements BankAccountDAO {
    @Override
    public void createBankAccounts(User user, Statement statement) throws SQLException {
        String sql = "insert into bankAccounts (accountName, cash, user_id)" +
                "values ('%s', %d, %d)";
        if (user.getBankAccounts() == null) {
            return;
        }
        for (BankAccount account : user.getBankAccounts()) {
            int affectedRow = statement.executeUpdate(
                    sql.formatted(account.getAccountName(), account.getCash(), user.getId()),
                    Statement.RETURN_GENERATED_KEYS
            );
            if (affectedRow == 0) {
                throw new SQLException("Failed BankAccount creation.");
            }

            try (ResultSet accountID = statement.getGeneratedKeys()) {
                if (accountID.next()) {
                    account.setId(accountID.getLong("id"));
                } else {
                    throw new SQLException("No BankAccount ID obtained.");
                }
            }
        }
    }

    @Override
    public void updateBankAccounts(User user, Statement statement) throws SQLException {
        String sql = "update bankaccounts " +
                "set accountname = '%s'," +
                "cash = %d " +
                "where user_id = %d and id = %d";
        for (BankAccount account : user.getBankAccounts()) {
            statement.execute(sql.formatted(
                    account.getAccountName(),
                    account.getCash(),
                    user.getId(),
                    account.getId()
            ));
        }
    }
}
