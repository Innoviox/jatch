package main.java.jatch;

import java.io.IOException;
import java.util.Map;

import com.google.googlejavaformat.java.FormatterException;

import main.java.jatch.files.*;
import main.java.jatch.script.ExprEval;

public class Jatch {
	public static void main(String[] args) throws IOException, FormatterException {
		Jatch.convert("var");
	}
	
	public static void convert(String file) throws IOException, FormatterException {
		String filename = file + ".sb2";
		String dir = file + "/";
		Reader.init();
		Unzipper.unzip(filename, dir);
		for (Map<String, Object> child: Reader.getChildren(dir + "project.json")) 
			Reader.compileSource(Reader.scriptToJava(child));
	}
}