package main.java.jatch.script.vars;

import main.java.jatch.graphics.Showable;

public class Variable implements Showable {
	private Value value;
	@Override
	public void hide() {}
	@Override
	public void show() {}
	public Variable(Value value) { this.value = value; }
	public Value getValue() { return value; }
}
