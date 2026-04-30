package ke.ac.egerton.ams.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import ke.ac.egerton.ams.dao.*;
import ke.ac.egerton.ams.models.*;
import ke.ac.egerton.ams.util.EmailSender;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Officer Review Servlet
 * Handles individual applicant review and decision.
 * URL: /officer/review
 */
public class OfficerReviewServlet extends HttpServlet {
    
    private OfficerDAO officerDAO;
    private ApplicantDAO applicantDAO;
    private ApplicationDAO applicationDAO;
    private DocumentDAO documentDAO;
    private GuardianDAO guardianDAO;
    
    @Override
    public void init() throws ServletException {
        officerDAO = new OfficerDAO();
        applicantDAO = new ApplicantDAO();
        applicationDAO = new ApplicationDAO();
        documentDAO = new DocumentDAO();
        guardianDAO = new GuardianDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/officer/login");
            return;
        }
        
        Officer officer = (Officer) session.getAttribute("loggedInOfficer");
        
        if (officer == null) {
            response.sendRedirect(request.getContextPath() + "/officer/login");
            return;
        }
        
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/officer/dashboard");
            return;
        }
        
        try {
            Long applicationId = Long.parseLong(idParam);
            
            // Mark as under review (calls sp_open_review)
            officerDAO.openReview(applicationId, officer.getId());
            
            // Get application summary
            ApplicationSummary summary = officerDAO.getApplicationSummaryById(applicationId);
            
            if (summary == null) {
                request.setAttribute("error", "Application not found");
                request.getRequestDispatcher("/WEB-INF/views/officer_review.jsp")
                       .forward(request, response);
                return;
            }
            
            // Get detailed data
            ApplicantPersonal personal = applicantDAO.findPersonalByApplicationId(applicationId);
            ApplicantProgramme programme = applicationDAO.findProgrammeByApplicationId(applicationId);
            ApplicantAcademics academics = applicationDAO.findAcademicsByApplicationId(applicationId);
            List<SubjectGrade> subjectGrades = officerDAO.getSubjectGradesForReview(applicationId);
            ApplicantDocuments documents = documentDAO.findByApplicationId(applicationId);
            List<ApplicantGuardian> guardians = guardianDAO.findByApplicationId(applicationId);
            ApplicantExtra extra = applicationDAO.findExtraByApplicationId(applicationId);
            
            // Check if decision already made
            boolean decisionMade = "ACCEPTED".equals(summary.getStatus()) || 
                                   "REJECTED".equals(summary.getStatus());
            
            request.setAttribute("officer", officer);
            request.setAttribute("summary", summary);
            request.setAttribute("personal", personal);
            request.setAttribute("programme", programme);
            request.setAttribute("academics", academics);
            request.setAttribute("subjectGrades", subjectGrades);
            request.setAttribute("documents", documents);
            request.setAttribute("guardians", guardians);
            request.setAttribute("extra", extra);
            request.setAttribute("decisionMade", decisionMade);
            
            request.getRequestDispatcher("/WEB-INF/views/officer_review.jsp")
                   .forward(request, response);
                   
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/officer/dashboard");
        } catch (SQLException e) {
            request.setAttribute("error", "Database error: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/officer_review.jsp")
                   .forward(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/officer/login");
            return;
        }
        
        Officer officer = (Officer) session.getAttribute("loggedInOfficer");
        
        if (officer == null) {
            response.sendRedirect(request.getContextPath() + "/officer/login");
            return;
        }
        
        String idParam = request.getParameter("applicationId");
        String action = request.getParameter("action");
        String notes = request.getParameter("officerNotes");
        
        if (idParam == null || action == null) {
            response.sendRedirect(request.getContextPath() + "/officer/dashboard");
            return;
        }
        
        try {
            Long applicationId = Long.parseLong(idParam);
            
            // Get application details for email
            ApplicationSummary summary = officerDAO.getApplicationSummaryById(applicationId);
            
            if (summary == null) {
                request.setAttribute("error", "Application not found");
                response.sendRedirect(request.getContextPath() + "/officer/dashboard");
                return;
            }
            
            String decisionMessage;
            
            if ("accept".equals(action)) {
                decisionMessage = "Congratulations! Your application to Egerton University has been accepted. " +
                        "Please check your email for further instructions regarding registration and reporting.";
                
                officerDAO.acceptApplication(applicationId, officer.getId(), notes, decisionMessage);
                
                // Send acceptance email
                try {
                    EmailSender emailSender = new EmailSender();
                    emailSender.sendAcceptanceNotification(
                        summary.getEmail(),
                        summary.getFullName(),
                        summary.getReferenceNumber(),
                        summary.getProgrammeName()
                    );
                } catch (Exception e) {
                    System.err.println("Failed to send acceptance email: " + e.getMessage());
                }
                
            } else if ("reject".equals(action)) {
                decisionMessage = "We regret to inform you that your application was not successful for the current intake. " +
                        "We encourage you to apply again in future intakes.";
                
                officerDAO.rejectApplication(applicationId, officer.getId(), notes, decisionMessage);
                
                // Send rejection email
                try {
                    EmailSender emailSender = new EmailSender();
                    emailSender.sendRejectionNotification(
                        summary.getEmail(),
                        summary.getFullName(),
                        summary.getReferenceNumber()
                    );
                } catch (Exception e) {
                    System.err.println("Failed to send rejection email: " + e.getMessage());
                }
            }
            
            // Redirect back to dashboard
            response.sendRedirect(request.getContextPath() + "/officer/dashboard?message=Decision recorded successfully");
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/officer/dashboard");
        } catch (SQLException e) {
            request.setAttribute("error", "Failed to record decision: " + e.getMessage());
            // Redirect back with error
            response.sendRedirect(request.getContextPath() + "/officer/review?id=" + idParam + 
                    "&error=" + java.net.URLEncoder.encode(e.getMessage(), "UTF-8"));
        }
    }
}
