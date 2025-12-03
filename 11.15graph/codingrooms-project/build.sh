#!/bin/bash

# Create build directory if it doesn't exist
mkdir -p build

# Compile all Java files to the build directory
javac -d build/ *.java

# Check if compilation was successful
if [ $? -eq 0 ]; then
    echo "Compilation successful!"
    echo "Running Main..."
    echo ""
    # Run the program from the build directory
    java -cp build/ Main
else
    echo "Compilation failed!"
    exit 1
fi

