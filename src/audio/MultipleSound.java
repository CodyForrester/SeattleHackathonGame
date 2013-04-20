package audio;

public class MultipleSound implements Sound {

	private int[] sources;
	private int sourceIndex;
	
	public MultipleSound(String filepath, int maxSources) {
		sources = AudioPlayer.loadSource(filepath, maxSources, false);
	}
	
	public int getSource() {
		int nextSource = sources[sourceIndex];
		sourceIndex = (sourceIndex + 1) % sources.length;
		return nextSource;
	}
	
}
