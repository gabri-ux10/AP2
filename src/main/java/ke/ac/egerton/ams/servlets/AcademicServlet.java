package ke.ac.egerton.ams.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import ke.ac.egerton.ams.dao.ApplicationDAO;
import ke.ac.egerton.ams.models.*;
import ke.ac.egerton.ams.util.Validator;

import java.io.IOException;
import java.sql.SQLException;
import java.time.Year;
import java.util.*;

/**
 * Academic Background Servlet (Step 3)
 * Handles KCSE academic details and subject grades.
 * URL: /apply/academics
 */
public class AcademicServlet extends HttpServlet {
    
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
        
        // Generate years for dropdown (2018 to current year)
        int currentYear = Year.now().getValue();
        List<Integer> years = new ArrayList<>();
        for (int y = currentYear; y >= 2018; y--) {
            years.add(y);
        }
        
        request.setAttribute("academics", appBean.getAcademics());
        request.setAttribute("subjectGrades", appBean.getSubjectGrades());
        request.setAttribute("subjects", SubjectGrade.SUBJECTS);
        request.setAttribute("grades", SubjectGrade.GRADES);
        request.setAttribute("years", years);
        request.setAttribute("schoolTypes", ApplicantAcademics.SchoolType.values());
        request.setAttribute("currentStep", 3);
        
        request.getRequestDispatcher("/WEB-INF/views/step3_academics.jsp")
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
        params.put("kcseIndexNumber", request.getParameter("kcseIndexNumber"));
        params.put("yearOfExam", request.getParameter("yearOfExam"));
        params.put("schoolName", request.getParameter("schoolName"));
        params.put("schoolType", request.getParameter("schoolType"));
        params.put("overallGrade", request.getParameter("overallGrade"));
        
        // Collect subject grades
        List<SubjectGrade> subjectGrades = new ArrayList<>();
        String[] subjectNames = request.getParameterValues("subjectName");
        String[] subjectGradeValues = request.getParameterValues("subjectGrade");
        
        if (subjectNames != null && subjectGradeValues != null) {
            for (int i = 0; i < subjectNames.length && i < subjectGradeValues.length; i++) {
                if (subjectNames[i] != null && !subjectNames[i].isEmpty() &&
                    subjectGradeValues[i] != null && !subjectGradeValues[i].isEmpty()) {
                    SubjectGrade sg = new SubjectGrade();
                    sg.setSubjectName(subjectNames[i]);
                    sg.setGrade(subjectGradeValues[i]);
                    sg.setSubjectOrder(i + 1);
                    subjectGrades.add(sg);
                }
            }
        }
        
        // Validate
        Map<String, String> errors = Validator.validateStep3(params, subjectGrades.size());
        
        if (!errors.isEmpty()) {
            // Repopulate form
            ApplicantAcademics academics = new ApplicantAcademics();
            populateAcademics(academics, params);
            
            int currentYear = Year.now().getValue();
            List<Integer> years = new ArrayList<>();
            for (int y = currentYear; y >= 2018; y--) {
                years.add(y);
            }
            
            request.setAttribute("academics", academics);
            request.setAttribute("subjectGrades", subjectGrades);
            request.setAttribute("subjects", SubjectGrade.SUBJECTS);
            request.setAttribute("grades", SubjectGrade.GRADES);
            request.setAttribute("years", years);
            request.setAttribute("schoolTypes", ApplicantAcademics.SchoolType.values());
            request.setAttribute("errors", errors);
            request.setAttribute("currentStep", 3);
            
            request.getRequestDispatcher("/WEB-INF/views/step3_academics.jsp")
                   .forward(request, response);
            return;
        }
        
        try {
            // Create/update academics
            ApplicantAcademics academics = appBean.getAcademics();
            if (academics == null) {
                academics = new ApplicantAcademics();
            }
            academics.setApplicationId(applicationId);
            populateAcademics(academics, params);
            
            // Save to database
            if (applicationDAO.academicsExists(applicationId)) {
                applicationDAO.updateAcademics(academics);
            } else {
                academics = applicationDAO.saveAcademics(academics);
            }
            
            // Save subject grades
            for (SubjectGrade sg : subjectGrades) {
                sg.setApplicationId(applicationId);
                sg.setAcademicId(academics.getId());
            }
            applicationDAO.saveSubjectGrades(applicationId, academics.getId(), subjectGrades);
            
            // Update session
            appBean.setAcademics(academics);
            appBean.setSubjectGrades(subjectGrades);
            appBean.completeStep(3);
            session.setAttribute("applicationBean", appBean);
            session.setAttribute("currentStep", 4);
            
            // Update application current step
            applicationDAO.updateCurrentStep(applicationId, 4);
            
            // Redirect to step 4
            response.sendRedirect(request.getContextPath() + "/apply/documents");
            
        } catch (SQLException e) {
            int currentYear = Year.now().getValue();
            List<Integer> years = new ArrayList<>();
            for (int y = currentYear; y >= 2018; y--) {
                years.add(y);
            }
            
            request.setAttribute("error", "Database error: " + e.getMessage());
            request.setAttribute("academics", appBean.getAcademics());
            request.setAttribute("subjectGrades", subjectGrades);
            request.setAttribute("subjects", SubjectGrade.SUBJECTS);
            request.setAttribute("grades", SubjectGrade.GRADES);
            request.setAttribute("years", years);
            request.setAttribute("schoolTypes", ApplicantAcademics.SchoolType.values());
            request.setAttribute("currentStep", 3);
            
            request.getRequestDispatcher("/WEB-INF/views/step3_academics.jsp")
                   .forward(request, response);
        }
    }
    
    private void populateAcademics(ApplicantAcademics academics, Map<String, String> params) {
        academics.setKcseIndexNumber(Validator.sanitize(params.get("kcseIndexNumber"), 11));
        
        String yearStr = params.get("yearOfExam");
        if (yearStr != null && !yearStr.isEmpty()) {
            academics.setYearOfExam(Integer.parseInt(yearStr));
        }
        
        academics.setSchoolName(Validator.sanitize(params.get("schoolName"), 255));
        
        String schoolType = params.get("schoolType");
        if (schoolType != null && !schoolType.isEmpty()) {
            academics.setSchoolType(schoolType);
        }
        
        String overallGrade = params.get("overallGrade");
        if (overallGrade != null && !overallGrade.isEmpty()) {
            academics.setOverallGrade(overallGrade);
        }
    }
}
