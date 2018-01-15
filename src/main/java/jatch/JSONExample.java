package main.java.jatch;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.java.com.cedarsoftware.util.io.JsonReader;

public class JSONExample {
	public static void main(String[] args) throws IOException {
		String json = new String(Files.readAllBytes(Paths.get("project.json")), StandardCharsets.UTF_8);
		Map<String, Object> arg = new HashMap<String, Object>();
		arg.put(JsonReader.USE_MAPS, true);
		Map<String, Object[]> data = (Map<String, Object[]>) JsonReader.jsonToJava(json, arg);
		Object[] children = data.get("children");
		Object scripts = ((Map) children[0]).get("scripts");
		System.out.println(Arrays.deepToString((Object[]) scripts));
	}
}
