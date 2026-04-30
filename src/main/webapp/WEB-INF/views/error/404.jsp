<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Page Not Found - Egerton University AMS</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
    <div class="auth-container">
        <div class="auth-card" style="text-align: center;">
            <h1 style="font-size: 5rem; color: var(--primary-color); margin-bottom: 1rem;">404</h1>
            <h2 style="margin-bottom: 1rem;">Page Not Found</h2>
            <p class="text-muted" style="margin-bottom: 2rem;">
                The page you are looking for does not exist or has been moved.
            </p>
            <a href="${pageContext.request.contextPath}/" class="btn btn-primary btn-lg">
                Go to Home
            </a>
        </div>
    </div>
</body>
</html>
