package ygraphs.ai.smart_fox.games;

public class State extends GameModel {
	// constants for board marking
	public static final String POS_MARKED_BLACK = "black";
	public static final String POS_MARKED_WHITE = "white";
	public static final String POS_MARKED_ARROW = "arrow";
	public static final String POS_AVAILABLE = "available";
	private String[][] board;
	int rows, columns;

	// constructor for new state with rows and columns of the game board
	State(int rows, int columns) {
		this.rows = rows;
		this.columns = columns;
		board = new String[rows][columns];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				this.board[i][j] = "available";
			}
		}
	}

	// marks a new move on the current instance of state.board
	public boolean positionMarked(GameMove move) {
		boolean valid = true;
		if ((((move.newX >= board.length ? 1 : 0) | (move.newY >= board.length ? 1 : 0)) != 0) || (move.newX <= -1)
				|| (move.newY <= -1)) {
			valid = false;
		} else if (!board[move.newX][move.newY].equalsIgnoreCase("available")) {
			valid = false;
		}

		board[move.newX][move.newY] = board[move.x][move.y];
		board[move.x][move.y] = "available";
		board[move.arrowX][move.arrowY] = "arrow";

		return valid;
	}

	// receives a move and a state, then executes the move on the state and
	// returns a new board
	public static State result(State state, GameMove move) {
		State newState = new State(state.rows, state.columns);
		for (int i = 0; i < state.board.length; i++) {
			for (int j = 0; j < state.board[0].length; j++) {
				newState.board[i][j] = state.board[i][j];
			}
		}
		newState.positionMarked(move);
		return newState;

	}

	// get method for board
	public void setBoardLocation(int x, int y, String text) {
		this.board[x][y] = text;
	}

	public String[][] getBoard() {
		return this.board;
	}
}
