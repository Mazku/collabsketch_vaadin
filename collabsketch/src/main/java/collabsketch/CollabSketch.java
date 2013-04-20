package collabsketch;


import java.util.ArrayList;

import com.vaadin.ui.UI;

import collabsketch.client.CollabSketchClientRpc;
import collabsketch.client.CollabSketchServerRpc;
import collabsketch.client.CollabSketchState;
import collabsketch.client.DrawLine;
import collabsketch.client.DrawPoint;

// This is the server-side UI component that provides public API 
// for CollabSketch
public class CollabSketch extends com.vaadin.ui.AbstractComponent {
	
	final static private String[] colors = new String[] {
		"800000", "ff0000", "0000ff", "f472d0", "f0a30a", "6a00ff", 
		"76608a", "ffff00", "808000", "800080", "ff00ff", "00ff00", 
		"008000", "000080", "00ffff", "008080", "a4c400", "ffa500"
	};

	final private CollabSketchLineContainer lineContainer;
	
	final CollabSketchUpdateListener listener;
	
	final String sessionID; 
	
	
	// To process events from the client, we implement ServerRpc
	private CollabSketchServerRpc rpc = new CollabSketchServerRpc() {

		@Override
		public void drawingEnded(final DrawLine line) {
			Thread thread = new Thread(new Runnable() {
				
				@Override
				public void run() {
					lineContainer.lineDrawed(listener, line);
					lineContainer.getLines().add(line);
				}
			});
			thread.start();
		}
	};
	

	public CollabSketch(CollabSketchLineContainer lineContainer, UI ui) {
		this(lineContainer, ui, 800, 600);
	}

	public CollabSketch(CollabSketchLineContainer lineContainer, UI ui, int width, int height) {
		this.lineContainer = lineContainer;
		setImmediate(true);
		getState().canvasWidth = width;
		getState().canvasHeight = height;
		
		String color;
		
		sessionID = ui.getSession().getSession().getId();
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
		
		
		System.out.println("Color for the line " + color.toString());
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
		System.out.println("Collab component created!");
		
		System.out.println("Amount of lines " + lineContainer.getLines().size());
		if (!lineContainer.getLines().isEmpty()) {
			getState().lines = (ArrayList<DrawLine>) lineContainer.getLines();
		}
		
		lineContainer.getListeners().put(sessionID, listener);
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
	
	@Override
	public void detach() {
		lineContainer.getListeners().remove(listener);
		lineContainer.getSessionColors().remove(sessionID);
		super.detach();
	}
}
