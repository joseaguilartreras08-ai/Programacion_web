@echo off
REM Arranca la base de datos (XAMPP, puerto 3307), el backend (8081) y el frontend (4200).
REM Cada servicio abre su propia ventana; para detenerlo, cierra su ventana.

echo Iniciando base de datos MySQL de XAMPP (puerto 3307)...
start "MySQL XAMPP" /min "C:\xampp\mysql\bin\mysqld.exe" --defaults-file=C:\xampp\mysql\bin\my.ini --standalone
timeout /t 5 /nobreak >nul

echo Iniciando backend (puerto 8081)...
start "Backend Clinica" cmd /k "cd /d %~dp0backend-83681 && set JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-21.0.5.11-hotspot&& set SERVER_PORT=8081&& set DB_URL=jdbc:mysql://localhost:3307/clinica?serverTimezone=America/Bogota&& set DB_PASSWORD=&& mvnw.cmd spring-boot:run"

echo Iniciando frontend (puerto 4200)...
start "Frontend Clinica" cmd /k "cd /d %~dp0frontend26-83681 && npm start"

echo.
echo Cuando termine de cargar, abre http://localhost:4200
pause
