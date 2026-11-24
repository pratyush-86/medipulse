package com.medipulse.servlet;

import com.medipulse.dao.*;
import com.medipulse.exception.MediPulseException;
import com.medipulse.exception.ValidationException;
import com.medipulse.model.Appointment;
import com.medipulse.service.AnalyticsService;
import com.medipulse.service.AppointmentService;
import com.medipulse.service.NotificationService;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Admin: users, appointments, hospitals, settings, analytics. */
@WebServlet("/admin")
public class AdminServlet extends BaseServlet {
    private final UserDAO userDAO = new UserDAO();
    private final AppointmentDAO apptDAO = new AppointmentDAO();
    private final HospitalDAO hospitalDAO = new HospitalDAO();
    private final SettingDAO settingDAO = new SettingDAO();
    private final AnalyticsService analytics = new AnalyticsService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            List<Appointment> appointments = apptDAO.findAll();
            req.setAttribute("users", userDAO.findAll());
            req.setAttribute("appointments", appointments);
            req.setAttribute("counts", userDAO.countByRole());
            req.setAttribute("statusSummary", analytics.statusSummary(appointments));
            req.setAttribute("topDoctors", analytics.topDoctors(appointments, 5));
            req.setAttribute("hospitals", hospitalDAO.findAll());
            req.setAttribute("settings", settingDAO.getAll());
            req.setAttribute("activity", NotificationService.getInstance().recent());
        } catch (MediPulseException e) {
            flashErr(req, e.getMessage());
        }
        forward(req, res, "admin.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        try {
            switch (param(req, "action")) {
                case "addUser":
                    userDAO.register(param(req, "name"), param(req, "email"), req.getParameter("password"),
                                     param(req, "role"), param(req, "phone"), param(req, "specialization"));
                    flashOk(req, "User created");
                    break;
                case "deleteUser":
                    flashOk(req, userDAO.delete(intParam(req, "id")) ? "User deleted" : "Admin accounts cannot be deleted");
                    break;
                case "status":
                    AppointmentService.getInstance().changeStatus(intParam(req, "id"), param(req, "status"));
                    flashOk(req, "Appointment updated");
                    break;
                case "addHospital":
                    hospitalDAO.add(param(req, "name"), param(req, "city"), param(req, "address"),
                                    param(req, "phone"), param(req, "type"));
                    flashOk(req, "Hospital added");
                    break;
                case "deleteHospital":
                    hospitalDAO.delete(intParam(req, "id"));
                    flashOk(req, "Hospital removed");
                    break;
                case "saveSettings":
                    Map<String, String> s = new LinkedHashMap<>();
                    s.put("clinic_name", param(req, "clinic_name"));
                    s.put("slot_minutes", param(req, "slot_minutes"));
                    s.put("working_hours", param(req, "working_hours"));
                    settingDAO.save(s);
                    flashOk(req, "Settings saved");
                    break;
                default:
                    throw new ValidationException("Unknown action");
            }
        } catch (MediPulseException e) {
            flashErr(req, e.getMessage());
        }
        redirect(req, res, "/admin");
    }
}
