package ke.ac.egerton.ams.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import ke.ac.egerton.ams.dao.OfficerDAO;
import ke.ac.egerton.ams.models.*;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Status Servlet
 * Displays applicant's application status.
 * URL: /apply/status
 */
public class StatusServlet extends HttpServlet {
    
    private OfficerDAO officerDAO;
    
    @Override
    public void init() throws ServletException {
        officerDAO = new OfficerDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/auth?action=login");
            return;
        }
        
        ApplicantAccount account = (ApplicantAccount) session.getAttribute("applicantAccount");
        
        if (account == null) {
            response.sendRedirect(request.getContextPath() + "/auth?action=login");
            return;
        }
        
        try {
            // Get application status
            ApplicationSummary status = officerDAO.getApplicantStatus(account.getId());
            
            if (status == null) {
                request.setAttribute("noApplication", true);
            } else {
                request.setAttribute("status", status);
                request.setAttribute("statusColor", status.getStatusColor());
                request.setAttribute("statusDisplay", status.getStatusDisplay());
            }
            
            request.getRequestDispatcher("/WEB-INF/views/status.jsp")
                   .forward(request, response);
                   
        } catch (SQLException e) {
            request.setAttribute("error", "Failed to load application status: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/status.jsp")
                   .forward(request, response);
        }
    }
}
