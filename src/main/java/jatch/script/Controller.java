package main.java.jatch.script;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sound.midi.MidiUnavailableException;

import main.java.jatch.graphics.DrawController;

public class Controller extends Component implements KeyListener, MouseListener {
	private final List<Thread> threads;
	private final List<Sprite> sprites;
	private String oldBackdrop;
	private Stage stage;
	private boolean mouseDown;
	private static final Map<String, Boolean> keys = new HashMap<String, Boolean>();
	
	public Controller(List<Sprite> sprites, Stage stage) {
		threads = new ArrayList<Thread>();
		this.sprites = sprites;
		for (Sprite s: sprites) s.initialize(this);

		this.stage = stage;
		mouseDown = false;
		oldBackdrop = stage.getBackdrop();
	}
	
	public void broadcast(String msg) throws Exception {
		for (Sprite s: sprites) s.whenIRecieve(msg);
	}
	
	public void whenFlagClicked() throws Exception {

		for (Sprite s: sprites) {
			Runnable update = () -> { try {
				s.whenFlagClicked();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}};
			threads.add(new Thread(update));
			threads.get(threads.size() - 1).start();
		}
	}
	
	public void whenKeyPressed(String key) throws Exception {
		for (Sprite s: sprites) s.whenKeyPressed(key);
	}
	
	public void whenBackdropSwitches(String newbn) throws Exception {
		for (Sprite s: sprites) s.whenBackdropSwitches(newbn);
	}
	
	public void whenAttrGreater(String attr, Object val) throws Exception {
		for (Sprite s: sprites) s.whenFlagClicked();
	}

	public void update() throws Exception {
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
	/**
	 * Overridden from mouseListener
	 */
	public void mouseEntered(MouseEvent e) { }
	/**
	 * Overridden from mouseListener
	 */
	public void mouseExited(MouseEvent e) { }
	public List<Thread> getThreads() { return threads; }
	public List<Sprite> getSprites() { return sprites; }
	public Stage getStage() { return stage; }
	public boolean isMouseDown() { return mouseDown; }
	public DrawController getDrawController() {
		// TODO Auto-generated method stub
		return null;
	}

	public void set(String var, Object nv) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		field(var).set(this, nv);
	}
	
	public Field field(String var) throws NoSuchFieldException, SecurityException { return this.getClass().getField(var); }
	public Object fieldget(String var) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException { return field(var).get(this); }
}
