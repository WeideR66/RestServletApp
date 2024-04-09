package dao;

import entities.User;

public interface UserDAO {
    User createUser(User user);
    User getUserById(long id);
    User updateUser(User user);
    void deleteUser(long id);
}
