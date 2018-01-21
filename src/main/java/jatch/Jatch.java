package main.java.jatch;

import java.io.IOException;
import java.util.Map;

import com.google.googlejavaformat.java.FormatterException;

import main.java.jatch.files.*;
import main.java.jatch.script.ExprEval;

public class Jatch {
	public static void main(String[] args) throws FormatterException, IOException {
		ExprEval ee = new ExprEval("[=, [+, [+, 1, 0], [-, 3, 1]], 3]");
		System.out.println(ee);
		Jatch.convert("repeat-multiexpr");
	}
	
	public static void convert(String file) throws FormatterException, IOException {
		String filename = file + ".sb2";
		String dir = file + "/";
		Reader.init();
		Unzipper.unzip(filename, dir);
		for (Map<String, Object> child: Reader.getChildren(dir + "project.json")) 
			System.out.println(Reader.scriptToJava(child));		
	}
}