package main.java.jatch.files;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import main.java.com.cedarsoftware.util.io.JsonReader;

public class Reader {
	@SuppressWarnings("unchecked")
	public static Map<String, Object[]> read(String filename) throws IOException {
		String json = new String(Files.readAllBytes(Paths.get(filename)), StandardCharsets.UTF_8);
		Map<String, Object> arg = new HashMap<String, Object>();
		arg.put(JsonReader.USE_MAPS, true);
		Map<String, Object[]> data = (Map<String, Object[]>) JsonReader.jsonToJava(json, arg);
		return data;
	}
}
