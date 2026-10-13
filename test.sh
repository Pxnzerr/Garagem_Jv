#!/usr/bin/env bash
set -e

echo "[Garagem_Jv] Compilando codigo e testes..."
mkdir -p bin
javac -encoding UTF-8 -d bin src/garagem/model/*.java src/garagem/service/*.java src/garagem/*.java

echo "[Garagem_Jv] Executando suite de testes automatizados..."
echo ""
java -cp bin garagem.TesteVirtual
