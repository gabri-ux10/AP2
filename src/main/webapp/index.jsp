<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Egerton University - Admission Management System</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Playfair+Display:wght@600;700;800&family=DM+Sans:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <style>
        :root {
            --green-deep: #1a4d2e;
            --green-mid: #2d7a4f;
            --green-bright: #3a9e65;
            --green-pale: #e8f5ee;
            --gold: #c9a84c;
            --gold-pale: #fdf6e3;
            --cream: #faf8f4;
            --text-dark: #111a14;
            --text-mid: #3d4f42;
            --text-light: #7a8f80;
            --white: #ffffff;
            --shadow-sm: 0 2px 12px rgba(26, 77, 46, 0.08);
            --shadow-md: 0 8px 40px rgba(26, 77, 46, 0.13);
        }

        *, *::before, *::after {
            box-sizing: border-box;
            margin: 0;
            padding: 0;
        }

        html {
            scroll-behavior: smooth;
        }

        body {
            font-family: "DM Sans", sans-serif;
            background: var(--cream);
            color: var(--text-dark);
            overflow-x: hidden;
        }

        a {
            text-decoration: none;
        }

        .landing-nav {
            position: fixed;
            top: 0;
            left: 0;
            right: 0;
            z-index: 100;
            background: var(--green-deep);
            display: flex;
            align-items: center;
            justify-content: space-between;
            padding: 0 5%;
            min-height: 68px;
            box-shadow: 0 2px 20px rgba(0, 0, 0, 0.25);
        }

        .landing-brand {
            display: flex;
            flex-direction: column;
        }

        .landing-brand-name {
            font-family: "Playfair Display", serif;
            font-size: 1.1rem;
            font-weight: 700;
            color: var(--white);
            letter-spacing: 0.04em;
        }

        .landing-brand-sub {
            font-size: 0.68rem;
            font-weight: 400;
            color: rgba(255, 255, 255, 0.55);
            letter-spacing: 0.12em;
            text-transform: uppercase;
            margin-top: 1px;
        }

        .landing-nav-links {
            display: flex;
            gap: 8px;
            align-items: center;
            flex-wrap: wrap;
            justify-content: flex-end;
        }

        .landing-nav-link {
            color: rgba(255, 255, 255, 0.8);
            font-size: 0.85rem;
            font-weight: 500;
            padding: 7px 16px;
            border-radius: 6px;
            transition: background 0.2s, color 0.2s;
            letter-spacing: 0.03em;
        }

        .landing-nav-link:hover {
            background: rgba(255, 255, 255, 0.12);
            color: #fff;
        }

        .landing-nav-link-highlight {
            background: var(--gold);
            color: var(--green-deep);
            font-weight: 600;
        }

        .landing-nav-link-highlight:hover {
            background: #e0be6a;
            color: var(--green-deep);
        }

        .hero {
            min-height: 100vh;
            display: flex;
            align-items: center;
            position: relative;
            overflow: hidden;
            padding-top: 68px;
        }

        .hero-bg {
            position: absolute;
            inset: 0;
            background:
                radial-gradient(ellipse 80% 60% at 70% 40%, rgba(45, 122, 79, 0.18) 0%, transparent 70%),
                radial-gradient(ellipse 50% 80% at 10% 80%, rgba(201, 168, 76, 0.12) 0%, transparent 60%),
                linear-gradient(160deg, #f0f7f2 0%, #faf8f4 45%, #f5f0e8 100%);
        }

        .hero-bg::before {
            content: "";
            position: absolute;
            top: -120px;
            right: -120px;
            width: 600px;
            height: 600px;
            border-radius: 50%;
            border: 2px solid rgba(45, 122, 79, 0.1);
            animation: slowSpin 40s linear infinite;
        }

        .hero-bg::after {
            content: "";
            position: absolute;
            top: -80px;
            right: -80px;
            width: 480px;
            height: 480px;
            border-radius: 50%;
            border: 1px solid rgba(201, 168, 76, 0.15);
            animation: slowSpin 60s linear infinite reverse;
        }

        @keyframes slowSpin {
            to {
                transform: rotate(360deg);
            }
        }

        .hero-accent-line {
            position: absolute;
            left: 5%;
            top: 20%;
            bottom: 20%;
            width: 3px;
            background: linear-gradient(to bottom, transparent, var(--green-mid), var(--gold), transparent);
            opacity: 0.35;
        }

        .hero-content {
            position: relative;
            z-index: 2;
            max-width: 760px;
            padding: 80px 5% 80px 8%;
            animation: heroIn 0.9s cubic-bezier(0.22, 1, 0.36, 1) both;
        }

        @keyframes heroIn {
            from {
                opacity: 0;
                transform: translateY(32px);
            }

            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        .hero-eyebrow {
            display: inline-flex;
            align-items: center;
            gap: 8px;
            font-size: 0.75rem;
            font-weight: 600;
            letter-spacing: 0.18em;
            text-transform: uppercase;
            color: var(--green-mid);
            margin-bottom: 24px;
        }

        .hero-eyebrow::before {
            content: "";
            width: 28px;
            height: 2px;
            background: var(--gold);
            display: block;
        }

        .hero-title {
            font-family: "Playfair Display", serif;
            font-size: clamp(2.4rem, 5vw, 3.8rem);
            line-height: 1.12;
            color: var(--green-deep);
            margin-bottom: 24px;
        }

        .hero-title em {
            font-style: normal;
            background: linear-gradient(90deg, var(--green-mid), var(--green-bright));
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            background-clip: text;
        }

        .hero-sub {
            font-size: 1.05rem;
            line-height: 1.7;
            color: var(--text-mid);
            max-width: 520px;
            margin-bottom: 40px;
            font-weight: 300;
        }

        .hero-cta {
            display: flex;
            flex-wrap: wrap;
            gap: 14px;
            align-items: center;
            margin-bottom: 56px;
        }

        .btn-primary {
            display: inline-flex;
            align-items: center;
            gap: 10px;
            background: var(--green-deep);
            color: #fff;
            font-size: 0.95rem;
            font-weight: 600;
            letter-spacing: 0.02em;
            padding: 15px 32px;
            border-radius: 8px;
            box-shadow: 0 4px 20px rgba(26, 77, 46, 0.35);
            transition: transform 0.18s, box-shadow 0.18s, background 0.18s;
            cursor: pointer;
            border: none;
        }

        .btn-primary:hover {
            transform: translateY(-2px);
            box-shadow: 0 8px 28px rgba(26, 77, 46, 0.45);
            background: #163f24;
            color: #fff;
        }

        .btn-primary svg {
            transition: transform 0.18s;
        }

        .btn-primary:hover svg {
            transform: translateX(3px);
        }

        .btn-secondary {
            display: inline-flex;
            align-items: center;
            gap: 10px;
            background: transparent;
            color: var(--green-deep);
            font-size: 0.95rem;
            font-weight: 600;
            padding: 14px 28px;
            border-radius: 8px;
            border: 2px solid var(--green-mid);
            transition: background 0.18s, color 0.18s;
            cursor: pointer;
        }

        .btn-secondary:hover {
            background: var(--green-pale);
            color: var(--green-deep);
        }

        .hero-stats {
            display: flex;
            gap: 40px;
            flex-wrap: wrap;
        }

        .stat {
            display: flex;
            flex-direction: column;
        }

        .stat-num {
            font-family: "Playfair Display", serif;
            font-size: 2rem;
            font-weight: 700;
            color: var(--green-deep);
            line-height: 1;
        }

        .stat-label {
            font-size: 0.75rem;
            font-weight: 500;
            letter-spacing: 0.1em;
            text-transform: uppercase;
            color: var(--text-light);
            margin-top: 4px;
        }

        .stat-divider {
            width: 1px;
            background: rgba(26, 77, 46, 0.15);
            align-self: stretch;
        }

        .hero-image-card {
            position: absolute;
            right: 4%;
            top: 50%;
            transform: translateY(-50%);
            width: 340px;
            background: var(--white);
            border-radius: 16px;
            box-shadow: var(--shadow-md), 0 0 0 1px rgba(26, 77, 46, 0.06);
            overflow: hidden;
            animation: cardIn 1s 0.3s cubic-bezier(0.22, 1, 0.36, 1) both;
            display: none;
        }

        @keyframes cardIn {
            from {
                opacity: 0;
                transform: translateY(-40%) scale(0.95);
            }

            to {
                opacity: 1;
                transform: translateY(-50%) scale(1);
            }
        }

        .card-img-placeholder {
            width: 100%;
            height: 200px;
            background: linear-gradient(135deg, var(--green-deep) 0%, var(--green-mid) 100%);
            display: flex;
            align-items: center;
            justify-content: center;
            position: relative;
            overflow: hidden;
        }

        .card-img-placeholder::after {
            content: "";
            position: absolute;
            inset: 0;
            background: radial-gradient(circle at 70% 30%, rgba(255, 255, 255, 0.15), transparent 60%);
        }

        .campus-icon {
            opacity: 0.5;
            width: 80px;
            height: 80px;
        }

        .hero-card-body {
            padding: 20px;
        }

        .card-tag {
            display: inline-block;
            background: var(--green-pale);
            color: var(--green-mid);
            font-size: 0.7rem;
            font-weight: 600;
            letter-spacing: 0.1em;
            text-transform: uppercase;
            padding: 4px 10px;
            border-radius: 4px;
            margin-bottom: 10px;
        }

        .card-title {
            font-family: "Playfair Display", serif;
            font-size: 1rem;
            color: var(--text-dark);
            margin-bottom: 6px;
        }

        .card-meta {
            font-size: 0.8rem;
            color: var(--text-light);
        }

        .card-deadline {
            margin-top: 14px;
            padding-top: 14px;
            border-top: 1px solid rgba(0, 0, 0, 0.06);
            display: flex;
            justify-content: space-between;
            align-items: center;
            gap: 12px;
            font-size: 0.8rem;
            color: var(--text-mid);
        }

        .deadline-badge {
            background: var(--gold-pale);
            color: #8a6200;
            border: 1px solid rgba(201, 168, 76, 0.3);
            padding: 3px 8px;
            border-radius: 4px;
            font-size: 0.72rem;
            font-weight: 600;
            white-space: nowrap;
        }

        .process {
            background: var(--white);
            padding: 80px 5%;
            position: relative;
        }

        .process::before {
            content: "";
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            height: 4px;
            background: linear-gradient(90deg, var(--green-deep), var(--green-bright), var(--gold));
        }

        .section-header {
            text-align: center;
            margin-bottom: 56px;
        }

        .section-eyebrow {
            display: inline-flex;
            align-items: center;
            gap: 10px;
            font-size: 0.72rem;
            font-weight: 600;
            letter-spacing: 0.18em;
            text-transform: uppercase;
            color: var(--green-mid);
            margin-bottom: 14px;
        }

        .section-eyebrow span {
            width: 20px;
            height: 2px;
            background: var(--gold);
            display: block;
        }

        .section-title {
            font-family: "Playfair Display", serif;
            font-size: clamp(1.6rem, 3vw, 2.4rem);
            color: var(--green-deep);
            line-height: 1.2;
        }

        .section-sub {
            font-size: 0.95rem;
            color: var(--text-light);
            margin-top: 10px;
            font-weight: 300;
        }

        .steps-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 20px;
            max-width: 1100px;
            margin: 0 auto;
        }

        .step-card {
            background: var(--cream);
            border-radius: 12px;
            padding: 28px 24px;
            border: 1px solid rgba(26, 77, 46, 0.07);
            position: relative;
            overflow: hidden;
            transition: transform 0.22s, box-shadow 0.22s, border-color 0.22s;
        }

        .step-card:hover {
            transform: translateY(-4px);
            box-shadow: var(--shadow-md);
            border-color: rgba(45, 122, 79, 0.2);
        }

        .step-card::before {
            content: "";
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            height: 3px;
            background: linear-gradient(90deg, var(--green-mid), var(--green-bright));
            opacity: 0;
            transition: opacity 0.22s;
        }

        .step-card:hover::before {
            opacity: 1;
        }

        .step-number {
            font-family: "Playfair Display", serif;
            font-size: 3rem;
            font-weight: 800;
            color: rgba(26, 77, 46, 0.08);
            line-height: 1;
            position: absolute;
            top: 12px;
            right: 16px;
        }

        .step-icon {
            width: 40px;
            height: 40px;
            background: var(--green-pale);
            border-radius: 10px;
            display: flex;
            align-items: center;
            justify-content: center;
            margin-bottom: 14px;
        }

        .step-icon svg {
            stroke: var(--green-mid);
        }

        .step-label {
            font-size: 0.65rem;
            font-weight: 600;
            letter-spacing: 0.14em;
            text-transform: uppercase;
            color: var(--green-bright);
            margin-bottom: 4px;
        }

        .step-name {
            font-family: "Playfair Display", serif;
            font-size: 1rem;
            color: var(--text-dark);
            line-height: 1.3;
        }

        .docs-section {
            background: var(--green-pale);
            padding: 60px 5%;
        }

        .docs-inner {
            max-width: 900px;
            margin: 0 auto;
            display: grid;
            gap: 32px;
            grid-template-columns: 1fr 1fr;
            align-items: center;
        }

        .docs-text h2 {
            font-family: "Playfair Display", serif;
            font-size: 1.7rem;
            color: var(--green-deep);
            margin-bottom: 10px;
        }

        .docs-text p {
            font-size: 0.9rem;
            color: var(--text-mid);
            line-height: 1.7;
            font-weight: 300;
        }

        .docs-list {
            list-style: none;
            display: flex;
            flex-direction: column;
            gap: 12px;
        }

        .docs-list li {
            display: flex;
            align-items: center;
            gap: 12px;
            background: var(--white);
            padding: 14px 18px;
            border-radius: 10px;
            font-size: 0.88rem;
            color: var(--text-dark);
            font-weight: 500;
            box-shadow: var(--shadow-sm);
        }

        .doc-icon {
            width: 32px;
            height: 32px;
            flex-shrink: 0;
            background: var(--green-pale);
            border-radius: 8px;
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .doc-icon svg {
            stroke: var(--green-mid);
        }

        .doc-optional {
            margin-left: auto;
            font-size: 0.7rem;
            color: var(--text-light);
            font-weight: 400;
            padding: 2px 8px;
            background: rgba(0, 0, 0, 0.04);
            border-radius: 4px;
        }

        .landing-footer {
            background: var(--green-deep);
            color: rgba(255, 255, 255, 0.55);
            padding: 32px 5%;
            display: flex;
            justify-content: space-between;
            align-items: center;
            flex-wrap: wrap;
            gap: 16px;
            font-size: 0.8rem;
        }

        .footer-brand {
            font-family: "Playfair Display", serif;
            font-size: 1rem;
            color: #fff;
            font-weight: 600;
        }

        .footer-links {
            display: flex;
            gap: 20px;
            flex-wrap: wrap;
        }

        .footer-links a {
            color: rgba(255, 255, 255, 0.6);
            transition: color 0.15s;
        }

        .footer-links a:hover {
            color: #fff;
        }

        .reveal {
            opacity: 0;
            transform: translateY(24px);
            transition: opacity 0.7s, transform 0.7s;
        }

        .reveal.visible {
            opacity: 1;
            transform: none;
        }

        @media (min-width: 1100px) {
            .hero-image-card {
                display: block;
            }

            .hero-content {
                max-width: 600px;
            }
        }

        @media (max-width: 700px) {
            .docs-inner {
                grid-template-columns: 1fr;
            }
        }

        @media (max-width: 600px) {
            .landing-nav-links .landing-nav-link:not(.landing-nav-link-highlight) {
                display: none;
            }
        }

        @media (max-width: 1024px) {
            .hero {
                min-height: auto;
                padding-bottom: 40px;
            }

            .hero-content {
                max-width: 100%;
                padding: 72px 6% 40px;
            }

            .hero-accent-line {
                display: none;
            }

            .hero-image-card {
                position: relative;
                top: auto;
                right: auto;
                transform: none;
                width: min(340px, calc(100% - 48px));
                margin: 0 auto 32px;
                display: block;
            }
        }

        @media (max-width: 768px) {
            .landing-nav {
                padding: 12px 5%;
                align-items: flex-start;
                gap: 14px;
                flex-direction: column;
            }

            .landing-nav-links {
                width: 100%;
                justify-content: flex-start;
            }

            .hero {
                padding-top: 110px;
            }

            .hero-content {
                padding: 40px 5% 30px;
            }

            .hero-stats {
                gap: 20px;
            }

            .stat-divider {
                display: none;
            }

            .landing-footer {
                flex-direction: column;
                align-items: flex-start;
            }
        }
    </style>
</head>
<body>
    <nav class="landing-nav">
        <div class="landing-brand">
            <span class="landing-brand-name">Egerton University</span>
            <span class="landing-brand-sub">Admission Management System</span>
        </div>
        <div class="landing-nav-links">
            <a href="${pageContext.request.contextPath}/auth?action=login" class="landing-nav-link">Applicant Login</a>
            <a href="${pageContext.request.contextPath}/auth?action=register" class="landing-nav-link">Register</a>
            <a href="${pageContext.request.contextPath}/officer/login" class="landing-nav-link landing-nav-link-highlight">Officer Portal</a>
        </div>
    </nav>

    <section class="hero">
        <div class="hero-bg"></div>
        <div class="hero-accent-line"></div>

        <div class="hero-content">
            <div class="hero-eyebrow">Undergraduate Admissions 2025</div>

            <h1 class="hero-title">
                Your future at<br>
                <em>Egerton University</em><br>
                starts here.
            </h1>

            <p class="hero-sub">
                Apply online for undergraduate degree programmes through our secure,
                streamlined portal. Complete your application in 7 guided steps
                all from one place.
            </p>

            <div class="hero-cta">
                <a href="${pageContext.request.contextPath}/auth?action=register" class="btn-primary">
                    Start My Application
                    <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" aria-hidden="true">
                        <path d="M5 12h14M12 5l7 7-7 7"></path>
                    </svg>
                </a>
                <a href="${pageContext.request.contextPath}/auth?action=login" class="btn-secondary">
                    Continue Application
                </a>
            </div>

            <div class="hero-stats">
                <div class="stat">
                    <span class="stat-num">7</span>
                    <span class="stat-label">Simple Steps</span>
                </div>
                <div class="stat-divider"></div>
                <div class="stat">
                    <span class="stat-num">60+</span>
                    <span class="stat-label">Programmes</span>
                </div>
                <div class="stat-divider"></div>
                <div class="stat">
                    <span class="stat-num">100%</span>
                    <span class="stat-label">Online Process</span>
                </div>
            </div>
        </div>

        <div class="hero-image-card">
            <div class="card-img-placeholder">
                <svg class="campus-icon" viewBox="0 0 24 24" fill="none" stroke="white" stroke-width="1" aria-hidden="true">
                    <path d="M3 21h18M5 21V7l7-4 7 4v14M9 21v-4h6v4M9 9h1m4 0h1M9 13h1m4 0h1"></path>
                </svg>
            </div>
            <div class="hero-card-body">
                <span class="card-tag">Admissions Open</span>
                <div class="card-title">2025/2026 Academic Year</div>
                <div class="card-meta">Njoro Campus, Egerton University</div>
                <div class="card-deadline">
                    <span>Application Deadline</span>
                    <span class="deadline-badge">Apply Early</span>
                </div>
            </div>
        </div>
    </section>

    <section class="process">
        <div class="section-header reveal">
            <div class="section-eyebrow"><span></span>Application Process<span></span></div>
            <h2 class="section-title">Seven steps to your admission</h2>
            <p class="section-sub">Follow the guided process below and save your progress at any point.</p>
        </div>

        <div class="steps-grid">
            <div class="step-card reveal">
                <span class="step-number">1</span>
                <div class="step-icon">
                    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke-width="2" aria-hidden="true">
                        <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"></path>
                        <circle cx="12" cy="7" r="4"></circle>
                    </svg>
                </div>
                <div class="step-label">Step 1</div>
                <div class="step-name">Personal Information</div>
            </div>

            <div class="step-card reveal">
                <span class="step-number">2</span>
                <div class="step-icon">
                    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke-width="2" aria-hidden="true">
                        <path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z"></path>
                    </svg>
                </div>
                <div class="step-label">Step 2</div>
                <div class="step-name">Programme Selection</div>
            </div>

            <div class="step-card reveal">
                <span class="step-number">3</span>
                <div class="step-icon">
                    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke-width="2" aria-hidden="true">
                        <path d="M22 10v6M2 10l10-5 10 5-10 5z"></path>
                        <path d="M6 12v5c3 3 9 3 12 0v-5"></path>
                    </svg>
                </div>
                <div class="step-label">Step 3</div>
                <div class="step-name">Academic Background</div>
            </div>

            <div class="step-card reveal">
                <span class="step-number">4</span>
                <div class="step-icon">
                    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke-width="2" aria-hidden="true">
                        <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"></path>
                        <polyline points="17 8 12 3 7 8"></polyline>
                        <line x1="12" y1="3" x2="12" y2="15"></line>
                    </svg>
                </div>
                <div class="step-label">Step 4</div>
                <div class="step-name">Document Upload</div>
            </div>

            <div class="step-card reveal">
                <span class="step-number">5</span>
                <div class="step-icon">
                    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke-width="2" aria-hidden="true">
                        <path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"></path>
                        <circle cx="9" cy="7" r="4"></circle>
                        <path d="M23 21v-2a4 4 0 0 0-3-3.87"></path>
                        <path d="M16 3.13a4 4 0 0 1 0 7.75"></path>
                    </svg>
                </div>
                <div class="step-label">Step 5</div>
                <div class="step-name">Guardian Information</div>
            </div>

            <div class="step-card reveal">
                <span class="step-number">6</span>
                <div class="step-icon">
                    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke-width="2" aria-hidden="true">
                        <circle cx="12" cy="12" r="10"></circle>
                        <path d="M12 8v4"></path>
                        <path d="M12 16h.01"></path>
                    </svg>
                </div>
                <div class="step-label">Step 6</div>
                <div class="step-name">Extra Information</div>
            </div>

            <div class="step-card reveal">
                <span class="step-number">7</span>
                <div class="step-icon">
                    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke-width="2" aria-hidden="true">
                        <polyline points="20 6 9 17 4 12"></polyline>
                    </svg>
                </div>
                <div class="step-label">Step 7</div>
                <div class="step-name">Review &amp; Submit</div>
            </div>
        </div>
    </section>

    <section class="docs-section">
        <div class="docs-inner reveal">
            <div class="docs-text">
                <h2>Before you begin</h2>
                <p>
                    Have the following documents ready in PDF or image format.
                    You will be asked to upload them during Step 4 of your application.
                    Ensure the files are clear and legible.
                </p>
            </div>
            <ul class="docs-list">
                <li>
                    <span class="doc-icon">
                        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke-width="2" aria-hidden="true">
                            <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"></path>
                            <polyline points="14 2 14 8 20 8"></polyline>
                        </svg>
                    </span>
                    KCSE Certificate or Result Slip
                </li>
                <li>
                    <span class="doc-icon">
                        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke-width="2" aria-hidden="true">
                            <rect x="3" y="4" width="18" height="18" rx="2"></rect>
                            <path d="M16 2v4M8 2v4M3 10h18"></path>
                        </svg>
                    </span>
                    Birth Certificate
                </li>
                <li>
                    <span class="doc-icon">
                        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke-width="2" aria-hidden="true">
                            <rect x="2" y="5" width="20" height="14" rx="2"></rect>
                            <path d="M2 10h20"></path>
                        </svg>
                    </span>
                    National ID
                    <span class="doc-optional">Optional</span>
                </li>
                <li>
                    <span class="doc-icon">
                        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke-width="2" aria-hidden="true">
                            <rect x="3" y="3" width="18" height="18" rx="2"></rect>
                            <circle cx="8.5" cy="8.5" r="1.5"></circle>
                            <polyline points="21 15 16 10 5 21"></polyline>
                        </svg>
                    </span>
                    Passport-size Photo
                </li>
            </ul>
        </div>
    </section>

    <footer class="landing-footer">
        <span class="footer-brand">Egerton University</span>
        <div class="footer-links">
            <a href="${pageContext.request.contextPath}/auth?action=login">Applicant Login</a>
            <a href="${pageContext.request.contextPath}/auth?action=register">Register</a>
            <a href="${pageContext.request.contextPath}/officer/login">Officer Portal</a>
        </div>
        <span>&copy; 2025 Egerton University. All rights reserved.</span>
    </footer>

    <script>
        const observer = new IntersectionObserver(entries => {
            entries.forEach((entry, index) => {
                if (entry.isIntersecting) {
                    setTimeout(() => entry.target.classList.add("visible"), index * 80);
                    observer.unobserve(entry.target);
                }
            });
        }, { threshold: 0.1 });

        document.querySelectorAll(".reveal").forEach(el => observer.observe(el));
    </script>
</body>
</html>
