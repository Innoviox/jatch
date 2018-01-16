package main.java.jatch.script;

import java.awt.Color;

import main.java.jatch.script.vars.ShowableList;
import main.java.jatch.script.vars.Value;
import main.java.jatch.script.vars.Variable;

public abstract class Sprite {
	// Variables
	public Variable xPos, yPos, dir, costumeN, backName, size, vol, tempo, answer, loudness, timer;
	private Thread thread;
	private Controller controller;
	/* public Map<String, Variable> vars = new HashMap(); */
	
	// Motion
	void move(double steps) {}
	void turnR(double degs) {}
	void turnL(double degs) {}
	
	void point(double degs) {}
	void pointTowards(Sprite thing) {}
	void pointTowardsPtr() {}
	
	void goTo(double x, double y) {}
	void goTo(Sprite thing) {}
	void goToPtr() {}
	void glide(double secs, double x, double y) {}
	
	void changeX(double dx){}
	void setX(double nx){}
	void changeY(double dy){}
	void setY(double ny){}
	
	void bounce(){}
	
	void setRot(String s){}
	
	void showXPos() { xPos.show(); }
	void showYPos() { yPos.show(); }
	void showDir() { dir.show(); }
	
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
	
	void showCostumeN() { costumeN.show(); }
	void showBackName() { backName.show(); }
	void showSize() { size.show(); }
	
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
	void showVol() { vol.show(); }
	
	void changeTempo(int dt){}
	void setTempo(int nt){}
	void togTempo() { tempo.show(); }
	
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
	void set(Variable var, Value nv){}
	void change(Variable var, Value dv){}
	void show(Variable var){}
	void hide(Variable var){}
	
	void add(Value item, ShowableList list){}
	void delete(int idx, ShowableList list){}
	void insert(Value item, int idx, ShowableList list){}
	void replace(int idx, ShowableList list, Value item){}
	Value item(int idx, ShowableList list) {
		return list.get(idx).getValue();
	}
	int length(ShowableList list) { return list.size(); }
	boolean contains(ShowableList list, Value item) {
		return list.contains(item);
	}
	void show(ShowableList list){}
	void hide(ShowableList list){}
	
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
	
	void stop(String process) {
		if (process.equals("all")) controller.stop();
	}
	
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
	Variable getAttr(String attr, Sprite sprite) { return null; } // Store variables in Map?
	
	// Don't need to implement?
	Variable current(String s) { return null; }
	int daysSince2000() { return 0; }
	String username() { return ""; }
}
