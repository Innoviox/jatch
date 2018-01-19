package main.java.jatch.script;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import main.java.jatch.files.Reader;

public class Script {
	public String cmd;
	public List<Object> args;
	public boolean recursive;
	
	private static final Reader r = new Reader();
	
	public Script(String cmd, List<Object> args) {
		this.cmd = r.getJavaFunction(cmd);
		if (this.cmd == null) this.cmd = cmd;
		this.args = args;
	}
	
	public String toString() {
		return cmd + "(" + args + ");";
	}
	
	@Deprecated
	public Object call(Sprite s) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Class<?>[] argClasses = new Class[args.size()];
		for (int i = 0; i < args.size(); i++) argClasses[i] = args.get(i).getClass();
		return s.getClass().getMethod(cmd, argClasses).invoke(s, args.toArray());
	}
}
