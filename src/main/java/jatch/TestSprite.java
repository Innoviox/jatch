package main.java.jatch;

import main.java.jatch.script.Sprite;

public class TestSprite extends Sprite {
	public void whenGreenFlag() {
		System.out.println("Whenning");
	}
	
	public void move(Long l) {
		System.out.println("Forwarding " + l);
	}
}
