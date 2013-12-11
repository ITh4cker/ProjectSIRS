VLCPATH='/Applications/VLC.app/Contents/MacOS/lib'
VLCJPATH='vlcj-2.4.1/jna-3.5.2.jar:vlcj-2.4.1/platform-3.5.2.jar:vlcj-2.4.1/vlcj-2.4.1.jar:.'
WEBCAM_OSX_PATH='qtcapture://'
WEBCAM_LINUX_PATH='/dev/video0'

javac -cp $VLCJPATH WebcamVirus.java
java -cp $VLCJPATH -Djna.library.path=$VLCPATH WebcamVirus $WEBCAM_OSX_PATH &

while [ true ]
do
	watch -n 3 open saved.png;
done
