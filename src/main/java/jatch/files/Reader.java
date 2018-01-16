package main.java.jatch.files;

import java.io.IOException;
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
		}
		
		return extracted;
	}
	
	public static String getJavaFunction(String scratchMethod) {
		return null;
	}
	
	public List<String> scriptToJava(Map child) {
		Object[] scripts = (Object[]) child.get("scripts");
		List<String> javas = new ArrayList<String>();
		for (Object o: scripts) {
			
		}
		return null;
	}
}