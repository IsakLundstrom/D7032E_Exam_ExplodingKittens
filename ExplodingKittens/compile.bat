@echo off
set JDK_HOME=c:\PROGRA~1\Java\jdk-18.0.2.1
@mkdir bin
@echo "Compiling code ..."
%JDK_HOME%/bin/javac.exe -d bin -cp src src/main/explodingkittens/ExplodingKittens.java

set JUNIT=lib\org.junit4-4.3.1.jar

@echo "Compiling tests ..."
%JDK_HOME%/bin/javac.exe -d bin -cp %JUNIT%;src src/test/TestRequirements.java