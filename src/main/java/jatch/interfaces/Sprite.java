package main.java.jatch.interfaces;

import java.awt.Color;

public interface Sprite {
	// Motion
	void move(double steps);
	void turnR(double degs);
	void turnL(double degs);
	
	void point(double degs);
	void pointTowards(Sprite thing);
	void pointTowardsPointer();
	
	void goTo(double x, double y);
	void goTo(Sprite thing);
	void goToPointer();
	void glide(double secs, double x, double y);
	
	void changeX(double dx);
	void setX(double nx);
	void changeY(double dy);
	void setY(double ny);
	
	void bounce();
	
	void setRot(String s);
	
	default void showXPos() { getXPos().show(); }
	default void showYPos() { getYPos().show(); }
	default void showDir() { getDir().show(); }
	Variable getXPos();
	Variable getYPos();
	Variable getDir();
	
	// Looks
	void say(String s, double secs);
	void say(String s);
	void think(String s, double secs);
	void think(String s);
	
	void show();
	void hide();
	
	void switchCostume(String nc);
	void nextCostume();
	void switchBackdrop(String nb);
	
	void changeEffect(String effect, double n);
	void setEffect(String effect, double n);
	void clearGraphics();
	
	void changeSize(double ds);
	void setSize(double ns);
	
	void front();
	void back(double n);
	
	default void showCostumeN() { getCostumeN().show(); }
	default void showBackName() { getBackName().show(); }
	default void showSize() { getSize().show(); }
	Variable getCostumeN();
	Variable getBackName();
	Variable getSize();
	
	// Sound
	void playSound(String s);
	void playSoundUntilDone(String s);
	void stopSound();
	
	void playDrum(int drum, double beats);
	void rest(double beats);
	
	void playNote(int note, double beats);
	void setInstr(int instr);
	
	void changeVol(int dv);
	void setVol(int nv);
	default void showVol() { getVolume().show(); }
	Variable getVolume();
	
	void changeTempo(int dt);
	void setTempo(int nt);
	default void togTempo() { getTempo().show(); }
	Variable getTempo();
	
	// Pen
	void clear();
	
	void stamp();
	
	void down();
	void up();
	
	void setColor(Color nc);
	void changeColor(int dc);
	void setColor(int nc);
	
	void changeShade(int ds);
	void setShade(int ns);
	
	void changeSize(int ds);
	void setSize(int ns);
	
	// Data
	void set(Variable var, Value nv);
	void change(Variable var, Value dv);
	void show(Variable var);
	void hide(Variable var);
	
	void add(Value item, List list);
	void delete(int idx, List list);
	void insert(Value item, int idx, List list);
	void replace(int idx, List list, Value item);
	Value item(int idx, List list);
	default int length(List list) { return list.size(); }
	boolean contains(List list, Value item);
	void show(List list);
	void hide(List list);
	
	// Events
	void broadcast(String msg);
	void broadcastWait(String msg);
	
	// Control
	void wait(double secs);
}
