package main.java.jatch.script;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import main.java.jatch.script.vars.Value;

public class Controller implements KeyListener, MouseListener {
	private List<Thread> threads;
	private List<Sprite> sprites;
	private boolean mouseDown;
	
	public Controller(List<Sprite> sprites) {
		threads = new ArrayList<Thread>();
		this.sprites = sprites;
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
	
	public void whenAttrGreater(String attr, Value val) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		for (Sprite s: sprites) run(s.whenFlagClicked(), s);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		try {
			whenKeyPressed(KeyEvent.getKeyText(e.getKeyCode()));
		} catch (NoSuchMethodException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InvocationTargetException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		try {
			whenKeyPressed(KeyEvent.getKeyText(e.getKeyCode()));
		} catch (NoSuchMethodException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InvocationTargetException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		mouseDown = true;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		mouseDown = true;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		mouseDown = false;
	}

	@Override
	public void mouseEntered(MouseEvent e) { }

	@Override
	public void mouseExited(MouseEvent e) { }
}
