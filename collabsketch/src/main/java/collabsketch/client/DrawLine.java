package collabsketch.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import collabsketch.client.DrawPoint;

public class DrawLine implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public List<DrawPoint> points = new ArrayList<DrawPoint>();
	
	public DrawLine() {
	}
	
	public void addPoint(DrawPoint point) {
		points.add(point);
	}
	
	public List<DrawPoint> getPointsFrom(int index) {
		return points.subList(index, points.size() - 1);
	}

	public void addPoints(List<DrawPoint> points) {
		this.points.addAll(points);
	}
}
