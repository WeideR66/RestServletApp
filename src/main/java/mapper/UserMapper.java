package mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import entities.Address;
import entities.BankAccount;
import entities.User;

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
            user.addBankAccount(new BankAccount(
                    resultSet.getLong("bankaccount_id"),
                    resultSet.getString("accountname"),
                    resultSet.getInt("cash")
            ));
        }
        return (user.getId() == null) ? null : user;
    }

    public static User getUserObjectFromJSON(String json) {
        try {
            return jsonMapper.readValue(json, User.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getJSONFromUserObject(User user) {
        try {
            return jsonMapper.writeValueAsString(user);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
