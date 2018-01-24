package main.java.jatch.script;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

import main.java.jatch.files.Reader;

public class Stage extends JComponent { // implements MouseListener {
	private String backdrop;
	private Image img;
	private Controller c;
	
	public Stage(String backdrop, Image img) {
		this.backdrop = backdrop;
		this.img = img.getScaledInstance((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth(), (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight(), BufferedImage.TYPE_INT_ARGB);
	}
	
	public Stage(String backdrop) {
		this(backdrop, Reader.getImageFile(backdrop));
	}
	
	public void initialize(Controller c) {
		this.c = c;
		for (Sprite s: c.getSprites()) this.addMouseListener(s);
		// this.addMouseListener(c.getStage());			
	}
	
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(img, 0, 0, this);
    }
    
	public String getBackdrop() { return backdrop; }
	public void setBackdrop(String newbn) { backdrop = newbn; }
	public Image getImage() { return img; }
	public void setImage(Image img) { this.img = img; }

	/*
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	*/
}
