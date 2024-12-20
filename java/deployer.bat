@echo off

set "TEMP=C:\Users\Ndimby Razafinjatovo\Desktop\NAINA\lamba\temp"
set "WEB_SOURCE=C:\Users\Ndimby Razafinjatovo\Desktop\NAINA\lamba\web"
set "lib=C:\Users\Ndimby Razafinjatovo\Desktop\NAINA\lamba\lib"
set "config=C:\Users\Ndimby Razafinjatovo\Desktop\NAINA\lamba\conf"
set "SRC=C:\Users\Ndimby Razafinjatovo\Desktop\NAINA\lamba\src"
set "WEBAPPS=C:\Program Files\Apache Software Foundation\Tomcat 10.0\webapps"
set "projectName=lamba"

if exist "%TEMP%" (
    rmdir /s /q "%TEMP%"
)

mkdir "%TEMP%"
mkdir "%TEMP%\WEB-INF"
mkdir "%TEMP%\WEB-INF\classes"
mkdir "%TEMP%\WEB-INF\lib"
mkdir "%TEMP%\WEB-INF\views"

xcopy "%config%" "%TEMP%\WEB-INF\" /s /e /i
xcopy "%WEB_SOURCE%\*" "%TEMP%\" /s /e /i
xcopy "%lib%\*" "%TEMP%\WEB-INF\lib\" /s /e /i

cd "%SRC%"
javac -cp "%lib%\*" -d "%TEMP%\WEB-INF\classes" *.java
echo Compilation terminée

cd "%TEMP%"

jar -cvf "%projectName%.war" .
echo Fichier WAR créé

xcopy /s /e /i /y "%TEMP%\%projectName%.war" "%WEBAPPS%"
echo Déploiement du projet dans Tomcat effectué avec succès.

pause
