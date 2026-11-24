package com.medipulse.servlet;

import com.medipulse.dao.*;
import com.medipulse.exception.MediPulseException;
import com.medipulse.exception.ValidationException;
import com.medipulse.model.Feedback;
import com.medipulse.model.User;
import com.medipulse.service.AppointmentService;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Doctor: schedule, patient records, feedback. */
@WebServlet("/doctor")
public class DoctorServlet extends BaseServlet {
    private final UserDAO userDAO = new UserDAO();
    private final AppointmentDAO apptDAO = new AppointmentDAO();
    private final RecordDAO recordDAO = new RecordDAO();
    private final FeedbackDAO feedbackDAO = new FeedbackDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        User u = currentUser(req);
        try {
            req.setAttribute("appointments", apptDAO.findByDoctor(u.getId()));
            req.setAttribute("records", recordDAO.findByDoctor(u.getId()));
            req.setAttribute("patients", userDAO.findByRole("PATIENT"));
            List<Feedback> feedback = feedbackDAO.findByDoctor(u.getId());
            req.setAttribute("feedback", feedback);
            double avg = feedback.stream().mapToInt(Feedback::getRating).average().orElse(0);
            req.setAttribute("avgRating", String.format("%.1f", avg));
        } catch (MediPulseException e) {
            flashErr(req, e.getMessage());
        }
        forward(req, res, "doctor.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        User u = currentUser(req);
        try {
            switch (param(req, "action")) {
                case "status":
                    AppointmentService.getInstance().changeStatus(intParam(req, "id"), param(req, "status"));
                    flashOk(req, "Appointment updated");
                    break;
                case "addRecord":
                    recordDAO.add(intParam(req, "patientId"), u.getId(), param(req, "diagnosis"), param(req, "treatment"));
                    flashOk(req, "Patient record saved");
                    break;
                default:
                    throw new ValidationException("Unknown action");
            }
        } catch (MediPulseException e) {
            flashErr(req, e.getMessage());
        }
        redirect(req, res, "/doctor");
    }
}
