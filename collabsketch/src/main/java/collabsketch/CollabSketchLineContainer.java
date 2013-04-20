package collabsketch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

import collabsketch.client.DrawLine;

public class CollabSketchLineContainer implements Serializable {

	private ArrayList<DrawLine> drawedLines = new ArrayList<DrawLine>();
	
	private HashSet<CollabSketchUpdateListener> listeners = new HashSet<CollabSketchUpdateListener>();
	
	public ArrayList<DrawLine> getLines() {
		return drawedLines;
	}
	
	public HashSet<CollabSketchUpdateListener> getListeners() {
		return listeners;
	}
	
	public void canvasCleared(CollabSketchUpdateListener caller) {
		for (CollabSketchUpdateListener listener : listeners) {
			if (!listener.equals(caller)) {
				listener.canvasCleared();
			}
		}
	}
	
	public void lineDrawed(CollabSketchUpdateListener caller, DrawLine line) {
		System.out.println("Line drawed, calling " + listeners.size() + " listeners.");
		for (CollabSketchUpdateListener listener : listeners) {
			System.out.println("Comparing listeners " + caller.hashCode() + " to " + listener.hashCode());
			if (!listener.equals(caller)) {
				System.out.println("Calling lineAdded at listener " + listener.hashCode());
				listener.lineAdded(line);
			}
		}
	}
}
