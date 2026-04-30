om# Egerton University Admission Management System (AMS)

A full-featured web application for undergraduate admission management at Egerton University. Applicants complete a 7-step wizard to submit applications; Admission Officers review and make accept/reject decisions.

## Technology Stack

- **IDE**: Apache NetBeans (latest stable)
- **Build Tool**: Apache Maven (WAR packaging)
- **Web Server**: Apache Tomcat 10.x
- **Architecture**: MVC (Model/View/Controller)
- **Backend**: Java - Jakarta EE, Servlet API 5.0+
- **View Layer**: JSP (JavaServer Pages) + JSTL + HTML5 + CSS3 + Vanilla JavaScript
- **Database**: MySQL 8.x (JDBC with PreparedStatements)
- **Password Hashing**: jBCrypt
- **PDF Generation**: Apache PDFBox
- **File Uploads**: Apache Commons FileUpload
- **Email**: Jakarta Mail API

## Project Structure

```
EgertonAMS/
├── pom.xml                              # Maven configuration
├── egerton_ams_schema.sql               # Complete MySQL database schema
├── README.md                            # This file
└── src/main/
    ├── java/ke/ac/egerton/ams/
    │   ├── dao/                         # Data Access Objects
    │   │   ├── ApplicantDAO.java
    │   │   ├── ApplicationDAO.java
    │   │   ├── DocumentDAO.java
    │   │   ├── GuardianDAO.java
    │   │   ├── OfficerDAO.java
    │   │   └── ProgrammeDAO.java
    │   ├── filters/                     # Servlet Filters
    │   │   ├── AuthFilter.java
    │   │   └── EncodingFilter.java
    │   ├── models/                      # POJOs/Models
    │   │   ├── ApplicantAccount.java
    │   │   ├── ApplicantAcademics.java
    │   │   ├── ApplicantDocuments.java
    │   │   ├── ApplicantExtra.java
    │   │   ├── ApplicantGuardian.java
    │   │   ├── ApplicantPersonal.java
    │   │   ├── ApplicantProgramme.java
    │   │   ├── Application.java
    │   │   ├── ApplicationBean.java
    │   │   ├── ApplicationSummary.java
    │   │   ├── DashboardCounts.java
    │   │   ├── Intake.java
    │   │   ├── Officer.java
    │   │   ├── Programme.java
    │   │   ├── School.java
    │   │   └── SubjectGrade.java
    │   ├── servlets/                    # Servlet Controllers
    │   │   ├── AuthServlet.java
    │   │   ├── LogoutServlet.java
    │   │   ├── PersonalInfoServlet.java
    │   │   ├── ProgrammeServlet.java
    │   │   ├── AcademicServlet.java
    │   │   ├── DocumentServlet.java
    │   │   ├── GuardianServlet.java
    │   │   ├── ExtraInfoServlet.java
    │   │   ├── SubmitServlet.java
    │   │   ├── CongratulationsServlet.java
    │   │   ├── StatusServlet.java
    │   │   ├── OfficerLoginServlet.java
    │   │   ├── OfficerDashboardServlet.java
    │   │   └── OfficerReviewServlet.java
    │   └── util/                        # Utilities
    │       ├── DBConnection.java
    │       ├── EmailSender.java
    │       ├── FileUploadHandler.java
    │       ├── PDFGenerator.java
    │       └── Validator.java
    ├── resources/
    │   └── db.properties                # Database configuration
    └── webapp/
        ├── index.jsp                    # Landing page
        ├── assets/
        │   ├── css/style.css           # Main stylesheet
        │   └── js/
        │       ├── validate.js          # Client-side validation
        │       └── wizard.js            # Wizard functionality
        └── WEB-INF/
            ├── web.xml                  # Servlet configuration
            └── views/
                ├── login.jsp
                ├── register.jsp
                ├── step1_personal.jsp
                ├── step2_programme.jsp
                ├── step3_academics.jsp
                ├── step4_documents.jsp
                ├── step5_guardian.jsp
                ├── step6_extra.jsp
                ├── step7_review.jsp
                ├── congratulations.jsp
                ├── status.jsp
                ├── officer_login.jsp
                ├── officer_dashboard.jsp
                ├── officer_review.jsp
                └── error/
                    ├── 404.jsp
                    └── 500.jsp
```

