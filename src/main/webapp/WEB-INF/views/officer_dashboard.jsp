<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Officer Dashboard - Egerton University AMS</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/officer-portal.css">
</head>
<body class="officer-portal">
    <div class="officer-shell">
        <header class="officer-topbar">
            <div class="officer-brand">
                <div class="officer-brand-mark">EU</div>
                <div>
                    <div class="officer-brand-title">EGERTON UNIVERSITY</div>
                    <div class="officer-brand-subtitle">Admission Management System</div>
                </div>
            </div>
            <div class="officer-topbar-meta">
                <div class="officer-chip">${academicYearLabel}</div>
                <div class="officer-user">
                    <span class="officer-avatar">AO</span>
                    <span>${officer.fullName}</span>
                </div>
            </div>
        </header>

        <div class="officer-layout">
            <aside class="officer-sidebar">
                <div class="officer-nav-label">Menu</div>
                <nav class="officer-nav">
                    <a href="${pageContext.request.contextPath}/officer/dashboard" class="officer-nav-link active">
                        <span class="officer-nav-key">D</span>
                        <span>Dashboard</span>
                    </a>
                    <a href="#applications" class="officer-nav-link">
                        <span class="officer-nav-key">A</span>
                        <span>Applications</span>
                    </a>
                    <a href="${pageContext.request.contextPath}/logout" class="officer-nav-link">
                        <span class="officer-nav-key">L</span>
                        <span>Logout</span>
                    </a>
                </nav>
            </aside>

            <main class="officer-main">
                <div class="officer-page-head">
                    <div>
                        <h1 class="officer-page-title">Admissions Dashboard</h1>
                        <p class="officer-page-subtitle">
                            Undergraduate applications overview. Last updated ${lastUpdatedAt}.
                        </p>
                    </div>
                </div>

                <c:if test="${not empty param.message}">
                    <div class="alert alert-success">${param.message}</div>
                </c:if>

                <c:if test="${not empty error}">
                    <div class="alert alert-danger">${error}</div>
                </c:if>

                <div class="officer-section-label">Key Metrics</div>
                <div class="officer-kpi-grid">
                    <div class="officer-card officer-kpi kpi-total">
                        <div class="officer-card-body">
                            <div class="officer-kpi-label">Total Submitted</div>
                            <div class="officer-kpi-value kpi-blue">${counts.submittedTotal}</div>
                            <div class="officer-kpi-meta">
                                <strong>${counts.totalApplications}</strong> total records in the system
                            </div>
                        </div>
                    </div>
                    <div class="officer-card officer-kpi kpi-accepted">
                        <div class="officer-card-body">
                            <div class="officer-kpi-label">Accepted</div>
                            <div class="officer-kpi-value kpi-green">${counts.accepted}</div>
                            <div class="officer-kpi-meta">
                                <strong>${counts.acceptanceRateRounded}%</strong> of submitted applications
                            </div>
                        </div>
                    </div>
                    <div class="officer-card officer-kpi kpi-pending">
                        <div class="officer-card-body">
                            <div class="officer-kpi-label">Pending / Under Review</div>
                            <div class="officer-kpi-value kpi-orange">${counts.reviewQueueTotal}</div>
                            <div class="officer-kpi-meta">
                                <strong>${counts.pendingReview}</strong> new and <strong>${counts.underReview}</strong> in progress
                            </div>
                        </div>
                    </div>
                    <div class="officer-card officer-kpi kpi-rejected">
                        <div class="officer-card-body">
                            <div class="officer-kpi-label">Rejected</div>
                            <div class="officer-kpi-value kpi-red">${counts.rejected}</div>
                            <div class="officer-kpi-meta">
                                <strong>${counts.rejectionRateRounded}%</strong> of submitted applications
                            </div>
                        </div>
                    </div>
                    <div class="officer-card officer-kpi kpi-disabled">
                        <div class="officer-card-body">
                            <div class="officer-kpi-label">Disabled Applicants</div>
                            <div class="officer-kpi-value kpi-purple">${demographics.disabledCount}</div>
                            <div class="officer-kpi-meta">
                                <strong>${demographics.disabledPercentageRounded}%</strong> of submitted profiles
                            </div>
                        </div>
                    </div>
                </div>

                <div class="officer-section-label">Application Trends and Demographics</div>
                <div class="officer-grid-2">
                    <section class="officer-card officer-chart">
                        <div class="officer-card-header">
                            <div>
                                <div class="officer-card-title">Monthly Application Trend</div>
                                <div class="officer-card-subtitle">Submitted applications by month</div>
                            </div>
                        </div>
                        <div class="officer-card-body">
                            <c:choose>
                                <c:when test="${empty trendPoints}">
                                    <div class="officer-empty">No submitted applications are available yet.</div>
                                </c:when>
                                <c:otherwise>
                                    <div class="trend-bars">
                                        <c:forEach var="point" items="${trendPoints}">
                                            <div class="trend-item">
                                                <div class="trend-value">${point.totalSubmitted}</div>
                                                <div class="trend-column">
                                                    <div class="trend-column-fill"
                                                         style="height: ${maxTrendCount > 0 ? (point.totalSubmitted * 100.0 / maxTrendCount) : 0}%;">
                                                    </div>
                                                </div>
                                                <div class="trend-label">${point.monthLabel}</div>
                                            </div>
                                        </c:forEach>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </section>

                    <section class="officer-card officer-chart">
                        <div class="officer-card-header">
                            <div>
                                <div class="officer-card-title">Gender and Disability Profile</div>
                                <div class="officer-card-subtitle">Submitted applicant demographics</div>
                            </div>
                        </div>
                        <div class="officer-card-body">
                            <c:choose>
                                <c:when test="${demographics.totalApplicants == 0}">
                                    <div class="officer-empty">Demographic charts will appear after applications are submitted.</div>
                                </c:when>
                                <c:otherwise>
                                    <div class="donut-layout">
                                        <div class="donut-chart" style="--male:${demographics.malePercentage};">
                                            <div class="donut-inner">
                                                <div class="donut-value">${demographics.malePercentageRounded}%</div>
                                                <div class="donut-label">Male</div>
                                            </div>
                                        </div>
                                        <div class="officer-legend">
                                            <div class="officer-legend-item">
                                                <div class="officer-legend-title">
                                                    <span class="legend-dot blue"></span>
                                                    <span>Male applicants</span>
                                                </div>
                                                <strong>${demographics.maleCount}</strong>
                                            </div>
                                            <div class="officer-legend-item">
                                                <div class="officer-legend-title">
                                                    <span class="legend-dot purple"></span>
                                                    <span>Female applicants</span>
                                                </div>
                                                <strong>${demographics.femaleCount}</strong>
                                            </div>
                                            <div class="officer-legend-item">
                                                <div class="officer-legend-title">
                                                    <span class="legend-dot green"></span>
                                                    <span>Declared disability</span>
                                                </div>
                                                <strong>${demographics.disabledCount}</strong>
                                            </div>
                                        </div>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </section>
                </div>

                <div class="officer-section-label">Programme Demand</div>
                <div class="officer-grid-2">
                    <section class="officer-card officer-chart">
                        <div class="officer-card-header">
                            <div>
                                <div class="officer-card-title">Most Applied Programmes</div>
                                <div class="officer-card-subtitle">Top choices by submitted applications</div>
                            </div>
                        </div>
                        <div class="officer-card-body">
                            <c:choose>
                                <c:when test="${empty popularProgrammes}">
                                    <div class="officer-empty">Popular programme insights will appear once applications are submitted.</div>
                                </c:when>
                                <c:otherwise>
                                    <div class="bar-list">
                                        <c:forEach var="programme" items="${popularProgrammes}">
                                            <div class="bar-item">
                                                <div class="bar-header">
                                                    <span>${programme.programmeName}</span>
                                                    <span>${programme.totalApplied}</span>
                                                </div>
                                                <div class="bar-track">
                                                    <div class="bar-fill" style="width: ${programme.volumePercentage}%;"></div>
                                                </div>
                                            </div>
                                        </c:forEach>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </section>

                    <section class="officer-card officer-chart">
                        <div class="officer-card-header">
                            <div>
                                <div class="officer-card-title">Least Applied Programmes</div>
                                <div class="officer-card-subtitle">Lowest demand among programmes with submissions</div>
                            </div>
                        </div>
                        <div class="officer-card-body">
                            <c:choose>
                                <c:when test="${empty leastPopularProgrammes}">
                                    <div class="officer-empty">Least-demand trends will appear once applications are submitted.</div>
                                </c:when>
                                <c:otherwise>
                                    <div class="bar-list">
                                        <c:forEach var="programme" items="${leastPopularProgrammes}">
                                            <div class="bar-item">
                                                <div class="bar-header">
                                                    <span>${programme.programmeName}</span>
                                                    <span>${programme.totalApplied}</span>
                                                </div>
                                                <div class="bar-track">
                                                    <div class="bar-fill muted" style="width: ${programme.volumePercentage}%;"></div>
                                                </div>
                                            </div>
                                        </c:forEach>
                                    </div>
                                    <div class="officer-panel-note">Only programmes with at least one submitted application are shown here.</div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </section>
                </div>

                <div class="officer-section-label">Geography and Programme Breakdown</div>
                <div class="officer-grid-2">
                    <section class="officer-card officer-chart">
                        <div class="officer-card-header">
                            <div>
                                <div class="officer-card-title">County Origin of Applicants</div>
                                <div class="officer-card-subtitle">Top counties by submitted applications</div>
                            </div>
                        </div>
                        <div class="officer-card-body">
                            <c:choose>
                                <c:when test="${empty countyStats}">
                                    <div class="officer-empty">County distribution will appear after applicants submit profiles.</div>
                                </c:when>
                                <c:otherwise>
                                    <div class="county-list">
                                        <c:forEach var="county" items="${countyStats}">
                                            <div class="county-item">
                                                <div class="county-row">
                                                    <span>${county.county}</span>
                                                    <span class="county-count">${county.applicantCount}</span>
                                                </div>
                                                <div class="county-track">
                                                    <div class="county-fill"
                                                         style="width: ${maxCountyCount > 0 ? (county.applicantCount * 100.0 / maxCountyCount) : 0}%;">
                                                    </div>
                                                </div>
                                            </div>
                                        </c:forEach>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </section>

                    <section class="officer-card officer-chart">
                        <div class="officer-card-header">
                            <div>
                                <div class="officer-card-title">Programme Funnel Snapshot</div>
                                <div class="officer-card-subtitle">Decision split for the current programme list</div>
                            </div>
                        </div>
                        <div class="officer-card-body">
                            <div class="officer-meta-list">
                                <div class="officer-meta-row">
                                    <span>Submitted applications</span>
                                    <strong>${counts.submittedTotal}</strong>
                                </div>
                                <div class="officer-meta-row">
                                    <span>Accepted decisions</span>
                                    <strong>${counts.accepted}</strong>
                                </div>
                                <div class="officer-meta-row">
                                    <span>Rejected decisions</span>
                                    <strong>${counts.rejected}</strong>
                                </div>
                                <div class="officer-meta-row">
                                    <span>Still in the review queue</span>
                                    <strong>${counts.reviewQueueTotal}</strong>
                                </div>
                                <div class="officer-meta-row">
                                    <span>Profiles with disability declaration</span>
                                    <strong>${demographics.disabledCount}</strong>
                                </div>
                            </div>
                        </div>
                    </section>
                </div>

                <section class="officer-card" style="margin-top: 1rem;">
                    <div class="officer-card-header">
                        <div>
                            <div class="officer-card-title">Applicants Per Programme</div>
                            <div class="officer-card-subtitle">Ranked programme demand with decision split</div>
                        </div>
                    </div>
                    <div class="officer-card-body officer-table-shell">
                        <table class="officer-table programme-table">
                            <thead>
                                <tr>
                                    <th>#</th>
                                    <th>Programme</th>
                                    <th>School / Faculty</th>
                                    <th>Total Applied</th>
                                    <th>Accepted</th>
                                    <th>Rejected</th>
                                    <th>Pending</th>
                                    <th>Volume</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="programme" items="${programmeStats}">
                                    <tr>
                                        <td><span class="rank-badge ${programme.rankBadgeClass}">${programme.rank}</span></td>
                                        <td>${programme.programmeName}</td>
                                        <td class="table-meta">${programme.schoolName}</td>
                                        <td>${programme.totalApplied}</td>
                                        <td><span class="status-dot dot-green"></span>${programme.accepted}</td>
                                        <td><span class="status-dot dot-red"></span>${programme.rejected}</td>
                                        <td><span class="status-dot dot-orange"></span>${programme.pending}</td>
                                        <td>
                                            <div class="mini-bar">
                                                <div class="mini-fill" style="width: ${programme.volumePercentage}%;"></div>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </section>

                <div class="officer-section-label" id="applications">Applications</div>
                <section class="officer-card">
                    <div class="officer-card-header">
                        <div>
                            <div class="officer-card-title">Search and Filter</div>
                            <div class="officer-card-subtitle">Narrow the review queue without leaving the dashboard</div>
                        </div>
                    </div>
                    <div class="officer-card-body">
                        <form action="${pageContext.request.contextPath}/officer/dashboard" method="get">
                            <div class="officer-filter-grid">
                                <div class="form-group" style="margin-bottom: 0;">
                                    <label for="status">Status</label>
                                    <select id="status" name="status">
                                        <option value="ALL" ${status == 'ALL' or empty status ? 'selected' : ''}>All Applications</option>
                                        <option value="SUBMITTED" ${status == 'SUBMITTED' ? 'selected' : ''}>Pending Review</option>
                                        <option value="UNDER_REVIEW" ${status == 'UNDER_REVIEW' ? 'selected' : ''}>Under Review</option>
                                        <option value="ACCEPTED" ${status == 'ACCEPTED' ? 'selected' : ''}>Accepted</option>
                                        <option value="REJECTED" ${status == 'REJECTED' ? 'selected' : ''}>Rejected</option>
                                    </select>
                                </div>
                                <div class="form-group" style="margin-bottom: 0;">
                                    <label for="search">Search</label>
                                    <input type="text" id="search" name="search" value="${search}"
                                           placeholder="Search by applicant name or reference number">
                                </div>
                                <button type="submit" class="btn btn-primary">Apply Filters</button>
                                <a href="${pageContext.request.contextPath}/officer/dashboard" class="btn btn-secondary">Reset</a>
                            </div>
                        </form>
                    </div>
                </section>

                <section class="officer-card" style="margin-top: 1rem;">
                    <div class="officer-card-header">
                        <div>
                            <div class="officer-card-title">Applications Awaiting Action</div>
                            <div class="officer-card-subtitle">${totalCount} matching applications</div>
                        </div>
                    </div>
                    <div class="officer-card-body officer-table-shell">
                        <table class="officer-table">
                            <thead>
                                <tr>
                                    <th>Reference No.</th>
                                    <th>Applicant Name</th>
                                    <th>Programme</th>
                                    <th>Submitted</th>
                                    <th>Status</th>
                                    <th>Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:choose>
                                    <c:when test="${empty applications}">
                                        <tr>
                                            <td colspan="6">
                                                <div class="officer-empty">No applications match the current filters.</div>
                                            </td>
                                        </tr>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach var="app" items="${applications}">
                                            <tr>
                                                <td><span class="app-ref">${app.referenceNumber}</span></td>
                                                <td>${app.fullName}</td>
                                                <td>${app.programmeName}</td>
                                                <td>${app.submittedAtFormatted}</td>
                                                <td>
                                                    <span class="badge badge-${app.statusColor}">${app.statusDisplay}</span>
                                                </td>
                                                <td>
                                                    <a href="${pageContext.request.contextPath}/officer/review?id=${app.applicationId}"
                                                       class="btn btn-sm btn-primary">
                                                        ${app.status == 'ACCEPTED' or app.status == 'REJECTED' ? 'View' : 'Review'}
                                                    </a>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </c:otherwise>
                                </c:choose>
                            </tbody>
                        </table>
                    </div>
                </section>

                <c:if test="${totalPages > 1}">
                    <div class="officer-pagination">
                        <c:if test="${currentPage > 1}">
                            <a href="${pageContext.request.contextPath}/officer/dashboard?page=${currentPage - 1}&status=${status}&search=${search}"
                               class="page-link">Previous</a>
                        </c:if>

                        <c:forEach begin="1" end="${totalPages}" var="page">
                            <a href="${pageContext.request.contextPath}/officer/dashboard?page=${page}&status=${status}&search=${search}"
                               class="page-link ${page == currentPage ? 'active' : ''}">${page}</a>
                        </c:forEach>

                        <c:if test="${currentPage < totalPages}">
                            <a href="${pageContext.request.contextPath}/officer/dashboard?page=${currentPage + 1}&status=${status}&search=${search}"
                               class="page-link">Next</a>
                        </c:if>
                    </div>
                </c:if>
            </main>
        </div>
    </div>
</body>
</html>
