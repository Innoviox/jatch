package main.java.jatch.script;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import main.java.jatch.script.vars.Value;

public class Controller {
	private List<Thread> threads;
	private List<Sprite> sprites;
	
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
		
	}
	public void whenClicked() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		
	}
	public void whenBackdropSwitches(String newbn) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		
	}
	public void whenAttrGreater(String attr, Value val) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		
	}
	public void whenIRecieve(String msg) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		
	}
}
