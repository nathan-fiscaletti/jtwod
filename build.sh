#!/bin/bash

if [[ "$1" != "--debug" ]]; then
    if [[ "$1" != "--release" ]]; then
        echo "Usage: ./build.sh (--release | --debug)"
        exit
    fi
fi

# Create Temporary Directory
mkdir tmp

# Compile jtwod Files
echo "Compiling jtwod classes..."
cp -R src/ tmp/
cd tmp/
find . -name "*.java" > sources.txt
javac -cp ../lib/tinysound-1.1.1.jar @sources.txt
rm sources.txt

file="lib-jtwod-static.jar"

if [[ "$1" == "--debug" ]];
then
    echo "Packaging source (debug)..."
    file="lib-jtwod-static-debug.jar"
else
    echo "Removing source (release)..."
    find . -name '*.java' -delete
fi

# Extract TinySound library and License
echo "Migrating TinySound..."
jar -xf ../lib/tinysound-1.1.1.jar
cp ../lib/TINYSOUND_LICENSE ./
cd ..

# Compile final Jar
echo "Compiling final jar..."
jar -cvf $file -C tmp . >/dev/null

# Clean up
echo "Cleaning up..."
rm -rf tmp

echo "Done."
echo "Output: $file"
