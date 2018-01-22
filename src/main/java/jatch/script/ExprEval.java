package main.java.jatch.script;

import java.util.Arrays;

public class ExprEval {
	private String op, v1, v2;
	private boolean arith, comp, bool, not, read;
	private String result;
	protected int convs;
	public static Sprite s;
	
	public ExprEval(String[] cond, Sprite s) {
		this.s = s;
		op = cond[0];
		arith = op.equals("+") || op.equals("-") || op.equals("*") || op.equals("/");
		comp = op.equals(">") || op.equals("<") || op.equals("=") || op.equals(">=") || op.equals("<=");
		bool = op.equals("and") || op.equals("or");
		not = op.equals("not");
		read = op.equals("readVariable");
		v1 = cond[1];
		int i = 1;
		try {
			Integer.parseInt(v1);
		} catch (NumberFormatException e) {
			ExprEval ee = new ExprEval(Arrays.copyOfRange(cond, i, cond.length), s);
			v1 = ee.parse();
			i += ee.convs * 2;
			convs += ee.convs;
			convs++;
			i += 2;
		}
		i++;
		if (!not && !read) {
			v2 = cond[i];
			try {
				Integer.parseInt(v2);
			} catch (NumberFormatException e) {
				ExprEval ee = new ExprEval(Arrays.copyOfRange(cond, i, cond.length), s);
				v2 = ee.parse();
				i += ee.convs * 2;
				convs += ee.convs;
				convs++;
				i += 2;
			}
		}
	}
	
	public ExprEval(String cond, Sprite s) {
		this(stringToArr(cond), s);
	}
	
	private static String[] stringToArr(String cond) {
		return cond.replaceAll("\\[", "").replaceAll("\\]", "").split(", ");
	}

	public String parse() {
		if (arith) {
			int i1 = Integer.parseInt(v1), i2 = Integer.parseInt(v2);
			if (op.equals("+")) result = (i1 + i2) + "";
			else if (op.equals("-")) result = (i1 - i2) + "";
			else if (op.equals("/")) result = (i1 / i2) + "";
			else if (op.equals("*")) result = (i1 * i2) + "";
		} else if (comp) {
			int i1 = Integer.parseInt(v1), i2 = Integer.parseInt(v2);
			if (op.equals("=")) result = (i1 == i2) + "";
			else if (op.equals(">")) result = (i1 > i2) + "";
			else if (op.equals("<")) result = (i1 < i2) + "";
			else if (op.equals(">=")) result = (i1 >= i2) + "";
			else if (op.equals("<=")) result = (i1 <= i2) + "";			
		} else if (bool) {
			boolean b1 = Boolean.parseBoolean(v1), b2 = Boolean.parseBoolean(v2);
			if (op.equals("and")) result = (b1 && b2) + "";
			else if (op.equals("or")) result = (b1 || b2) + "";
		} else if (not) {
			boolean b1 = Boolean.parseBoolean(v1);
			result = (!b1) + "";
		} else {
			Object o = null;
			try {
				o = s.fieldget(v1);
			} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
				e.printStackTrace();
			}
			result = o + "";
		}
		
		return result;
	}
	
	public static String parse(String[] cond) {
		return new ExprEval(cond, s).parse();
	}
	
	public String getResult() { return result; }
	
	public String toString() {
		return v1 + " " + op + " " + v2 + " = " + parse();
	}

}
