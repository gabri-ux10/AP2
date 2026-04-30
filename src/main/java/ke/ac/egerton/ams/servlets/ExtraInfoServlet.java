package ke.ac.egerton.ams.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import ke.ac.egerton.ams.dao.ApplicationDAO;
import ke.ac.egerton.ams.models.*;
import ke.ac.egerton.ams.util.Validator;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

/**
 * Extra Information Servlet (Step 6)
 * Handles special needs/disability information.
 * URL: /apply/extra
 */
public class ExtraInfoServlet extends HttpServlet {
    
    private ApplicationDAO applicationDAO;
    
    @Override
    public void init() throws ServletException {
        applicationDAO = new ApplicationDAO();
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
        
        request.setAttribute("extra", appBean.getExtra());
        request.setAttribute("currentStep", 6);
        
        request.getRequestDispatcher("/WEB-INF/views/step6_extra.jsp")
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
        params.put("hasDisability", request.getParameter("hasDisability"));
        params.put("disabilityDescription", request.getParameter("disabilityDescription"));
        
        // Validate
        Map<String, String> errors = Validator.validateStep6(params);
        
        if (!errors.isEmpty()) {
            ApplicantExtra extra = new ApplicantExtra();
            String hasDisability = params.get("hasDisability");
            extra.setHasDisability("yes".equalsIgnoreCase(hasDisability) || "1".equals(hasDisability));
            extra.setDisabilityDescription(params.get("disabilityDescription"));
            
            request.setAttribute("extra", extra);
            request.setAttribute("errors", errors);
            request.setAttribute("currentStep", 6);
            
            request.getRequestDispatcher("/WEB-INF/views/step6_extra.jsp")
                   .forward(request, response);
            return;
        }
        
        try {
            // Create extra record
            ApplicantExtra extra = appBean.getExtra();
            if (extra == null) {
                extra = new ApplicantExtra();
            }
            extra.setApplicationId(applicationId);
            
            String hasDisability = params.get("hasDisability");
            extra.setHasDisability("yes".equalsIgnoreCase(hasDisability) || "1".equals(hasDisability));
            
            if (extra.isHasDisability()) {
                extra.setDisabilityDescription(
                    Validator.sanitize(params.get("disabilityDescription"), 1000));
            } else {
                extra.setDisabilityDescription(null);
            }
            
            // Save to database
            if (applicationDAO.extraExists(applicationId)) {
                applicationDAO.updateExtra(extra);
            } else {
                extra = applicationDAO.saveExtra(extra);
            }
            
            // Update session
            appBean.setExtra(extra);
            appBean.completeStep(6);
            session.setAttribute("applicationBean", appBean);
            session.setAttribute("currentStep", 7);
            
            // Update application current step
            applicationDAO.updateCurrentStep(applicationId, 7);
            
            // Redirect to step 7
            response.sendRedirect(request.getContextPath() + "/apply/submit");
            
        } catch (SQLException e) {
            request.setAttribute("error", "Database error: " + e.getMessage());
            request.setAttribute("extra", appBean.getExtra());
            request.setAttribute("currentStep", 6);
            
            request.getRequestDispatcher("/WEB-INF/views/step6_extra.jsp")
                   .forward(request, response);
        }
    }
}
