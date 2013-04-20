package collabsketch.demo;

import collabsketch.CollabSketch;
import collabsketch.CollabSketchLineContainer;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Alignment;
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
    	
    	
    	// Initialize our new UI component
    	final CollabSketch collabsketch = new CollabSketch(DemoUI.lines, this);
    	collabsketch.setSizeFull();
    	
		System.out.println("Demo application init");
		
    	// Show it in the middle of the screen
    	final VerticalLayout layout = new VerticalLayout();
        layout.setStyleName("demoContentLayout");
        layout.setSizeFull();
        layout.addComponent(new Button("Reset canvas", new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				collabsketch.clearCanvas();
			}
		}));
        layout.addComponent(collabsketch);
        layout.setExpandRatio(collabsketch, 1.0f);
        //layout.setComponentAlignment(collabsketch, Alignment.MIDDLE_CENTER);
        setContent(layout);
    }

}
