javac -d ../bin ../src/*.java -Xlint
jar cfvm FARC-NEW.jar Manifest.txt -C ../bin .

pause