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

		fact.setVarNum(64);
		rules = buildRules2();

		updateBoard();
	}

	private BDD buildRules2() {
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

	private BDD buildRules() {
		BDD[] rows = new BDD[y];
		for (int i = 0; i < rows.length; i++) {

			BDD row = fact.zero();
			BDD notRow = fact.zero();
			for (int j = 0; j < y; j++) {
				row.xorWith(fact.ithVar((i * x) + j));
				notRow.orWith(fact.nithVar((i * x) + j));
			}
			row.andWith(notRow);
			rows[i] = row;
		}

		BDD[] columns = new BDD[x];

		for (int i = 0; i < columns.length; i++) {
			BDD column = fact.zero();
			BDD notColumn = fact.zero();
			for (int j = 0; j < y; j++) {
				column.xorWith(fact.ithVar((j * x) + i));
				notColumn.orWith(fact.nithVar((j * x) + i));
			}
			column.xorWith(fact.zero());
			column.andWith(notColumn);
			columns[i] = column;
		}

		// north-west/south-east diagonal

		// from top left towards right
		BDD[] diag = new BDD[y];
		int z = 0;
		for (int i = 0; i + z < columns.length; i++) {

			BDD row = fact.zero();
			BDD notRow = fact.zero();
			for (int j = 0; j + z < rows.length && i + z < columns.length; j++) {
				// System.out.println("i"+i + " j"+j +" z"+z+
				// " : ((j+z)*x)+(i+z)"+(((j+z)*x)+(i+z)));
				row.xorWith(fact.ithVar(((j + z) * x) + (i + z)));
				notRow.orWith(fact.nithVar(((j + z) * x) + (i + z)));
				z++;
			}
			row.xorWith(fact.zero());
			row.andWith(notRow);
			diag[i] = row;
			z = 0;
		}
		// from leftside of rows and down
		BDD[] diag2 = new BDD[x - 1];
		z = 0;
		for (int i = 1; i + z < rows.length; i++) {

			BDD row = fact.zero();
			BDD notRow = fact.zero();
			for (int j = 0; j + z < columns.length && i + z < columns.length; j++) {
				row.xorWith(fact.ithVar(((i + z) * x) + (j + z)));
				notRow.orWith(fact.nithVar(((i + z) * x) + (j + z)));
				z++;
			}
			row.xorWith(fact.zero());
			row.andWith(notRow);
			diag2[i - 1] = row;
			z = 0;
		}

		BDD conjunction = fact.one();
		for (int i = 0; i < columns.length; i++) {
			// System.out.println(columns[i]);
			conjunction.andWith(columns[i]);
		}

		for (int i = 0; i < rows.length; i++) {
			conjunction.andWith(rows[i]);
		}

		/*
		 * for (int i = 0; i < diag.length; i++) { conjunction.andWith(diag[i]);
		 * }
		 * 
		 * System.out.println(conjunction); for (int i = 0; i < diag2.length;
		 * i++) { conjunction.andWith(diag2[i]); }
		 */
		System.out.println(conjunction);
		System.out.println(conjunction.isZero());
		return conjunction;
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

		// System.out.println(fact.ithVar(column+(row*x)).isOne());
		// System.out.println(rules);

		// insert queen, and set appropriate variable = true in BDD
		// change related variables = false, by calling mark(var X),
		// E.G.: queen in 0,0 (length 8*8), set variables 1-7 = false

		// loop through all fields on board
		// if var x_i == false, mark with cross (-1)

		// check vertical/horizontal rows, if no queen and only one var
		// "available"
		// set = true (and call method mark(var X))

		// check if tautology = we have won
		// check if unsatisfiable = we have lost

		return true;
	}

	private void placeRemaining() {
		//cols
		for (int i = 0; i < x; i++) {
			// rows
			for (int j = 0; j < y; j++) {
				//col
				int column = -13;
				for (int j2 = 0; j2 < x; j2++) {
					if (j2==i) {
						continue;
					}
					if (board[i][j]!=-1) {
						break;
					}
					column = j2;
				}
				// row
				int row = -17;
				for (int j2 = 0; j2 < y; j2++) {
					
				}
			}
		}
		
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

	private void paintDiagonal(int c, int r, int[][] board) {
		int i = 1;
		while (c + i < board.length && r + i < board[0].length) {
			if (board[c + i][r + i] == 0) {
				board[c + i][r + i] = -1;
			}
			i++;
		}
		i = 1;
		while (c - i >= 0 && r + i < board[0].length) {
			if (board[c - i][r + i] == 0) {
				board[c - i][r + i] = -1;
			}
			i++;
		}
		i = 1;
		while (c + i < board.length && r - i >= 0) {
			if (board[c + i][r - i] == 0) {
				board[c + i][r - i] = -1;
			}
			i++;
		}
		i = 1;
		while (c - i >= 0 && r - i >= 0) {
			if (board[c - i][r - i] == 0) {
				board[c - i][r - i] = -1;
			}
			i++;
		}

	}

	private void paintVertical(int c, int r, int[][] board) {
		for (int i = 0; i < board[c].length; i++) {
			if (board[c][i] == 0) {
				board[c][i] = -1;
			}
		}

	}

	private void paintHorizontal(int c, int r, int[][] board) {
		for (int i = 0; i < board.length; i++) {
			if (board[i][r] == 0) {
				board[i][r] = -1;
			}
		}

	}
}
