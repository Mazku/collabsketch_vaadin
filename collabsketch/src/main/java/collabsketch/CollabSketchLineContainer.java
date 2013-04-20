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
	
	private Set<CollabSketchUpdateListener> listeners = new HashSet<CollabSketchUpdateListener>();
	private Map<String, String> sessionColors = new HashMap<String, String>();
	
	public List<DrawLine> getLines() {
		return drawedLines;
	}
	
	public Set<CollabSketchUpdateListener> getListeners() {
		return listeners;
	}
	
	public Map<String, String> getSessionColors() {
		return sessionColors;
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
