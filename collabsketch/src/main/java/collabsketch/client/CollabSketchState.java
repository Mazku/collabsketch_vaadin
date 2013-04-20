package collabsketch.client;

import java.util.ArrayList;

public class CollabSketchState extends com.vaadin.shared.AbstractComponentState {

	public ArrayList<DrawLine> lines = new ArrayList<DrawLine>();
	
	public int canvasWidth = 800;
	public int canvasHeight = 600;

	public String color = "000000";
}