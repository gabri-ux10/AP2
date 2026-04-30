package ke.ac.egerton.ams.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import ke.ac.egerton.ams.dao.OfficerDAO;
import ke.ac.egerton.ams.models.*;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Officer Dashboard Servlet
 * Displays all applications for review.
 * URL: /officer/dashboard
 */
public class OfficerDashboardServlet extends HttpServlet {
    
    private OfficerDAO officerDAO;
    private static final int PAGE_SIZE = 20;
    private static final int TREND_MONTHS = 8;
    private static final int COUNTY_LIMIT = 10;
    private static final int PROGRAMME_PANEL_LIMIT = 5;
    
    @Override
    public void init() throws ServletException {
        officerDAO = new OfficerDAO();
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
        
        // Get filter parameters
        String status = request.getParameter("status");
        String search = request.getParameter("search");
        String pageStr = request.getParameter("page");
        
        int page = 1;
        if (pageStr != null && !pageStr.isEmpty()) {
            try {
                page = Integer.parseInt(pageStr);
                if (page < 1) page = 1;
            } catch (NumberFormatException e) {
                page = 1;
            }
        }
        
        int offset = (page - 1) * PAGE_SIZE;
        
        try {
            // Get dashboard counts
            DashboardCounts counts = officerDAO.getDashboardCounts();
            DemographicStats demographics = officerDAO.getDemographicStats();
            List<MonthlyTrendPoint> trendPoints = officerDAO.getMonthlySubmissionTrend(TREND_MONTHS);
            List<CountyApplicationStat> countyStats = officerDAO.getTopCountyStats(COUNTY_LIMIT);
            List<ProgrammeApplicationStat> programmeStats = officerDAO.getProgrammeApplicationStats();
            decorateProgrammeStats(programmeStats);
            
            // Get applications
            List<ApplicationSummary> applications = officerDAO.getApplicationSummaries(
                    status, search, offset, PAGE_SIZE);
            
            // Get total count for pagination
            int totalCount = officerDAO.countApplications(status, search);
            int totalPages = (int) Math.ceil((double) totalCount / PAGE_SIZE);
            
            request.setAttribute("officer", officer);
            request.setAttribute("counts", counts);
            request.setAttribute("demographics", demographics);
            request.setAttribute("trendPoints", trendPoints);
            request.setAttribute("countyStats", countyStats);
            request.setAttribute("programmeStats", programmeStats);
            request.setAttribute("popularProgrammes", getTopProgrammeStats(programmeStats, PROGRAMME_PANEL_LIMIT));
            request.setAttribute("leastPopularProgrammes", getLeastProgrammeStats(programmeStats, PROGRAMME_PANEL_LIMIT));
            request.setAttribute("maxTrendCount", getMaxTrendCount(trendPoints));
            request.setAttribute("maxCountyCount", getMaxCountyCount(countyStats));
            request.setAttribute("academicYearLabel", getAcademicYearLabel());
            request.setAttribute("lastUpdatedAt", LocalDateTime.now().format(
                    DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm")));
            request.setAttribute("applications", applications);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("totalCount", totalCount);
            request.setAttribute("status", status);
            request.setAttribute("search", search);
            
            request.getRequestDispatcher("/WEB-INF/views/officer_dashboard.jsp")
                   .forward(request, response);
                   
        } catch (SQLException e) {
            request.setAttribute("error", "Failed to load applications: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/officer_dashboard.jsp")
                   .forward(request, response);
        }
    }

    private void decorateProgrammeStats(List<ProgrammeApplicationStat> programmeStats) {
        int maxTotalApplied = 0;
        for (ProgrammeApplicationStat stat : programmeStats) {
            if (stat.getTotalApplied() > maxTotalApplied) {
                maxTotalApplied = stat.getTotalApplied();
            }
        }

        int rank = 1;
        for (ProgrammeApplicationStat stat : programmeStats) {
            stat.setRank(rank++);
            stat.setMaxTotalApplied(maxTotalApplied);
        }
    }

    private List<ProgrammeApplicationStat> getTopProgrammeStats(List<ProgrammeApplicationStat> programmeStats,
            int limit) {
        List<ProgrammeApplicationStat> filtered = new ArrayList<>();
        for (ProgrammeApplicationStat stat : programmeStats) {
            if (stat.getTotalApplied() > 0) {
                filtered.add(stat);
            }
            if (filtered.size() == limit) {
                break;
            }
        }
        return filtered;
    }

    private List<ProgrammeApplicationStat> getLeastProgrammeStats(List<ProgrammeApplicationStat> programmeStats,
            int limit) {
        List<ProgrammeApplicationStat> filtered = new ArrayList<>();
        for (ProgrammeApplicationStat stat : programmeStats) {
            if (stat.getTotalApplied() > 0) {
                filtered.add(stat);
            }
        }

        filtered.sort(Comparator.comparingInt(ProgrammeApplicationStat::getTotalApplied)
                .thenComparing(ProgrammeApplicationStat::getProgrammeName));

        if (filtered.size() <= limit) {
            return filtered;
        }
        return new ArrayList<>(filtered.subList(0, limit));
    }

    private int getMaxTrendCount(List<MonthlyTrendPoint> trendPoints) {
        int max = 0;
        for (MonthlyTrendPoint point : trendPoints) {
            if (point.getTotalSubmitted() > max) {
                max = point.getTotalSubmitted();
            }
        }
        return max;
    }

    private int getMaxCountyCount(List<CountyApplicationStat> countyStats) {
        int max = 0;
        for (CountyApplicationStat stat : countyStats) {
            if (stat.getApplicantCount() > max) {
                max = stat.getApplicantCount();
            }
        }
        return max;
    }

    private String getAcademicYearLabel() {
        LocalDate today = LocalDate.now();
        int startYear = today.getMonthValue() >= 7 ? today.getYear() : today.getYear() - 1;
        return "AY " + startYear + " / " + (startYear + 1);
    }
}
