package audio;

public class Sound {

	private int[] sources;
	private int sourceIndex;
	
	public Sound(String filepath, int maxSources) {
		sources = AudioPlayer.loadSource(filepath, maxSources);
	}
	
	public int getSource() {
		int nextSource = sources[sourceIndex];
		sourceIndex = (sourceIndex + 1) % sources.length;
		return nextSource;
	}
	
}
