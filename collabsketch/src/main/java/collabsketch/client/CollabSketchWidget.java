package collabsketch.client;


import java.awt.Window;
import java.util.ArrayList;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Touch;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.TouchCancelEvent;
import com.google.gwt.event.dom.client.TouchCancelHandler;
import com.google.gwt.event.dom.client.TouchEndEvent;
import com.google.gwt.event.dom.client.TouchEndHandler;
import com.google.gwt.event.dom.client.TouchMoveEvent;
import com.google.gwt.event.dom.client.TouchMoveHandler;
import com.google.gwt.event.dom.client.TouchStartEvent;
import com.google.gwt.event.dom.client.TouchStartHandler;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.ui.VerticalPanel;

// Extend any GWT Widget
public class CollabSketchWidget extends VerticalPanel {
	
	boolean drawing = false;
	float last_x = 0;
	float last_y = 0;
	private CollabSketchServerRpc rpc;
	static final float dist_buffer = 7;
	ArrayList<DrawPoint> points = new ArrayList<DrawPoint>();
	final Canvas canv;
	final Context2d context;
	String color;
	int lines;
	
	public @UiConstructor CollabSketchWidget(int width, int height, String color) {
		this.color = color;
		setBorderWidth(10);
		setSize(width + "px", height + "px");
		
		canv = Canvas.createIfSupported();
		context = canv.getContext2d();
		canv.setCoordinateSpaceWidth(width);
		canv.setCoordinateSpaceHeight(height);
		canv.setSize(width + "px", height + "px");
		add(canv);
		
		canv.addMouseDownHandler(new MouseDownHandler() {
			
			@Override
			public void onMouseDown(MouseDownEvent event) {
				startDrawing(event.getClientX(), event.getClientY());
			}
		});
		
		canv.addMouseMoveHandler(new MouseMoveHandler() {
			
			@Override
			public void onMouseMove(MouseMoveEvent event) {
				if (drawing) {
					continueDrawing(event.getClientX(), event.getClientY());
				}
			}
		});
		
		canv.addMouseOutHandler(new MouseOutHandler() {
			
			@Override
			public void onMouseOut(MouseOutEvent event) {
				if (drawing) {
					endDrawing();
				}
			}
		});
		
		canv.addMouseUpHandler(new MouseUpHandler() {
			
			@Override
			public void onMouseUp(MouseUpEvent event) {
				if (drawing) {
					endDrawing();
				}
			}
		});
		
		canv.addTouchStartHandler(new TouchStartHandler() {
			
			@Override
			public void onTouchStart(TouchStartEvent event) {
				if (event.getTouches().length() == 1) {
					Touch firstTouch = event.getTouches().get(0);
					startDrawing(firstTouch.getClientX(), firstTouch.getClientY());
				}
			}
		});
		
		canv.addTouchMoveHandler(new TouchMoveHandler() {
			
			@Override
			public void onTouchMove(TouchMoveEvent event) {
				if (drawing && event.getTouches().length() == 1) {
					Touch firstTouch = event.getTouches().get(0);
					continueDrawing(firstTouch.getClientX(), firstTouch.getClientY());
					event.preventDefault();
				}
			}
		});
		
		canv.addTouchEndHandler(new TouchEndHandler() {
			
			@Override
			public void onTouchEnd(TouchEndEvent event) {
				if (drawing) {
					endDrawing();
					event.preventDefault();
				}
			}
		});
	}

	protected void startDrawing(int clientX, int clientY) {
		Context2d context = canv.getContext2d();
		drawing = true;
		float x = clientX - canv.getAbsoluteLeft();
		float y = clientY - canv.getAbsoluteTop();
		context.beginPath();
		context.setLineWidth(5);
		context.setStrokeStyle(color);
		context.moveTo(x, y);
		points.add(new DrawPoint(x, y));
		last_x = x;
		last_y = y;
	}
	
	private void continueDrawing(int clientX, int clientY) {
		Context2d context = canv.getContext2d();
		float x = clientX - canv.getAbsoluteLeft();
		float y = clientY - canv.getAbsoluteTop();
		
		if (getDistance(last_x, last_y, x, y) > dist_buffer) {
			context.lineTo(x, y);
			context.moveTo(x, y);
			points.add(new DrawPoint(x, y));
			context.stroke();
			last_x = x;
			last_y = y;
		}
	}
	
	protected void endDrawing() {
		drawing = false;
		Context2d context = canv.getContext2d();
		context.closePath();
		DrawLine line = new DrawLine();
		GWT.log("Sending line to server with " + points.size() + " points.");
		line.addPoints(points);
		line.color = color;
		rpc.drawingEnded(line);
		points.clear();
		last_x = 0;
		last_y = 0;
		lines++;
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
		context.setStrokeStyle(line.color);
		boolean first = true;
		for(DrawPoint point : line.points) {
			if (first) {
				context.moveTo(point.x, point.y);
				first = false;
			} else {
				GWT.log("Adding point at " + point.x + ", " + point.y);
				context.lineTo(point.x, point.y);
				context.moveTo(point.x, point.y);
			}
		}
		context.closePath();
		context.stroke();
		lines++;
	}

	public void clearCanvas() {
		context.clearRect(0, 0, canv.getCoordinateSpaceWidth(), canv.getCoordinateSpaceHeight());
	}

	public void updateCanvasSize(int canvasWidth, int canvasHeight) {
		canv.setCoordinateSpaceWidth(canvasWidth);
		canv.setCoordinateSpaceHeight(canvasHeight);
		canv.setSize(canvasWidth + "px", canvasHeight + "px");
	}
	
	public void updateColor(String color) {
		this.color = color;
	}

}