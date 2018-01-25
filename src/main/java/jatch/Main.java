package main.java.jatch;

import java.io.IOException;

import com.google.googlejavaformat.java.FormatterException;

public class Main {
	public static void main(String[] args) throws IOException, FormatterException {
		Jatch.convert(args[0]);
	}
}
