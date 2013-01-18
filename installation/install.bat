@echo off
if not exist %home%\.autoplay ( 
	echo create configure folder
	md %home%\.autoplay 
) else (
	echo configure folder exists
)
echo 	
echo copy cmdmp3.exe
copy /Y cmdmp3.exe %home%\.autoplay
echo copy Configs.xml
copy /Y Configs.xml %home%\.autoplay



