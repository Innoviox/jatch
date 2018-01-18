package main.java.jatch.graphics;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import main.java.jatch.script.Controller;

public class DrawController {
    private Canvas canvas;
    private Runnable update;
    private Controller controller;
    
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public DrawController(Controller c) { 
    		controller = c; 
    		canvas = new Canvas(controller.getSprites());
    	}
    
    public void start() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    		controller.whenFlagClicked();
        update = () -> update();
        scheduler.scheduleAtFixedRate(update,1,1000/60, TimeUnit.MILLISECONDS);
    }

	private void update() {
		controller.update();
		canvas.paint(canvas.getGraphics());
	}
}