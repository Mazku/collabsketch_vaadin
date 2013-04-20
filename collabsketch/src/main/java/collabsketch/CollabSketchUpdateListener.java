package collabsketch;

import com.vaadin.ui.UI;

import collabsketch.client.DrawLine;

public abstract class CollabSketchUpdateListener {

	private UI ui;
	
	public CollabSketchUpdateListener(UI ui) {
		this.ui = ui;
	}
	
	public UI getUi() {
		return ui;
	}
	
	public abstract void lineAdded(DrawLine line);
	
	public abstract void canvasCleared();
}
