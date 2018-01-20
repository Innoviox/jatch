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

import main.java.com.cedarsoftware.util.io.JsonReader;
import main.java.jatch.script.Script;

public class Reader {
	private final Map<String, String> cmds = new HashMap<String, String>();;
	
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

	
	@SuppressWarnings("unchecked")
	public static Map<String, Object[]> read(String filename) throws IOException {
		String json = new String(Files.readAllBytes(Paths.get(filename)), StandardCharsets.UTF_8);
		Map<String, Object> arg = new HashMap<String, Object>();
		arg.put(JsonReader.USE_MAPS, true);
		Map<String, Object[]> data = (Map<String, Object[]>) JsonReader.jsonToJava(json, arg);
		return data;
	}
	
	public static Map<String, Object> getChild(Map<String, Object[]> data, int child) {
		return (Map<String, Object>) data.get("children")[child];
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
	
	public String scriptToJava(Map<String, Object> child) {
		List<Script> scripts = extractScripts(child);
		String java = "";
		for (Script s: scripts) {			
			if (s.cmd.equals("DUMMY")) java += "}\n";
			else { 
				String cmd = s.cmd;
				try {
					java += String.format(cmd, s.args.toArray()) + ";\n";
				} catch(NullPointerException e) {
					java += cmd + "\n";
				}
				if (cmd.equals("CONTROL")) {
					for (Object o: s.args) {
						// TODO: Implement Control
					}
				}
			}
		}
			
		return java;
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