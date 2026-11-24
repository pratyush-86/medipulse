package com.medipulse.servlet;

import com.medipulse.model.User;
import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;

/** Servlet Filter: only logged-in users with the right role can open a dashboard. */
@WebFilter({"/admin", "/doctor", "/patient", "/profile"})
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest rq, ServletResponse rs, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) rq;
        HttpServletResponse res = (HttpServletResponse) rs;
        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession(false);
        User user = (session == null) ? null : (User) session.getAttribute("user");
        String path = req.getServletPath();
        boolean allowed = user != null
                && (path.equals("/profile") || path.equals(user.getDashboardPath()));
        if (!allowed) {
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        chain.doFilter(rq, rs);
    }
}
