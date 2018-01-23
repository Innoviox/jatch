package main.java.jatch.graphics;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import main.java.jatch.script.Controller;

public class DrawController {
    private Canvas canvas;
    // private Runnable update;
    private Controller controller;
    
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public DrawController(Controller c) { 
    		controller = c; 
    		canvas = new Canvas(controller.getSprites(), controller.getStage());
    	}
    
    public void start() throws Exception {
    		canvas.show();
    		controller.whenFlagClicked();
        Runnable update = () -> {
			try {
				update();
			} catch (Exception e) {
				System.out.println("EXCEPTION! RUN FOR YOUR LIFE!");
				e.printStackTrace();
			}
		};
        scheduler.scheduleAtFixedRate(update,1,1000/60, TimeUnit.MILLISECONDS);
    }

	private void update() throws Exception {
		controller.update();
		canvas.paint(canvas.getGraphics());
	}
	
	public Canvas getCanvas() { return canvas; }
}
