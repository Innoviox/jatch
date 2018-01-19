package main.java.jatch.script;

public class Operators {
	private String op, v1, v2;
	private boolean arith, comp, bool, not;
	private String result;
	
	public Operators(String[] cond) {
		op = cond[0];
		arith = op.equals("+") || op.equals("-") || op.equals("*") || op.equals("/");
		comp = op.equals(">") || op.equals("<") || op.equals("=") || op.equals(">=") || op.equals("<=");
		bool = op.equals("and") || op.equals("or");
		not = op.equals("not");
		v1 = cond[1];
		if (!not) v2 = cond[2];
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
		} else {
			boolean b1 = Boolean.parseBoolean(v1);
			result = (!b1) + "";
		}
		
		return result;
	}
	
	public static String parse(String[] cond) {
		return new Operators(cond).parse();
	}
	
	public String getResult() { return result; }
}
