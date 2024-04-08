package dao;

import entities.User;

public interface UserDAO {
    User createUser(User user);
    User getUserById(long id);
    User updateUser(long id, User user);
    boolean deleteUser(long id);
}
