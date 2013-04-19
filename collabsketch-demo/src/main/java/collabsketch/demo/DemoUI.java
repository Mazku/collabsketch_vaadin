package collabsketch.demo;

import collabsketch.CollabSketch;
import collabsketch.CollabSketchLineContainer;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Theme("demo")
@Title("CollabSketch Add-on Demo")
@SuppressWarnings("serial")
public class DemoUI extends UI
{

	static CollabSketchLineContainer lines = new CollabSketchLineContainer();
	
    @Override
    protected void init(VaadinRequest request) {
    	
    	// Initialize our new UI component
    	final CollabSketch collabsketch = new CollabSketch(lines);

		System.out.println("Demo application init");
		
    	// Show it in the middle of the screen
    	final VerticalLayout layout = new VerticalLayout();
        layout.setStyleName("demoContentLayout");
        layout.setSizeFull();
        layout.addComponent(collabsketch);
        layout.setComponentAlignment(collabsketch, Alignment.MIDDLE_CENTER);
        setContent(layout);
    }

}
