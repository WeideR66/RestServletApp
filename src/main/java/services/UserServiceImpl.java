package services;

import dao.UserDAO;
import dao.UserDAOImpl;
import entities.User;
import exceptions.NoSuchUserException;
import mapper.UserMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

public class UserServiceImpl implements UserService {

    private final UserDAO userDAO = new UserDAOImpl();
    private final UserMapper userMapper = new UserMapper();

    @Override
    public void getUser(long user_id, PrintWriter out) throws SQLException, IOException {
        User user = userDAO.getUserById(user_id);
        if (user == null) {
            throw new NoSuchUserException(String.format("Пользователь с ID - %d отсутствует", user_id));
        }
        userMapper.writeJSONFromUserObject(out, user);
    }

    @Override
    public void createUser(BufferedReader in, PrintWriter out) throws SQLException, IOException {
        User user = userMapper.getUserObjectFromJSON(in);
        userDAO.createUser(user);
        userMapper.writeJSONFromUserObject(out, user);
    }

    @Override
    public void updateUser(BufferedReader in, PrintWriter out) throws SQLException, IOException {
        User user = userMapper.getUserObjectFromJSON(in);
        if (userDAO.getUserById(user.getId()) == null) {
            throw new NoSuchUserException(String.format("Пользователь с ID - %d отсутствует", user.getId()));
        }
        userDAO.updateUser(user);
        userMapper.writeJSONFromUserObject(out, user);
    }

    @Override
    public void deleteUser(long user_id, PrintWriter out) throws SQLException, IOException {
        boolean result = userDAO.deleteUser(user_id);
        if (result) {
            out.print(String.format("{\"success\":\"Пользователь с ID - %d удален.\"}", user_id));
        } else {
            throw new NoSuchUserException(String.format("Пользователь с ID - %d отсутствует", user_id));
        }
    }
}
