package main.java.jatch.graphics;

import javax.swing.*;

import main.java.jatch.script.Sprite;
import main.java.jatch.script.Stage;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import TurtleGraphics.*;

public class Canvas extends JFrame {
	public static final int MAXWIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	public static final int MAXHEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();

	private List<Sprite> objects;
	private Stage stage;
	
	private BufferedImage bi = new BufferedImage(MAXWIDTH, MAXHEIGHT, BufferedImage.TYPE_INT_ARGB);
	
	
	public Canvas(List<Sprite> objects, Stage stage) {
		// super(MAXWIDTH, MAXHEIGHT);
		this.setSize(MAXWIDTH, MAXHEIGHT);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.objects = objects;
		this.stage = stage;
		this.setContentPane(stage);
	}

	public void paint(Graphics g) {
		// TODO: Implement layering & hiding
		/*
		Graphics b = bi.getGraphics();
		b.setColor(Color.black);
		b.fillRect(0,0,MAXWIDTH,MAXHEIGHT);
		g.drawImage(stage.getImage(), 0, 0, MAXWIDTH, MAXHEIGHT, new Color(255, 255, 255), null);
		*/
		/*
		List<Sprite> layered = new ArrayList<Sprite>();
		List<Sprite> fronts = new ArrayList<Sprite>();
		List<Sprite> backs = new ArrayList<Sprite>();
		List<Sprite> mids = new ArrayList<Sprite>();
		
		List<List<Sprite>> total = new ArrayList<List<Sprite>>();
		for (Sprite p: objects) {
			if (p.draw()) {
				if (p.fti()) add(fronts, p);
				else if (p.bti()) add(backs, p);
				else add(mids, p);
			}
		}
		
		total.add(fronts);
		total.add(backs);
		total.add(mids);
		
		int currLayer = 0;
		for (List<Sprite> l: total) {
			for (Sprite s: l) {
				s.setLayer(currLayer++);
				layered.add(s);
			}
		}
			
		for (Sprite p: layered) {
			p.setFti(false);
			p.setBti(false);
		}
		
		*/
		
		super.paintComponents(g);
		// g.drawImage(stage.getImage(), 0, 0, MAXWIDTH, MAXHEIGHT, new Color(0, 0, 0), null);
		for (Sprite p : objects) paint(p, g);

		// Graphics g2 = this.getGraphics();
		// g.drawImage(bi, 0, 0, null);
		// repaint();
		// Toolkit.getDefaultToolkit().sync();
	}

	private void add(List<Sprite> fronts, Sprite p) {
		int pos = 0;
		int layer = p.getLayer();
		for (Sprite s: fronts) {
			if (s.getLayer() > layer) break;
			pos++;
		}
		fronts.add(pos, p);
	}

	private void paint(Sprite p, Graphics g) {	
		g.drawImage(p.getImage(), MAXWIDTH / 2 + (int)p.getXPos(), MAXHEIGHT / 2 + (int)p.getYPos(), null);
	}
}