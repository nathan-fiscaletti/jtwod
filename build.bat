@echo off

if NOT "%1"=="--debug" (
    if NOT "%1"=="--release" (
        echo "Usage: build.bat (--release | --debug)"
        exit
    )
)

REM Compile jtwod Files
echo "Compiling jtwod classes..."
xcopy ".\src\*" ".\tmp\" /s /e /y
cd tmp/
dir /s /B *.java > sources.txt
javac -cp "../lib/tinysound-1.1.1.jar" @sources.txt
del sources.txt

set file="lib-jtwod-static.jar"

if "%1"=="--debug" (
    echo "Packaging source (debug)..."
    set file="lib-jtwod-static-debug.jar"
) else (
    echo "Removing source (release)..."
    del /s *.java
)

REM Extract TinySound library and License
echo "Migrating TinySound..."
jar -xf ../lib/tinysound-1.1.1.jar
copy ../lib/TINYSOUND_LICENSE ./
cd ..

REM Compile final Jar
echo "Compiling final jar..."
jar -cvf %file% -C tmp .

REM Clean up
echo "Cleaning up..."
rm -rf tmp

echo "Done."
echo "Output: %file%"
