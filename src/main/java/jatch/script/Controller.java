package main.java.jatch.script;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class Controller implements KeyListener, MouseListener {
	private final List<Thread> threads;
	private final List<Sprite> sprites;
	private String oldBackdrop;
	private Stage stage;
	private boolean mouseDown;
	
	public Controller(List<Sprite> sprites, Stage stage) {
		threads = new ArrayList<Thread>();
		this.sprites = sprites;
		this.stage = stage;
		mouseDown = false;
		oldBackdrop = stage.getBackdrop();
	}
	
	public void run(List<Script> scripts, Sprite sprite) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		Thread t = new Thread() {{
			for (Script script: scripts) script.call(sprite);
		}};
		threads.add(t);
		t.run();
	}
	
	public void broadcast(String msg) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		for (Sprite s: sprites) run(s.whenIRecieve(msg), s);
	}
	
	public void whenFlagClicked() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		for (Sprite s: sprites) run(s.whenFlagClicked(), s);
	}
	
	public void whenKeyPressed(String key) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		for (Sprite s: sprites) run(s.whenKeyPressed(key), s);
	}
	
	public void whenBackdropSwitches(String newbn) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		for (Sprite s: sprites) run(s.whenBackdropSwitches(newbn), s);
	}
	
	public void whenAttrGreater(String attr, Object val) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		for (Sprite s: sprites) run(s.whenFlagClicked(), s);
	}

	public void update() {
		String newbn = stage.getBackdrop();
		if (!oldBackdrop.equals(newbn)) {
			oldBackdrop = newbn;
			try {
				whenBackdropSwitches(newbn);
			} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		/*
		try {
			whenKeyPressed(KeyEvent.getKeyText(e.getKeyCode()));
		} catch (Exception e1) { }
		*/
	}

	@Override
	public void keyPressed(KeyEvent e) {
		try {
			whenKeyPressed(KeyEvent.getKeyText(e.getKeyCode()));
		} catch (Exception e1) { }
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	public void mouseClicked(MouseEvent e) { mouseDown = true; }
	public void mousePressed(MouseEvent e) { mouseDown = true; }
	public void mouseReleased(MouseEvent e) { mouseDown = false; }
	public void mouseEntered(MouseEvent e) { }
	public void mouseExited(MouseEvent e) { }
	public List<Thread> getThreads() { return threads; }
	public List<Sprite> getSprites() { return sprites; }
	public Stage getStage() { return stage; }
}
