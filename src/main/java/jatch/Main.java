package main.java.jatch;

import java.io.IOException;
import java.util.Map;

public class Main {
	public static void main(String[] args) throws IOException {
		String filename = "test.sb2";
		String dir = "test/";
		Unzipper.unzip(filename, dir);
		Map<String, Object[]> data = Reader.read(dir + "project.json");
	}
}
