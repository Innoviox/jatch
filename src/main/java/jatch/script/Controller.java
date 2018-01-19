package main.java.jatch.script;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.java.jatch.graphics.DrawController;

public class Controller implements KeyListener, MouseListener {
	private final List<Thread> threads;
	private final List<Sprite> sprites;
	private String oldBackdrop;
	private Stage stage;
	private boolean mouseDown;
	private static final Map<String, Boolean> keys = new HashMap<String, Boolean>();
	
	public Controller(List<Sprite> sprites, Stage stage) {
		threads = new ArrayList<Thread>();
		this.sprites = sprites;
		this.stage = stage;
		mouseDown = false;
		oldBackdrop = stage.getBackdrop();
	}
	
	public void broadcast(String msg) {
		for (Sprite s: sprites) s.whenIRecieve(msg);
	}
	
	public void whenFlagClicked() {
		for (Sprite s: sprites) s.whenFlagClicked();
	}
	
	public void whenKeyPressed(String key) {
		for (Sprite s: sprites) s.whenKeyPressed(key);
	}
	
	public void whenBackdropSwitches(String newbn) {
		for (Sprite s: sprites) s.whenBackdropSwitches(newbn);
	}
	
	public void whenAttrGreater(String attr, Object val) {
		for (Sprite s: sprites) s.whenFlagClicked();
	}

	public void update() {
		String newbn = stage.getBackdrop();
		if (!oldBackdrop.equals(newbn)) {
			oldBackdrop = newbn;
			whenBackdropSwitches(newbn);
		}
		// TODO: Check for attrGreater
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
			keys.put(KeyEvent.getKeyText(e.getKeyCode()), true);
			whenKeyPressed(KeyEvent.getKeyText(e.getKeyCode()));
		} catch (Exception e1) { }
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		keys.put(KeyEvent.getKeyText(e.getKeyCode()), false);
	}
	
	public boolean keyDown(String k) {
		Boolean b = keys.get(k);
		return b==null?false:b;
	}
	public void mouseClicked(MouseEvent e) { mouseDown = true; }
	public void mousePressed(MouseEvent e) { mouseDown = true; }
	public void mouseReleased(MouseEvent e) { mouseDown = false; }
	public void mouseEntered(MouseEvent e) { }
	public void mouseExited(MouseEvent e) { }
	public List<Thread> getThreads() { return threads; }
	public List<Sprite> getSprites() { return sprites; }
	public Stage getStage() { return stage; }

	public DrawController getDrawController() {
		// TODO Auto-generated method stub
		return null;
	}
}
