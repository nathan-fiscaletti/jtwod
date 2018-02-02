#!/bin/bash

# Check for compile flag.
if [[ "$1" != "--debug" ]]; then
    if [[ "$1" != "--release" ]]; then
        echo "Usage: ./build.sh (--release | --debug)"
        exit 1
    fi
fi

# Verify that the javac command is in path
if ! [ -x "$(command -v javac)" ]; then
  echo "Error: Command 'javac' not in path." >&2
  exit 2
fi

# Verify that the jar command is in path
if ! [ -x "$(command -v jar)" ]; then
  echo "Error: Command 'jar' not in path." >&2
  exit 2
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

# Documentation
if ! [ -x "$(command -v javadoc)" ]; then
  echo "Could not generate javadocs, Error: command 'javadoc' not in path."
else
  echo "Generating JavaDocs... (./docs)"
  javadoc -sourcepath ./src/ -d ./docs -subpackages jtwod -exclude jtwod.examples >/dev/null 2>&1
fi

echo "Done."
echo "Output: $file"
