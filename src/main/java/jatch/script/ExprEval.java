package main.java.jatch.script;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExprEval {
	private String op, v1, v2;
	private boolean arith, comp, bool, not;
	private String result;
	protected int convs;
	
	public ExprEval(String[] cond) {
		op = cond[0];
		arith = op.equals("+") || op.equals("-") || op.equals("*") || op.equals("/");
		comp = op.equals(">") || op.equals("<") || op.equals("=") || op.equals(">=") || op.equals("<=");
		bool = op.equals("and") || op.equals("or");
		not = op.equals("not");
		v1 = cond[1];
		int i = 1;
		try {
			Integer.parseInt(v1);
		} catch (NumberFormatException e) {
			ExprEval ee = new ExprEval(Arrays.copyOfRange(cond, i, cond.length));
			v1 = ee.parse();
			i += ee.convs * 2;
			convs += ee.convs;
			convs++;
			i += 2;
		}
		i++;
		if (!not) {
			v2 = cond[i];
			try {
				Integer.parseInt(v2);
			} catch (NumberFormatException e) {
				ExprEval ee = new ExprEval(Arrays.copyOfRange(cond, i, cond.length));
				v2 = ee.parse();
				i += ee.convs * 2;
				convs += ee.convs;
				convs++;
				i += 2;
			}
		}
	}
	
	public ExprEval(String cond) {
		this(stringToArr(cond));
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
