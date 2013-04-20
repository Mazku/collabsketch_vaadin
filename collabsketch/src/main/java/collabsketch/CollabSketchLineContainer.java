package collabsketch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import collabsketch.client.DrawLine;

public class CollabSketchLineContainer implements Serializable {

	private List<DrawLine> drawedLines = new ArrayList<DrawLine>();
	
	private Map<String, CollabSketchUpdateListener> listeners = new HashMap<String, CollabSketchUpdateListener>();
	private Map<String, String> sessionColors = new HashMap<String, String>();
	
	public List<DrawLine> getLines() {
		return drawedLines;
	}
	
	public Map<String, CollabSketchUpdateListener> getListeners() {
		return listeners;
	}
	
	public Map<String, String> getSessionColors() {
		return sessionColors;
	}
	
	public void canvasCleared(CollabSketchUpdateListener caller) {
		for (CollabSketchUpdateListener listener : listeners.values()) {
			if (!listener.equals(caller)) {
				listener.canvasCleared();
			}
		}
	}
	
	public void lineDrawed(CollabSketchUpdateListener caller, DrawLine line) {
		System.out.println("Line drawed, calling " + listeners.size() + " listeners.");
		for (CollabSketchUpdateListener listener : listeners.values()) {
			if (!listener.equals(caller)) {
				listener.lineAdded(line);
			}
		}
	}
}
