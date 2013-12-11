import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.headless.HeadlessMediaPlayer;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.File;
import javax.imageio.ImageIO;
import uk.co.caprica.vlcj.binding.internal.libvlc_state_t;

public class WebcamVirus {
	// Sleep time in miliseconds
	private static final int TIME_SLEEP = 3*1000;

	/**
	 * args[0] = webcam path
	 */
	public static void main(String[] args) throws Exception {
		String webcamPath = null;
		if (args.length < 1) {
			System.out.println("Missing webcam path!");
			System.exit(-1);
		} else {
			System.out.println("Webcam Path: " + args[0]);
			webcamPath = args[0];
		}
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
	    HeadlessMediaPlayer mediaPlayer = mediaPlayerFactory.newHeadlessMediaPlayer();

	    // Initialize webcam processing
	    if (! mediaPlayer.startMedia(webcamPath)) {
	    	System.out.println("Error while starting media player.");
	    	System.exit(-1);
	    }

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
