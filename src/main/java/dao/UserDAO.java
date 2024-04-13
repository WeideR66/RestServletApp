package dao;

import entities.User;

import java.sql.SQLException;

public interface UserDAO {
    void createUser(User user) throws SQLException;
    User getUserById(long id) throws SQLException;
    User updateUser(User user) throws SQLException;
    boolean deleteUser(long id) throws SQLException;
}
