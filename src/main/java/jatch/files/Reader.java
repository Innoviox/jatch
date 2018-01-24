package main.java.jatch.files;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.MissingFormatArgumentException;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.google.googlejavaformat.java.Formatter;
import com.google.googlejavaformat.java.FormatterException;
import com.cedarsoftware.util.io.JsonReader;

import main.java.jatch.script.Script;
import main.java.jatch.script.Sprite;
import main.java.jatch.script.Stage;

public class Reader {
	private static final Map<String, String> cmds = new HashMap<String, String>();
	private static final Map<String, String> hooks = new HashMap<String, String>();
	private static final String var = "private Object %s = %s;";
	
	public static void init() {
        String line = "";
        try (BufferedReader br = new BufferedReader(new FileReader(new File("commands-2.csv")))) {
            while ((line = br.readLine()) != null) {
                String[] cmd = line.split(",");
                cmds.put(cmd[0], cmd[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String s = "whenGreenFlag,public void whenFlagClicked() throws Exception {\n" + 
        		"whenKeyPressed,public void whenKeyPressed(String key) throws Exception {:if (\"%s\".equals(key)) {\n" + 
        		"whenClicked,public void whenClicked() throws Exception {\n" + 
        		"whenSceneStarts,public void whenBackdropSwitches(String newbn) throws Exception {:if (\"%s\".equals(newbn)) {\n" + 
        		"whenSensorGreaterThan,public void whenAttrGreater(String attr/ Object value) throws Exception {\n" + 
        		"whenIReceive,public void whenIRecieve(String msg) throws Exception {:if (\"%s\".equals(msg)) {\n" + 
        		"whenCloned,public void whenCloned() throws Exception {";
        for (String hook: s.split("\n")) {
    	        String[] h = hook.split(",");
    	        hooks.put(h[0], h[1].split(":")[0].replaceAll("/", ","));
        }
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String, Object[]> read(String filename) throws IOException {
		String json = new String(Files.readAllBytes(Paths.get(filename)), StandardCharsets.UTF_8);
		Map<String, Object> arg = new HashMap<String, Object>();
		arg.put(JsonReader.USE_MAPS, true);
		Map<String, Object[]> data = (Map<String, Object[]>) JsonReader.jsonToJava(json, arg);
		return data;
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getChild(Map<String, Object[]> data, int child) {
		return (Map<String, Object>) data.get("children")[child];
	}
	
	public static Map<String, Object> getChild(String fn, int child) throws IOException {
		return getChild(read(fn), child);
	}
	
	public static List<Map<String, Object>> getChildren(String fn) throws IOException {
		return getChildren(read(fn));
	}
	
	public static List<Map<String, Object>> getChildren(Map<String, Object[]> data) {
		List<Map<String, Object>> children = new ArrayList<>();
		int i = 0;
		while (true) {
			try {
				children.add(getChild(data, i++));
			} catch (IndexOutOfBoundsException e) {
				break;
			}
		}
		return children;
	}

	public static Object[] getScripts(Map<String, Object> child) {
		return (Object[]) child.get("scripts");
	}
	
	public static List<Script> extractScripts(Map<String, Object> child) {
		List<Script> extracted = new ArrayList<Script>();
		Object[] scripts = getScripts(child);
		for (Object scriptHolder: scripts) {
			Object[] scriptL = (Object[]) ((Object[]) scriptHolder)[2];
			for (Object _script: scriptL) {
				Object[] script = (Object[]) _script;
				List<Object> inputs = new ArrayList<Object>();
				String cmd = (String) script[0];
				for (int i = 1; i < script.length; i++) inputs.add(script[i]);
				extracted.add(new Script(cmd, inputs));
			}
			extracted.add(new Script("DUMMY", new ArrayList<Object>()));
		}
		
		return extracted;
	}
	
	public static String getJavaFunction(String scratchMethod) {
		return cmds.get(scratchMethod);
	}
	
	public static String scriptToJava(Map<String, Object> child, String dir) throws FormatterException {
		if (child == null || child.get("target") != null) {
			throw new NullPointerException("Child is null or nonextant");
		}
		init();
		List<Script> scripts = extractScripts(child);
		String java = String.format("package compiled;"
				+ "import main.java.jatch.script.*;import main.java.jatch.files.Reader;import java.util.ArrayList;import java.awt.Image;"
				+ "public class %s extends Sprite {"
				+ "private String tempString;private boolean tempBool;private Controller controller;private ExprEval ee;"
				+ "public %s() { this(false); } public %s(boolean cloned) { this.cloned = cloned; this.addMouseListener(this); %s }", 
				child.get("objName"), child.get("objName"), child.get("objName"), "%s");
		
		if (child.containsKey("variables")) {
			for (Object _vars: (Object[]) child.get("variables")) {
				Map<String, String> vars = (Map<String, String>) _vars;
				java += String.format(var, vars.get("name"), vars.get("value"));
			}
		}
		
		String costumes = "costumes = new ArrayList<Image>();";
		String add = "costumes.add(Reader.getImageFile(\"%s\"));";
		for (Object _cost: (Object[]) child.get("costumes")) {
			Map<String, Object> cost = (Map<String, Object>) _cost;
			costumes += String.format(add, costumeName(cost, dir));
		}
		
		java = String.format(java, costumes);
		
		scriptsToJava(scripts);
		for (String hook: hooks.keySet()) {
			if (hook.startsWith("public")) java += hook;
			java += hooks.get(hook);
			if (!java.endsWith("}\n")) java += "}\n";
		}
		// java += "}";
		System.out.println(java);
		try {
			return new Formatter().formatSource(java);
		} catch (FormatterException e) {
			return new Formatter().formatSource(java + "}");
		}
	}
	
	public static String scriptsToJava(List<Script> scripts) {
		String java = "";
		String header = null;
		String currHook = null;
		String ee = "ee = new ExprEval(\"%s\", this);\n";
		String eevar = "ee = new ExprEval(%s);\n";
		String tb = "tempBool = Boolean.parseBoolean(ee.parse())";
		for (Script s: scripts) {	
			if ("DUMMY".equals(s.cmd)) {
				java += "}\n";
				if (header != null) {
					java += "}\n";
					header = null;
				}
				if (currHook != null) {
					String old = hooks.get(currHook);
					if (old == null) old = "";
					if (old.endsWith("}\n")) old = old.substring(0, old.length() - 2);
					hooks.put(currHook, old + java); // getJavaFunction(currHook).split(":")[0]
					currHook = "";
					java = "";
				}
			}
			else { 
				String cmd = s.cmd;
				String[] spl = cmd.split(":");
				if ("procDef".equals(s.ocmd)) {
					String _sig = (String) s.args.get(0);
					/*
					sig = String.format(sig.replaceAll("%.", "%s,"), (Object[]) s.args.get(1))
							.replaceFirst(" ", "\\(");
					sig = sig.substring(0, sig.length() - 1) + ")";
					*/
					String sig = "";
					String[] parts = _sig.split(" ");
					Object[] names = (Object[]) s.args.get(1);
					sig += parts[0] + "(";
					for (int i = 1; i < parts.length; i++) {
						String type = "";
						switch (parts[i].charAt(1)) {
							case 'n': type = "int"; break;
							case 's': type = "String"; break;
							case 'b': type = "boolean"; break;
						}
						sig += type + " " + names[i - 1] + ", ";
					}
					currHook = String.format(cmd, sig.substring(0, sig.length() - 2) + ")");
					header = "";
				} else if ("call".equals(s.ocmd)) {
					String fn = ((String) s.args.get(0)).split(" ")[0] + "(";
					for (int i = 1; i < s.args.size(); i++) fn += s.args.get(i) + ",";
					fn = fn.substring(0, fn.length() - 1) + ");";
					java += fn;
				} else if ("CONTROL".equals(spl[0])) {
					String cntrl = spl[1];
					for (int i = 0; i < s.args.size(); i++) {
						Object[] o = null;
						Long l = null;
						try {
							o = (Object[]) s.args.get(i);
						} catch (ClassCastException e) {
							l = (Long) s.args.get(i);
						}
						String n = spl[i + 2];
						if ("EXPR".equals(n)) {
							if (o != null) {
								if (o[0].equals("touching:")) {
									cntrl = String.format(getJavaFunction((String)o[0]), o[1]) + cntrl;
								} else if (s.ocmd.equals("doUntil")) {
									String add;
									if (o[0].equals("getParam")) 
										add = String.format(eevar, o[1]) + tb;
									else
										add = String.format(ee, Arrays.deepToString(o)) + tb;
									cntrl = String.format(cntrl, "%s", add);
								} else {
									if (o[0].equals("getParam")) 
										java += String.format(eevar, o[1]);
									else
										java += String.format(ee, Arrays.deepToString(o));
									cntrl = tb + cntrl;
								}
							} else {
								cntrl = String.format(cntrl, l, "%s");
							}
						} else if ("FN".equals(n)) {
							
							List<Script> fn = extractScripts(o);
							String fnJava = scriptsToJava(fn);
							try {
								cntrl = String.format(cntrl, fnJava);
							} catch (MissingFormatArgumentException e) {
								cntrl = String.format(cntrl, fnJava, "%s");
							}
						}
						// TODO: More casing support!			
					}
					System.out.println(cntrl);
					java += cntrl + "\n";
				} else {
					try {
						List<Object> args = new ArrayList<Object>();
						for (int i = 0; i < s.args.size(); i++) {
							Object[] oarr = null;
							Object o = null;
							try {
								oarr = (Object[]) s.args.get(i);
							} catch (ClassCastException e) {
								o = (Object) s.args.get(i);
							}
							if (oarr != null) {
								args.add(oarr[1]);
							} else {
								try {
									args.add((Long)o);
								} catch (ClassCastException e) {
									args.add("\"" + o + "\"");
								}
							}
						}
						String add = String.format(spl[0].replaceAll("/", ","), args.toArray());
						if (!add.endsWith("{")) java += add + ";";
						else {
							// java += add;
							if (s.args.size() > 0) {	
								header = String.format(spl[1], args.toArray());
								java += header;
							}
							currHook = s.ocmd;
						}
						java += "\n";
					} catch (NullPointerException e) {
						java += cmd + "\n";
					}
				}
			}
		}
		return java;
	}
	
	public static List<Script> extractScripts(Object[] scripts) {
		List<Script> extracted = new ArrayList<Script>();
		for (Object _script: scripts) {
			Object[] script = (Object[]) _script;
			List<Object> inputs = new ArrayList<Object>();
			String cmd = (String) script[0];
			for (int i = 1; i < script.length; i++) inputs.add(script[i]);
			extracted.add(new Script(cmd, inputs));
		}		
		return extracted;
	}

	public static void compileSingleSource(String java) throws IOException, FormatterException {
		String name = java.split("public class ")[1].split(" ")[0].trim();
		System.out.println(java);

		File f = new File("src/compiled/" + name + ".java");
		f.getParentFile().mkdirs(); 
		f.createNewFile();
		
		FileWriter fw = new FileWriter(f);
		fw.write(new Formatter().formatSource(java));
		fw.flush();
		fw.close();
		
    }
	
	public static Image getImageFile(String name) {
		System.out.println("Reading: " + name);
		BufferedImage bi = null;
		try {
			bi = ImageIO.read(new FileInputStream(new File(name)));
			System.out.println("Success reading: " + name);
		} catch (IOException e) {
			System.err.println("Couldn't read: " + name);
		}
		return bi;
	}
	
	public static AudioInputStream getSoundFile(String name) {
		// TODO Auto-generated method stub
		try {
			return AudioSystem.getAudioInputStream(new File(name));
		} catch (UnsupportedAudioFileException | IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void compileSource(Map<String, Object[]> data, String dir) throws IOException, FormatterException {
		deleteDir(new File("src/compiled"));
		
		List<String> children = new ArrayList<String>();
		for (Map<String, Object> child: getChildren(data)) {
			try {
				compileSingleSource(scriptToJava(child, dir));
				children.add((String) child.get("objName"));
			} catch (NullPointerException e) {
				
			}
		}		
		
		String controller = "package compiled;import main.java.jatch.script.*;import java.util.List;public class GameController extends Controller {  public GameController(List<Sprite> sprites, Stage stage) {super(sprites, stage);}";
		if (data.containsKey("variables")) {
			for (Object _var: data.get("variables")) {
				Map<String, Object> variable = (Map<String, Object>) _var;
				controller += String.format(var, variable.get("name"), variable.get("value"));
			}
		}
		controller += "}";
		compileSingleSource(controller);
		
		String main = "package compiled;import main.java.jatch.script.*;import main.java.jatch.graphics.*;import java.util.ArrayList;import java.util.List;public class Game {public static void main(String[] args) throws Exception {"
				+ "List<Sprite> sprites = new ArrayList<Sprite>();";
		for (String s: children) {
			main += String.format("sprites.add(new %s());", s);
		}
		main += "Stage stage = new Stage(\"" + costumeName(((Map<String, Object>)((Object[])data.get("costumes"))[0]), dir) + "\");";
		main += "Controller c = new GameController(sprites, stage);DrawController dc = new DrawController(c);dc.start();}}";
		compileSingleSource(main);
	}
	
	public static void compileSource(String path) throws IOException, FormatterException {
		compileSource(read(path + "project.json"), path);
	}
	
	private static String costumeName(Map<String, Object> cost, String dir) {
		return String.format( dir + "%s.%s", cost.get("baseLayerID"), cost.get("baseLayerMD5").toString().split("\\.")[1]);
	}
	
	private static void deleteDir(File file) {
	    File[] contents = file.listFiles();
	    if (contents != null) {
	        for (File f : contents) {
	            deleteDir(f);
	        }
	    }
	    file.delete();
	}
}