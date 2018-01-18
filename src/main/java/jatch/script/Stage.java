package main.java.jatch.script;

import java.awt.Image;

import main.java.jatch.files.Reader;

public class Stage {
	private String backdrop;
	private Image img;
	
	public Stage(String backdrop, Image img) {
		this.backdrop = backdrop;
		this.img = img;
	}
	
	public Stage(String backdrop) {
		this(backdrop, Reader.getImageFile(backdrop));
	}
	
	public String getBackdrop() { return backdrop; }
	public void setBackdrop(String newbn) { backdrop = newbn; }
	public Image getImage() { return img; }
	public void setImage(Image img) { this.img = img; }
}
