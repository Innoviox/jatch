package main.java.jatch;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import main.java.jatch.files.*;
import main.java.jatch.script.Script;

public class Main {
	public static void main(String[] args) throws IOException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		String filename = "test-recursive3.sb2";
		String dir = "test-recursive3/";
		Unzipper.unzip(filename, dir);
		Map<String, Object[]> data = Reader.read(dir + "project.json");
		List<Script> scripts = Reader.extractScripts(Reader.getChild(data, 0));
		for (Script s: scripts) {
			// System.out.println(s);
		}
		System.out.println(new Reader().scriptToJava(Reader.getChild(data, 0)));
	}
}