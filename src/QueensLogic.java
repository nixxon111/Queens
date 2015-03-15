import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;
import net.sf.javabdd.JFactory;

public class QueensLogic {
	private int x = 0;
	private int y = 0;
	private int[][] board;
	final private int TWO_MIL = 2000000;
	final private int TWO_HUN_THOUSAND = 200000;

	BDDFactory fact;
	BDD[] variables;
	
	public QueensLogic() {
		
	}

	public void initializeGame(int size) {
		this.x = size;
		this.y = size;
		this.board = new int[x][y];
		
		fact = JFactory.init(TWO_MIL, TWO_HUN_THOUSAND);
		variables = new BDD[x*y];
		fact.setVarNum(x*y);
	}

	public int[][] getGameBoard() {
		return board;
	}

	public boolean insertQueen(int column, int row) {
		
		// insert queen, and set appropriate variable = true in BDD
		// change related variables = false, by calling mark(var X), 
		// E.G.: queen in 0,0 (length 8*8), set variables 1-7 = false
		
		// loop through all fields on board
		// if var x_i == false, mark with cross (-1)
		
		//check vertical/horizontal rows, if no queen and only one var "available"
		// set = true (and call method mark(var X))
		
		// check if tautology = we have won
		// check if unsatisfiable = we have lost
		
		
		if (board[column][row] == 1) {

			board[column][row] = 0;

		} else if (board[column][row] == -1) {

		} else {
			board[column][row] = 1;

		}

		int cols = board.length;
		int rows = board[0].length;

		for (int c = 0; c < cols; c++) {
			for (int r = 0; r < rows; r++) {
				int player = board[c][r];
				if (player == -1) {
					board[c][r] = 0;
				}

			}
		}
		for (int c = 0; c < cols; c++) {
			for (int r = 0; r < rows; r++) {
				int player = board[c][r];
				if (player == 1) {
					paintDiagonal(c, r, board);
					paintHorizontal(c, r, board);
					paintVertical(c, r, board);
				}

			}
		}
		return true;
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
