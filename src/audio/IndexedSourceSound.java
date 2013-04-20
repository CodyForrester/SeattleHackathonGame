package audio;

public class IndexedSourceSound {

	private int[] sources;
	
	public IndexedSourceSound(String filepath, int maxSources, boolean looping) {
		sources = AudioPlayer.loadSource(filepath, maxSources, looping);
	}
	
	public int getSource() {
		return sources[0];
	}
	
	public int getSource(int index) {
		return sources[index];
	}
	
}
