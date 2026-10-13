#!/usr/bin/env bash
set -e

echo "[Garagem_Jv] Compilando aplicacao..."
mkdir -p bin
javac -encoding UTF-8 -d bin src/garagem/model/*.java src/garagem/service/*.java src/garagem/*.java

echo "[Garagem_Jv] Iniciando sistema..."
echo ""
java -cp bin garagem.Main
