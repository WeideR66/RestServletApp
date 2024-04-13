package servlets;

import exceptions.NoSuchUserException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import services.UserService;
import services.UserServiceImpl;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet(name = "userServlet", urlPatterns = {"/api/user"})
public class UserServlet extends HttpServlet {

    private final UserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");

        long userId;
        try {
            userId = Long.parseLong(req.getParameter("id"));
        } catch (NumberFormatException e) {
            sendError(400, "Отсутствует ID пользователя", out, resp);
            return;
        }

        try {
            userService.getUser(userId, out);
        } catch (NoSuchUserException e1) {
            sendError(404, e1.getMessage(), out, resp);
        } catch (IOException | SQLException e2) {
            sendError(500, "Внутренняя ошибка сервера", out, resp);
        } finally {
            out.flush();
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");

        try {
            userService.createUser(req.getReader(), out);
        } catch (IOException e1) {
            sendError(400, "Неверный формат User JSON", out, resp);
        } catch (SQLException e2) {
            sendError(500, "Внутренняя ошибка сервера", out, resp);
        } finally {
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
        resp.setContentType("application/json");

        try {
            userService.updateUser(req.getReader(), out);
        } catch (NoSuchUserException e1) {
            sendError(404, e1.getMessage(), out, resp);
        } catch (IOException e2) {
            sendError(400, "Неверный формат User JSON", out, resp);
        } catch (SQLException e3) {
            sendError(500, "Внутренняя ошибка сервера", out, resp);
        } finally {
            out.flush();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");

        long userId;
        try {
            userId = Long.parseLong(req.getParameter("id"));
        } catch (NumberFormatException e) {
            sendError(400, "Отсутствует ID пользователя", out, resp);
            return;
        }

        try {
            userService.deleteUser(userId, out);
        } catch (NoSuchUserException e1) {
            sendError(404, e1.getMessage(), out, resp);
        } catch (SQLException | IOException e2) {
            sendError(500, "Внутренняя ошибка сервера", out, resp);
        } finally {
            out.flush();
        }
    }

    private void sendError(int status, String error, PrintWriter out, HttpServletResponse resp) {
        resp.setStatus(status);
        out.print(String.format("{\"error\":\"%s\"}", error));
    }
}
