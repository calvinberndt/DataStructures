#!/usr/bin/env bash
set -euo pipefail

# Ensure output directory exists
mkdir -p bin

# Compile source files into the bin directory
javac -d bin Main.java

echo "Compiled classes written to bin/"
