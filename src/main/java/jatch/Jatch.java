package main.java.jatch;

import java.io.IOException;
import java.util.Map;

import com.google.googlejavaformat.java.FormatterException;

import main.java.jatch.files.*;

public class Jatch {
	public static void convert(String file) throws IOException, FormatterException {
		String filename = file + ".sb2";
		String dir = file + "/";
		Reader.init();
		Unzipper.unzip(filename, dir);
		Reader.compileSource(dir);
	}
}