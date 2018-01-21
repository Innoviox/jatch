package main.java.jatch;

import java.io.IOException;

import com.google.googlejavaformat.java.FormatterException;

import main.java.jatch.files.*;

public class Main {
	public static void main(String[] args) throws FormatterException, IOException {
		String filename = "test-recursive3.sb2";
		String dir = "test-recursive3/";
		Reader.init();
		Unzipper.unzip(filename, dir);
		System.out.println(Reader.scriptToJava(Reader.getChild(dir + "project.json", 0)));
	}
}