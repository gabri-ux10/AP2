package ke.ac.egerton.ams.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

import java.io.IOException;

/**
 * Congratulations Servlet
 * Displays submission confirmation page.
 * URL: /apply/congratulations
 */
public class CongratulationsServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/auth");
            return;
        }
        
        // Check for submission data
        Boolean submissionComplete = (Boolean) session.getAttribute("submissionComplete");
        
        if (submissionComplete == null || !submissionComplete) {
            // No recent submission, redirect to auth
            response.sendRedirect(request.getContextPath() + "/auth");
            return;
        }
        
        // Get submission details
        String referenceNumber = (String) session.getAttribute("submittedRefNumber");
        String fullName = (String) session.getAttribute("submittedName");
        String programmeName = (String) session.getAttribute("submittedProgramme");
        
        request.setAttribute("referenceNumber", referenceNumber);
        request.setAttribute("fullName", fullName);
        request.setAttribute("programmeName", programmeName);
        
        // Clear submission flag (allow one view only)
        session.removeAttribute("submissionComplete");
        
        request.getRequestDispatcher("/WEB-INF/views/congratulations.jsp")
               .forward(request, response);
    }
}
