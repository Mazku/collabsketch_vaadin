package collabsketch;


import java.util.ArrayList;

import com.vaadin.server.ClientConnector.DetachEvent;
import com.vaadin.ui.UI;

import collabsketch.client.CollabSketchClientRpc;
import collabsketch.client.CollabSketchServerRpc;
import collabsketch.client.CollabSketchState;
import collabsketch.client.DrawLine;
import collabsketch.client.DrawPoint;

// This is the server-side UI component that provides public API 
// for CollabSketch
public class CollabSketch extends com.vaadin.ui.AbstractComponent {
	private static final long serialVersionUID = 1L;

	final static private String[] colors = new String[] {
		"ff0000", "0000ff", "f472d0", "f0a30a", "6a00ff", "800000",
		"76608a", "ffff00", "808000", "800080", "ff00ff", "00ff00", 
		"008000", "000080", "00ffff", "008080", "a4c400", "ffa500"
	};

	final private CollabSketchLineContainer lineContainer;
	
	final CollabSketchUpdateListener listener;
	
	final String sessionID; 
	
	final UI ui;
	
	
	// To process events from the client, we implement ServerRpc
	private CollabSketchServerRpc rpc = new CollabSketchServerRpc() {

		@Override
		public void drawingEnded(final DrawLine line) {
			lineContainer.getLines().add(line);
			lineContainer.lineDrawed(listener, line);
		}
	};
	

	public CollabSketch(CollabSketchLineContainer lineContainer, UI ui) {
		this(lineContainer, 800, 600);
	}

	public CollabSketch(CollabSketchLineContainer lineContainer, int width, int height) {
		this.lineContainer = lineContainer;
		setImmediate(true);
		getState().canvasWidth = width;
		getState().canvasHeight = height;
		
		String color;
		ui = UI.getCurrent();
		sessionID =  ui.getSession().getSession().getId();
		if (lineContainer.getSessionColors().containsKey(sessionID)) {
			color = lineContainer.getSessionColors().get(sessionID);
		} else {
			int user = this.lineContainer.getListeners().size();
			if (user >= colors.length) {
				color = colors[user % colors.length]; 
			} else {
				color = colors[user];
			}
			lineContainer.getSessionColors().put(sessionID, color.toString());
		}
		
		getState().color = color;
		listener = new CollabSketchUpdateListener(ui) {
				
				@Override
				public void lineAdded(final DrawLine line) {
					listener.getUi().runSafely(new Runnable() {
						
						@Override
						public void run() {
							getRpcProxy(CollabSketchClientRpc.class).drawLine(line); 
						}
					});
				}

				@Override
				public void canvasCleared() {
					listener.getUi().runSafely(new Runnable() {
						
						@Override
						public void run() {
							getRpcProxy(CollabSketchClientRpc.class).clearCanvas(); 
						}
					});
				}
			};
		
		// To receive events from the client, we register ServerRpc
		registerRpc(rpc);
		if (!lineContainer.getLines().isEmpty()) {
			getState().lines = (ArrayList<DrawLine>) lineContainer.getLines();
		}
		
		lineContainer.getListeners().put(sessionID, listener);

		
		ui.addDetachListener(new DetachListener() {
			
			@Override
			public void detach(DetachEvent event) {
				CollabSketch.this.lineContainer.getListeners().remove(listener);
				CollabSketch.this.lineContainer.getSessionColors().remove(sessionID);
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
		listener.canvasCleared();
		lineContainer.canvasCleared(listener);
	}
	
}
