@echo off
set "JAVA_HOME=C:\Program Files\Java\jdk-26.0.1"
set "PATH=%JAVA_HOME%\bin;%PATH%"
"C:\Program Files\JetBrains\IntelliJ IDEA 2025.3\plugins\maven\lib\maven3\bin\mvn.cmd" %*
