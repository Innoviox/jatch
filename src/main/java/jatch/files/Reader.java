package main.java.jatch.files;

import java.awt.Image;
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
        try (BufferedReader br = new BufferedReader(new FileReader(new File("commands.csv")))) {
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
		String java = String.format("package compiled;\nimport main.java.jatch.script.*;import main.java.jatch.files.Reader;import java.util.ArrayList;import java.awt.Image;\npublic class %s extends Sprite {\nprivate String tempString;private boolean tempBool;private Controller controller;public %s() { this(false); } public %s(boolean cloned) { this.cloned = cloned; %s }", child.get("objName"), child.get("objName"), child.get("objName"), "%s");
		
		if (child.containsKey("variables")) {
			for (Object _vars: (Object[]) child.get("variables")) {
				Map<String, String> vars = (Map<String, String>) _vars;
				java += String.format(var, vars.get("name"), vars.get("value"));
			}
		}
		
		String costumes = "costumes = new ArrayList<Image>();";
		String add = "costumes.add(Reader.getImageFile(\"" + dir + "%s.%s\"));";
		for (Object _cost: (Object[]) child.get("costumes")) {
			Map<String, Object> cost = (Map<String, Object>) _cost;
			costumes += String.format(add, cost.get("baseLayerID"), cost.get("baseLayerMD5").toString().split("\\.")[1]);
		}
		
		java = String.format(java, costumes);
		
		scriptsToJava(scripts);
		for (String hook: hooks.keySet()) {
			java += hooks.get(hook);
			if (!java.endsWith("}\n")) java += "}\n";
		}
		System.out.println(java);
		java += "}";
		return new Formatter().formatSource(java);
	}

	public static String scriptsToJava(List<Script> scripts) {
		String java = "";
		String header = null;
		String currHook = null;
		String ee = "ExprEval ee = new ExprEval(\"%s\", this);\n";
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
					if (old.endsWith("}\n")) old = old.substring(0, old.length() - 2);
					hooks.put(currHook, old + java); // getJavaFunction(currHook).split(":")[0]
					currHook = "";
					java = "";
				}
			}
			else { 
				String cmd = s.cmd;
				String[] spl = cmd.split(":");

				if ("CONTROL".equals(spl[0])) {
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
									String add = String.format(ee, Arrays.deepToString(o)) + tb;
									cntrl = String.format(cntrl, add, "%s");
								} else {
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
					java += cntrl + "\n";
				} else {
					try {
						String add = String.format(spl[0].replaceAll("/", ","), s.args.toArray());
						if (!add.endsWith("{")) java += add + ";";
						else {
							// java += add;
							if (s.args.size() > 0) {	
								header = String.format(spl[1], s.args.toArray());
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
		try {
			return ImageIO.read(new File(name));
		} catch (IOException e) {
			System.out.println("Couldn't read: " + name);
			return null;
		}
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
		main += "Stage stage = new Stage(\"" + ((Map<String, Object>)((Object[])data.get("costumes"))[0]).get("costumeName") + "\");";
		main += "Controller c = new GameController(sprites, stage);DrawController dc = new DrawController(c);dc.start();}}";
		compileSingleSource(main);
	}
	
	public static void compileSource(String path) throws IOException, FormatterException {
		compileSource(read(path + "project.json"), path);
	}
}