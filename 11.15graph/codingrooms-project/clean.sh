#!/bin/bash

# Remove all .class files from the current directory
find . -maxdepth 1 -name "*.class" -type f -delete

# Remove the build directory if it exists
if [ -d "build" ]; then
    rm -rf build
    echo "Removed build directory"
fi

echo "Cleanup complete!"

