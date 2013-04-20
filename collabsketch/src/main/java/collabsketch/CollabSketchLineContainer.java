package collabsketch;

import java.util.ArrayList;
import java.util.HashSet;

import collabsketch.client.DrawLine;

public class CollabSketchLineContainer {

	private ArrayList<DrawLine> drawedLines = new ArrayList<DrawLine>();
	
	private HashSet<CollabSketchUpdateListener> listeners = new HashSet<CollabSketchUpdateListener>();
	
	public ArrayList<DrawLine> getLines() {
		return drawedLines;
	}
	
	public HashSet<CollabSketchUpdateListener> getListeners() {
		return listeners;
	}
}
