package main.java.jatch;

import java.io.IOException;
import java.util.Map;

import com.google.googlejavaformat.java.FormatterException;

import main.java.jatch.files.*;

public class Jatch {
	public static void main(String[] args) throws IOException, FormatterException {
		// Jatch.convert("forever-touching-multisprite-rotation");
		// Jatch.convert("fn-call");
		Jatch.convert("test");
	}
	
	public static void convert(String file) throws IOException, FormatterException {
		String filename = file + ".sb2";
		String dir = file + "/";
		Reader.init();
		Unzipper.unzip(filename, dir);
		Reader.compileSource(dir);
	}
}