package mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
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
    private static final ObjectMapper jsonMapper = new ObjectMapper();

    public static User getUserObjectFromResultSet(ResultSet resultSet) throws SQLException {
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

    public static User getUserObjectFromJSON(String json) throws JsonProcessingException {
        return jsonMapper.readValue(json, User.class);
    }

    public static User getUserObjectFromJSON(BufferedReader reader) throws IOException {
        return jsonMapper.readValue(reader, User.class);
    }

    public static String getJSONFromUserObject(User user) throws JsonProcessingException {
        return jsonMapper.writeValueAsString(user);
    }

    public static void writeJSONFromUserObject(PrintWriter printWriter, User user) throws IOException {
        jsonMapper.writeValue(printWriter, user);
    }
}
