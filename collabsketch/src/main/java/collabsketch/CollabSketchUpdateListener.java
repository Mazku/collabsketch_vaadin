package collabsketch;

import collabsketch.client.DrawLine;

public interface CollabSketchUpdateListener {
	public void LineAdded(DrawLine line);
	
	public void canvasCleared();
}
