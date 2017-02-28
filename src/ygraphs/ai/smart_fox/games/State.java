package ygraphs.ai.smart_fox.games;

public class State {
	private String[][] board;
	int rows, columns;
	public State(int rows, int columns) {
		this.rows = rows;
		this.columns = columns;
		board = new String[rows][columns];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				this.board[i][j].equalsIgnoreCase("available");
			}
		}
	}

	public boolean positionMarked(int row, int column, int arow, int acol, int qfr, int qfc) {
		boolean valid = true;
		if ((((row >= board.length ? 1 : 0) | (column >= board.length ? 1 : 0)) != 0) || (row <= -1)
				|| (column <= -1)) {
			valid = false;
		} else if (!board[row][column].equalsIgnoreCase("available")) {
			valid = false;
		}

		board[row][column] = board[qfr][qfc];
		board[qfr][qfc] = "available";
		board[arow][acol] = "arrow";

		return valid;
	}
	public State result(State state, GameMove move){
		State newState = new State(state.rows, state.columns);
		for (int i = 0; i < state.board.length; i++) {
			for (int j = 0; j < state.board[0].length; j++) {
				newState.board[i][j] = state.board[i][j];
			}
		}
		newState.positionMarked(move.newX, move.newY, move.arrowX, move.arrowY, move.x, move.y);
		return newState;
		
	}
}