## Setup Instructions

### 1. Database Setup

1. Install MySQL 8.x
2. Create the database and tables:
   ```bash
   mysql -u root -p < egerton_ams_schema.sql
   ```

### 2. Configuration

Edit `src/main/resources/db.properties`:

```properties
# Database
db.url=jdbc:mysql://localhost:3306/egerton_ams?useSSL=false&serverTimezone=UTC
db.username=your_username
db.password=your_password

# Email (for notifications)
mail.smtp.host=smtp.gmail.com
mail.smtp.port=587
mail.username=your_email@gmail.com
mail.password=your_app_password
```

### 3. Build & Deploy

## Setup Instructions

### 1. Clone Repository
\`\`\`bash
git clone https://github.com/YOUR_USERNAME/REPO_NAME.git
cd EgertonAMS
\`\`\`

### 2. Configure Database
1. Copy database configuration template:
   \`\`\`bash
   cp db.properties.template src/main/resources/db.properties
   \`\`\`

2. Edit \`src/main/resources/db.properties\` and add your credentials:
   - MySQL username & password
   - SMTP email credentials (for notifications)

### 3. Create Database
\`\`\`bash
mysql -u root -p < egerton_ams_schema.sql
\`\`\`

### 4. Run Application
\`\`\`bash
mvn jetty:run
\`\`\`

Access at: http://localhost:8081/ams/

### 4. Access the Application

- **Home Page**: http://localhost:8080/ams/
- **Applicant Portal**: http://localhost:8080/ams/auth
- **Officer Portal**: http://localhost:8080/ams/officer/login

### Default Officer Credentials

- **Email**: admin@egerton.ac.ke
- **Password**: officer123

## Application Features

### Applicant Side (7-Step Wizard)

1. **Personal Information**: Name, DOB, gender, national ID, birth certificate, phone, email, location, photo
2. **Programme Selection**: Choose from 54 undergraduate degree programmes
3. **Academic Background**: KCSE details, school info, overall grade, 7-8 subject grades
4. **Document Upload**: KCSE certificate, birth certificate, national ID (optional)
5. **Guardian Information**: 1 required + 1 optional guardian
6. **Extra Information**: Special needs/disability declaration
7. **Review & Submit**: Final review, declaration, submission

### Post-Submission

- Unique Reference Number generated (EGU-YYYY-NNNNNN format)
- Confirmation email sent
- Status page with live updates

### Officer Side

- Secure login with 3-attempt lockout
- Dashboard with status filters and search
- Full application review with all details
- Accept/Reject with notes
- Automatic status updates for applicants

## Validation Rules

Four critical fields have strict integer-only validation (client + server):

| Field | Requirement | Error Message |
|-------|-------------|---------------|
| National ID | Optional, exactly 8 digits | "National ID must be exactly 8 digits" |
| Birth Certificate | Required, exactly 7 digits | "Birth Certificate Number must be exactly 7 digits" |
| Phone Number | Required, +254 + 9 digits | "Phone number must be exactly 9 digits after +254" |
| KCSE Index Number | Required, exactly 11 digits | "KCSE Index Number must be exactly 11 digits" |

## Database Schema

- **12 Tables**: applicant_accounts, officers, schools, programmes, intakes, applications, applicant_personal, applicant_programme, applicant_academics, subject_grades, applicant_documents, applicant_guardians, applicant_extra
- **4 Views**: vw_dashboard_counts, vw_application_summary, vw_applicant_status, vw_subject_grades
- **4 Stored Procedures**: sp_generate_reference, sp_open_review, sp_accept_application, sp_reject_application
- **54 Undergraduate Programmes** pre-seeded

## KCSE Subjects Supported

Mathematics, English, Kiswahili, Biology, Chemistry, Physics, History & Government, Geography, CRE, IRE, Business Studies, Computer Studies, Agriculture, Home Science, Art & Design, Music, French

## Security Features

- BCrypt password hashing
- Session-based authentication
- Officer login lockout (3 failed attempts = 15 minute lock)
- HTTP-only session cookies
- Input validation (client + server)
- SQL injection prevention (PreparedStatements)

## License

Egerton University - Internal Use Only

---

*Built for Egerton University Admissions Office*
