<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Step 2: Programme Selection - Egerton University AMS</title>
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
                    <div class="wizard-step completed">
                        <span class="step-number">1</span>
                        <span class="step-label">Personal</span>
                    </div>
                    <div class="wizard-step active">
                        <span class="step-number">2</span>
                        <span class="step-label">Programme</span>
                    </div>
                    <div class="wizard-step">
                        <span class="step-number">3</span>
                        <span class="step-label">Academics</span>
                    </div>
                    <div class="wizard-step">
                        <span class="step-number">4</span>
                        <span class="step-label">Documents</span>
                    </div>
                    <div class="wizard-step">
                        <span class="step-number">5</span>
                        <span class="step-label">Guardian</span>
                    </div>
                    <div class="wizard-step">
                        <span class="step-number">6</span>
                        <span class="step-label">Extra Info</span>
                    </div>
                    <div class="wizard-step">
                        <span class="step-number">7</span>
                        <span class="step-label">Submit</span>
                    </div>
                </div>
            </div>
            
            <div class="wizard-body">
                <h3 style="margin-bottom: 1.5rem; color: var(--primary-color);">
                    Step 2: Programme Selection
                </h3>
                
                <c:if test="${not empty error}">
                    <div class="alert alert-danger">${error}</div>
                </c:if>
                
                <form action="${pageContext.request.contextPath}/apply/programme" method="post" 
                      class="wizard-form needs-validation">
                    
                    <!-- Level and Category (Fixed) -->
                    <div class="form-row">
                        <div class="form-group">
                            <label>Level</label>
                            <input type="text" value="Undergraduate" readonly 
                                   style="background-color: var(--bg-light);">
                        </div>
                        <div class="form-group">
                            <label>Category</label>
                            <input type="text" value="Degree" readonly 
                                   style="background-color: var(--bg-light);">
                        </div>
                    </div>
                    
                    <!-- Programme Selection -->
                    <div class="form-group">
                        <label for="programmeId">Select Programme <span class="required">*</span></label>
                        <select id="programmeId" name="programmeId" required>
                            <option value="">-- Select a Programme --</option>
                            <c:forEach var="prog" items="${programmes}">
                                <option value="${prog.id}" 
                                    ${programme.programmeId == prog.id ? 'selected' : ''}>
                                    ${prog.programmeName}
                                </option>
                            </c:forEach>
                        </select>
                        <c:if test="${not empty errors.programmeId}">
                            <div class="field-error">${errors.programmeId}</div>
                        </c:if>
                    </div>
                    
                    <!-- Intake Selection -->
                    <div class="form-row">
                        <div class="form-group">
                            <label for="intakeYear">Intake Year <span class="required">*</span></label>
                            <select id="intakeYear" name="intakeYear" required>
                                <option value="">Select Year</option>
                                <c:forEach var="year" items="${intakeYears}">
                                    <option value="${year}" 
                                        ${programme.intakeYear == year or params.intakeYear == year ? 'selected' : ''}>
                                        ${year}
                                    </option>
                                </c:forEach>
                            </select>
                            <c:if test="${not empty errors.intakeYear}">
                                <div class="field-error">${errors.intakeYear}</div>
                            </c:if>
                        </div>
                        <div class="form-group">
                            <label for="intakeSemester">Intake Semester <span class="required">*</span></label>
                            <select id="intakeSemester" name="intakeSemester" required>
                                <option value="">Select Semester</option>
                                <option value="SEPTEMBER" 
                                    ${programme.intakeSemester == 'SEPTEMBER' or params.intakeSemester == 'SEPTEMBER' ? 'selected' : ''}>
                                    September
                                </option>
                                <option value="JANUARY" 
                                    ${programme.intakeSemester == 'JANUARY' or params.intakeSemester == 'JANUARY' ? 'selected' : ''}>
                                    January
                                </option>
                            </select>
                            <c:if test="${not empty errors.intakeSemester}">
                                <div class="field-error">${errors.intakeSemester}</div>
                            </c:if>
                        </div>
                    </div>
                    
                    <!-- Study Mode -->
                    <div class="form-row">
                        <div class="form-group">
                            <label for="studyMode">Study Mode <span class="required">*</span></label>
                            <select id="studyMode" name="studyMode" required>
                                <option value="">Select Study Mode</option>
                                <option value="FULL_TIME" 
                                    ${programme.studyMode == 'FULL_TIME' or params.studyMode == 'FULL_TIME' ? 'selected' : ''}>
                                    Full-time
                                </option>
                                <option value="PART_TIME" 
                                    ${programme.studyMode == 'PART_TIME' or params.studyMode == 'PART_TIME' ? 'selected' : ''}>
                                    Part-time
                                </option>
                            </select>
                            <c:if test="${not empty errors.studyMode}">
                                <div class="field-error">${errors.studyMode}</div>
                            </c:if>
                        </div>
                        <div class="form-group">
                            <label>Campus</label>
                            <input type="text" value="Main Campus (Njoro)" readonly 
                                   style="background-color: var(--bg-light);">
                        </div>
                    </div>
                    
                    <div class="form-actions">
                        <a href="${pageContext.request.contextPath}/apply/personal" class="btn btn-secondary btn-lg">
                            ← Back
                        </a>
                        <button type="submit" class="btn btn-primary btn-lg">
                            Save & Continue →
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
