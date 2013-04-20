package audio;

public class SingleSound implements Sound {

	private int source;
	
	public SingleSound(String filepath, boolean looping) {
		source = AudioPlayer.loadSource(filepath, 1, looping)[0];
	}
	
	public int getSource() {
		return source;
	}
	
}
