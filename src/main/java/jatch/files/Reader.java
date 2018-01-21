package main.java.jatch.files;

import java.awt.Image;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.googlejavaformat.java.Formatter;
import com.google.googlejavaformat.java.FormatterException;
import com.cedarsoftware.util.io.JsonReader;

import main.java.jatch.script.ExprEval;
import main.java.jatch.script.Script;

public class Reader {
	private static final Map<String, String> cmds = new HashMap<String, String>();;
	
	public Reader() {
        String line = "";
        try (BufferedReader br = new BufferedReader(new FileReader(new File("commands.csv")))) {
            while ((line = br.readLine()) != null) {
                String[] cmd = line.split(",");
                cmds.put(cmd[0], cmd[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	public static Reader init() {
		return new Reader();
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
	
	public String getJavaFunction(String scratchMethod) {
		return cmds.get(
				scratchMethod);
	}
	
	public static String scriptToJava(Map<String, Object> child) throws FormatterException {
		List<Script> scripts = extractScripts(child);
		String java = String.format("public class %s extends Sprite {\n", child.get("objName"));
		java += scriptsToJava(scripts) + "}";
		return new Formatter().formatSource(java);
	}

	public static String scriptsToJava(List<Script> scripts) {
		String java = "";
		String header = null;
		
		for (Script s: scripts) {			
			if ("DUMMY".equals(s.cmd)) {
				java += "}\n";
				if (header != null) {
					java += "}\n";
					header = null;
				}
			}
			else { 
				String cmd = s.cmd;
				// TODO: Implement Control
				String[] spl = cmd.split(":");
				if ("CONTROL".equals(spl[0])) {
					String cntrl = spl[1];
					for (int i = 0; i < s.args.size(); i++) {
						Object[] o = (Object[]) s.args.get(i);
						String n = spl[i + 2];
						if ("EXPR".equals(n)) {
							String add = String.format("ExprEval ee = new ExprEval(\"%s\");\n", Arrays.toString(o));
							cntrl = String.format(cntrl, "Boolean.parseBoolean(ee.parse())", "%s");
							java += add;
						} else if ("FN".equals(n)) {
							List<Script> fn = extractScripts(o);
							String fnJava = scriptsToJava(fn);
							cntrl = String.format(cntrl, fnJava);
						}
						// TODO: More casing support!			
					}
					java += cntrl + "\n";
				} else {
					try {
						String add = String.format(spl[0], s.args.toArray());
						if (!add.endsWith("{")) java += add + ";";
						else {
							java += add;
							if (s.args.size() > 0) {	
								header = String.format(spl[1], s.args.toArray());
								java += header;
							}
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


	public static Image getImageFile(String name) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static FileInputStream getSoundFile(String name) {
		// TODO Auto-generated method stub
		return null;
	}
}