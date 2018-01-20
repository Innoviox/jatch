package main.java.jatch.script;

public class ExprEval {
	private String op, v1, v2;
	private boolean arith, comp, bool, not;
	private String result;
	
	public ExprEval(String[] cond) {
		op = cond[0];
		arith = op.equals("+") || op.equals("-") || op.equals("*") || op.equals("/");
		comp = op.equals(">") || op.equals("<") || op.equals("=") || op.equals(">=") || op.equals("<=");
		bool = op.equals("and") || op.equals("or");
		not = op.equals("not");
		v1 = cond[1];
		if (!not) v2 = cond[2];
	}
	
	public ExprEval(String cond) {
		this(_inparse(cond));
	}
	
	private static String[] _inparse(String cond) {
		String[] cnd = cond.split(", ");
		String[] newcnd = new String[3];
		newcnd[0] = cnd[0].substring(1, 2);
		newcnd[1] = cnd[1];
		newcnd[2] = cnd[2].substring(0, 1);
		return newcnd;
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
		return new ExprEval(cond).parse();
	}
	
	public String getResult() { return result; }
	
	public String toString() {
		return v1 + " " + op + " " + v2 + " = " + parse();
	}
}
