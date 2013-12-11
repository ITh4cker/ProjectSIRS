VLCPATH='/Applications/VLC.app/Contents/MacOS/lib'
VLCJPATH='vlcj-2.4.1/jna-3.5.2.jar:vlcj-2.4.1/platform-3.5.2.jar:vlcj-2.4.1/vlcj-2.4.1.jar:.'

javac -cp $VLCJPATH WebcamVirus.java
java -cp $VLCJPATH -Djna.library.path=$VLCPATH WebcamVirus &

while [ true ]
do
	watch -n 3 open saved.png;
done
