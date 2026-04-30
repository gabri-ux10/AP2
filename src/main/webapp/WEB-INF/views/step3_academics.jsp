<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Step 3: Academic Background - Egerton University AMS</title>
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
                    <div class="wizard-step active"><span class="step-number">3</span><span class="step-label">Academics</span></div>
                    <div class="wizard-step"><span class="step-number">4</span><span class="step-label">Documents</span></div>
                    <div class="wizard-step"><span class="step-number">5</span><span class="step-label">Guardian</span></div>
                    <div class="wizard-step"><span class="step-number">6</span><span class="step-label">Extra Info</span></div>
                    <div class="wizard-step"><span class="step-number">7</span><span class="step-label">Submit</span></div>
                </div>
            </div>
            
            <div class="wizard-body">
                <h3 style="margin-bottom: 1.5rem; color: var(--primary-color);">
                    Step 3: Academic Background (KCSE)
                </h3>
                
                <c:if test="${not empty error}">
                    <div class="alert alert-danger">${error}</div>
                </c:if>
                
                <form action="${pageContext.request.contextPath}/apply/academics" method="post" 
                      class="wizard-form needs-validation">
                    
                    <!-- KCSE Index Number -->
                    <div class="form-row">
                        <div class="form-group">
                            <label for="kcseIndexNumber">KCSE Index Number <span class="required">*</span></label>
                            <input type="text" id="kcseIndexNumber" name="kcseIndexNumber" 
                                   value="${academics.kcseIndexNumber}" required maxlength="11"
                                   pattern="[0-9]{11}" inputmode="numeric">
                            <p class="help-text">Exactly 11 digits</p>
                            <c:if test="${not empty errors.kcseIndexNumber}">
                                <div class="field-error">${errors.kcseIndexNumber}</div>
                            </c:if>
                        </div>
                        <div class="form-group">
                            <label for="yearOfExam">Year of Examination <span class="required">*</span></label>
                            <select id="yearOfExam" name="yearOfExam" required>
                                <option value="">Select Year</option>
                                <c:forEach var="year" items="${years}">
                                    <option value="${year}" ${academics.yearOfExam == year ? 'selected' : ''}>
                                        ${year}
                                    </option>
                                </c:forEach>
                            </select>
                            <c:if test="${not empty errors.yearOfExam}">
                                <div class="field-error">${errors.yearOfExam}</div>
                            </c:if>
                        </div>
                    </div>
                    
                    <!-- School Details -->
                    <div class="form-row">
                        <div class="form-group">
                            <label for="schoolName">School Name <span class="required">*</span></label>
                            <input type="text" id="schoolName" name="schoolName" 
                                   value="${academics.schoolName}" required maxlength="255">
                            <c:if test="${not empty errors.schoolName}">
                                <div class="field-error">${errors.schoolName}</div>
                            </c:if>
                        </div>
                        <div class="form-group">
                            <label for="schoolType">School Type <span class="required">*</span></label>
                            <select id="schoolType" name="schoolType" required>
                                <option value="">Select Type</option>
                                <option value="NATIONAL" ${academics.schoolType == 'NATIONAL' ? 'selected' : ''}>National</option>
                                <option value="EXTRA_COUNTY" ${academics.schoolType == 'EXTRA_COUNTY' ? 'selected' : ''}>Extra County</option>
                                <option value="COUNTY" ${academics.schoolType == 'COUNTY' ? 'selected' : ''}>County</option>
                                <option value="PRIVATE" ${academics.schoolType == 'PRIVATE' ? 'selected' : ''}>Private</option>
                                <option value="INTERNATIONAL" ${academics.schoolType == 'INTERNATIONAL' ? 'selected' : ''}>International</option>
                            </select>
                            <c:if test="${not empty errors.schoolType}">
                                <div class="field-error">${errors.schoolType}</div>
                            </c:if>
                        </div>
                    </div>
                    
                    <!-- Overall Grade -->
                    <div class="form-group" style="max-width: 300px;">
                        <label for="overallGrade">Overall Mean Grade <span class="required">*</span></label>
                        <select id="overallGrade" name="overallGrade" required>
                            <option value="">Select Grade</option>
                            <c:forEach var="grade" items="${grades}">
                                <option value="${grade}" ${academics.overallGradeDisplay == grade ? 'selected' : ''}>
                                    ${grade}
                                </option>
                            </c:forEach>
                        </select>
                        <c:if test="${not empty errors.overallGrade}">
                            <div class="field-error">${errors.overallGrade}</div>
                        </c:if>
                    </div>
                    
                    <!-- Subject Grades -->
                    <div style="margin-top: 2rem;">
                        <h4 style="margin-bottom: 1rem; color: var(--primary-color);">
                            Subject Grades <span class="required">*</span>
                        </h4>
                        <p class="help-text" style="margin-bottom: 1rem;">
                            Enter 7-8 subjects with their grades. All subjects shown on your KCSE certificate.
                        </p>
                        
                        <c:if test="${not empty errors.subjects}">
                            <div class="alert alert-danger">${errors.subjects}</div>
                        </c:if>
                        
                        <div id="subject-grades-container">
                            <c:choose>
                                <c:when test="${not empty subjectGrades}">
                                    <c:forEach var="sg" items="${subjectGrades}" varStatus="status">
                                        <div class="subject-row form-row">
                                            <div class="form-group">
                                                <label>Subject ${status.index + 1} <span class="required">*</span></label>
                                                <select name="subjectName" required>
                                                    <option value="">Select Subject</option>
                                                    <c:forEach var="sub" items="${subjects}">
                                                        <option value="${sub}" ${sg.subjectName == sub ? 'selected' : ''}>${sub}</option>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                            <div class="form-group">
                                                <label>Grade <span class="required">*</span></label>
                                                <select name="subjectGrade" required>
                                                    <option value="">Select Grade</option>
                                                    <c:forEach var="g" items="${grades}">
                                                        <option value="${g}" ${sg.grade == g ? 'selected' : ''}>${g}</option>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                            <div class="form-group" style="align-self: end;">
                                                <button type="button" class="btn btn-sm btn-danger remove-subject-btn">Remove</button>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach begin="1" end="7" varStatus="status">
                                        <div class="subject-row form-row">
                                            <div class="form-group">
                                                <label>Subject ${status.index} <span class="required">*</span></label>
                                                <select name="subjectName" required>
                                                    <option value="">Select Subject</option>
                                                    <c:forEach var="sub" items="${subjects}">
                                                        <option value="${sub}">${sub}</option>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                            <div class="form-group">
                                                <label>Grade <span class="required">*</span></label>
                                                <select name="subjectGrade" required>
                                                    <option value="">Select Grade</option>
                                                    <c:forEach var="g" items="${grades}">
                                                        <option value="${g}">${g}</option>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                            <div class="form-group" style="align-self: end;">
                                                <button type="button" class="btn btn-sm btn-danger remove-subject-btn" style="display:none;">Remove</button>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
                        </div>
                        
                        <button type="button" id="add-subject-btn" class="btn btn-secondary" style="margin-top: 1rem;">
                            + Add Another Subject
                        </button>
                    </div>
                    
                    <div class="form-actions">
                        <a href="${pageContext.request.contextPath}/apply/programme" class="btn btn-secondary btn-lg">
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

    <script>
        window.KCSE_SUBJECTS = [
            <c:forEach var="sub" items="${subjects}" varStatus="s">"${sub}"<c:if test="${!s.last}">,</c:if></c:forEach>
        ];
    </script>
    <script src="${pageContext.request.contextPath}/assets/js/validate.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/wizard.js"></script>
</body>
</html>
