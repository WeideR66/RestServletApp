package dao;

import entities.Address;
import entities.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AddressDAOImpl implements AddressDAO {
    @Override
    public void createAddress(User user, Statement statement) throws SQLException {
        Address address = user.getAddress();
        String sql = String.format("insert into addresses (country, city, street, number, user_id)" +
                        "values ('%s', '%s', '%s', %d, %d)",
                address.getCountry(),
                address.getCity(),
                address.getStreet(),
                address.getNumber(),
                user.getId());

        int affectedRow = statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
        if (affectedRow == 0) {
            throw new SQLException("Failed Address creation.");
        }

        try (ResultSet addressID = statement.getGeneratedKeys()) {
            if (addressID.next()) {
                address.setId(addressID.getLong("id"));
            } else {
                throw new SQLException("No Address ID obtained.");
            }
        }
    }

    @Override
    public void updateAddress(User user, Statement statement) throws SQLException {
        Address address = user.getAddress();
        String sql = String.format("update addresses " +
                        "set country = '%s'," +
                        "city = '%s'," +
                        "street = '%s'," +
                        "number = %d " +
                        "where user_id = %d",
                address.getCountry(),
                address.getCity(),
                address.getStreet(),
                address.getNumber(),
                user.getId()
        );
        statement.execute(sql);
    }
}
