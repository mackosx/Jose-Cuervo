package AI;

import java.util.ArrayList;

/**
 * Node Class for State Space Search -nodes have accessible states through
 * getters as well as the move used to get to that state, if null then initial
 * state also contains the evaluation of that node state
 */

public class Node {

	int[][] queenLocations = new int[4][2];
	private State state;
	private Node parent;
	private String type;
	private GameMove move;
	public int hValue;
	private boolean expanded  = false;
	ArrayList<Node> validMoves = new ArrayList<Node>();
	String opposite = "";

	public Node(State state, String type) {
		this.type = type;
		setState(state);
		validMoves = new ArrayList<Node>();
		parent = null;
		getQueens(type);
		opposite = type.equals("white")?"black":"white";
	}

	public State state() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}
	public String getType(){
		return this.type;
	}
	
	
	public ArrayList<Node> getChildren(){
		return validMoves;
	}
	public ArrayList<Node> generateChildren(String type) {
		if(!expanded){
			getQueenMoves(this.state, type);
			expanded = true;
			return validMoves;
		}else
			return validMoves;
		
	}

	public int[][] getQueens(String colour) {
		// If opposite = true, will return location of opposite queens
		int[][] queens = new int[4][2];

		
		int counter = 0;
		for (int i = 0; i < state.getBoard().length; i++) {
			for (int j = 0; j < state.getBoard()[0].length; j++) {
				if (this.state.getBoard()[i][j] == colour) {
					queenLocations[counter][0] = i;
					queenLocations[counter][1] = j;
					queens[counter][0] = i;
					queens[counter][1] = j;
					counter++;
				}
			}
		}
		return queens;
	}

	/**
	 * 
	 * @param game
	 *            - current board state
	 * @param type
	 *            - player color, black or white
	 * @return - all valid moves for the current board state
	 */
	public ArrayList<Node> getQueenMoves(State game, String type) {
		
		validMoves.clear();
		for (int queen = 0; queen < queenLocations.length; queen++) {
			int currRow = getQueens(type)[queen][0];
			int currCol = getQueens(type)[queen][1];
			//int moveCount = 0;
			// check above
			for (int i = currRow - 1; i >= 0; i--) {
				if (!occupied(i, currCol, currRow, currCol, game)) {
					// for each position, add all possible arrow shots to move
					// list
					getArrowMoves(new GameMove(currRow, currCol, i, currCol), type);
				} else {
					break;
				} 
			}

			// check below
			for (int i = currRow + 1; i < game.getBoard().length; i++) {
				if (!occupied(i, currCol, currRow, currCol, game)) {
					getArrowMoves(new GameMove(currRow, currCol, i, currCol), type);
				} else {
					break;
				}
			}

			// check left
			for (int j = currCol - 1; j >= 0; j--) {
				if (!occupied(currRow, j, currRow, currCol, game)) {
					getArrowMoves(new GameMove(currRow, currCol, currRow, j), type);
				} else {
					break;
				}
			}

			// check right
			for (int j = currCol + 1; j < game.getBoard().length; j++) {
				if (!occupied(currRow, j, currRow, currCol, game)) {
					getArrowMoves(new GameMove(currRow, currCol, currRow, j), type);
				} else {
					break;
				}
			}

			// check up-right
			for (int i = currRow + 1, j = currCol - 1; i < game.getBoard().length && j >= 0; i++, j--) {
				if (!occupied(i, j, currRow, currCol, game)) {
					// for each position, add all possible arrow shots to move
					// list
					getArrowMoves(new GameMove(currRow, currCol, i, j), type);
				} else {
					break;
				}
			}

			// check up-left
			for (int i = currRow - 1, j = currCol - 1; i >= 0 && j >= 0; i--, j--) {
				if (!occupied(i, j, currRow, currCol, game)) {
					// for each position, add all possible arrow shots to move
					// list
					getArrowMoves(new GameMove(currRow, currCol, i, j), type);
				} else {
					break;
				}
			}

			// check down-right
			for (int i = currRow + 1, j = currCol + 1; i < game.getBoard().length
					&& j < game.getBoard().length; i++, j++) {
				if (!occupied(i, j, currRow, currCol, game)) {
					// for each position, add all possible arrow shots to move
					// list
					getArrowMoves(new GameMove(currRow, currCol, i, j), type);
				} else {
					break;
				}
			}

			// check down left
			for (int i = currRow - 1, j = currCol + 1; i >= 0 && j < game.getBoard().length; i--, j++) {
				if (!occupied(i, j, currRow, currCol, game)) {
					// for each position, add all possible arrow shots to move
					// list
					getArrowMoves(new GameMove(currRow, currCol, i, j), type);
				} else {
					break;
				}
			}

		}
		return validMoves;

	}

	/**
	 * 
	 * @param move
	 *            - move made a queen with out arrow position
	 * @return - map of all moves with arrow positions
	 */
	int count = 0;

	public void getArrowMoves(GameMove move, String type) {
		// check left
		for (int j = move.newCol - 1; j >= 0; j--) {
			if (!occupied(move.newRow, j, move.row, move.col, state)) {
				// change to use move
				GameMove tempMove = new GameMove(move.row, move.col, move.newRow, move.newCol, move.newRow, j);
				State temp = state.result(state, tempMove);
				Node child = new Node(temp, type);
				
				child.parent = this;
				child.move = tempMove;
				validMoves.add(child); // insert a new search tree node
				

			} else {
				break;
			}
		}
		// check right
		for (int j = move.newCol + 1; j < state.getBoard().length; j++) {
			if (!occupied(move.newRow, j, move.row, move.col, state)) {
				GameMove tempMove = new GameMove(move.row, move.col, move.newRow, move.newCol, move.newRow, j);
				State temp = state.result(state, tempMove);
				Node child = new Node(temp, type);
				
				child.parent = this;
				child.move = tempMove;
				validMoves.add(child);
				

			} else {
				break;
			}
		}
		// check up
		for (int i = move.newRow - 1; i >= 0; i--) {
			if (!occupied(i, move.newCol, move.row, move.col, state)) {
				GameMove tempMove = new GameMove(move.row, move.col, move.newRow, move.newCol, i, move.newCol);
				State temp = state.result(state, tempMove);
				Node child = new Node(temp, type);
				
				child.move = tempMove;
				child.parent = this;
				validMoves.add(child);
				

			} else {
				break;
			}
		}
		// check down
		for (int i = move.newRow + 1; i < state.getBoard().length; i++) {
			if (!occupied(i, move.newCol, move.row, move.col, state)) {
				GameMove tempMove = new GameMove(move.row, move.col, move.newRow, move.newCol, i, move.newCol);
				State temp = state.result(state, tempMove);
				Node child = new Node(temp, type);
				
				child.parent = this;
				child.move = tempMove;
				validMoves.add(child);
				


			} else {
				break;
			}
		}
		// check up-right
		for (int i = move.newRow + 1, j = move.newCol + 1; i < state.getBoard().length
				&& j < state.getBoard().length; i++, j++) {
			if (!occupied(i, j, move.row, move.col, state)) {
				// for each position, add all possible arrow shots to move list
				GameMove tempMove = new GameMove(move.row, move.col, move.newRow, move.newCol, i, j);
				State temp = state.result(state, tempMove);
				Node child = new Node(temp, type);
				
				child.parent = this;
				child.move = tempMove;
				validMoves.add(child);
				

			} else {
				break;
			}
		}
		// check down-right
		for (int i = move.newRow - 1, j = move.newCol + 1; i >= 0 && j < state.getBoard().length; i--, j++) {
			if (!occupied(i, j, move.row, move.col, state)) {
				// for each position, add all possible arrow shots to move list
				GameMove tempMove = new GameMove(move.row, move.col, move.newRow, move.newCol, i, j);
				State temp = state.result(state, tempMove);
				Node child = new Node(temp, type);
				
				child.parent = this;
				child.move = tempMove;
				validMoves.add(child);
				


			} else {
				break;
			}
		}
		// check up-left
		for (int i = move.newRow + 1, j = move.newCol - 1; i < state.getBoard().length && j >= 0; i++, j--) {
			if (!occupied(i, j, move.row, move.col, state)) {
				// for each position, add all possible arrow shots to move list
				GameMove tempMove = new GameMove(move.row, move.col, move.newRow, move.newCol, i, j);
				State temp = state.result(state, tempMove);
				Node child = new Node(temp, type);
				
				child.parent = this;
				child.move = tempMove;
				validMoves.add(child);
				


			} else {
				break;
			}
		}
		// check down left
		for (int i = move.newRow - 1, j = move.newCol - 1; i >= 0 && j >= 0; i--, j--) {
			if (!occupied(i, j, move.row, move.col, state)) {
				// for each position, add all possible arrow shots to move list
				GameMove tempMove = new GameMove(move.row, move.col, move.newRow, move.newCol, i, j);
				State temp = state.result(state, tempMove);
				Node child = new Node(temp, type);
				
				child.parent = this;
				child.move = tempMove;
				validMoves.add(child);
				

			} else {
				break;
			}
		}


	}

	/**
	 * 
	 * @param row
	 *            row to check occupancy
	 * @param col
	 *            col to check occupancy
	 * @param currRow
	 *            current row of the queen
	 * @param currCol
	 *            current column of the queen
	 * @param game
	 *            current game board
	 * @return
	 */
	public boolean occupied(int row, int col, int currRow, int currCol, State game) {
		/*
		 * check for currX, currY to see if the square is the current location
		 * of queen. if position is marked with anything other than available,
		 * it is occupied
		 */

		if ((currRow == row && currCol == col) || game.getBoard()[row][col].equalsIgnoreCase(State.POS_AVAILABLE)) {
			return false;
		} else {
			return true;
		}
	}

	public Node getParent() {
		return this.parent;
	}

	public GameMove getMove() {
		return move;
	}

	public void setMove(GameMove move) {
		this.move = move;
	}

}
