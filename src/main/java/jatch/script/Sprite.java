package main.java.jatch.script;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;

import javax.sound.midi.Instrument;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;
import javax.swing.JComponent;

import main.java.jatch.files.Reader;
import main.java.jatch.script.vars.ListShower;
import main.java.jatch.script.vars.VarShower;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

import TurtleGraphics.*;

public abstract class Sprite extends JComponent implements MouseListener {
	// Variables
	/*
	public Variable<Double> xPos, yPos, dir, size, vol, tempo, loudness, timer;
	public Variable<String> costumeN, backName,  answer;
	*/
	public final String USERNAME = "username";
	public double xPos, yPos, dir, size, vol, tempo, loudness, timer;
	public String costumeN, backName,  answer, rotStyle;
	protected Map<String, List<Object>> lists;
	protected Thread thread;
	protected List<Thread> soundThreads;
	protected Controller controller;
	protected boolean touchingPtr;
	protected Pen pen;
	protected BufferedImage img;
	protected Map<String, Double> effects;
	/* public Map<String, Variable> vars = new HashMap(); */
	protected boolean draw;
	protected int layer;
	protected boolean bti = false, fti = false;
	protected int instrument;
	protected int penSize;
	protected boolean cloned;
	protected List<Image> costumes;
	public static Synthesizer midiSynth;
	public static Instrument[] instr;
	protected MidiChannel[] mChannels;
	public static final int MAXWIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	public static final int MAXHEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	public AffineTransform at;
	private BufferedImage _img;
	
	public static void initialize() throws MidiUnavailableException {
        midiSynth = MidiSystem.getSynthesizer(); 
        midiSynth.open();

        //get and load default instrument and channel lists
        instr = midiSynth.getDefaultSoundbank().getInstruments();
        
	}
	
	public void initialize(Controller c) {
		this.controller = c;
		this.lists = new HashMap<String, List<Object>>();
		// this.pen = new StandardPen(c.getDrawController().getCanvas());
		this.img = (BufferedImage)costumes.get(0);
		at = new AffineTransform();
		this._img = deepCopy(img);
		for (Sprite s: c.getSprites()) this.addMouseListener(s);
		// this.addMouseListener(controller.getStage());
	}
	
