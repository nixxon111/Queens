import net.sf.javabdd.*;

public class BDDExamples {

	/**
	 * Gives some examples on how the BDD package is used
	 */
	public static void main(String[] args) {
		BDDFactory fact = JFactory.init(300,30);
		fact.setVarNum(25);

		BDD True = fact.one();
		BDD False = fact.zero();

		System.out.println(fact.ithVar(4).isZero());
		// the expression x0
		BDD x_0 = fact.ithVar(0);

		// the expression not x1
		BDD nx_1 = fact.nithVar(1);
		

		// the expression (not x1 or x0) and (True or false)
		BDD b = nx_1.or(x_0).and(True.or(False));

		// Checks whether or not expression is unsat
		System.out.println("b is unsat? : " + b.isZero());

		// Checks whether or not expression is unsat
		System.out.println("False is unsat? : " + False.isZero());

		// Checks whether or not expression is unsat
		System.out.println("True is unsat? : " + True.isOne());

		System.out.println(True.and(False).or(True).xor(False).isOne() + " <-");
		
		// is lower of a variable = false?
		System.out.println(x_0.low().isZero());

		// checks whether expression is tautology
		System.out.println("b is tautology? : " + b.isOne());

		// In order to restrict or quantify the expression to a given assignment
		// we give the assignment as a conjunction where positive variables
		// indicate that the variable should be restricted to false, and vice
		// versa.
		BDD restriction_x1_true_x0_false = fact.ithVar(1).and(fact.nithVar(0));
int x=5, y=5;
		BDD restricted = b.restrict(restriction_x1_true_x0_false);
		BDD existed = b.exist(x_0);
		System.out.println("resticted:"+restricted);
		System.out.println();
		BDD[] rows = new BDD[y];
		BDD row = fact.zero();
		for (int i = 0; i < rows.length; i++) {
			for (int j = 0; j < y; j++) {
				System.out.println((i*x)+j);
				row = row.or(fact.ithVar((i*x)+j));
			}
			rows[i] = row;
		}
		
		BDD[] columns = new BDD[x];
		BDD column = fact.zero();
		for (int i = 0; i < columns.length; i++) {
			for (int j = 0; j < y; j++) {
				column = column.or(fact.ithVar((j*x)+i));
			}
			columns[i] = column;
		}
		
		BDD conjunction = fact.one();
		for (int i = 0; i < columns.length; i++) {
			conjunction = conjunction.and(columns[i]);
		}
		for (int i = 0; i < rows.length; i++) {
			conjunction = conjunction.and(rows[i]);
		}
		System.out.println(conjunction);
		System.out.println("True is unsat? : " + conjunction.isZero());

		// Checks whether or not expression is unsat
		System.out.println("True is valid : " + conjunction.isOne());
		
		System.out.println((fact.ithVar(0).or(fact.ithVar(1)).or(fact.ithVar(2))));
		
		

		// Exist. should be tautology
		System.out
				.println("Existiential quant. cause taut: " + existed.isOne());

		// Restriction shoule be unsat:
		System.out.println("Restriction caused unsat: " + restricted.isZero());

		// how to perform replacement
		BDDPairing replacement = fact.makePair();
		int[] from = { 1 };
		int[] to = { 0 };
		replacement.set(from, to);

		BDD b_replaced = existed.replace(replacement);

	}

}
