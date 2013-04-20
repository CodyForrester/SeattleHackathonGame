package audio;

import java.io.BufferedInputStream;
import java.nio.FloatBuffer;
import java.util.LinkedList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.util.WaveData;

public class AudioPlayer {

	// SUPER HACKY
	public static Sound SHOOT;
	public static Sound MUSIC;
	
	public static void start(){
		init();
		SHOOT = new Sound("Sounds/laserb.wav", 5);
		MUSIC = new Sound("Sounds/SPOOGHIT.wav", 10);
		// tee hee
		//play(SHOOT);
	}
	
	/** Position of the source sound. */
	private static final FloatBuffer sourcePos = (FloatBuffer)BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f }).rewind();

	/** Velocity of the source sound. */
	private static final FloatBuffer sourceVel = (FloatBuffer)BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f }).rewind();

	/** Position of the listener. */
	private static final FloatBuffer listenerPos = (FloatBuffer)BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f }).rewind();

	/** Velocity of the listener. */
	private static final FloatBuffer listenerVel = (FloatBuffer)BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f }).rewind();

	/** Orientation of the listener. (first 3 elements are "at", second 3 are "up") */
	private static final FloatBuffer listenerOri = (FloatBuffer)BufferUtils.createFloatBuffer(6).put(new float[] { 0.0f, 0.0f, -1.0f,  0.0f, 1.0f, 0.0f }).rewind();

	public static List<int[]> loadedSounds = new LinkedList<int[]>();
	
	public static int[] loadSource(String filepath, int maxSources) {
		// Load wav data into a buffer.
		//AL10.alGenBuffers(buffer);
		int buffer = AL10.alGenBuffers();

		int error = AL10.alGetError();
		if(error != AL10.AL_NO_ERROR) {
			System.err.println("Encountered AL error code " + error + "!");
			System.exit(-1);
		}

		//Loads the wave file from your file system
		java.io.FileInputStream fin = null;
		try {
			fin = new java.io.FileInputStream(filepath);
		} catch (java.io.FileNotFoundException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		WaveData waveFile = WaveData.create(new BufferedInputStream(fin));
		try {
			fin.close();
		} catch (java.io.IOException ex) {}

		//Loads the wave file from this class's package in your classpath
		//WaveData waveFile = WaveData.create("FancyPants.wav");
		
		AL10.alBufferData(buffer, waveFile.format, waveFile.data, waveFile.samplerate);
		waveFile.dispose();

		int[] sources = new int[maxSources];
		
		for (int i = 0; i < maxSources; i++) {
		
		// Bind the buffer with the source.
		sources[i] = AL10.alGenSources();

		error = AL10.alGetError();
		if(error != AL10.AL_NO_ERROR) {
			System.err.println("Encountered AL error code " + error + "!");
			System.exit(-1);
		}

		AL10.alSourcei(sources[i], AL10.AL_BUFFER,   buffer   );
		AL10.alSourcef(sources[i], AL10.AL_PITCH,    1.0f     );
		AL10.alSourcef(sources[i], AL10.AL_GAIN,     1.0f     );
		AL10.alSource (sources[i], AL10.AL_POSITION, sourcePos);
		AL10.alSource (sources[i], AL10.AL_VELOCITY, sourceVel);

		}
		
		// Do another error check and return.
		error = AL10.alGetError();
		if(error != AL10.AL_NO_ERROR) {
			System.err.println("Encountered AL error code " + error + "!");
			System.exit(-1);
		}

		return sources;
	}

	public static void init() {
		try{
			AL.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		AL10.alGetError();
		
		AL10.alListener(AL10.AL_POSITION,    listenerPos);
		AL10.alListener(AL10.AL_VELOCITY,    listenerVel);
		AL10.alListener(AL10.AL_ORIENTATION, listenerOri);
	}

	public static void end() {
		AL.destroy();
	}

	public static void play(Sound sound) {
		AL10.alSourcePlay(sound.getSource());
	}

	public static void stop(Sound sound) {
		AL10.alSourceStop(sound.getSource());
	}

	public static void pause(Sound sound) {
		AL10.alSourcePause(sound.getSource());
	}

}
