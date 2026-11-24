package com.medipulse.servlet;

import com.medipulse.dao.UserDAO;
import com.medipulse.exception.MediPulseException;
import com.medipulse.model.User;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Profile management for every role. */
@WebServlet("/profile")
public class ProfileServlet extends BaseServlet {
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        forward(req, res, "profile.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        User u = currentUser(req);
        try {
            userDAO.updateProfile(u.getId(), param(req, "name"), param(req, "phone"));
            String newPw = req.getParameter("password");
            if (newPw != null && !newPw.isEmpty()) {
                userDAO.changePassword(u.getId(), newPw);
            }
            req.getSession().setAttribute("user", userDAO.findById(u.getId()));
            flashOk(req, "Profile updated successfully");
        } catch (MediPulseException e) {
            flashErr(req, e.getMessage());
        }
        redirect(req, res, "/profile");
    }
}
