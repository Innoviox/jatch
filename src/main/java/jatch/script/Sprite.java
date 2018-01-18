package main.java.jatch.script;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import main.java.jatch.script.vars.ListShower;
import main.java.jatch.script.vars.VarShower;

public abstract class Sprite implements MouseListener {
	// Variables
	/*
	public Variable<Double> xPos, yPos, dir, size, vol, tempo, loudness, timer;
	public Variable<String> costumeN, backName,  answer;
	*/
	public double xPos, yPos, dir, size, vol, tempo, loudness, timer;
	public String costumeN, backName,  answer, rotStyle;
	private Thread thread;
	private Controller controller;
	private boolean touchingPtr;
	private Image img;
	/* public Map<String, Variable> vars = new HashMap(); */
	
	// Motion
	public void move(double steps) {
		xPos += Math.cos(dir) * steps;
		yPos += Math.sin(dir) * steps;
	}
	
	public void turnR(double degs) {
		dir += degs;
		// TODO: rotate actual image
		// TODO: account for different rotation styles
	}
	
	public void turnL(double degs) {
		dir -= degs;
		// TODO: rotate actual image
		// TODO: account for different rotation styles
	}
	
	public void point(double degs) {
		dir = degs;
	}
	
	public void pointTowards(Sprite thing) {
		dir = Math.atan((xPos - thing.xPos / (yPos - thing.yPos)));
	}
	
	public void pointTowardsPtr() {
		dir = Math.atan((xPos - mouseX()) / (yPos - mouseY()));
	}
	
	public void goTo(double x, double y) {
		xPos = x;
		yPos = y;
	}
	
	public void goTo(Sprite thing) {
		xPos = thing.xPos;
		yPos = thing.yPos;
	}
	
	public void goToPtr() {
		xPos = mouseX();
		yPos = mouseY();
	}
	
	public void glide(double secs, double x, double y) throws InterruptedException {
		final long sleep = 10;
		int steps = (int) ((secs * 1000) / sleep);
		int xInc = (int)(x - xPos) / steps;
		int yInc = (int)(y - yPos) / steps;
		
		for (int i = 0; i < steps; i++) {
			Thread.sleep((long) sleep);
			xPos += xInc;
			yPos += yInc;
		}
	}
	
	public void changeX(double dx) {
		xPos += dx;
	}
	
	public void setX(double nx) {
		xPos = nx;
	}
	
	public void changeY(double dy) {
		yPos += dy;
	}
	
	public void setY(double ny) {
		yPos = ny;
	}
	
	public void bounce() {
		if (onEdge()) dir = -dir;
	}
	
	private boolean onEdge() {
		// TODO Auto-generated method stub
		return false;
	}

	void setRot(String s) {
		rotStyle = s;
	}
	
	void showXPos() { VarShower.show(xPos, "xPos"); }
	void showYPos() { VarShower.show(yPos, "yPos"); }
	void showDir() {VarShower.show(dir, "dir"); }
	
	// Looks
	void say(String s, double secs){}
	void say(String s){}
	void think(String s, double secs){}
	void think(String s){}
	
	void show(){}
	void hide(){}
	
	void switchCostume(String nc){}
	void nextCostume(){}
	void switchBackdrop(String nb){}
	
	void changeEffect(String effect, double n){}
	void setEffect(String effect, double n){}
	void clearGraphics(){}
	
	void changeSize(double ds){}
	void setSize(double ns){}
	
	void front(){}
	void back(double n){}
	
	void showCostumeN() { VarShower.show(costumeN, "costumeN"); }
	void showBackName() { VarShower.show(backName, "backName"); }
	void showSize() { VarShower.show(size, "size"); }
	
	// Sound
	void playSound(String s){}
	void playSoundUntilDone(String s){}
	void stopSound(){}
	
	void playDrum(int drum, double beats){}
	void rest(double beats){}
	
	void playNote(int note, double beats){}
	void setInstr(int instr){}
	
	void changeVol(int dv){}
	void setVol(int nv){}
	void showVol() { VarShower.show(vol, "vol"); }
	
	void changeTempo(int dt){}
	void setTempo(int nt){}
	void showTempo() { VarShower.show(tempo, "tempo"); }
	
	// Pen
	void clear(){}
	
	void stamp(){}
	
	void down(){}
	void up(){}
	
	void setColor(Color nc){}
	void changeColor(int dc){}
	void setColor(int nc){}
	
	void changeShade(int ds){}
	void setShade(int ns){}
	
	void changeSize(int ds){}
	void setSize(int ns){}
	
	// Data
	void set(VarShower var, Object nv){}
	void change(VarShower var, Object dv){}
	void show(VarShower var){}
	void hide(VarShower var){}
	
	void add(Object item, List list){}
	void delete(int idx, List list){}
	void insert(Object item, int idx, List list){}
	void replace(int idx, List list, Object item){}
	Object item(int idx, List list) {
		return list.get(idx);
	}
	int length(List list) { return list.size(); }
	boolean contains(List list, Object item) {
		return list.contains(item);
	}
	void show(List list){}
	void hide(List list){}
	
	// Events
	void broadcast(String msg){}
	void broadcastWait(String msg){}
	
	// Control
	void wait(double secs) throws InterruptedException {
		thread.sleep((long) secs);
	}
	
	void waitUntil(boolean cond) {
		while (!cond) {};
	}
	
	/*
	void stop(String process) {
		if (process.equals("all")) controller.stop();
	}
	*/
	
	void clone(Sprite thing) {
		
	}
	
	void deleteClone() {
		
	}
	
	// Sensing
	boolean touching(Sprite thing) {
		return false;
	}
	boolean touchingPtr() {
		return false;
	}
	boolean touchingColor(Color c) {
		return false;
	}
	boolean colorTouchingColor(Color c1, Color c2) {
		return false;
	}
	double distance(Sprite thing) { return 0.0; }
	double distancePtr() { return 0.0; }
	
	void ask(String s) {}
	
	boolean keyPressed(String key) { return false; }
	boolean mouseDown() { return false; }
	double mouseX() { return 0; }
	double mouseY() { return 0; }
	// Not doing video
	void resetTimer() {}
	VarShower getAttr(String attr, Sprite sprite) { return null; } // Store variables in Map?
	
	// Don't need to implement?
	VarShower current(String s) { return null; }
	int daysSince2000() { return 0; }
	String username() { return ""; }
	
	// Hooks
	public abstract List<Script> whenFlagClicked();
	public abstract List<Script> whenKeyPressed(String key);
	public abstract List<Script> whenClicked();
	public abstract List<Script> whenBackdropSwitches(String newbn);
	public abstract List<Script> whenAttrGreater(String attr, Object val);
	public abstract List<Script> whenIRecieve(String msg);
	

	@Override
	public void mouseClicked(MouseEvent e) {
		// whenClicked();
	}

	@Override
	public void mousePressed(MouseEvent e) {	
		whenClicked();
	}

	@Override
	public void mouseReleased(MouseEvent e) { }

	@Override
	public void mouseEntered(MouseEvent e) { 
		touchingPtr = true;
	}

	@Override
	public void mouseExited(MouseEvent e) { 
		touchingPtr = false;
	}
	
	public Image getImage() {
		return img;
	}
	public double getXPos() {
		return xPos;
	}
	public double getYPos() {
		return yPos;
	}
}
