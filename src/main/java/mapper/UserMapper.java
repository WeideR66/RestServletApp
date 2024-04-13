package mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import entities.Address;
import entities.BankAccount;
import entities.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper {
    private final ObjectMapper jsonMapper = new ObjectMapper();

    public User getUserObjectFromResultSet(ResultSet resultSet) throws SQLException {
        User user = new User();
        while (resultSet.next()) {
            if (user.getId() == null) {
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setSurname(resultSet.getString("surname"));
                user.setAge(resultSet.getInt("age"));
                user.setAddress(new Address(
                        resultSet.getLong("address_id"),
                        resultSet.getString("country"),
                        resultSet.getString("city"),
                        resultSet.getString("street"),
                        resultSet.getInt("number")
                ));
            }
            if (resultSet.getLong("bankaccount_id") != 0) {
                user.addBankAccount(new BankAccount(
                        resultSet.getLong("bankaccount_id"),
                        resultSet.getString("accountname"),
                        resultSet.getInt("cash")
                ));
            }
        }
        return (user.getId() == null) ? null : user;
    }

    public User getUserObjectFromJSON(BufferedReader reader) throws IOException {
        return jsonMapper.readValue(reader, User.class);
    }

    public void writeJSONFromUserObject(PrintWriter printWriter, User user) throws IOException {
        jsonMapper.writeValue(printWriter, user);
    }
}
