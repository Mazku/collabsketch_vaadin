package collabsketch.client;

import java.io.Serializable;

public class DrawPoint implements Serializable {
	private static final long serialVersionUID = 1L;
	private float x;
	private float y;
	
	public DrawPoint() {
		
	}
	
	public DrawPoint(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
}
