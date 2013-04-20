package collabsketch.client;

import com.vaadin.shared.communication.ServerRpc;

// ServerRpc is used to pass events from client to server
public interface CollabSketchServerRpc extends ServerRpc {
	public void drawingEnded(DrawLine line);
}
