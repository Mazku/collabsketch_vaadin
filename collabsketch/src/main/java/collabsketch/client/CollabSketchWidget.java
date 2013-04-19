package collabsketch.client;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.FillStrokeStyle;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.layout.client.Layout;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

// Extend any GWT Widget
public class CollabSketchWidget extends VerticalPanel {

	boolean mouseDown = false;
	float last_x = 0;
	float last_y = 0;
	static final float dist_buffer = 7;
	
	public CollabSketchWidget() {
		add(new Label("test"));

		final Canvas canv = Canvas.createIfSupported();
		final Context2d context = canv.getContext2d();
		canv.setWidth("800px");
		canv.setHeight("800px");
		canv.setCoordinateSpaceHeight(800);
		canv.setCoordinateSpaceWidth(800);
		add(canv);
		
		canv.addMouseDownHandler(new MouseDownHandler() {
			
			@Override
			public void onMouseDown(MouseDownEvent event) {
				mouseDown = true;
				float x = event.getClientX() - canv.getAbsoluteLeft();
				float y = event.getClientY()  - canv.getAbsoluteTop();
				context.beginPath();
				context.setLineWidth(5);
				context.moveTo(x, y);
				last_x = x;
				last_y = y;
			}
		});
		
		canv.addMouseMoveHandler(new MouseMoveHandler() {
			
			@Override
			public void onMouseMove(MouseMoveEvent event) {
				if (mouseDown) {
					float x = event.getClientX() - canv.getAbsoluteLeft();
					float y = event.getClientY() - canv.getAbsoluteTop();
					
					if (getDistance(last_x, last_y, x, y) > dist_buffer) {
						context.lineTo(x, y);
						context.moveTo(x, y);
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
					mouseDown = false;
					context.closePath();
					last_x = 0;
					last_y = 0;
				}
			}
		});
		
		canv.addMouseUpHandler(new MouseUpHandler() {
			
			@Override
			public void onMouseUp(MouseUpEvent event) {
				if (mouseDown) {
					mouseDown = false;
					context.closePath();
					context.stroke();
					last_x = 0;
					last_y = 0;
				}
			}
		});
	}
	
	protected float getDistance(float x1, float y1, float x2, float y2) {
		return (float) Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
	}

}