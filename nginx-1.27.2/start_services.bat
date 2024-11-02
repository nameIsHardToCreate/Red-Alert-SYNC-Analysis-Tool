@echo off
REM 设置当前目录
set CURRENT_DIR=%cd%

REM 检查 Nginx 是否已经在运行
tasklist | findstr /i "nginx.exe" >nul
if %errorlevel%==0 (
    echo Nginx is already running.
) else (
    echo Starting Nginx...
    start cmd /c "%CURRENT_DIR%\nginx.exe"
)

REM 启动 demo JAR 包
echo Starting demo application...
java -jar "%CURRENT_DIR%\demo.jar"

echo Both services checked. Press any key to exit.
pause