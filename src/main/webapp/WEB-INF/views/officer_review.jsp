<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Review Application - Egerton University AMS</title>
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
                    <a href="${pageContext.request.contextPath}/officer/dashboard" class="officer-nav-link">
                        <span class="officer-nav-key">D</span>
                        <span>Dashboard</span>
                    </a>
                    <a href="${pageContext.request.contextPath}/officer/dashboard#applications" class="officer-nav-link active">
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
                        <h1 class="officer-page-title">Application Review</h1>
                        <p class="officer-page-subtitle">
                            Detailed applicant profile for officer review and decision making.
                        </p>
                    </div>
                    <div class="officer-page-actions">
                        <span class="officer-status-pill badge-${summary.statusColor}">${summary.statusDisplay}</span>
                        <a href="${pageContext.request.contextPath}/officer/dashboard#applications" class="btn btn-secondary">Back to Dashboard</a>
                    </div>
                </div>

                <c:if test="${not empty error}">
                    <div class="alert alert-danger">${error}</div>
                </c:if>

                <c:if test="${not empty param.error}">
                    <div class="alert alert-danger">${param.error}</div>
                </c:if>

                <div class="review-layout">
                    <div class="review-stack">
                        <section class="officer-card">
                            <div class="officer-card-header">
                                <div>
                                    <div class="officer-card-title">Personal Information</div>
                                    <div class="officer-card-subtitle">Identity and contact details</div>
                                </div>
                            </div>
                            <div class="officer-card-body">
                                <div class="review-grid-dark">
                                    <div class="review-item">
                                        <div class="review-item-label">Full Name</div>
                                        <div class="review-item-value">${personal.fullName}</div>
                                    </div>
                                    <div class="review-item">
                                        <div class="review-item-label">Date of Birth</div>
                                        <div class="review-item-value">${personal.dateOfBirth}</div>
                                    </div>
                                    <div class="review-item">
                                        <div class="review-item-label">Gender</div>
                                        <div class="review-item-value">${personal.genderDisplay}</div>
                                    </div>
                                    <div class="review-item">
                                        <div class="review-item-label">Nationality</div>
                                        <div class="review-item-value">${personal.nationality}</div>
                                    </div>
                                    <div class="review-item">
                                        <div class="review-item-label">National ID</div>
                                        <div class="review-item-value">${not empty personal.nationalId ? personal.nationalId : 'N/A'}</div>
                                    </div>
                                    <div class="review-item">
                                        <div class="review-item-label">Birth Certificate No.</div>
                                        <div class="review-item-value">${personal.birthCertNumber}</div>
                                    </div>
                                    <div class="review-item">
                                        <div class="review-item-label">Phone Number</div>
                                        <div class="review-item-value">${personal.phoneNumber}</div>
                                    </div>
                                    <div class="review-item">
                                        <div class="review-item-label">Email</div>
                                        <div class="review-item-value">${personal.email}</div>
                                    </div>
                                    <div class="review-item">
                                        <div class="review-item-label">County</div>
                                        <div class="review-item-value">${personal.county}</div>
                                    </div>
                                    <div class="review-item">
                                        <div class="review-item-label">Town</div>
                                        <div class="review-item-value">${personal.town}</div>
                                    </div>
                                </div>
                            </div>
                        </section>

                        <section class="officer-card">
                            <div class="officer-card-header">
                                <div>
                                    <div class="officer-card-title">Programme Selection</div>
                                    <div class="officer-card-subtitle">Applicant intake and study mode</div>
                                </div>
                            </div>
                            <div class="officer-card-body">
                                <div class="review-grid-dark">
                                    <div class="review-item">
                                        <div class="review-item-label">Programme</div>
                                        <div class="review-item-value">${programme.programmeName}</div>
                                    </div>
                                    <div class="review-item">
                                        <div class="review-item-label">Level</div>
                                        <div class="review-item-value">${programme.levelDisplay}</div>
                                    </div>
                                    <div class="review-item">
                                        <div class="review-item-label">Category</div>
                                        <div class="review-item-value">${programme.categoryDisplay}</div>
                                    </div>
                                    <div class="review-item">
                                        <div class="review-item-label">Study Mode</div>
                                        <div class="review-item-value">${programme.studyModeDisplay}</div>
                                    </div>
                                    <div class="review-item">
                                        <div class="review-item-label">Campus</div>
                                        <div class="review-item-value">${programme.campus}</div>
                                    </div>
                                    <div class="review-item">
                                        <div class="review-item-label">Intake</div>
                                        <div class="review-item-value">${programme.intakeYear} ${programme.intakeSemester}</div>
                                    </div>
                                </div>
                            </div>
                        </section>

                        <section class="officer-card">
                            <div class="officer-card-header">
                                <div>
                                    <div class="officer-card-title">Academic Background</div>
                                    <div class="officer-card-subtitle">KCSE profile and subject performance</div>
                                </div>
                            </div>
                            <div class="officer-card-body">
                                <div class="review-grid-dark">
                                    <div class="review-item">
                                        <div class="review-item-label">Index Number</div>
                                        <div class="review-item-value">${academics.kcseIndexNumber}</div>
                                    </div>
                                    <div class="review-item">
                                        <div class="review-item-label">Year of Exam</div>
                                        <div class="review-item-value">${academics.yearOfExam}</div>
                                    </div>
                                    <div class="review-item">
                                        <div class="review-item-label">School</div>
                                        <div class="review-item-value">${academics.schoolName}</div>
                                    </div>
                                    <div class="review-item">
                                        <div class="review-item-label">School Type</div>
                                        <div class="review-item-value">${academics.schoolTypeDisplay}</div>
                                    </div>
                                    <div class="review-item">
                                        <div class="review-item-label">Overall Mean Grade</div>
                                        <div class="review-item-value review-accent">${academics.overallGradeDisplay}</div>
                                    </div>
                                </div>

                                <div style="margin-top: 1rem;">
                                    <div class="officer-card-subtitle" style="margin-bottom: 0.7rem;">Subject grades</div>
                                    <div class="officer-table-shell">
                                        <table class="officer-table">
                                            <thead>
                                                <tr>
                                                    <th>#</th>
                                                    <th>Subject</th>
                                                    <th>Grade</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:forEach var="sg" items="${subjectGrades}">
                                                    <tr>
                                                        <td>${sg.subjectOrder}</td>
                                                        <td>${sg.subjectName}</td>
                                                        <td><strong>${sg.grade}</strong></td>
                                                    </tr>
                                                </c:forEach>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </section>

                        <section class="officer-card">
                            <div class="officer-card-header">
                                <div>
                                    <div class="officer-card-title">Uploaded Documents</div>
                                    <div class="officer-card-subtitle">Supporting records submitted by the applicant</div>
                                </div>
                            </div>
                            <div class="officer-card-body">
                                <div class="review-grid-dark">
                                    <div class="review-item">
                                        <div class="review-item-label">KCSE Certificate</div>
                                        <div class="review-item-value">
                                            <c:choose>
                                                <c:when test="${not empty documents and not empty documents.kcseCertPath}">
                                                    <a href="${pageContext.request.contextPath}${documents.kcseCertPath}" target="_blank" class="btn btn-sm btn-secondary">View Document</a>
                                                </c:when>
                                                <c:otherwise>Not uploaded</c:otherwise>
                                            </c:choose>
                                        </div>
                                    </div>
                                    <div class="review-item">
                                        <div class="review-item-label">Birth Certificate</div>
                                        <div class="review-item-value">
                                            <c:choose>
                                                <c:when test="${not empty documents and not empty documents.birthCertPath}">
                                                    <a href="${pageContext.request.contextPath}${documents.birthCertPath}" target="_blank" class="btn btn-sm btn-secondary">View Document</a>
                                                </c:when>
                                                <c:otherwise>Not uploaded</c:otherwise>
                                            </c:choose>
                                        </div>
                                    </div>
                                    <div class="review-item">
                                        <div class="review-item-label">National ID</div>
                                        <div class="review-item-value">
                                            <c:choose>
                                                <c:when test="${not empty documents and not empty documents.nationalIdPath}">
                                                    <a href="${pageContext.request.contextPath}${documents.nationalIdPath}" target="_blank" class="btn btn-sm btn-secondary">View Document</a>
                                                </c:when>
                                                <c:otherwise>Not provided</c:otherwise>
                                            </c:choose>
                                        </div>
                                    </div>
                                    <div class="review-item">
                                        <div class="review-item-label">Application Summary PDF</div>
                                        <div class="review-item-value">
                                            <c:choose>
                                                <c:when test="${not empty documents and not empty documents.summaryPdfPath}">
                                                    <a href="${pageContext.request.contextPath}${documents.summaryPdfPath}" target="_blank" class="btn btn-sm btn-primary">View PDF Summary</a>
                                                </c:when>
                                                <c:otherwise>Not generated</c:otherwise>
                                            </c:choose>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </section>

                        <section class="officer-card">
                            <div class="officer-card-header">
                                <div>
                                    <div class="officer-card-title">Guardian Information</div>
                                    <div class="officer-card-subtitle">Primary and secondary contacts</div>
                                </div>
                            </div>
                            <div class="officer-card-body">
                                <c:choose>
                                    <c:when test="${empty guardians}">
                                        <div class="officer-empty">No guardian details were provided.</div>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="review-grid-dark">
                                            <c:forEach var="guardian" items="${guardians}">
                                                <div class="review-item">
                                                    <div class="review-item-label">${guardian.primary ? 'Primary Guardian' : 'Secondary Guardian'}</div>
                                                    <div class="review-item-value">${guardian.fullName}</div>
                                                    <div class="officer-card-subtitle" style="margin-top: 0.45rem;">
                                                        ${guardian.relationshipDisplay} | ${guardian.phoneNumber}
                                                    </div>
                                                </div>
                                            </c:forEach>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </section>

                        <c:if test="${not empty extra}">
                            <section class="officer-card">
                                <div class="officer-card-header">
                                    <div>
                                        <div class="officer-card-title">Additional Information</div>
                                        <div class="officer-card-subtitle">Special needs and supporting notes</div>
                                    </div>
                                </div>
                                <div class="officer-card-body">
                                    <div class="review-grid-dark">
                                        <div class="review-item">
                                            <div class="review-item-label">Special Needs / Disability</div>
                                            <div class="review-item-value">${extra.hasDisabilityDisplay}</div>
                                        </div>
                                        <c:if test="${extra.hasDisability and not empty extra.disabilityDescription}">
                                            <div class="review-item">
                                                <div class="review-item-label">Description</div>
                                                <div class="review-item-value">${extra.disabilityDescription}</div>
                                            </div>
                                        </c:if>
                                    </div>
                                </div>
                            </section>
                        </c:if>
                    </div>

                    <aside class="review-summary">
                        <section class="officer-card review-highlight">
                            <div class="officer-card-body">
                                <div class="review-item-label">Reference Number</div>
                                <div class="review-ref">${summary.referenceNumber}</div>
                                <div class="officer-meta-list" style="margin-top: 1rem;">
                                    <div class="officer-meta-row">
                                        <span>Applicant</span>
                                        <strong>${summary.fullName}</strong>
                                    </div>
                                    <div class="officer-meta-row">
                                        <span>Programme</span>
                                        <strong>${summary.programmeName}</strong>
                                    </div>
                                    <div class="officer-meta-row">
                                        <span>Submitted</span>
                                        <strong>${summary.submittedAtFormatted}</strong>
                                    </div>
                                    <div class="officer-meta-row">
                                        <span>Officer</span>
                                        <strong>${not empty summary.reviewingOfficer ? summary.reviewingOfficer : 'Not assigned yet'}</strong>
                                    </div>
                                </div>
                            </div>
                        </section>

                        <section class="officer-card">
                            <div class="officer-card-header">
                                <div>
                                    <div class="officer-card-title">Review Summary</div>
                                    <div class="officer-card-subtitle">Current workflow position for this application</div>
                                </div>
                            </div>
                            <div class="officer-card-body">
                                <div class="officer-meta-list">
                                    <div class="officer-meta-row">
                                        <span>Status</span>
                                        <strong>${summary.statusDisplay}</strong>
                                    </div>
                                    <div class="officer-meta-row">
                                        <span>Intake</span>
                                        <strong>${summary.intakeDisplay}</strong>
                                    </div>
                                    <div class="officer-meta-row">
                                        <span>Study Mode</span>
                                        <strong>${summary.studyModeDisplay}</strong>
                                    </div>
                                    <div class="officer-meta-row">
                                        <span>Decision Date</span>
                                        <strong>${summary.decidedAtFormatted}</strong>
                                    </div>
                                </div>
                            </div>
                        </section>

                        <section class="officer-card decision-panel ${summary.status == 'ACCEPTED' ? 'accepted' : summary.status == 'REJECTED' ? 'rejected' : ''}">
                            <div class="officer-card-header">
                                <div>
                                    <div class="officer-card-title">${decisionMade ? 'Recorded Decision' : 'Make Decision'}</div>
                                    <div class="officer-card-subtitle">
                                        ${decisionMade ? 'This application already has a final outcome.' : 'Accept or reject after reviewing the full profile.'}
                                    </div>
                                </div>
                            </div>
                            <div class="officer-card-body">
                                <c:choose>
                                    <c:when test="${decisionMade}">
                                        <div class="decision-banner ${summary.status == 'ACCEPTED' ? 'accepted' : 'rejected'}">
                                            ${summary.statusDisplay} was recorded on ${summary.decidedAtFormatted}.
                                        </div>
                                        <div class="officer-list">
                                            <div class="review-item">
                                                <div class="review-item-label">Officer Notes</div>
                                                <div class="review-item-value">${not empty summary.officerNotes ? summary.officerNotes : 'No internal notes were recorded.'}</div>
                                            </div>
                                            <div class="review-item">
                                                <div class="review-item-label">Decision Message</div>
                                                <div class="review-item-value">${summary.decisionMessage}</div>
                                            </div>
                                            <div class="review-item">
                                                <div class="review-item-label">Decided By</div>
                                                <div class="review-item-value">${summary.reviewingOfficer}</div>
                                            </div>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <form action="${pageContext.request.contextPath}/officer/review" method="post">
                                            <input type="hidden" name="applicationId" value="${summary.applicationId}">
                                            <div class="form-group">
                                                <label for="officerNotes">Internal Notes</label>
                                                <textarea id="officerNotes" name="officerNotes" rows="5"
                                                          placeholder="Capture the review rationale, missing items, or decision notes."></textarea>
                                            </div>
                                            <div class="decision-actions">
                                                <button type="submit" name="action" value="accept"
                                                        class="btn btn-success btn-lg"
                                                        onclick="return confirm('Accept this application?');">
                                                    Accept Application
                                                </button>
                                                <button type="submit" name="action" value="reject"
                                                        class="btn btn-danger btn-lg"
                                                        onclick="return confirm('Reject this application?');">
                                                    Reject Application
                                                </button>
                                            </div>
                                        </form>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </section>
                    </aside>
                </div>
            </main>
        </div>
    </div>
</body>
</html>
