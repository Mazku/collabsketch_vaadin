package collabsketch;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import collabsketch.client.DrawLine;

public class CollabSketchLineContainer {

	private ArrayList<DrawLine> drawedLines = new ArrayList<DrawLine>();
	
	public ArrayList<DrawLine> getLines() {
		return drawedLines;
	}
}
