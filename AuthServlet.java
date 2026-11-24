package com.medipulse.servlet;

import com.medipulse.dao.UserDAO;
import com.medipulse.exception.MediPulseException;
import com.medipulse.exception.ValidationException;
import com.medipulse.model.User;
import com.medipulse.service.NotificationService;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Login, patient registration, forgot password and logout. */
@WebServlet({"", "/login", "/register", "/forgot", "/logout"})
public class AuthServlet extends BaseServlet {
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        switch (req.getServletPath()) {
            case "/logout":
                req.getSession().invalidate();
                redirect(req, res, "/login");
                break;
            case "/register":
                forward(req, res, "register.jsp");
                break;
            case "/forgot":
                forward(req, res, "forgot.jsp");
                break;
            default:
                forward(req, res, "login.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String path = req.getServletPath();
        try {
            if (path.equals("/register")) {
                userDAO.register(param(req, "name"), param(req, "email"), req.getParameter("password"),
                                 "PATIENT", param(req, "phone"), null);
                flashOk(req, "Account created. Please sign in.");
                redirect(req, res, "/login");
            } else if (path.equals("/forgot")) {
                String pw = req.getParameter("password");
                if (pw == null || !pw.equals(req.getParameter("confirm"))) {
                    throw new ValidationException("Passwords do not match");
                }
                userDAO.resetPassword(param(req, "email"), pw);
                flashOk(req, "Password updated. Please sign in.");
                redirect(req, res, "/login");
            } else {
                User user = userDAO.login(param(req, "email"), req.getParameter("password"));
                req.getSession().setAttribute("user", user);
                NotificationService.getInstance().notifyAsync(user.getName() + " signed in as " + user.getRole());
                redirect(req, res, user.getDashboardPath());   // polymorphic: each role has its own path
            }
        } catch (MediPulseException e) {
            req.setAttribute("error", e.getMessage());
            forward(req, res, path.equals("/register") ? "register.jsp"
                              : path.equals("/forgot") ? "forgot.jsp" : "login.jsp");
        }
    }
}
