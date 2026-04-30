package ke.ac.egerton.ams.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import ke.ac.egerton.ams.dao.ApplicationDAO;
import ke.ac.egerton.ams.dao.ProgrammeDAO;
import ke.ac.egerton.ams.models.*;
import ke.ac.egerton.ams.util.Validator;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

/**
 * Programme Selection Servlet (Step 2)
 * Handles programme selection.
 * URL: /apply/programme
 */
public class ProgrammeServlet extends HttpServlet {
    
    private ApplicationDAO applicationDAO;
    private ProgrammeDAO programmeDAO;
    
    @Override
    public void init() throws ServletException {
        applicationDAO = new ApplicationDAO();
        programmeDAO = new ProgrammeDAO();
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
        
        try {
            // Load programmes
            List<Programme> programmes = programmeDAO.getAllActiveProgrammes();
            List<String> intakeYears = programmeDAO.getAvailableIntakeYears();
            
            request.setAttribute("programmes", programmes);
            request.setAttribute("intakeYears", intakeYears);
            request.setAttribute("programme", appBean.getProgramme());
            request.setAttribute("currentStep", 2);
            
            request.getRequestDispatcher("/WEB-INF/views/step2_programme.jsp")
                   .forward(request, response);
                   
        } catch (SQLException e) {
            request.setAttribute("error", "Failed to load programmes: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/step2_programme.jsp")
                   .forward(request, response);
        }
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
        params.put("programmeId", request.getParameter("programmeId"));
        params.put("intakeYear", request.getParameter("intakeYear"));
        params.put("intakeSemester", request.getParameter("intakeSemester"));
        params.put("studyMode", request.getParameter("studyMode"));
        
        // Validate
        Map<String, String> errors = Validator.validateStep2(params);
        
        if (!errors.isEmpty()) {
            try {
                List<Programme> programmes = programmeDAO.getAllActiveProgrammes();
                List<String> intakeYears = programmeDAO.getAvailableIntakeYears();
                
                request.setAttribute("programmes", programmes);
                request.setAttribute("intakeYears", intakeYears);
                request.setAttribute("errors", errors);
                request.setAttribute("params", params);
                request.setAttribute("currentStep", 2);
                
                request.getRequestDispatcher("/WEB-INF/views/step2_programme.jsp")
                       .forward(request, response);
            } catch (SQLException e) {
                throw new ServletException(e);
            }
            return;
        }
        
        try {
            Long programmeId = Long.parseLong(params.get("programmeId"));
            String intakeYear = params.get("intakeYear");
            String intakeSemester = params.get("intakeSemester");
            String studyMode = params.get("studyMode");
            
            // Find or create intake
            Intake intake = programmeDAO.findIntake(programmeId, intakeYear, intakeSemester);
            if (intake == null) {
                // Use first available intake for this programme
                List<Intake> intakes = programmeDAO.getOpenIntakesForProgramme(programmeId);
                if (!intakes.isEmpty()) {
                    intake = intakes.get(0);
                } else {
                    throw new ServletException("No intake available for selected programme");
                }
            }
            
            // Load programme details
            Programme prog = programmeDAO.findProgrammeById(programmeId);
            
            // Create/update applicant programme
            ApplicantProgramme appProgramme = appBean.getProgramme();
            if (appProgramme == null) {
                appProgramme = new ApplicantProgramme();
            }
            appProgramme.setApplicationId(applicationId);
            appProgramme.setProgrammeId(programmeId);
            appProgramme.setIntakeId(intake.getId());
            appProgramme.setStudyMode(studyMode);
            appProgramme.setProgrammeName(prog.getProgrammeName());
            appProgramme.setIntakeYear(intakeYear);
            appProgramme.setIntakeSemester(intakeSemester);
            
            // Save to database
            if (applicationDAO.programmeExists(applicationId)) {
                applicationDAO.updateProgramme(appProgramme);
            } else {
                appProgramme = applicationDAO.saveProgramme(appProgramme);
            }
            
            // Update application intake
            applicationDAO.updateIntake(applicationId, intake.getId());
            
            // Update session
            appBean.setProgramme(appProgramme);
            appBean.completeStep(2);
            session.setAttribute("applicationBean", appBean);
            session.setAttribute("currentStep", 3);
            
            // Update application current step
            applicationDAO.updateCurrentStep(applicationId, 3);
            
            // Redirect to step 3
            response.sendRedirect(request.getContextPath() + "/apply/academics");
            
        } catch (SQLException e) {
            request.setAttribute("error", "Database error: " + e.getMessage());
            try {
                List<Programme> programmes = programmeDAO.getAllActiveProgrammes();
                List<String> intakeYears = programmeDAO.getAvailableIntakeYears();
                request.setAttribute("programmes", programmes);
                request.setAttribute("intakeYears", intakeYears);
            } catch (SQLException ex) {
                // Ignore
            }
            request.setAttribute("currentStep", 2);
            request.getRequestDispatcher("/WEB-INF/views/step2_programme.jsp")
                   .forward(request, response);
        }
    }
}
