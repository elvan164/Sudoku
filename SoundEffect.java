import java.io.*;
import java.net.URL;
import javax.sound.sampled.*;

public enum SoundEffect {

	Background("BG.wav"), Win("win.wav");

	public static enum Volume {
		MUTE, LOW, MEDIUM, HIGH
	}

	public Clip clip;
	public static Volume volume = Volume.HIGH;

	public FloatControl gainControl;

	SoundEffect(String soundFileName) {
		try {
			URL url = this.getClass().getClassLoader().getResource(soundFileName);
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
			clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			final FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			gainControl.setValue(-10.0f);

		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	public void play() {
		if (volume != Volume.MUTE) {
			if (clip.isRunning()) {
				clip.stop();
			}
			clip.setFramePosition(0);
			clip.start();
		}
	}

	public void stop() {
		clip.stop();
	}

	public static void volume(float volume, Clip clip) {
		FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		gainControl.setValue(volume);
	}

	static void init() {
		values();
	}
}