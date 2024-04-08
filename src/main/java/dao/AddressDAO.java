package dao;

import entities.Address;
import entities.User;

import java.sql.SQLException;
import java.sql.Statement;

public interface AddressDAO {
    void createAddress(User user, Statement statement) throws SQLException;
}
