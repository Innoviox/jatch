package main.java.jatch;

import java.io.IOException;
import java.util.Map;

import com.google.googlejavaformat.java.FormatterException;

import main.java.jatch.files.*;

public class Jatch {
	public static void main(String[] args) throws FormatterException, IOException {
		Jatch.convert("test-multisprite1");
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