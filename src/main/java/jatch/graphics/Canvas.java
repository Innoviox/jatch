package main.java.jatch.graphics;

import javax.swing.*;

import main.java.jatch.script.Sprite;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class Canvas extends JFrame {

	public static final int MAXWIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	public static final int MAXHEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();

	private List<Sprite> objects;

	BufferedImage bi = new BufferedImage(MAXWIDTH, MAXHEIGHT, BufferedImage.TYPE_INT_RGB);

	public Canvas(List<Sprite> objects) {
		this.setSize(MAXWIDTH, MAXHEIGHT);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

	public void paint(Graphics g) {
			Graphics b = bi.getGraphics();

			b.setColor(Color.black);

			b.fillRect(0,0,MAXWIDTH,MAXHEIGHT);

			for (Sprite p : objects)
				paint(p);

			g = this.getGraphics();

			g.drawImage(bi, 0, 0, null);
	}

	private void paint(Sprite p) {
		// TODO Auto-generated method stub
	}
}