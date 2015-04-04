import net.sf.javabdd.*;

public class CopyOfBDDExamples {

	/**
	 * Gives some examples on how the BDD package is used
	 */
	public static void main(String[] args) {
		BDDFactory fact = JFactory.init(300,30);
		fact.setVarNum(4);

		BDD True = fact.one();
		BDD False = fact.zero();
		BDD rules = (fact.ithVar(0).xor(True)).and((True).xor(fact.ithVar(3)));
		True.apply(rules, BDDFactory.and);
		System.out.println(rules);
		fact.ithVar(0).restrict(True);
		fact.ithVar(2).restrict(True);
		System.out.println(rules);
		System.out.println(fact.ithVar(0).isZero());
		rules.restrict(fact.ithVar(0).and(fact.ithVar(2)));
		//BDD trueXORfalse = True.xor(False).xor(False);
		//System.out.println(trueXORfalse.isOne());
		//rules.restrict(fact.ithVar(2));
		//rules.restrict(fact.ithVar(1));
		//rules.restrict(fact.ithVar(3));
		System.out.println(rules.isOne());
		// the expression x0
		BDD x_0 = fact.ithVar(0);
		
		BDDFactory f = JFactory.init(100,20);
		f.setVarNum(4);
		BDD res = f.ithVar(0).xor(f.ithVar(1)).and((f.ithVar(2)).xor(f.ithVar(3)));
		res.andWith(f.ithVar(0).xor(f.ithVar(2))).and((f.ithVar(1)).xor(f.ithVar(3)));
		System.out.println(res);

	}

}
