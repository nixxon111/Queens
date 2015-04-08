import java.util.ArrayList;

import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;
import net.sf.javabdd.JFactory;
import net.sf.javabdd.MicroFactory.bdd;

public class QueensLogic {
	private int x = 0;
	private int y = 0;
	private int[][] board;
	final private int TWO_MIL = 20000000;
	final private int TWO_HUN_THOUSAND = 2000000;

	BDDFactory fact;
	BDD rules;

	public QueensLogic() {

	}

	public void initializeGame(int size) {
		this.x = size;
		this.y = size;
		this.board = new int[x][y];

		fact = JFactory.init(TWO_MIL, TWO_HUN_THOUSAND);

		fact.setVarNum(x*x);
		rules = buildRules();

		updateBoard();
	}

	private BDD buildRules() {
		BDD[] rows = new BDD[y];
		// per column
		for (int i = 0; i < x; i++) {
			int[] vars = new int[x];
			// per row
			for (int j = 0; j < x; j++) {
				vars[j] = (j * x) + i;
			}
			rows[i] = oneTrue(vars);
		}
		BDD[] columns = new BDD[y];
		// per row
		for (int i = 0; i < x; i++) {
			int[] vars = new int[x];
			// per column
			for (int j = 0; j < x; j++) {
				vars[j] = (i * x) + j;
			}
			columns[i] = oneTrue(vars);
		}
		// diagonal south east
		// for each column
		BDD[] diag1 = new BDD[y];
		for (int i = 0; i < x; i++) {
			// for each row
			ArrayList<Integer> vars = new ArrayList<Integer>();
			for (int j = 0; j < x && i + j < x; j++) {
				vars.add((j * x) + (i + j));
			}
			System.out.print("vars: ");
			System.out.println(vars);
			diag1[i] = oneMaybeTrue(vars);
		}

		BDD[] diag2 = new BDD[y - 1];
		// row
		for (int i = 1; i < x; i++) {
			// for each column
			ArrayList<Integer> vars = new ArrayList<Integer>();
			for (int j = 0; j < x && i + j < x; j++) {
				vars.add(((i + j) * x) + (j));
			}
			System.out.print("vars2: ");
			System.out.println(vars);
			diag2[i - 1] = oneMaybeTrue(vars);
		}

		BDD[] diag3 = new BDD[y];
		for (int i = 0; i < x; i++) {
			// for each row
			ArrayList<Integer> vars = new ArrayList<Integer>();
			for (int j = 0; j < x && i - j >= 0; j++) {
				vars.add((j * x) + (i - j));
			}
			System.out.print("vars3: ");
			System.out.println(vars);
			diag3[i] = oneMaybeTrue(vars);
		}

		BDD[] diag4 = new BDD[y - 1];
		// row
		for (int i = 1; i < x; i++) {
			// for each column
			int z = 0;
			ArrayList<Integer> vars = new ArrayList<Integer>();
			for (int j = x - 1; j >= 0 && i + z < x; j--) {
				vars.add(((i + z) * x) + (j));
				z++;
			}
			System.out.print("vars4: ");
			System.out.println(vars);
			diag4[i - 1] = oneMaybeTrue(vars);
		}

		BDD conjunction = fact.one();
		for (int i = 0; i < columns.length; i++) {
			// System.out.println(columns[i]);
			conjunction.andWith(columns[i]);
		}

		for (int i = 0; i < rows.length; i++) {
			conjunction.andWith(rows[i]);
		}

		for (int i = 0; i < diag1.length; i++) {
			conjunction.andWith(diag1[i]);
		}
		for (int i = 0; i < diag2.length; i++) {
			conjunction.andWith(diag2[i]);
		}

		for (int i = 0; i < diag3.length; i++) {
			conjunction.andWith(diag3[i]);
		}
		for (int i = 0; i < diag4.length; i++) {
			conjunction.andWith(diag4[i]);
		}
		System.out.println(conjunction.isZero());
		return conjunction;
	}

