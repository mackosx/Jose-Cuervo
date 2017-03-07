package ygraphs.ai.smart_fox.games;

import java.util.LinkedList;
/**
 * Node Class for State Space Search
 * -nodes have accessible states through getters
 * as well as the move used to get to that state, if null then initial state
 * also contains the evaluation of that node state
 */

public class Node {

	int[][] queenLocations = new int[4][2];
	private LinkedList<Node> validMoves;
	private State state;
	private Node parent;
	private String type;
	private GameMove move;
	public int hValue;

	public Node(State state, String type) {
		this.type = type;
		setState(state);
		validMoves = new LinkedList<Node>();
		parent = null;
		getQueens(false);
	}

	public State state() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public LinkedList<Node> getChildren() {

		getQueenMoves(this.state, this.type == "white" ? "black" : "white");
		return validMoves;
	}

	public int[][] getQueens(boolean opposite) {
		//If opposite = true, will return location of opposite queens
		int[][] queens = new int[4][2];
		String colour = this.type;
		
		if(opposite)
			colour = colour.equalsIgnoreCase("white") ? "black" : "white";
		
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
	// testing with all queen
	public void getQueenMoves(State game, String type) {
		LinkedList<Node> tempList = new LinkedList<Node>();
		
		for (int queen = 0; queen < queenLocations.length; queen++) {
			int currRow = getQueens(false)[queen][0];
			int currCol = getQueens(false)[queen][1];
			System.out.println(currRow + ", " + currCol);
			
			// check above
			for (int i = currRow - 1; i >=0; i--) {
				if (!occupied(i, currCol, currRow, currCol, game)) {
					// for each position, add all possible arrow shots to move
					// list
					getArrowMoves(new GameMove(currRow, currCol, i, currCol, type));
				} else {
					break;
				}// 106 moves
			}
			
			// check below
			for (int i = currRow + 1; i < game.getBoard().length; i++) {
				if (!occupied(i, currCol, currRow, currCol, game)) {
					getArrowMoves(new GameMove(currRow, currCol, i, currCol, type));
				} else {
					break;
				}
			}
			
//			// check left
			for (int j = currCol - 1; j >= 0; j--) {
				if (!occupied(currRow, j, currRow, currCol, game)) {
					getArrowMoves(new GameMove(currRow, currCol, currRow, j, type));
				} else {
					break;
				}
			}
			
//			// check right
			for (int j = currCol + 1; j < game.getBoard().length; j++) {
				if (!occupied(currRow, j, currRow, currCol, game)) {
					getArrowMoves(new GameMove(currRow, currCol, currRow, j, type));
				} else {
					break;
				}
			}
			
			// check up-right
			for (int i = currRow + 1, j = currCol - 1; i < game.getBoard().length && j >= 0; i++, j--) {
				if (!occupied(i, j, currRow, currCol, game)) {
					// for each position, add all possible arrow shots to move
					// list
					getArrowMoves(new GameMove(currRow, currRow, i, j, type));
				} else {
					break;
				}
			}
			
			// check up-left
			for (int i = currRow - 1, j = currCol - 1; i >= 0 && j >= 0; i--, j--) {
				if (!occupied(i, j, currRow, currCol, game)) {
					// for each position, add all possible arrow shots to move
					// list
					getArrowMoves(new GameMove(currRow, currCol, i, j, type));
				} else {
					break;
				}
			}
			
			// check down-right
			for (int i = currRow + 1, j = currCol + 1; i < game.getBoard().length && j < game.getBoard().length; i++, j++) {
				if (!occupied(i, j, currRow, currCol, game)) {
					// for each position, add all possible arrow shots to move
					// list
					getArrowMoves(new GameMove(currRow, currCol, i, j, type));
				} else {
					break;
				}
			}
			
			// check down left
			for (int i = currRow - 1, j = currCol + 1; i >= 0 && j < game.getBoard().length; i--, j++) {
				if (!occupied(i, j, currRow, currCol, game)) {
					// for each position, add all possible arrow shots to move
					// list
					getArrowMoves(new GameMove(currRow, currCol, i, j, type));
				} else {
					break;
				}
			}
		}

	}

	/**
	 * 
	 * @param move
	 *            - move made a queen with out arrow position
	 * @return - map of all moves with arrow positions
	 */
	static int count = 0;
	public void getArrowMoves(GameMove move) {
		// check left
		for (int j = move.newCol - 1; j >=0; j--) {
			if (!occupied(move.newRow, j, move.row, move.col, state)) {
				// change to use move
				GameMove tempMove = new GameMove(move.row, move.col, move.newRow, move.newCol, move.newRow, j, type);
				State temp = state.result(state, tempMove);
				Node child = new Node(temp, move.type);
				child.parent = this;
				child.move = tempMove;
				System.out.println(tempMove.toString());
				validMoves.add(child); // insert a new search tree node
				System.out.println("added left arrow move");
				//System.out.println(temp.toString());
				System.out.println(++count);

			} else {
				break;
			}
		}
		// check right
		for (int j = move.newCol + 1 ; j < state.getBoard().length; j++) {
			if (!occupied(move.newRow, j, move.row, move.col, state)) {
				GameMove tempMove = new GameMove(move.row, move.col, move.newRow, move.newCol, move.newRow, j, type);
				State temp = state.result(state, tempMove);
				Node child = new Node(temp, move.type);
				child.parent = this;
				child.move = tempMove;
				validMoves.add(child);
				System.out.println(tempMove.toString());
				System.out.println("added right arrow move");
				//System.out.println(temp.toString());
				System.out.println(++count);


			} else {
				break;
			}
		}
		// check up
		for (int i = move.newRow -1; i >= 0; i--) {
			if (!occupied(i, move.newCol, move.row, move.col, state)) {
				GameMove tempMove = new GameMove(move.row, move.col, move.newRow, move.newCol, i, move.newCol, type);
				State temp = state.result(state, tempMove);
				Node child = new Node(temp, move.type);
				child.move = tempMove;
				child.parent = this;
				validMoves.add(child);
				System.out.println("added up arrow move");
				//System.out.println(temp.toString());
				System.out.println(++count);


			} else {
				break;
			}
		}
		// check down
		for (int i = move.newRow + 1; i < state.getBoard().length; i++) {
			if (!occupied(i, move.newCol, move.row, move.col, state)) {
				GameMove tempMove = new GameMove(move.row, move.col, move.newRow, move.newCol, i, move.newCol, type);
				State temp = state.result(state, tempMove);
				Node child = new Node(temp, move.type);
				child.parent = this;
				child.move = tempMove;
				validMoves.add(child);
				System.out.println("added down arrow move");
				System.out.println(temp.toString());
				System.out.println(++count);

			} else {
				break;
			}
		}
		// check up-right
		for (int i = move.newRow + 1, j = move.newCol + 1; i < state.getBoard().length && j < state.getBoard().length; i++, j++) {
			if (!occupied(i, j, move.row, move.col, state)) {
				// for each position, add all possible arrow shots to move list
				GameMove tempMove = new GameMove(move.row, move.col, move.newRow, move.newCol, i, j, type);
				State temp = state.result(state, tempMove);
				Node child = new Node(temp, move.type);
				child.parent = this;
				child.move = tempMove;
				validMoves.add(child);
				System.out.println("added up-right arrow move");
				//System.out.println(temp.toString());
				System.out.println(++count);


			} else {
				break;
			}
		}
		// check down-right
		for (int i = move.newRow - 1, j = move.newCol + 1; i >= 0 && j < state.getBoard().length; i--, j++) {
			if (!occupied(i, j, move.row, move.col, state)) {
				// for each position, add all possible arrow shots to move list
				GameMove tempMove = new GameMove(move.row, move.col, move.newRow, move.newCol, i, j, type);
				State temp = state.result(state, tempMove);
				Node child = new Node(temp, move.type);
				child.parent = this;
				child.move = tempMove;
				validMoves.add(child);
				System.out.println("added down-right arrow move");
				//System.out.println(temp.toString());
				System.out.println(++count);


			} else {
				break;
			}
		}
		// check up-left
		for (int i = move.newRow + 1, j = move.newCol - 1; i < state.getBoard().length && j >= 0; i++, j--) {
			if (!occupied(i, j, move.row, move.col, state)) {
				// for each position, add all possible arrow shots to move list
				GameMove tempMove = new GameMove(move.row, move.col, move.newRow, move.newCol, i, j, type);
				State temp = state.result(state, tempMove);
				Node child = new Node(temp, move.type);
				child.parent = this;
				child.move = tempMove;
				validMoves.add(child);
				System.out.println("added up-left arrow move");
				//System.out.println(temp.toString());
				System.out.println(++count);


			} else {
				break;
			}
		}
		// check down left
		for (int i = move.newRow - 1, j = move.newCol - 1; i >= 0 && j >= 0; i--, j--) {
			if (!occupied(i, j, move.row, move.col, state)) {
				// for each position, add all possible arrow shots to move list
				GameMove tempMove = new GameMove(move.row, move.col, move.newRow, move.newCol, i, j, type);
				State temp = state.result(state, tempMove);
				Node child = new Node(temp, move.type);
				child.parent = this;
				child.move = tempMove;
				validMoves.add(child);
				System.out.println("added down-left arrow move");
				//System.out.println(temp.toString());
				System.out.println(++count);


			} else {
				break;
			}
		}

	}
/**
 * 
 * @param row
 * 		row to check occupancy
 * @param col
 * 		col to check occupancy
 * @param currRow
 * 		current row of the queen
 * @param currCol
 * 		current column of the queen
 * @param game
 * 		current game board
 * @return
 */
	public boolean occupied(int row, int col, int currRow, int currCol, State game) {
		/*check for currX, currY to see if the square is the current location of queen.
		 * if position is marked with anything other than available, it is occupied
		 */ 
		
		if ((currRow == row && currCol == col)||game.getBoard()[row][col].equalsIgnoreCase(State.POS_AVAILABLE)) {
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
