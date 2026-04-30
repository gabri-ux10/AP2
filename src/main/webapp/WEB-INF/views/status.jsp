<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Application Status - Egerton University AMS</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
    <header class="header">
        <div class="container">
            <div class="header-content">
                <div class="header-logo">
                    <div>
                        <div class="header-title">EGERTON UNIVERSITY</div>
                        <div class="header-subtitle">Admission Management System</div>
                    </div>
                </div>
                <nav class="header-nav">
                    <a href="${pageContext.request.contextPath}/logout">Logout</a>
                </nav>
            </div>
        </div>
    </header>

    <main class="container">
        <div class="status-container">
            <c:choose>
                <c:when test="${noApplication}">
                    <div class="status-card">
                        <div class="status-header pending">
                            <div class="status-title">No Application Found</div>
                        </div>
                        <div class="status-body" style="text-align: center;">
                            <p>You haven't submitted an application yet.</p>
                            <a href="${pageContext.request.contextPath}/apply/personal" class="btn btn-primary btn-lg" style="margin-top: 1rem;">
                                Start Application
                            </a>
                        </div>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="status-card">
                        <div class="status-header ${status.status == 'ACCEPTED' ? 'accepted' : status.status == 'REJECTED' ? 'rejected' : status.status == 'UNDER_REVIEW' ? 'reviewing' : 'pending'}">
                            <div class="status-icon">
                                <c:choose>
                                    <c:when test="${status.status == 'ACCEPTED'}">✓</c:when>
                                    <c:when test="${status.status == 'REJECTED'}">✗</c:when>
                                    <c:when test="${status.status == 'UNDER_REVIEW'}">🔍</c:when>
                                    <c:otherwise>📋</c:otherwise>
                                </c:choose>
                            </div>
                            <div class="status-title">${status.statusDisplay}</div>
                        </div>
                        
                        <div class="status-body">
                            <div class="card" style="margin-bottom: 1.5rem;">
                                <div class="card-body">
                                    <div class="review-grid">
                                        <div class="review-item">
                                            <div class="review-item-label">Reference Number</div>
                                            <div class="review-item-value" style="font-weight: bold; color: var(--primary-color);">
                                                ${status.referenceNumber}
                                            </div>
                                        </div>
                                        <div class="review-item">
                                            <div class="review-item-label">Applicant Name</div>
                                            <div class="review-item-value">${status.fullName}</div>
                                        </div>
                                        <div class="review-item" style="grid-column: span 2;">
                                            <div class="review-item-label">Programme</div>
                                            <div class="review-item-value">${status.programmeName}</div>
                                        </div>
                                        <div class="review-item">
                                            <div class="review-item-label">Study Mode</div>
                                            <div class="review-item-value">${status.studyModeDisplay}</div>
                                        </div>
                                        <div class="review-item">
                                            <div class="review-item-label">Intake</div>
                                            <div class="review-item-value">${status.intakeYear}</div>
                                        </div>
                                        <div class="review-item">
                                            <div class="review-item-label">Submitted</div>
                                            <div class="review-item-value">${status.submittedAtFormatted}</div>
                                        </div>
                                        <c:if test="${not empty status.decidedAt}">
                                            <div class="review-item">
                                                <div class="review-item-label">Decision Date</div>
                                                <div class="review-item-value">${status.decidedAtFormatted}</div>
                                            </div>
                                        </c:if>
                                    </div>
                                </div>
                            </div>
                            
                            <c:if test="${not empty status.decisionMessage}">
                                <div class="alert ${status.status == 'ACCEPTED' ? 'alert-success' : 'alert-info'}">
                                    ${status.decisionMessage}
                                </div>
                            </c:if>
                            
                            <c:if test="${status.status == 'ACCEPTED'}">
                                <div class="alert alert-success" style="margin-top: 1rem;">
                                    <strong>Next Steps:</strong>
                                    <ol style="margin: 0.5rem 0 0 1.5rem;">
                                        <li>Check your email for the official admission letter</li>
                                        <li>Pay the required fees as per the fee structure</li>
                                        <li>Prepare all original documents for registration</li>
                                        <li>Report to the university on the specified date</li>
                                    </ol>
                                </div>
                            </c:if>
                            
                            <c:if test="${status.status == 'SUBMITTED' or status.status == 'UNDER_REVIEW'}">
                                <div class="alert alert-info" style="margin-top: 1rem;">
                                    <strong>Please Note:</strong>
                                    <p>Your application is being processed. You will receive an email notification once a decision is made. 
                                    Check back here regularly for updates.</p>
                                </div>
                            </c:if>
                            
                            <div style="text-align: center; margin-top: 2rem;">
                                <button onclick="window.print()" class="btn btn-secondary">
                                    Print Status
                                </button>
                            </div>
                        </div>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </main>

    <footer class="footer">
        <div class="container">
            <div class="footer-content">
                <p>&copy; 2025 Egerton University. All rights reserved.</p>
            </div>
        </div>
    </footer>
</body>
</html>
