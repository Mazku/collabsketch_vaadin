package collabsketch.client;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.layout.client.Layout;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

// Extend any GWT Widget
public class CollabSketchWidget extends Widget {

	public CollabSketchWidget() {
		Layout layout = new Layout(getElement());

		Canvas canv = Canvas.createIfSupported();
		layout.attachChild(canv.getCanvasElement());
	}

}