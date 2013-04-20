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

	final private CollabSketchLineContainer lineContainer;
	
	final CollabSketchUpdateListener listener;
	
	final String sessionID; 
	
	
	// To process events from the client, we implement ServerRpc
	private CollabSketchServerRpc rpc = new CollabSketchServerRpc() {

		@Override
		public void drawingEnded(DrawLine line) {
			lineContainer.lineDrawed(listener, line);
			lineContainer.getLines().add(line);
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
		
		StringBuffer color = new StringBuffer();
		int user = this.lineContainer.getListeners().size() + 1;
		
		sessionID = ui.getSession().getSession().getId();
		if (lineContainer.getSessionColors().containsKey(sessionID)) {
			color.append(lineContainer.getSessionColors().get(sessionID));
		} else {
			if (user % 3 == 0) {
				color.append("ff");
			} else if (user % 2 == 0) {
				String s = Integer.toHexString((user - (user % 3))*50);
				if (s.length() == 1) {
					s = "0"+s;
				} else if (s.length() > 2) {
					s = s.substring(0, 2);
				}
				color.append(s);
			} else {
				color.append("00");
			}
			
			if (user % 3 == 0) {
				color.append("00");
			} else if (user % 2 == 0) {
				color.append("ff");
			} else {
				String s = Integer.toHexString((user - (user % 3))*50);
				if (s.length() == 1) {
					s = "0"+s;
				} else if (s.length() > 2) {
					s = s.substring(0, 2);
				}
				color.append(s);
			}
			
			if (user % 3 == 0) {
				String s = Integer.toHexString((user - (user % 3))*50);
				if (s.length() == 1) {
					s = "0"+s;
				} else if (s.length() > 2) {
					s = s.substring(0, 2);
				}
				color.append(s);
			} else if (user % 2 == 0) {
				color.append("00");
			} else {
				color.append("ff");
			}
			
			lineContainer.getSessionColors().put(sessionID, color.toString());
		}
		
		
		System.out.println("Color for the line " + color.toString());
		getState().color = color.toString();
		listener = new CollabSketchUpdateListener(ui) {
				
				@Override
				public void lineAdded(final DrawLine line) {
					System.out.println("Pushing line to widget " + CollabSketch.this.hashCode());
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
		
		lineContainer.getListeners().add(listener);
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
