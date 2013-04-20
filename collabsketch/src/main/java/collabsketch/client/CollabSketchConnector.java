package collabsketch.client;

import collabsketch.CollabSketch;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractComponentConnector;
import com.vaadin.shared.ui.Connect;


// Connector binds client-side widget class to server-side component class
// Connector lives in the client and the @Connect annotation specifies the
// corresponding server-side component
@Connect(CollabSketch.class)
public class CollabSketchConnector extends AbstractComponentConnector {
	
	private final CollabSketchServerRpc rpc = RpcProxy.create(CollabSketchServerRpc.class, this);

	public CollabSketchConnector() {
		
		// To receive RPC events from server, we register ClientRpc implementation 
		registerRpc(CollabSketchClientRpc.class, new CollabSketchClientRpc() {
			public void alert(String message) {
				Window.alert(message);
			}

			@Override
			public void drawLine(DrawLine line) {
				getWidget().drawLine(line);
			}

			@Override
			public void updateState() {
				updateLines();
			}

			@Override
			public void clearCanvas() {
				getWidget().clearCanvas();
			}
		});

	}

	// We must implement createWidget() to create correct type of widget
	@Override
	protected Widget createWidget() {
		CollabSketchWidget widget = GWT.create(CollabSketchWidget.class);
		widget.addRpc(rpc);
		return widget;
	}
	
	// We must implement getWidget() to cast to correct type
	@Override
	public CollabSketchWidget getWidget() {
		return (CollabSketchWidget) super.getWidget();
	}

	// We must implement getState() to cast to correct type
	@Override
	public CollabSketchState getState() {
		return (CollabSketchState) super.getState();
	}

	// Whenever the state changes in the server-side, this method is called
	@Override
	public void onStateChanged(StateChangeEvent stateChangeEvent) {
		super.onStateChanged(stateChangeEvent);
		updateLines();
	}
	
	protected void updateLines() {
		GWT.log("Updating existing lines");
		if (!getState().lines.isEmpty()) {
			for (DrawLine line : getState().lines) {
				getWidget().drawLine(line);
			}
		}
	}

}
