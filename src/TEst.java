import javax.jws.Oneway;

import net.sf.javabdd.*;

public class TEst {

	/**
	 * Gives some examples on how the BDD package is used
	 */

	static BDDFactory fact = JFactory.init(300,30);
	
	public static void main(String[] args) {
		fact.setVarNum(9);

		BDD True = fact.one();
		BDD False = fact.zero();
		//BDD rules = (fact.ithVar(0).xor(True)).and((True).xor(fact.ithVar(3)));
		
		BDD rules = fact.zero().xor(fact.ithVar(0).xor(fact.ithVar(1)).xor(fact.ithVar(2)));
		BDD rules2 = fact.one().andWith((fact.nithVar(0).or(fact.nithVar(1)).or(fact.nithVar(2))));
		rules.andWith(rules2);
		
		BDD b1 = fact.zero();
		b1.xorWith(fact.ithVar(0));
		b1.xorWith(fact.ithVar(1));
		b1.xorWith(fact.ithVar(2));
		b1.xorWith(fact.ithVar(3));
		BDD b2 = fact.zero();
		b2.orWith(fact.nithVar(0));
		b2.orWith(fact.nithVar(1));
		b2.orWith(fact.nithVar(2));
		b2.orWith(fact.nithVar(3));
		
		b1.andWith(b2);
		System.out.println(b1);
		b1.restrictWith(fact.ithVar(0));
		
		for (int i = 1; i < 3; i++) {
			BDD c1 = b1.restrict(fact.ithVar(i));
			if (c1.isZero()) {
				System.out.println(i);
			}
		}
		
		System.out.println("----------------------------------------");
		
		BDD h = oneTrue(new int[]{0,1,2,3,4});
		System.out.println(h);
		h.restrictWith(fact.ithVar(0));
		System.out.println(h);
		for (int i = 0; i < 5; i++) {
			BDD c1 = h.restrict(fact.ithVar(i));
			System.out.println(i+" "+c1);
			if (c1.isZero()) {
				System.out.println(i);
			}
		}
		
	}
	
	
	
	public static BDD oneTrue(int[] vars) {
		BDD oneTrue = fact.zero();
		for (int i = 0; i < vars.length; i++) {
			BDD ithTrue = fact.one();
			for (int j = 0; j < vars.length; j++) {
				ithTrue.andWith(vars[i]==j ? fact.ithVar(j) : fact.nithVar(j));
			}
			System.out.println(ithTrue);
			oneTrue.orWith(ithTrue);
		}
		return oneTrue;
	}

}
