package ke.ac.egerton.ams.filters;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ke.ac.egerton.ams.models.ApplicantAccount;

import java.io.IOException;

/**
 * Authentication Filter for protecting applicant and officer pages.
 * Mapped to /apply/* and /officer/* in web.xml
 */
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // No initialization needed
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest  httpRequest  = (HttpServletRequest)  request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String requestURI  = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();

        // Get session without creating a new one
        HttpSession session = httpRequest.getSession(false);

        // ----------------------------------------------------------------
        // OFFICER PATHS  (/officer/*)
        // ----------------------------------------------------------------
        if (requestURI.startsWith(contextPath + "/officer/")) {

            // Officer login page is always accessible
            if (requestURI.equals(contextPath + "/officer/login")) {
                chain.doFilter(request, response);
                return;
            }

            // All other officer pages require a valid officer session
            if (session == null || session.getAttribute("loggedInOfficer") == null) {
                httpResponse.sendRedirect(contextPath + "/officer/login");
                return;
            }

            chain.doFilter(request, response);
            return;
        }

        // ----------------------------------------------------------------
        // APPLICANT PATHS  (/apply/*)
        // ----------------------------------------------------------------
        if (requestURI.startsWith(contextPath + "/apply/")) {

            // Congratulations page — only accessible after a completed submission
            if (requestURI.equals(contextPath + "/apply/congratulations")) {
                if (session != null && session.getAttribute("submissionComplete") != null) {
                    chain.doFilter(request, response);
                    return;
                }
                // Not coming from a valid submission — redirect to login
                httpResponse.sendRedirect(contextPath + "/auth?action=login");
                return;
            }

            // Status page — requires a logged-in applicant account only
            if (requestURI.equals(contextPath + "/apply/status")) {
                if (session == null || session.getAttribute("applicantAccount") == null) {
                    httpResponse.sendRedirect(contextPath + "/auth?action=login");
                    return;
                }
                chain.doFilter(request, response);
                return;
            }

            // All wizard pages — require a logged-in applicant account
            if (session == null) {
                httpResponse.sendRedirect(contextPath + "/auth?action=login");
                return;
            }

            ApplicantAccount account = (ApplicantAccount) session.getAttribute("applicantAccount");
            if (account == null) {
                httpResponse.sendRedirect(contextPath + "/auth?action=login");
                return;
            }

            // Step 1 (personal info) is always accessible once logged in
            if (requestURI.equals(contextPath + "/apply/personal")) {
                chain.doFilter(request, response);
                return;
            }

            // Steps 2–7 require an application to have been started
            Long applicationId = (Long) session.getAttribute("applicationId");
            if (applicationId == null) {
                httpResponse.sendRedirect(contextPath + "/apply/personal");
                return;
            }

            // Enforce wizard step ordering — no skipping ahead
            Integer currentStep  = (Integer) session.getAttribute("currentStep");
            if (currentStep == null) {
                currentStep = 1;
            }

            int requestedStep = getStepFromURI(requestURI, contextPath);

            if (requestedStep > 0 && requestedStep > currentStep + 1) {
                // Redirect back to the current step
                httpResponse.sendRedirect(getURIForStep(currentStep, contextPath));
                return;
            }

            // All checks passed
            chain.doFilter(request, response);
            return;
        }

        // ----------------------------------------------------------------
        // ALL OTHER PATHS — allow through
        // ----------------------------------------------------------------
        chain.doFilter(request, response);
    }

    /**
     * Maps a request URI to its wizard step number.
     * Returns 0 if the URI does not match any known step.
     */
    private int getStepFromURI(String uri, String contextPath) {
        if (uri.equals(contextPath + "/apply/personal"))   return 1;
        if (uri.equals(contextPath + "/apply/programme"))  return 2;
        if (uri.equals(contextPath + "/apply/academics"))  return 3;
        if (uri.equals(contextPath + "/apply/documents"))  return 4;
        if (uri.equals(contextPath + "/apply/guardian"))   return 5;
        if (uri.equals(contextPath + "/apply/extra"))      return 6;
        if (uri.equals(contextPath + "/apply/submit"))     return 7;
        return 0;
    }

    /**
     * Maps a wizard step number back to its URL.
     * Falls back to step 1 for any unrecognised value.
     */
    private String getURIForStep(int step, String contextPath) {
        return switch (step) {
            case 1 -> contextPath + "/apply/personal";
            case 2 -> contextPath + "/apply/programme";
            case 3 -> contextPath + "/apply/academics";
            case 4 -> contextPath + "/apply/documents";
            case 5 -> contextPath + "/apply/guardian";
            case 6 -> contextPath + "/apply/extra";
            case 7 -> contextPath + "/apply/submit";
            default -> contextPath + "/apply/personal";
        };
    }

    @Override
    public void destroy() {
        // No cleanup needed
    }
}