	private BDD oneMaybeTrue(ArrayList<Integer> vars) {
		BDD oneTrue = fact.zero();
		BDD noneTrue = fact.one();
		for (int i = 0; i < vars.size(); i++) {
			BDD ithTrue = fact.one();
			for (int j = 0; j < vars.size(); j++) {
				ithTrue.andWith(vars.get(i) == vars.get(j) ? fact.ithVar(vars
						.get(j)) : fact.nithVar(vars.get(j)));

			}
			// System.out.println("ithTrue " + i + " " + ithTrue);
			oneTrue.orWith(ithTrue);
		}
		for (int i = 0; i < vars.size(); i++) {
			noneTrue.andWith(fact.nithVar(vars.get(i)));
		}
		oneTrue.orWith(noneTrue);
		System.out.println("one or zero tru:" + oneTrue);
		return oneTrue;
	}

	public BDD oneTrue(int[] vars) {
		BDD oneTrue = fact.zero();
		for (int i = 0; i < vars.length; i++) {
			BDD ithTrue = fact.one();
			for (int j = 0; j < vars.length; j++) {
				ithTrue.andWith(vars[i] == vars[j] ? fact.ithVar(vars[j])
						: fact.nithVar(vars[j]));
			}
			// System.out.println("ithTrue " + i + " " + ithTrue);
			oneTrue.orWith(ithTrue);
		}
		return oneTrue;
	}


	public void restrict(int var_x, boolean isTrue) {
		if (isTrue) {
			rules.restrict(fact.ithVar(var_x));
		} else {
			rules.restrict(fact.nithVar(var_x));
		}
	}

	public void updateBoard() {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				if (board[i][j] == 0) {
					if (rules.restrict(fact.ithVar(i + j * x)).isZero()) {
						board[i][j] = -1;
					}
				}
			}
		}
	}

	public int[][] getGameBoard() {
		return board;
	}

	public boolean insertQueen(int column, int row) {
		assignQueen(column, row); // change board[][] = 1
		updateBDD(column, row); // rules.restrict(var)
		// restrict(column+(row*x), true);
		updateBoard();

		placeRemaining();

		return true;
	}

	private void placeRemaining() {
		//cols
		for (int i = 0; i < x; i++) {
			// rows
			for (int j = 0; j < y; j++) {
				//col
				if (board[i][j]==0) {
					checkIfOnly(i,j);
				}
			}
		}
		
	}

	private void checkIfOnly(int c, int r) {
//column
		for (int i = 0; i < x; i++) {
			if (i==c) {
				continue;
			}
			if (board[i][r]==0) {
				return;
			}
		}
		// row
		for (int i = 0; i < y; i++) {
			if (i==r) {
				continue;
			}
			if (board[c][i]==0) {
				return;
			}
		}
		board[c][r]=1;
		
	}

	private void assignQueen(int column, int row) {
		if (board[column][row] == 1) {

			board[column][row] = 0;

		} else if (board[column][row] == -1) {

		} else {
			board[column][row] = 1;

		}

	}

	private void updateBDD(int c, int r) {
		int var = c + r * x;
		rules.restrictWith(fact.ithVar(var));

		/*
		 * BDD[] rows = new BDD[y]; BDD row = fact.zero(); for (int i = 0; i <
		 * x; i++) { for (int j = 0; j < y; j++) { if (j==c && r==i) { row =
		 * row.xor(fact.one()); } else row = row.xor(fact.ithVar((i*x)+j)); }
		 * rows[i] = row; }
		 * 
		 * BDD[] columns = new BDD[x]; BDD column = fact.zero(); for (int i = 0;
		 * i < x; i++) { for (int j = 0; j < y; j++) { if (j==c && r==i) {
		 * column = column.xor(fact.one()); } else column =
		 * column.xor(fact.ithVar((j*x)+i)); } columns[i] = column; }
		 * 
		 * BDD conjunction = fact.one(); for (int i = 0; i < x; i++) {
		 * conjunction = conjunction.andWith(columns[i]); } for (int i = 0; i <
		 * y; i++) { conjunction = conjunction.andWith(rows[i]); }
		 * System.out.println(conjunction); return conjunction;
		 */
	}

}
