<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Step 7: Review & Submit - Egerton University AMS</title>
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
        <div class="wizard-container">
            <div class="wizard-header">
                <h2 class="wizard-title">Application Form</h2>
                <div class="wizard-steps">
                    <div class="wizard-step completed"><span class="step-number">1</span><span class="step-label">Personal</span></div>
                    <div class="wizard-step completed"><span class="step-number">2</span><span class="step-label">Programme</span></div>
                    <div class="wizard-step completed"><span class="step-number">3</span><span class="step-label">Academics</span></div>
                    <div class="wizard-step completed"><span class="step-number">4</span><span class="step-label">Documents</span></div>
                    <div class="wizard-step completed"><span class="step-number">5</span><span class="step-label">Guardian</span></div>
                    <div class="wizard-step completed"><span class="step-number">6</span><span class="step-label">Extra Info</span></div>
                    <div class="wizard-step active"><span class="step-number">7</span><span class="step-label">Submit</span></div>
                </div>
            </div>
            
            <div class="wizard-body">
                <h3 style="margin-bottom: 1.5rem; color: var(--primary-color);">
                    Step 7: Review & Submit
                </h3>
                
                <c:if test="${not empty error}">
                    <div class="alert alert-danger">${error}</div>
                </c:if>
                
                <c:if test="${not isComplete}">
                    <div class="alert alert-warning">
                        <strong>Application Incomplete</strong>
                        <p>Please complete all required steps before submitting your application.</p>
                    </div>
                </c:if>
                
                <!-- Personal Information Review -->
                <div class="review-section">
                    <h3>1. Personal Information 
                        <a href="${pageContext.request.contextPath}/apply/personal" class="btn btn-sm btn-secondary" style="float: right;">Edit</a>
                    </h3>
                    <div class="review-grid">
                        <div class="review-item">
                            <div class="review-item-label">Full Name</div>
                            <div class="review-item-value">${appBean.personal.fullName}</div>
                        </div>
                        <div class="review-item">
                            <div class="review-item-label">Date of Birth</div>
                            <div class="review-item-value">${appBean.personal.dateOfBirth}</div>
                        </div>
                        <div class="review-item">
                            <div class="review-item-label">Gender</div>
                            <div class="review-item-value">${appBean.personal.genderDisplay}</div>
                        </div>
                        <div class="review-item">
                            <div class="review-item-label">Phone Number</div>
                            <div class="review-item-value">${appBean.personal.phoneNumber}</div>
                        </div>
                        <div class="review-item">
                            <div class="review-item-label">Email</div>
                            <div class="review-item-value">${appBean.personal.email}</div>
                        </div>
                        <div class="review-item">
                            <div class="review-item-label">County/Town</div>
                            <div class="review-item-value">${appBean.personal.county}, ${appBean.personal.town}</div>
                        </div>
                    </div>
                </div>
                
                <!-- Programme Selection Review -->
                <div class="review-section">
                    <h3>2. Programme Selection
                        <a href="${pageContext.request.contextPath}/apply/programme" class="btn btn-sm btn-secondary" style="float: right;">Edit</a>
                    </h3>
                    <div class="review-grid">
                        <div class="review-item" style="grid-column: span 2;">
                            <div class="review-item-label">Programme</div>
                            <div class="review-item-value">${appBean.programme.programmeName}</div>
                        </div>
                        <div class="review-item">
                            <div class="review-item-label">Study Mode</div>
                            <div class="review-item-value">${appBean.programme.studyModeDisplay}</div>
                        </div>
                        <div class="review-item">
                            <div class="review-item-label">Intake</div>
                            <div class="review-item-value">${appBean.programme.intakeYear} ${appBean.programme.intakeSemester}</div>
                        </div>
                    </div>
                </div>
                
                <!-- Academic Background Review -->
                <div class="review-section">
                    <h3>3. Academic Background (KCSE)
                        <a href="${pageContext.request.contextPath}/apply/academics" class="btn btn-sm btn-secondary" style="float: right;">Edit</a>
                    </h3>
                    <div class="review-grid">
                        <div class="review-item">
                            <div class="review-item-label">Index Number</div>
                            <div class="review-item-value">${appBean.academics.kcseIndexNumber}</div>
                        </div>
                        <div class="review-item">
                            <div class="review-item-label">Year of Exam</div>
                            <div class="review-item-value">${appBean.academics.yearOfExam}</div>
                        </div>
                        <div class="review-item">
                            <div class="review-item-label">School</div>
                            <div class="review-item-value">${appBean.academics.schoolName}</div>
                        </div>
                        <div class="review-item">
                            <div class="review-item-label">Overall Grade</div>
                            <div class="review-item-value" style="font-size: 1.25rem; font-weight: bold; color: var(--primary-color);">
                                ${appBean.academics.overallGradeDisplay}
                            </div>
                        </div>
                    </div>
                    <div style="margin-top: 1rem;">
                        <strong>Subject Grades:</strong>
                        <div style="display: flex; flex-wrap: wrap; gap: 0.5rem; margin-top: 0.5rem;">
                            <c:forEach var="sg" items="${appBean.subjectGrades}">
                                <span class="badge badge-submitted">${sg.subjectName}: ${sg.grade}</span>
                            </c:forEach>
                        </div>
                    </div>
                </div>
                
                <!-- Documents Review -->
                <div class="review-section">
                    <h3>4. Documents
                        <a href="${pageContext.request.contextPath}/apply/documents" class="btn btn-sm btn-secondary" style="float: right;">Edit</a>
                    </h3>
                    <div class="review-grid">
                        <div class="review-item">
                            <div class="review-item-label">KCSE Certificate</div>
                            <div class="review-item-value">
                                <c:choose>
                                    <c:when test="${not empty appBean.documents.kcseCertPath}">
                                        <span style="color: var(--success-color);">✓ Uploaded</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span style="color: var(--danger-color);">✗ Not uploaded</span>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                        <div class="review-item">
                            <div class="review-item-label">Birth Certificate</div>
                            <div class="review-item-value">
                                <c:choose>
                                    <c:when test="${not empty appBean.documents.birthCertPath}">
                                        <span style="color: var(--success-color);">✓ Uploaded</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span style="color: var(--danger-color);">✗ Not uploaded</span>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>
                </div>
                
                <!-- Guardian Review -->
                <div class="review-section">
                    <h3>5. Guardian Information
                        <a href="${pageContext.request.contextPath}/apply/guardian" class="btn btn-sm btn-secondary" style="float: right;">Edit</a>
                    </h3>
                    <c:forEach var="guardian" items="${appBean.guardians}">
                        <div class="review-grid" style="margin-bottom: 1rem;">
                            <div class="review-item">
                                <div class="review-item-label">${guardian.primary ? 'Primary' : 'Secondary'} Guardian</div>
                                <div class="review-item-value">${guardian.fullName}</div>
                            </div>
                            <div class="review-item">
                                <div class="review-item-label">Relationship</div>
                                <div class="review-item-value">${guardian.relationshipDisplay}</div>
                            </div>
                            <div class="review-item">
                                <div class="review-item-label">Phone</div>
                                <div class="review-item-value">${guardian.phoneNumber}</div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
                
                <!-- Submit Form -->
                <form id="submit-form" action="${pageContext.request.contextPath}/apply/submit" method="post">
                    <div class="card" style="margin-top: 2rem; border: 2px solid var(--primary-color);">
                        <div class="card-header" style="background-color: var(--primary-color); color: white;">
                            Declaration
                        </div>
                        <div class="card-body">
                            <label class="checkbox-option" style="font-size: 0.95rem; line-height: 1.6;">
                                <input type="checkbox" id="declaration" name="declaration" required>
                                I declare that the information provided in this application is true and accurate to the best of my knowledge. 
                                I understand that providing false information may result in the cancellation of my application or admission. 
                                I have read and agree to abide by the rules and regulations of Egerton University.
                            </label>
                        </div>
                    </div>
                    
                    <div class="form-actions">
                        <a href="${pageContext.request.contextPath}/apply/extra" class="btn btn-secondary btn-lg">
                            ← Back
                        </a>
                        <button type="submit" class="btn btn-success btn-lg" ${not isComplete ? 'disabled' : ''}>
                            Submit Application
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </main>

    <script src="${pageContext.request.contextPath}/assets/js/validate.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/wizard.js"></script>
</body>
</html>
