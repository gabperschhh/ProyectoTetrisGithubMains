@echo off
echo Compilando archivos Java...
javac *.java

if %errorlevel% neq 0 (
    echo Error en la compilación.
    pause
    exit /b
)

echo Ejecutando programa...
java Main

pause
