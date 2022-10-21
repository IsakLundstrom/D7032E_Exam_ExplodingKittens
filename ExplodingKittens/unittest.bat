@echo off
set JDK_HOME=c:\PROGRA~1\Java\jdk-18.0.2.1
set JUNIT=lib\org.junit4-4.3.1.jar
@echo "Running unittests ..."
%JDK_HOME%/bin/java.exe -cp %JUNIT%;bin org.junit.runner.JUnitCore test.TestRequirements