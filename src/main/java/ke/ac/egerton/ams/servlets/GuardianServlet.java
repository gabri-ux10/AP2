package ke.ac.egerton.ams.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import ke.ac.egerton.ams.dao.ApplicationDAO;
import ke.ac.egerton.ams.dao.GuardianDAO;
import ke.ac.egerton.ams.models.*;
import ke.ac.egerton.ams.util.Validator;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

/**
 * Guardian Information Servlet (Step 5)
 * Handles guardian details collection.
 * URL: /apply/guardian
 */
public class GuardianServlet extends HttpServlet {
    
    private ApplicationDAO applicationDAO;
    private GuardianDAO guardianDAO;
    
    @Override
    public void init() throws ServletException {
        applicationDAO = new ApplicationDAO();
        guardianDAO = new GuardianDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        ApplicationBean appBean = (ApplicationBean) session.getAttribute("applicationBean");
        
        if (appBean == null) {
            response.sendRedirect(request.getContextPath() + "/apply/personal");
            return;
        }
        
        request.setAttribute("guardians", appBean.getGuardians());
        request.setAttribute("guardian1", appBean.getPrimaryGuardian());
        request.setAttribute("guardian2", appBean.getSecondaryGuardian());
        request.setAttribute("relationships", ApplicantGuardian.Relationship.values());
        request.setAttribute("currentStep", 5);
        
        request.getRequestDispatcher("/WEB-INF/views/step5_guardian.jsp")
               .forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        ApplicationBean appBean = (ApplicationBean) session.getAttribute("applicationBean");
        Long applicationId = (Long) session.getAttribute("applicationId");
        
        if (appBean == null || applicationId == null) {
            response.sendRedirect(request.getContextPath() + "/apply/personal");
            return;
        }
        
        // Collect parameters
        Map<String, String> params = new HashMap<>();
        params.put("guardian1Name", request.getParameter("guardian1Name"));
        params.put("guardian1Relationship", request.getParameter("guardian1Relationship"));
        params.put("guardian1Phone", request.getParameter("guardian1Phone"));
        params.put("guardian2Name", request.getParameter("guardian2Name"));
        params.put("guardian2Relationship", request.getParameter("guardian2Relationship"));
        params.put("guardian2Phone", request.getParameter("guardian2Phone"));
        
        // Validate
        Map<String, String> errors = Validator.validateStep5(params);
        
        if (!errors.isEmpty()) {
            // Create guardian objects for form re-display
            ApplicantGuardian g1 = new ApplicantGuardian();
            g1.setFullName(params.get("guardian1Name"));
            if (params.get("guardian1Relationship") != null && !params.get("guardian1Relationship").isEmpty()) {
                g1.setRelationship(params.get("guardian1Relationship"));
            }
            g1.setPhoneNumberShort(params.get("guardian1Phone"));
            g1.setPrimary(true);
            
            ApplicantGuardian g2 = null;
            if (params.get("guardian2Name") != null && !params.get("guardian2Name").isEmpty()) {
                g2 = new ApplicantGuardian();
                g2.setFullName(params.get("guardian2Name"));
                if (params.get("guardian2Relationship") != null && !params.get("guardian2Relationship").isEmpty()) {
                    g2.setRelationship(params.get("guardian2Relationship"));
                }
                g2.setPhoneNumberShort(params.get("guardian2Phone"));
                g2.setPrimary(false);
            }
            
            request.setAttribute("guardian1", g1);
            request.setAttribute("guardian2", g2);
            request.setAttribute("errors", errors);
            request.setAttribute("relationships", ApplicantGuardian.Relationship.values());
            request.setAttribute("currentStep", 5);
            
            request.getRequestDispatcher("/WEB-INF/views/step5_guardian.jsp")
                   .forward(request, response);
            return;
        }
        
        try {
            // Create guardian list
            List<ApplicantGuardian> guardians = new ArrayList<>();
            
            // Primary guardian (required)
            ApplicantGuardian g1 = new ApplicantGuardian();
            g1.setApplicationId(applicationId);
            g1.setFullName(Validator.sanitize(params.get("guardian1Name"), 255));
            g1.setRelationship(params.get("guardian1Relationship"));
            g1.setPhoneNumber(Validator.formatPhoneNumber(params.get("guardian1Phone")));
            g1.setPrimary(true);
            guardians.add(g1);
            
            // Secondary guardian (optional)
            String g2Name = params.get("guardian2Name");
            if (g2Name != null && !g2Name.trim().isEmpty()) {
                ApplicantGuardian g2 = new ApplicantGuardian();
                g2.setApplicationId(applicationId);
                g2.setFullName(Validator.sanitize(g2Name, 255));
                g2.setRelationship(params.get("guardian2Relationship"));
                g2.setPhoneNumber(Validator.formatPhoneNumber(params.get("guardian2Phone")));
                g2.setPrimary(false);
                guardians.add(g2);
            }
            
            // Save to database
            guardianDAO.saveGuardians(applicationId, guardians);
            
            // Update session
            appBean.setGuardians(guardians);
            appBean.completeStep(5);
            session.setAttribute("applicationBean", appBean);
            session.setAttribute("currentStep", 6);
            
            // Update application current step
            applicationDAO.updateCurrentStep(applicationId, 6);
            
            // Redirect to step 6
            response.sendRedirect(request.getContextPath() + "/apply/extra");
            
        } catch (SQLException e) {
            request.setAttribute("error", "Database error: " + e.getMessage());
            request.setAttribute("guardian1", appBean.getPrimaryGuardian());
            request.setAttribute("guardian2", appBean.getSecondaryGuardian());
            request.setAttribute("relationships", ApplicantGuardian.Relationship.values());
            request.setAttribute("currentStep", 5);
            
            request.getRequestDispatcher("/WEB-INF/views/step5_guardian.jsp")
                   .forward(request, response);
        }
    }
}
