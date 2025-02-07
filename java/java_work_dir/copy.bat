@echo off

set "root=D:\ITU\S5\Web_avance\cloud_final\java"

set "temp=%root%\MyApp\WEB-INF\classes"

set "src=%root%\java_work_dir\src"



:: copy all java files to temp directory
for /r "%src%" %%f in (*.java) do (
    xcopy "%%f" "%temp%"
)

