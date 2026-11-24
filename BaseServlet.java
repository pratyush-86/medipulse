package com.medipulse.servlet;

import com.medipulse.exception.ValidationException;
import com.medipulse.model.User;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Parent of all servlets: shared helpers (inheritance). */
public abstract class BaseServlet extends HttpServlet {

    protected User currentUser(HttpServletRequest req) {
        return (User) req.getSession().getAttribute("user");
    }

    protected void flashOk(HttpServletRequest req, String msg) {
        req.getSession().setAttribute("flashOk", msg);
    }

    protected void flashErr(HttpServletRequest req, String msg) {
        req.getSession().setAttribute("flashErr", msg);
    }

    protected int intParam(HttpServletRequest req, String name) throws ValidationException {
        try {
            return Integer.parseInt(req.getParameter(name));
        } catch (NumberFormatException e) {
            throw new ValidationException("Invalid value for " + name);
        }
    }

    protected String param(HttpServletRequest req, String name) {
        String v = req.getParameter(name);
        return v == null ? "" : v.trim();
    }

    protected void forward(HttpServletRequest req, HttpServletResponse res, String view)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/" + view).forward(req, res);
    }

    protected void redirect(HttpServletRequest req, HttpServletResponse res, String path) throws IOException {
        res.sendRedirect(req.getContextPath() + path);
    }
}
