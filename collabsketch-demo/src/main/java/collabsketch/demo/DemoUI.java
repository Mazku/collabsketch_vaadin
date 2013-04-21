package collabsketch.demo;

import collabsketch.CollabSketch;
import collabsketch.CollabSketchLineContainer;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

@Theme("demo")
@Title("CollabSketch Add-on Demo")
@SuppressWarnings("serial")
public class DemoUI extends UI
{

	public static CollabSketchLineContainer lines = new CollabSketchLineContainer();
	
    @Override
    protected void init(VaadinRequest request) {
    	final CollabSketch collabsketch = new CollabSketch(DemoUI.lines, 800, 600);
		
    	final VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.addComponent(new Button("Reset canvas", new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				collabsketch.clearCanvas();
			}
		}));
        layout.addComponent(collabsketch);
        layout.setExpandRatio(collabsketch, 1.0f);
        setContent(layout);
    }

}
