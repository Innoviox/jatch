package main.java.jatch.script;

import java.util.Arrays;
import java.util.List;

public class Script {
	public String cmd;
	public List<String> inputs;
	
	public Script(String cmd, List<String> inputs) {
		this.cmd = cmd;
		this.inputs = inputs;
	}
	
	public String toString() {
		return cmd + "(" + inputs + ");";
	}
}
