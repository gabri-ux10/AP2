package ke.ac.egerton.ams.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import ke.ac.egerton.ams.dao.ApplicationDAO;
import ke.ac.egerton.ams.dao.DocumentDAO;
import ke.ac.egerton.ams.models.ApplicantDocuments;
import ke.ac.egerton.ams.models.*;
import ke.ac.egerton.ams.util.EmailSender;
import ke.ac.egerton.ams.util.PDFGenerator;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Submit Servlet (Step 7)
 * Handles final review and application submission.
 * URL: /apply/submit
 */
public class SubmitServlet extends HttpServlet {
    
    private ApplicationDAO applicationDAO;
    private DocumentDAO documentDAO;
    
    @Override
    public void init() throws ServletException {
        applicationDAO = new ApplicationDAO();
        documentDAO = new DocumentDAO();
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
        
        // Check if all steps are complete
        request.setAttribute("isComplete", appBean.isReadyForSubmission());
        request.setAttribute("appBean", appBean);
        request.setAttribute("currentStep", 7);
        
        request.getRequestDispatcher("/WEB-INF/views/step7_review.jsp")
               .forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        ApplicationBean appBean = (ApplicationBean) session.getAttribute("applicationBean");
        Long applicationId = (Long) session.getAttribute("applicationId");
        ApplicantAccount account = (ApplicantAccount) session.getAttribute("applicantAccount");
        
        if (appBean == null || applicationId == null) {
            response.sendRedirect(request.getContextPath() + "/apply/personal");
            return;
        }
        
        // Check declaration
        String declaration = request.getParameter("declaration");
        if (!"on".equals(declaration) && !"true".equals(declaration)) {
            request.setAttribute("error", "You must agree to the declaration to submit");
            request.setAttribute("isComplete", appBean.isReadyForSubmission());
            request.setAttribute("appBean", appBean);
            request.setAttribute("currentStep", 7);
            request.getRequestDispatcher("/WEB-INF/views/step7_review.jsp")
                   .forward(request, response);
            return;
        }
        
        // Check if application is complete
        if (!appBean.isReadyForSubmission()) {
            request.setAttribute("error", "Please complete all required steps before submitting");
            request.setAttribute("isComplete", false);
            request.setAttribute("appBean", appBean);
            request.setAttribute("currentStep", 7);
            request.getRequestDispatcher("/WEB-INF/views/step7_review.jsp")
                   .forward(request, response);
            return;
        }
        
        try {
            ApplicantDocuments documents = appBean.getDocuments();
            if (documents != null && documents.hasRequiredDocuments()) {
                String pdfPath = generatePdfSummary(appBean, applicationId);
                documents.setSummaryPdfPath(pdfPath);
                documentDAO.updateSummaryPdfPath(applicationId, pdfPath);
                appBean.setDocuments(documents);
            }

            // Generate reference number and submit (uses stored procedure)
            String referenceNumber = applicationDAO.submitApplication(applicationId);
            appBean.setReferenceNumber(referenceNumber);
            
            // Send confirmation email
            try {
                EmailSender emailSender = new EmailSender();
                emailSender.sendSubmissionConfirmation(
                    appBean.getPersonal().getEmail(),
                    appBean.getPersonal().getFullName(),
                    referenceNumber,
                    appBean.getProgramme().getProgrammeName()
                );
            } catch (Exception e) {
                // Log error but don't fail submission
                System.err.println("Failed to send confirmation email: " + e.getMessage());
            }
            
            // Clear wizard step cookie
            Cookie stepCookie = new Cookie("wizardStep", "");
            stepCookie.setMaxAge(0);
            stepCookie.setPath("/");
            response.addCookie(stepCookie);
            
            // Store submission details for congratulations page
            session.setAttribute("submissionComplete", true);
            session.setAttribute("submittedRefNumber", referenceNumber);
            session.setAttribute("submittedName", appBean.getPersonal().getFullName());
            session.setAttribute("submittedProgramme", appBean.getProgramme().getProgrammeName());
            
            // Clear application session data
            session.removeAttribute("applicationBean");
            session.removeAttribute("applicationId");
            session.removeAttribute("currentStep");
            
            // Redirect to congratulations page
            response.sendRedirect(request.getContextPath() + "/apply/congratulations");
            
        } catch (SQLException e) {
            request.setAttribute("error", "Failed to submit application: " + e.getMessage());
            request.setAttribute("isComplete", appBean.isReadyForSubmission());
            request.setAttribute("appBean", appBean);
            request.setAttribute("currentStep", 7);
            request.getRequestDispatcher("/WEB-INF/views/step7_review.jsp")
                   .forward(request, response);
        } catch (IOException e) {
            request.setAttribute("error", "Failed to generate summary PDF: " + e.getMessage());
            request.setAttribute("isComplete", appBean.isReadyForSubmission());
            request.setAttribute("appBean", appBean);
            request.setAttribute("currentStep", 7);
            request.getRequestDispatcher("/WEB-INF/views/step7_review.jsp")
                   .forward(request, response);
        }
    }

    private String generatePdfSummary(ApplicationBean appBean, Long applicationId)
            throws IOException {
        String pdfDir = getServletContext().getRealPath("/pdf");
        File dir = new File(pdfDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String fileName = "application_" + applicationId + "_" + System.currentTimeMillis() + ".pdf";
        String filePath = pdfDir + File.separator + fileName;

        PDFGenerator generator = new PDFGenerator();
        generator.generateApplicationSummary(appBean, filePath);

        return "/pdf/" + fileName;
    }
}