	private BufferedImage deepCopy(BufferedImage bi) {
		 ColorModel cm = bi.getColorModel();
		 boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		 WritableRaster raster = bi.copyData(null);
		 return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}
	
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(_img, MAXWIDTH / 2 + (int)xPos, MAXHEIGHT / 2 + (int)yPos, this);
    }
    
	// Motion
    private void pointImg() {
    		// cmpdir();
    		// System.out.println(dir);
        // 4. translate it to the center of the component
    		at = new AffineTransform();

        // 3. do the actual rotation
        at.rotate(Math.toRadians(dir), img.getWidth() / 2, img.getHeight() / 2);

        // 2. just a scale because this image is big
        // at.scale(0.5, 0.5);

        // 1. translate the object so that you rotate it around the 
        //    center (easier :))
        // at.translate(-img.getWidth()/2, -img.getHeight()/2);    	
        AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
        _img = op.filter(img, null);
    }
    
    private void cmpdir() {
    		if (dir < -360) dir = -dir - 360;
    		if (dir > 360) dir = -dir + 360;
    }
	public void move(double steps) {
		xPos += Math.cos(Math.toRadians(dir)) * steps;
		yPos += Math.sin(Math.toRadians(dir)) * steps;
	}
	
	public void turnR(double degs) {
		if (dir > 0) dir += degs;
		else dir += degs;
		// TODO: rotate actual image
		// TODO: account for different rotation styles
		pointImg();
	}
	
	public void turnL(double degs) {
		if (dir < 0) dir -= degs;
		else dir -= degs;
		// TODO: rotate actual image
		// TODO: account for different rotation styles
		pointImg();
	}
	
	public void point(double degs) {
		dir = degs;
		pointImg();
	}
	
	public void pointTowards(Sprite thing) {
		dir = Math.atan((xPos - thing.xPos / (yPos - thing.yPos)));
		pointImg();
	}
	
	public void pointTowardsPtr() {
		dir = Math.atan((xPos - mouseX()) / (yPos - mouseY()));
		pointImg();
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
	public void say(String s, double secs) throws InterruptedException {
		say(s);
		Thread.sleep((long) secs);
	}
	
	public void say(String s) {
		//TODO: Implement Say graphics
		System.out.println(this.getClass() + ": " + s);
	}
	
	public void think(String s, double secs) throws InterruptedException {
		think(s);
		Thread.sleep((long) secs);
	}
	
	public void think(String s) {
		//TODO: Implement Think graphics
	}
	
	public void showSelf() {
		draw = true;
	}
	
	public void hideSelf() {
		draw = false;
	}
	
	public void switchCostume(String nc) {
		costumeN = nc;
		img = (BufferedImage) Reader.getImageFile(nc);
	}
	
	public void nextCostume() {
		switchCostume((Integer.parseInt(costumeN) + 1)+"");
	}
	
	public void switchBackdrop(String nb) {
		controller.getStage().setBackdrop(nb);
	}
	
	public void changeEffect(String effect, double n) {
		// TODO: Implement effects
	}
	
	public void setEffect(String effect, double n) {
		//TODO: Implement effects
	}
	
	public void clearGraphics() {
		effects.clear();
	}
	
	public void changeSize(double ds) {
		size += ds;
	}
	
	public void setSize(double ns) {
		size = ns;
	}
	
	public void front() {
		layer = 0;
		setFti(true);
	}
	
	public void back(double n) {
		layer = -1;
		setBti(true);
	}
	
	void showCostumeN() { VarShower.show(costumeN, "costumeN"); }
	void showBackName() { VarShower.show(backName, "backName"); }
	void showSize() { VarShower.show(size, "size"); }
	
	// Sound
	public void playSound(String s) throws IOException {
	    Thread st = new Thread() {{ // Delegate operation to another thread
	    		playSoundUntilDone(s);
	    }};
	    st.start();
	    soundThreads.add(st);
	}
	
	public void playSoundUntilDone(String s) throws IOException {
		AudioPlayer.player.start(new AudioStream(Reader.getSoundFile(s)));
	}
	
	public void stopSound() {
		for (Thread t: soundThreads) t.interrupt();
	}
	
	void playDrum(int drum, double beats){}
	void rest(double beats){}
	
	public void playNote(int note, double beats) {
		mChannels = midiSynth.getChannels();	
		midiSynth.loadInstrument(instr[instrument]);//load an instrument
		mChannels[0].noteOn(note, (int)vol);//On channel 0, play note number 60 with velocity 100 
		try { Thread.sleep((long)beats); // wait time in milliseconds to control duration
		} catch( InterruptedException e ) { }
		mChannels[0].noteOff(note);//turn of the note
	}
	
	public void setInstr(int instr) {
		instrument = instr;
	}
	
	public void changeVol(int dv) { vol += dv; }
	public void setVol(int nv) { vol = nv; }
	public void showVol() { VarShower.show(vol, "vol"); }
	
	public void changeTempo(int dt) { tempo += dt; }
	public void setTempo(int nt) { tempo = nt; }
	public void showTempo() { VarShower.show(tempo, "tempo"); }
	
	// Pen
	public void clear() {
		// TODO: find out how to clear a canvas
	}
	
	public void stamp() {
		// TODO: find out how to stamp a canvas
	}
	
	public void down() {
		pen.down();
	}
	
	public void up() {
		pen.up();
	}
	
	public void setColor(Color nc) {
		pen.setColor(nc);
	}
	
	public void changeColor(int dc) {
		// TODO: how to do ?
	}
	
	public void setColor(int nc) {
		// TODO: convert to Color
	}
	
	public void changeShade(int ds) {
		// TODO: what?
	}
	
	public void setShade(int ns) {
		// TODO: what?
	}
	
	public void changeSize(int ds) {
		penSize += ds;
		setSize(penSize);
	}
	
	public void setSize(int ns){
		penSize = ns;
		pen.setWidth(ns);
	}
	
	// Data
	public void set(String var, Object nv) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		try {
			field(var).set(this, nv);
		} catch (NoSuchFieldException e) {
			controller.set(var, nv);
		}
	}
	
	public void change(String var, double dv) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Field f = field(var);
		f.set(this, (double)f.get(this) + dv);
	}
	
	public void showVar(String var) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		VarShower.show(fieldget(var), var);
	}
	
	public void hideVar(String var) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		VarShower.hide(fieldget(var), var);
	}
	
	public Field field(String var) throws NoSuchFieldException, SecurityException { return this.getClass().getField(var); }
	public Object fieldget(String var) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException { return field(var).get(this); }
	
	public void add(Object item, String list) {
		get(list).add(item);
	}
	
	public void delete(int idx, String list) {
		get(list).remove(idx);
	}
	
	public void insert(Object item, int idx, String list) {
		get(list).add(idx, item);
	}
	
	public void replace(int idx, String list, Object item) {
		get(list).set(idx, item);
	}
	
	public Object item(int idx, String list) {
		return get(list).get(idx);
	}
	
	public int length(String list) { 
		return get(list).size(); 
	}
	
	public boolean contains(String list, Object item) {
		return get(list).contains(item);
	}
	
	public void showList(String list) {
		ListShower.show(get(list), list);
	}
	public void hideList(String list) {
		ListShower.hide(get(list), list);
	}
	
	private List<Object> get(String list) { return lists.get(list); }
	// Events
	public void broadcast(String msg) throws Exception {
		controller.broadcast(msg);
	}
	
	public void broadcastWait(String msg) throws Exception {
		controller.broadcast(msg);
	}
	
	// Control
	void wait(double secs) throws InterruptedException {
		Thread.sleep((long) secs);
	}
	
	void waitUntil(boolean cond) {
		while (!cond) {}
	}
	
	/*
	void stop(String process) {
		if (process.equals("all")) controller.stop();
	}
	*/
	
	public void clone(Sprite thing) {
		controller.getSprites().add(thing.clone());
	}
	
	public Sprite clone() {
		return null;
	}
	
	public void deleteClone() {
		if (this.cloned) controller.getSprites().remove(this);
	}
	
	// Sensing
	public boolean touching(Sprite thing) {
		return false;
	}
	public boolean touching(String thing) {
		return false;
	}
	public boolean touchingPtr() {
		return touchingPtr;
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
	
	// Operator (functions)
	public int random(int min, int max) {
		return (int)(Math.random()*max) + min;
	}
	
	public char letterOf(int index, String string) {
		return string.charAt(index-1);
	}
	
	public int lengthOf(String string) {
		return string.length();
	}
	
	// Not doing video
	void resetTimer() {}
	VarShower getAttr(String attr, Sprite sprite) { return null; } // Store variables in Map?
	
	// Don't need to implement?
	VarShower current(String s) { return null; }
	int daysSince2000() { return 0; }
	String username() { return ""; }
	
	// Hooks
	public abstract void whenFlagClicked() throws Exception;
	public abstract void whenKeyPressed(String key) throws Exception;
	public abstract void whenClicked() throws Exception;
	public abstract void whenBackdropSwitches(String newbn) throws Exception;
	public abstract void whenAttrGreater(String attr, Object val) throws Exception;
	public abstract void whenIRecieve(String msg) throws Exception;
	public abstract void whenCloned() throws Exception;

	@Override
	public void mouseClicked(MouseEvent e) {
		// whenClicked();
	}

	@Override
	public void mousePressed(MouseEvent e){	
		try {
			whenClicked();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
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
	public boolean draw() { return draw; }
	public int getLayer() { return layer; }

	public boolean bti() {
		return bti;
	}

	public void setBti(boolean bti) {
		this.bti = bti;
	}

	public boolean fti() {
		return fti;
	}

	public void setFti(boolean fti) {
		this.fti = fti;
	}

	public void setLayer(int layer) {
		this.layer = layer;
	}

	public Thread getThread() {
		return thread;
	}

	public void setThread(Thread thread) {
		this.thread = thread;
	}

	public boolean isTouchingPtr() {
		return touchingPtr;
	}
}
