<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Egerton University - Admission Management System</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
    <div class="landing-shell">
        <header class="landing-header">
            <div class="container landing-header-content">
                <div class="landing-brand">
                    <span class="landing-brand-name">EGERTON UNIVERSITY</span>
                    <span class="landing-brand-subtitle">Admission Management System</span>
                </div>

                <nav class="landing-nav" aria-label="Primary">
                    <a class="landing-nav-link" href="${pageContext.request.contextPath}/auth?action=login">
                        Applicant Login
                    </a>
                    <a class="landing-nav-link" href="${pageContext.request.contextPath}/auth?action=register">
                        Register
                    </a>
                    <a class="landing-nav-link landing-nav-link-highlight" href="${pageContext.request.contextPath}/officer/login">
                        Officer Portal
                    </a>
                </nav>
            </div>
        </header>

        <main>
            <section class="landing-hero">
                <div class="container landing-hero-content">
                    <div class="landing-copy">
                        <div class="landing-kicker">Undergraduate Admissions 2025</div>

                        <h1 class="landing-title">
                            Your future at
                            <span class="landing-title-accent">Egerton University</span>
                            starts here.
                        </h1>

                        <p class="landing-subtitle">
                            Apply online for undergraduate degree programmes through our secure,
                            guided portal. Create your account, save your progress, and complete
                            your application in seven clear steps from one place.
                        </p>

                        <div class="landing-cta-row">
                            <a href="${pageContext.request.contextPath}/auth?action=register"
                               class="btn btn-primary btn-lg landing-btn">
                                Start Application
                                <span class="landing-btn-icon" aria-hidden="true">&rarr;</span>
                            </a>
                            <a href="${pageContext.request.contextPath}/auth?action=login"
                               class="btn btn-secondary btn-lg landing-btn">
                                Continue Application
                            </a>
                        </div>

                        <div class="landing-stats" aria-label="Portal highlights">
                            <div class="landing-stat">
                                <span class="landing-stat-number">7</span>
                                <span class="landing-stat-label">Simple Steps</span>
                            </div>
                            <div class="landing-stat-divider" aria-hidden="true"></div>
                            <div class="landing-stat">
                                <span class="landing-stat-number">60+</span>
                                <span class="landing-stat-label">Programmes</span>
                            </div>
                            <div class="landing-stat-divider" aria-hidden="true"></div>
                            <div class="landing-stat">
                                <span class="landing-stat-number">100%</span>
                                <span class="landing-stat-label">Online Process</span>
                            </div>
                        </div>
                    </div>

                    <aside class="landing-aside-card" aria-label="Admissions summary">
                        <div class="landing-aside-visual">
                            <svg class="landing-campus-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.4" aria-hidden="true">
                                <path d="M3 21h18M5 21V7l7-4 7 4v14M9 21v-4h6v4M9 9h1m4 0h1M9 13h1m4 0h1"/>
                            </svg>
                        </div>
                        <div class="landing-aside-body">
                            <span class="landing-tag">Admissions Open</span>
                            <h2 class="landing-aside-title">2025/2026 Academic Year</h2>
                            <p class="landing-aside-meta">Egerton University undergraduate degree applications</p>
                            <div class="landing-deadline-row">
                                <span>Application deadline</span>
                                <span class="landing-deadline-badge">Apply Early</span>
                            </div>
                        </div>
                    </aside>
                </div>
            </section>

            <section class="landing-section landing-section-surface">
                <div class="container">
                    <div class="landing-section-header">
                        <div class="landing-section-kicker">Application Process</div>
                        <h2 class="landing-section-title">Seven steps to your admission</h2>
                        <p class="landing-section-subtitle">
                            Follow the guided journey below. You can log back in and continue
                            from where you stopped at any point.
                        </p>
                    </div>

                    <div class="landing-steps-grid">
                        <article class="landing-step-card">
                            <span class="landing-step-number">1</span>
                            <div class="landing-step-icon" aria-hidden="true">
                                <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                    <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/>
                                    <circle cx="12" cy="7" r="4"/>
                                </svg>
                            </div>
                            <div class="landing-step-label">Step 1</div>
                            <h3 class="landing-step-title">Personal Information</h3>
                        </article>

                        <article class="landing-step-card">
                            <span class="landing-step-number">2</span>
                            <div class="landing-step-icon" aria-hidden="true">
                                <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                    <path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z"/>
                                </svg>
                            </div>
                            <div class="landing-step-label">Step 2</div>
                            <h3 class="landing-step-title">Programme Selection</h3>
                        </article>

                        <article class="landing-step-card">
                            <span class="landing-step-number">3</span>
                            <div class="landing-step-icon" aria-hidden="true">
                                <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                    <path d="M22 10v6M2 10l10-5 10 5-10 5z"/>
                                    <path d="M6 12v5c3 3 9 3 12 0v-5"/>
                                </svg>
                            </div>
                            <div class="landing-step-label">Step 3</div>
                            <h3 class="landing-step-title">Academic Background</h3>
                        </article>

                        <article class="landing-step-card">
                            <span class="landing-step-number">4</span>
                            <div class="landing-step-icon" aria-hidden="true">
                                <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                    <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"/>
                                    <polyline points="17 8 12 3 7 8"/>
                                    <line x1="12" y1="3" x2="12" y2="15"/>
                                </svg>
                            </div>
                            <div class="landing-step-label">Step 4</div>
                            <h3 class="landing-step-title">Document Upload</h3>
                        </article>

                        <article class="landing-step-card">
                            <span class="landing-step-number">5</span>
                            <div class="landing-step-icon" aria-hidden="true">
                                <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                    <path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"/>
                                    <circle cx="9" cy="7" r="4"/>
                                    <path d="M23 21v-2a4 4 0 0 0-3-3.87"/>
                                    <path d="M16 3.13a4 4 0 0 1 0 7.75"/>
                                </svg>
                            </div>
                            <div class="landing-step-label">Step 5</div>
                            <h3 class="landing-step-title">Guardian Information</h3>
                        </article>

                        <article class="landing-step-card">
                            <span class="landing-step-number">6</span>
                            <div class="landing-step-icon" aria-hidden="true">
                                <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                    <circle cx="12" cy="12" r="10"/>
                                    <path d="M12 8v4"/>
                                    <path d="M12 16h.01"/>
                                </svg>
                            </div>
                            <div class="landing-step-label">Step 6</div>
                            <h3 class="landing-step-title">Extra Information</h3>
                        </article>

                        <article class="landing-step-card">
                            <span class="landing-step-number">7</span>
                            <div class="landing-step-icon" aria-hidden="true">
                                <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                    <polyline points="20 6 9 17 4 12"/>
                                </svg>
                            </div>
                            <div class="landing-step-label">Step 7</div>
                            <h3 class="landing-step-title">Review and Submit</h3>
                        </article>
                    </div>
                </div>
            </section>

            <section class="landing-section">
                <div class="container landing-docs-layout">
                    <div class="landing-docs-copy">
                        <h2>Before you begin</h2>
                        <p>
                            Keep your documents ready in PDF or image format before you start.
                            You will upload them during the application process, so clear and
                            legible files will help you move faster.
                        </p>
                    </div>

                    <ul class="landing-docs-list">
                        <li class="landing-doc-item">
                            <span class="landing-doc-icon" aria-hidden="true">
                                <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                    <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/>
                                    <polyline points="14 2 14 8 20 8"/>
                                </svg>
                            </span>
                            <span class="landing-doc-text">KCSE Certificate or Result Slip</span>
                        </li>
                        <li class="landing-doc-item">
                            <span class="landing-doc-icon" aria-hidden="true">
                                <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                    <rect x="3" y="4" width="18" height="18" rx="2"/>
                                    <path d="M16 2v4M8 2v4M3 10h18"/>
                                </svg>
                            </span>
                            <span class="landing-doc-text">Birth Certificate</span>
                        </li>
                        <li class="landing-doc-item">
                            <span class="landing-doc-icon" aria-hidden="true">
                                <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                    <rect x="2" y="5" width="20" height="14" rx="2"/>
                                    <path d="M2 10h20"/>
                                </svg>
                            </span>
                            <span class="landing-doc-text">National ID</span>
                            <span class="landing-doc-note">Optional</span>
                        </li>
                        <li class="landing-doc-item">
                            <span class="landing-doc-icon" aria-hidden="true">
                                <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                    <rect x="3" y="3" width="18" height="18" rx="2"/>
                                    <circle cx="8.5" cy="8.5" r="1.5"/>
                                    <polyline points="21 15 16 10 5 21"/>
                                </svg>
                            </span>
                            <span class="landing-doc-text">Passport-size Photo</span>
                        </li>
                    </ul>
                </div>
            </section>
        </main>

        <footer class="landing-footer">
            <div class="container landing-footer-content">
                <span class="landing-footer-brand">Egerton University</span>
                <div class="landing-footer-links">
                    <a href="${pageContext.request.contextPath}/auth?action=login">Applicant Login</a>
                    <a href="${pageContext.request.contextPath}/auth?action=register">Register</a>
                    <a href="${pageContext.request.contextPath}/officer/login">Officer Portal</a>
                </div>
                <span>&copy; 2025 Egerton University. All rights reserved.</span>
            </div>
        </footer>
    </div>
</body>
</html>
