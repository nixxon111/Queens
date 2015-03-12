/**
 * This class implements the logic behind the BDD for the n-queens problem You
 * should implement all the missing methods
 * 
 * @author Stavros Amanatidis
 *
 */

public class QueensLogic {
	private int x = 0;
	private int y = 0;
	private int[][] board;

	public QueensLogic() {
		// constructor
	}

	public void initializeGame(int size) {
		this.x = size;
		this.y = size;
		this.board = new int[x][y];
	}

	public int[][] getGameBoard() {
		return board;
	}

	public boolean insertQueen(int column, int row) {
		if (board[column][row] == 1) {

			board[column][row] = 0;
			
		}
		else if (board[column][row] == -1) {

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
