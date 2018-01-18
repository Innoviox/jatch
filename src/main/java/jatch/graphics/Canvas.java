package main.java.jatch.graphics;

import javax.swing.*;

import main.java.jatch.script.Sprite;
import main.java.jatch.script.Stage;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class Canvas extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -412864872409712349L;
	
	public static final int MAXWIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	public static final int MAXHEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();

	private List<Sprite> objects;
	private Stage stage;
	
	BufferedImage bi = new BufferedImage(MAXWIDTH, MAXHEIGHT, BufferedImage.TYPE_INT_RGB);

	public Canvas(List<Sprite> objects, Stage stage) {
		this.setSize(MAXWIDTH, MAXHEIGHT);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.objects = objects;
		this.stage = stage;
	}

	public void paint(Graphics g) {
			Graphics b = bi.getGraphics();

			b.setColor(Color.black);

			b.fillRect(0,0,MAXWIDTH,MAXHEIGHT);

			for (Sprite p : objects)
				paint(p, g);

			g = this.getGraphics();

			g.drawImage(bi, 0, 0, null);
	}

	private void paint(Sprite p, Graphics g) {
		g.drawImage(stage.getImage(), 0, 0, MAXWIDTH, MAXHEIGHT, new Color(0, 0, 0), null);
		g.drawImage(p.getImage(), (int)p.getXPos(), (int)p.getYPos(), null);
	}
}