package com.medipulse.servlet;

import com.medipulse.dao.*;
import com.medipulse.exception.MediPulseException;
import com.medipulse.exception.ValidationException;
import com.medipulse.model.User;
import com.medipulse.service.AppointmentService;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Patient: booking, history, medical records, feedback, nearby hospitals. */
@WebServlet("/patient")
public class PatientServlet extends BaseServlet {
    private final UserDAO userDAO = new UserDAO();
    private final AppointmentDAO apptDAO = new AppointmentDAO();
    private final RecordDAO recordDAO = new RecordDAO();
    private final FeedbackDAO feedbackDAO = new FeedbackDAO();
    private final HospitalDAO hospitalDAO = new HospitalDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        User u = currentUser(req);
        try {
            String q = param(req, "city");
            req.setAttribute("q", q);
            req.setAttribute("doctors", userDAO.findByRole("DOCTOR"));
            req.setAttribute("appointments", apptDAO.findByPatient(u.getId()));
            req.setAttribute("records", recordDAO.findByPatient(u.getId()));
            req.setAttribute("hospitals", hospitalDAO.search(q));
        } catch (MediPulseException e) {
            flashErr(req, e.getMessage());
        }
        forward(req, res, "patient.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        User u = currentUser(req);
        try {
            switch (param(req, "action")) {
                case "book":
                    AppointmentService.getInstance().book(u.getId(), intParam(req, "doctorId"),
                            param(req, "date"), param(req, "time"), param(req, "notes"));
                    flashOk(req, "Appointment booked. Waiting for doctor confirmation.");
                    break;
                case "cancel":
                    AppointmentService.getInstance().changeStatus(intParam(req, "id"), "CANCELLED");
                    flashOk(req, "Appointment cancelled");
                    break;
                case "feedback":
                    feedbackDAO.add(u.getId(), intParam(req, "doctorId"), intParam(req, "rating"), param(req, "comments"));
                    flashOk(req, "Thanks for your feedback");
                    break;
                default:
                    throw new ValidationException("Unknown action");
            }
        } catch (MediPulseException e) {
            flashErr(req, e.getMessage());
        }
        redirect(req, res, "/patient");
    }
}
