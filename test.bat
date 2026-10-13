@echo off
chcp 65001 > nul
echo [Garagem_Jv] Compilando codigo e testes...
if not exist bin mkdir bin
javac -encoding UTF-8 -d bin src/garagem/model/*.java src/garagem/service/*.java src/garagem/*.java
if %ERRORLEVEL% NEQ 0 (
    echo [ERRO] Falha na compilacao!
    exit /b %ERRORLEVEL%
)
echo [Garagem_Jv] Executando suite de testes automatizados...
echo.
java -cp bin garagem.TesteVirtual
