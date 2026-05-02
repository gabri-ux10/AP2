<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Step 5: Guardian Information - Egerton University AMS</title>
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
                    <div class="wizard-step active"><span class="step-number">5</span><span class="step-label">Guardian</span></div>
                    <div class="wizard-step"><span class="step-number">6</span><span class="step-label">Extra Info</span></div>
                    <div class="wizard-step"><span class="step-number">7</span><span class="step-label">Submit</span></div>
                </div>
            </div>
            
            <div class="wizard-body">
                <h3 style="margin-bottom: 1.5rem; color: var(--primary-color);">
                    Step 5: Guardian/Parent Information
                </h3>
                
                <c:if test="${not empty error}">
                    <div class="alert alert-danger">${error}</div>
                </c:if>
                
                <form action="${pageContext.request.contextPath}/apply/guardian" method="post" 
                      class="wizard-form needs-validation">
                    
                    <!-- Guardian 1 (Required) -->
                    <div class="card" style="margin-bottom: 1.5rem;">
                        <div class="card-header">
                            Primary Guardian/Parent <span class="required">*</span>
                        </div>
                        <div class="card-body">
                            <div class="form-row">
                                <div class="form-group">
                                    <label for="guardian1Name">Full Name <span class="required">*</span></label>
                                    <input type="text" id="guardian1Name" name="guardian1Name" 
                                           value="${guardian1.fullName}" required maxlength="255">
                                    <c:if test="${not empty errors.guardian1Name}">
                                        <div class="field-error">${errors.guardian1Name}</div>
                                    </c:if>
                                </div>
                                <div class="form-group">
                                    <label for="guardian1Relationship">Relationship <span class="required">*</span></label>
                                    <select id="guardian1Relationship" name="guardian1Relationship" required>
                                        <option value="">Select Relationship</option>
                                        <c:forEach var="rel" items="${relationships}">
                                            <option value="${rel}" ${guardian1.relationship == rel ? 'selected' : ''}>
                                                ${rel.name().charAt(0)}${rel.name().substring(1).toLowerCase()}
                                            </option>
                                        </c:forEach>
                                    </select>
                                    <c:if test="${not empty errors.guardian1Relationship}">
                                        <div class="field-error">${errors.guardian1Relationship}</div>
                                    </c:if>
                                </div>
                            </div>
                            <div class="form-group" style="max-width: 350px;">
                                <label for="guardian1Phone">Phone Number <span class="required">*</span></label>
                                <div class="input-wrapper">
                                    <span class="input-prefix">+254</span>
                                    <input type="text" id="guardian1Phone" name="guardian1Phone" 
                                           value="${guardian1.phoneNumberShort}" class="has-prefix"
                                           required maxlength="9" pattern="[0-9]{9}" inputmode="numeric"
                                           placeholder="712345678">
                                </div>
                                <p class="help-text">Exactly 9 digits after +254</p>
                                <c:if test="${not empty errors.guardian1Phone}">
                                    <div class="field-error">${errors.guardian1Phone}</div>
                                </c:if>
                            </div>
                        </div>
                    </div>
                    
                    <!-- Guardian 2 (Optional) -->
                    <div class="card">
                        <div class="card-header">
                            Secondary Guardian/Parent <span class="text-muted">(Optional)</span>
                        </div>
                        <div class="card-body">
                            <p class="help-text" style="margin-bottom: 1rem;">
                                Leave this section completely blank if you only want to provide one guardian.
                            </p>
                            <div class="form-row">
                                <div class="form-group">
                                    <label for="guardian2Name">Full Name</label>
                                    <input type="text" id="guardian2Name" name="guardian2Name" 
                                           value="${guardian2.fullName}" maxlength="255">
                                </div>
                                <div class="form-group">
                                    <label for="guardian2Relationship">Relationship</label>
                                    <select id="guardian2Relationship" name="guardian2Relationship">
                                        <option value="">Select Relationship</option>
                                        <c:forEach var="rel" items="${relationships}">
                                            <option value="${rel}" ${guardian2.relationship == rel ? 'selected' : ''}>
                                                ${rel.name().charAt(0)}${rel.name().substring(1).toLowerCase()}
                                            </option>
                                        </c:forEach>
                                    </select>
                                    <c:if test="${not empty errors.guardian2Relationship}">
                                        <div class="field-error">${errors.guardian2Relationship}</div>
                                    </c:if>
                                </div>
                            </div>
                            <div class="form-group" style="max-width: 350px;">
                                <label for="guardian2Phone">Phone Number</label>
                                <div class="input-wrapper">
                                    <span class="input-prefix">+254</span>
                                    <input type="text" id="guardian2Phone" name="guardian2Phone" 
                                           value="${guardian2.phoneNumberShort}" class="has-prefix"
                                           maxlength="9" pattern="[0-9]{9}" inputmode="numeric"
                                           placeholder="712345678">
                                </div>
                                <c:if test="${not empty errors.guardian2Phone}">
                                    <div class="field-error">${errors.guardian2Phone}</div>
                                </c:if>
                            </div>
                        </div>
                    </div>
                    
                    <div class="form-actions">
                        <a href="${pageContext.request.contextPath}/apply/documents" class="btn btn-secondary btn-lg">
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
