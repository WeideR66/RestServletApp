package services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

public interface UserService {
    void getUser(long user_id, PrintWriter out) throws SQLException, IOException;
    void createUser(BufferedReader in, PrintWriter out) throws SQLException, IOException;
    void updateUser(BufferedReader in, PrintWriter out) throws SQLException, IOException;
    void deleteUser(long user_id, PrintWriter out) throws SQLException, IOException;
}
