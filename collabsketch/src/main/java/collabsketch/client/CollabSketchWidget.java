package collabsketch.client;


import java.util.ArrayList;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.user.client.ui.VerticalPanel;

// Extend any GWT Widget
public class CollabSketchWidget extends VerticalPanel {
	
	boolean mouseDown = false;
	float last_x = 0;
	float last_y = 0;
	private CollabSketchServerRpc rpc;
	static final float dist_buffer = 7;
	ArrayList<DrawPoint> points = new ArrayList<DrawPoint>();
	final Canvas canv;
	final Context2d context;
	
	public CollabSketchWidget() {

		canv = Canvas.createIfSupported();
		context = canv.getContext2d();
		canv.setWidth("800px");
		canv.setHeight("800px");
		canv.setCoordinateSpaceHeight(800);
		canv.setCoordinateSpaceWidth(800);
		add(canv);
		
		canv.addMouseDownHandler(new MouseDownHandler() {
			
			@Override
			public void onMouseDown(MouseDownEvent event) {
				Context2d context = canv.getContext2d();
				mouseDown = true;
				float x = event.getClientX() - canv.getAbsoluteLeft();
				float y = event.getClientY()  - canv.getAbsoluteTop();
				context.beginPath();
				context.setLineWidth(5);
				context.moveTo(x, y);
				points.add(new DrawPoint(x, y));
				last_x = x;
				last_y = y;
			}
		});
		
		canv.addMouseMoveHandler(new MouseMoveHandler() {
			
			@Override
			public void onMouseMove(MouseMoveEvent event) {
				if (mouseDown) {
					Context2d context = canv.getContext2d();
					float x = event.getClientX() - canv.getAbsoluteLeft();
					float y = event.getClientY() - canv.getAbsoluteTop();
					
					if (getDistance(last_x, last_y, x, y) > dist_buffer) {
						context.lineTo(x, y);
						context.moveTo(x, y);
						points.add(new DrawPoint(x, y));
						context.stroke();
						last_x = x;
						last_y = y;
					}
				}
			}
		});
		
		canv.addMouseOutHandler(new MouseOutHandler() {
			
			@Override
			public void onMouseOut(MouseOutEvent event) {
				if (mouseDown) {
					endDrawing();
				}
			}
		});
		
		canv.addMouseUpHandler(new MouseUpHandler() {
			
			@Override
			public void onMouseUp(MouseUpEvent event) {
				if (mouseDown) {
					endDrawing();
				}
			}
		});
	}
	
	protected void endDrawing() {
		mouseDown = false;
		Context2d context = canv.getContext2d();
		context.closePath();
		DrawLine line = new DrawLine();
		GWT.log("Sending line to server with " + points.size() + " points.");
		line.addPoints(points);
		rpc.drawingEnded(line);
		/*rpc.drawBegin(points.get(0));
		for(DrawPoint point : points.subList(1, points.size() - 1)) {
			rpc.drawPoint(point);
		}
		rpc.drawingEnd();*/
		points.clear();
		last_x = 0;
		last_y = 0;
	}
	
	protected float getDistance(float x1, float y1, float x2, float y2) {
		return (float) Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
	}

	public void addRpc(CollabSketchServerRpc rpc) {
		this.rpc = rpc;
	}

	public void drawLine(DrawLine line) {
		Context2d context = canv.getContext2d();
		GWT.log("Drawing an existing line size " + line.points.size() + " into " + context);
		context.beginPath();
		context.setLineWidth(5);
		boolean first = true;
		for(DrawPoint point : line.points) {
			if (first) {
				context.moveTo(point.x, point.y);
				first = false;
			} else {
				GWT.log("Adding point at " + point.x + ", " + point.y);
				context.lineTo(point.x, point.y);
				context.moveTo(point.x, point.y);
				context.stroke();
			}
		}
		context.closePath();
	}

	public void clearCanvas() {
		canv.setWidth("800px");
		context.clearRect(0, 0, canv.getCoordinateSpaceWidth(), canv.getCoordinateSpaceHeight());
	}

}