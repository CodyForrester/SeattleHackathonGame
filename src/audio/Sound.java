package audio;

public class Sound {

	private int source;
	
	public Sound(String filepath) {
		source = AudioPlayer.loadSource(filepath);
	}
	
	public int getSource() {
		return source;
	}
	
}
