package main.java.jatch;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.google.googlejavaformat.java.FormatterException;

import main.java.jatch.files.*;
import main.java.jatch.script.Script;

public class Main {
	public static void main(String[] args) throws IOException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, FormatterException {
		String filename = "test-recursive3.sb2";
		String dir = "test-recursive3/";
		Reader.init();
		Unzipper.unzip(filename, dir);
		System.out.println(Reader.scriptToJava(Reader.getChild(dir + "project.json", 0)));
	}
}