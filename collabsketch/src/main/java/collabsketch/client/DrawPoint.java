package collabsketch.client;

import java.io.Serializable;

public class DrawPoint implements Serializable {
	private static final long serialVersionUID = 1L;
	public float x;
	public float y;
	
	public DrawPoint() {
		
	}
	
	public DrawPoint(float x, float y) {
		this.x = x;
		this.y = y;
	}
}
