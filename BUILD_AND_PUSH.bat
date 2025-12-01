@echo off
echo ========================================
echo Building Spring Boot JAR
echo ========================================
call mvn clean package

if %ERRORLEVEL% NEQ 0 (
    echo Build failed!
    pause
    exit /b 1
)

echo.
echo ========================================
echo Build successful! JAR created at:
echo target\webhook-solver-1.0.0.jar
echo ========================================
echo.
echo Next steps:
echo 1. Create a public GitHub repository
echo 2. Run these commands:
echo    git init
echo    git add .
echo    git commit -m "Initial commit"
echo    git remote add origin https://github.com/YOUR_USERNAME/webhook-solver.git
echo    git branch -M main
echo    git push -u origin main
echo.
echo See GITHUB_SETUP.md for detailed instructions
echo.
pause

