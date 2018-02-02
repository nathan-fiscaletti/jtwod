@echo off

REM Check for compile flag.
if NOT "%1"=="--debug" (
    if NOT "%1"=="--release" (
        echo Usage: build.bat (--release ^| --debug^)
        exit /B 1
    )
)

REM Verify that the javac command is in path.
WHERE javac > nul
IF %ERRORLEVEL% NEQ 0 (
    ECHO Error: Command 'javac' not in path.
    exit /B 2
)

REM Verify that the jar command is in path.
WHERE jar > nul
IF %ERRORLEVEL% NEQ 0 (
    ECHO Error: Command 'jar' not in path.
    exit /B 3
)

REM Compile jtwod files.
echo Compiling jtwod classes^.^.^.
xcopy ".\src\*" ".\tmp\" /s /e /y > nul
cd tmp/
dir /s /B *.java > sources.txt
javac -cp "../lib/tinysound-1.1.1.jar" @sources.txt
del sources.txt
set file="lib-jtwod-static.jar"
if "%1"=="--debug" (
    echo Packaging source ^(debug^)^.^.^.
    set file="lib-jtwod-static-debug.jar"
) else (
    echo Removing source ^(release^)^.^.^.
    del /Q /s *.java > nul
)

REM Extract TinySound library and license.
@echo Migrating TinySound...
jar -xf ../lib/tinysound-1.1.1.jar > nul
xcopy /f /y ..\lib\TINYSOUND_LICENSE .\ > nul
cd ..

REM Compile final Jar.
@echo Compiling final jar...
jar -cvf %file% -C tmp . > nul

REM Clean up.
@echo Cleaning up...
rmdir /S /Q tmp

REM Generate JavDocs
WHERE javadoc > nul
IF %ERRORLEVEL% NEQ 0 (
    ECHO Could not generate javadocs, Error: command 'javadoc' not in path.
) ELSE (
    ECHO Generating JavaDocs^.^.^. ^(^.^/docs^)
    javadoc -sourcepath ./src/ -d ./docs -subpackages jtwod -exclude jtwod.examples > nul
)

@echo Done.
echo Output: %file%
