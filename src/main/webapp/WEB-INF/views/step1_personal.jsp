<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Step 1: Personal Information - Egerton University AMS</title>
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
                    <div class="wizard-step active">
                        <span class="step-number">1</span>
                        <span class="step-label">Personal</span>
                    </div>
                    <div class="wizard-step">
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
                    Step 1: Personal Information
                </h3>
                
                <c:if test="${not empty error}">
                    <div class="alert alert-danger">${error}</div>
                </c:if>
                
                <form action="${pageContext.request.contextPath}/apply/personal" method="post" 
                      enctype="multipart/form-data" class="wizard-form needs-validation">
                    
                    <!-- Name Fields -->
                    <div class="form-row">
                        <div class="form-group">
                            <label for="firstName">First Name <span class="required">*</span></label>
                            <input type="text" id="firstName" name="firstName" 
                                   value="${personal.firstName}" required maxlength="100">
                            <c:if test="${not empty errors.firstName}">
                                <div class="field-error">${errors.firstName}</div>
                            </c:if>
                        </div>
                        <div class="form-group">
                            <label for="middleName">Middle Name</label>
                            <input type="text" id="middleName" name="middleName" 
                                   value="${personal.middleName}" maxlength="100">
                        </div>
                        <div class="form-group">
                            <label for="lastName">Last Name <span class="required">*</span></label>
                            <input type="text" id="lastName" name="lastName" 
                                   value="${personal.lastName}" required maxlength="100">
                            <c:if test="${not empty errors.lastName}">
                                <div class="field-error">${errors.lastName}</div>
                            </c:if>
                        </div>
                    </div>
                    
                    <!-- DOB and Gender -->
                    <div class="form-row">
                        <div class="form-group">
                            <label for="dateOfBirth">Date of Birth <span class="required">*</span></label>
                            <input type="date" id="dateOfBirth" name="dateOfBirth" 
                                   value="${personal.dateOfBirth}" required>
                            <c:if test="${not empty errors.dateOfBirth}">
                                <div class="field-error">${errors.dateOfBirth}</div>
                            </c:if>
                        </div>
                        <div class="form-group">
                            <label>Gender <span class="required">*</span></label>
                            <div class="radio-group" style="margin-top: 0.5rem;">
                                <label class="radio-option">
                                    <input type="radio" name="gender" value="MALE" 
                                           ${personal.gender == 'MALE' ? 'checked' : ''} required>
                                    Male
                                </label>
                                <label class="radio-option">
                                    <input type="radio" name="gender" value="FEMALE"
                                           ${personal.gender == 'FEMALE' ? 'checked' : ''}>
                                    Female
                                </label>
                            </div>
                            <c:if test="${not empty errors.gender}">
                                <div class="field-error">${errors.gender}</div>
                            </c:if>
                        </div>
                        <div class="form-group">
                            <label for="nationality">Nationality <span class="required">*</span></label>
                            <input type="text" id="nationality" name="nationality" 
                                   value="${not empty personal.nationality ? personal.nationality : 'Kenyan'}" 
                                   required maxlength="100">
                        </div>
                    </div>
                    
                    <!-- ID Numbers -->
                    <div class="form-row">
                        <div class="form-group">
                            <label for="nationalId">National ID Number</label>
                            <div class="input-wrapper">
                                <input type="text" id="nationalId" name="nationalId" 
                                       value="${personal.nationalId}" maxlength="8"
                                       pattern="[0-9]{8}" inputmode="numeric">
                            </div>
                            <p class="help-text">Optional - exactly 8 digits if provided</p>
                            <c:if test="${not empty errors.nationalId}">
                                <div class="field-error">${errors.nationalId}</div>
                            </c:if>
                        </div>
                        <div class="form-group">
                            <label for="birthCertNumber">Birth Certificate Number <span class="required">*</span></label>
                            <input type="text" id="birthCertNumber" name="birthCertNumber" 
                                   value="${personal.birthCertNumber}" required maxlength="7"
                                   pattern="[0-9]{7}" inputmode="numeric">
                            <p class="help-text">Exactly 7 digits</p>
                            <c:if test="${not empty errors.birthCertNumber}">
                                <div class="field-error">${errors.birthCertNumber}</div>
                            </c:if>
                        </div>
                    </div>
                    
                    <!-- Contact -->
                    <div class="form-row">
                        <div class="form-group">
                            <label for="phoneNumber">Phone Number <span class="required">*</span></label>
                            <div class="input-wrapper">
                                <span class="input-prefix">+254</span>
                                <input type="text" id="phoneNumber" name="phoneNumber" 
                                       value="${personal.phoneNumberShort}" class="has-prefix"
                                       required maxlength="9" pattern="[0-9]{9}" inputmode="numeric"
                                       placeholder="712345678">
                            </div>
                            <p class="help-text">Exactly 9 digits after +254</p>
                            <c:if test="${not empty errors.phoneNumber}">
                                <div class="field-error">${errors.phoneNumber}</div>
                            </c:if>
                        </div>
                        <div class="form-group">
                            <label for="email">Email Address <span class="required">*</span></label>
                            <input type="email" id="email" name="email" 
                                   value="${personal.email}" required maxlength="255">
                            <c:if test="${not empty errors.email}">
                                <div class="field-error">${errors.email}</div>
                            </c:if>
                        </div>
                    </div>
                    
                    <!-- Location -->
                    <div class="form-row">
                        <div class="form-group">
                            <label for="county">County <span class="required">*</span></label>
                            <select id="county" name="county" required>
                                <option value="">Select County</option>
                                <c:forEach var="county" items="${counties}">
                                    <option value="${county}" ${personal.county == county ? 'selected' : ''}>
                                        ${county}
                                    </option>
                                </c:forEach>
                            </select>
                            <c:if test="${not empty errors.county}">
                                <div class="field-error">${errors.county}</div>
                            </c:if>
                        </div>
                        <div class="form-group">
                            <label for="town">Town/Location <span class="required">*</span></label>
                            <input type="text" id="town" name="town" 
                                   value="${personal.town}" required maxlength="100">
                            <c:if test="${not empty errors.town}">
                                <div class="field-error">${errors.town}</div>
                            </c:if>
                        </div>
                    </div>
                    
                    <!-- Photo Upload -->
                    <div class="form-group">
                        <label for="photo">Passport Photo <span class="required">*</span></label>
                        <div class="file-input-wrapper">
                            <input type="file" id="photo" name="photo" 
                                   accept="image/jpeg,image/png,.jpg,.jpeg,.png">
                            <div class="file-input-button">
                                <span>Choose Photo</span>
                            </div>
                        </div>
                        <p class="help-text">JPG or PNG, max 2MB. Clear passport-size photo with white background.</p>
                        <c:if test="${not empty personal.photoPath}">
                            <div class="file-preview">
                                <span style="color: var(--success-color);">✓</span>
                                <span>Photo uploaded</span>
                            </div>
                        </c:if>
                        <c:if test="${not empty errors.photo}">
                            <div class="field-error">${errors.photo}</div>
                        </c:if>
                    </div>
                    
                    <div class="form-actions">
                        <span></span>
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
