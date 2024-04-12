package servlets;

import dao.UserDAO;
import dao.UserDAOImpl;
import entities.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mapper.UserMapper;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "userServlet", urlPatterns = {"/api/user"})
public class UserServlet extends HttpServlet {

    private UserDAO userDAO = new UserDAOImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();

        long userId;
        try {
            userId = Long.parseLong(req.getParameter("id"));
        } catch (NumberFormatException e) {
            resp.setStatus(400);
            out.print("{\"error\": \"Отсутствует Id пользователя\"}");
            out.flush();
            return;
        }

        User user = userDAO.getUserById(userId);
        if (user == null) {
            resp.setStatus(404);
            out.print("{\"error\": \"Пользователь не найден\"}");
            out.flush();
            return;
        }

        try {
            UserMapper.writeJSONFromUserObject(out, user);
            out.flush();
        } catch (IOException e) {
            resp.setStatus(500);
            out.print("{\"error\": \"Ошибка сериализации JSON, ИЗВИНИТЕ!!!\"}");
            out.flush();
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();

        User user;
        try {
            user = UserMapper.getUserObjectFromJSON(req.getReader());
            userDAO.createUser(user);
            UserMapper.writeJSONFromUserObject(out, user);
            out.flush();
        } catch (IOException e) {
            resp.setStatus(400);
            out.print("{\"error\": \"Неверный формат пользователя\"}");
            out.flush();
        }

    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getMethod();
        if (!method.equals("PATCH")) {
            super.service(req, resp);
            return;
        }
        this.doPatch(req, resp);
    }

    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();

        User user;
        try {
            user = UserMapper.getUserObjectFromJSON(req.getReader());
            userDAO.updateUser(user);
            UserMapper.writeJSONFromUserObject(out, user);
            out.flush();
        } catch (IOException e) {
            resp.setStatus(400);
            out.print("{\"error\": \"Неверный формат пользователя\"}");
            out.flush();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();

        long userId;
        try {
            userId = Long.parseLong(req.getParameter("id"));
        } catch (NumberFormatException e) {
            resp.setStatus(400);
            out.print("{\"error\": \"Отсутствует Id пользователя\"}");
            out.flush();
            return;
        }

        boolean result = userDAO.deleteUser(userId);
        if (result) {
            resp.setStatus(200);
            out.print("{\"message\": \"Пользователь удален\"}");
            out.flush();
        } else {
            resp.setStatus(404);
            out.print("{\"error\": \"Пользователь отсутствует\"}");
            out.flush();
        }
    }
}
