package collabsketch;


import collabsketch.client.CollabSketchClientRpc;
import collabsketch.client.CollabSketchServerRpc;
import collabsketch.client.CollabSketchState;
import collabsketch.client.DrawLine;
import collabsketch.client.DrawPoint;

// This is the server-side UI component that provides public API 
// for CollabSketch
public class CollabSketch extends com.vaadin.ui.AbstractComponent {

	private CollabSketchLineContainer lineContainer;
	
	int linesAtClient = 0;
	
	// To process events from the client, we implement ServerRpc
	private CollabSketchServerRpc rpc = new CollabSketchServerRpc() {

		@Override
		public void drawingEnded(DrawLine line) {
			lineContainer.getLines().add(line);
			for(CollabSketchUpdateListener listener : lineContainer.getListeners()) {
				listener.LineAdded(line);
			}
			System.out.println("Line drawed with " + line.points.size() + " points!");
		}
	};

	public CollabSketch(CollabSketchLineContainer lineContainer) {
		setImmediate(true);
		this.lineContainer = lineContainer;
		// To receive events from the client, we register ServerRpc
		registerRpc(rpc);
		System.out.println("Collab component created!");
		
		System.out.println("Amount of lines " + lineContainer.getLines().size());
		if (!lineContainer.getLines().isEmpty()) {
			getState().lines = lineContainer.getLines();
		}
		
		lineContainer.getListeners().add(new CollabSketchUpdateListener() {
			
			@Override
			public void LineAdded(DrawLine line) {
				getRpcProxy(CollabSketchClientRpc.class).drawLine(line);
			}

			@Override
			public void canvasCleared() {
				getRpcProxy(CollabSketchClientRpc.class).clearCanvas();
			}
		});
	}
	
	// We must override getState() to cast the state to CollabSketchState
	@Override
	public CollabSketchState getState() {
		return (CollabSketchState) super.getState();
	}

	public void clearCanvas() {
		lineContainer.getLines().clear();
		getState().lines.clear();
		for (CollabSketchUpdateListener listener : lineContainer.getListeners()) {
			listener.canvasCleared();
		}
	}
}
