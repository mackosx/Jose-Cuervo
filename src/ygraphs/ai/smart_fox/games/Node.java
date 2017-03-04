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
		getQueens();
	}

	public State state() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public LinkedList<Node> getChildren() {

		validMoves = getQueenMoves(this.state, this.type == "white" ? "black" : "white");
		return validMoves;
	}

	public int[][] getQueens() {
		int counter = 0;
		for (int i = 0; i < state.getBoard().length; i++) {
			for (int j = 0; j < state.getBoard()[0].length; j++) {
				if (this.state.getBoard()[i][j] == this.type) {
					queenLocations[counter][0] = i;
					queenLocations[counter][1] = j;
					counter++;
				}
			}
		}
		return this.queenLocations;
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
	public LinkedList<Node> getQueenMoves(State game, String type) {
		LinkedList<Node> tempList = new LinkedList<Node>();
		for (int queen = 0; queen < queenLocations.length; queen++) {
			int currX = getQueens()[queen][0];
			int currY = getQueens()[queen][1];
			System.out.println(currX + ", " + currY);
			// check above
			for (int y = currY - 1; y >=0; y--) {
				if (!occupied(currX, y, currX, currY, game)) {
					// for each position, add all possible arrow shots to move
					// list
					tempList.addAll(getArrowMoves(new GameMove(currX, currY, currX, y, type)));
				} else {
					break;
				}// 106 moves
			}
			// check below
			for (int y = currY + 1; y < game.getBoard().length; y++) {
				if (!occupied(currX, y, currX, currY, game)) {
					tempList.addAll(getArrowMoves(new GameMove(currX, currY, currX, y, type)));
				} else {
					break;
				}
			}
//			// check left
			for (int x = currX - 1; x >= 0; x--) {
				if (!occupied(x, currY, currX, currY, game)) {
					tempList.addAll(getArrowMoves(new GameMove(currX, currY, x, currY, type)));
				} else {
					break;
				}
			}
//			// check right
			int count = 0;
			for (int x = currX + 1; x < game.getBoard().length; x++) {
				if (!occupied(x, currY, currX, currY, game)) {
					tempList.addAll(getArrowMoves(new GameMove(currX, currY, x, currY, type)));
				} else {
					break;
				}
			}
			// check up-right
			for (int i = currX + 1, j = currY - 1; i < game.getBoard().length && j >= 0; i++, j--) {
				if (!occupied(i, j, currX, currY, game)) {
					// for each position, add all possible arrow shots to move
					// list
					tempList.addAll(getArrowMoves(new GameMove(currX, currY, i, j, type)));
				} else {
					break;
				}
			}
			// check up-left
			for (int i = currX - 1, j = currY - 1; i >= 0 && j >= 0; i--, j--) {
				if (!occupied(i, j, currX, currY, game)) {
					// for each position, add all possible arrow shots to move
					// list
					tempList.addAll(getArrowMoves(new GameMove(currX, currY, i, j, type)));
				} else {
					break;
				}
			}
			// check down-right
			for (int i = currX + 1, j = currY + 1; i < game.getBoard().length && j < game.getBoard().length; i++, j++) {
				if (!occupied(i, j, currX, currY, game)) {
					// for each position, add all possible arrow shots to move
					// list
					tempList.addAll(getArrowMoves(new GameMove(currX, currY, i, j, type)));
				} else {
					break;
				}
			}
			// check down left
			for (int i = currX - 1, j = currY + 1; i >= 0 && j < game.getBoard().length; i--, j++) {
				if (!occupied(i, j, currX, currY, game)) {
					// for each position, add all possible arrow shots to move
					// list
					tempList.addAll(getArrowMoves(new GameMove(currX, currY, i, j, type)));
				} else {
					break;
				}
			}
		}

		return tempList;

	}

	/**
	 * 
	 * @param move
	 *            - move made a queen with out arrow position
	 * @return - map of all moves with arrow positions
	 */
	int count = 0;
	public LinkedList<Node> getArrowMoves(GameMove move) {
		// check above
		for (int y = move.newY - 1; y >=0; y--) {
			if (!occupied(move.newX, y, move.x, move.y, state)) {
				// change to use move
				GameMove tempMove = new GameMove(move.x, move.y, move.newX, move.newY, move.newX, y, type);
				State temp = state.result(state, tempMove);
				Node child = new Node(temp, move.type);
				child.parent = this;
				child.move = tempMove;
				System.out.println(tempMove.toString());
				validMoves.add(child); // insert a new search tree node
				System.out.println("added above arrow move");
				System.out.println(temp.toString());
				System.out.println(++count);

			} else {
				break;
			}
		}
		// check below
		for (int y = move.newY + 1 ; y < state.getBoard().length; y++) {
			if (!occupied(move.newX, y, move.x, move.y, state)) {
				GameMove tempMove = new GameMove(move.x, move.y, move.newX, move.newY, move.newX, y, type);
				State temp = state.result(state, tempMove);
				Node child = new Node(temp, move.type);
				child.parent = this;
				child.move = tempMove;
				validMoves.add(child);
				System.out.println(tempMove.toString());
				System.out.println("added below arrow move");
				System.out.println(temp.toString());
				System.out.println(++count);


			} else {
				break;
			}
		}
		// check left
		for (int x = move.newX -1; x >= 0; x--) {
			if (!occupied(x, move.newY, move.x, move.y, state)) {
				GameMove tempMove = new GameMove(move.x, move.y, move.newX, move.newY, x, move.newY, type);
				State temp = state.result(state, tempMove);
				Node child = new Node(temp, move.type);
				child.move = tempMove;
				child.parent = this;
				validMoves.add(child);
				System.out.println("added left arrow move");
				System.out.println(temp.toString());
				System.out.println(++count);


			} else {
				break;
			}
		}
		// check right
		for (int x = move.newX + 1; x < state.getBoard().length; x++) {
			if (!occupied(x, move.newY, move.x, move.y, state)) {
				GameMove tempMove = new GameMove(move.x, move.y, move.newX, move.newY, x, move.newY, type);
				State temp = state.result(state, tempMove);
				Node child = new Node(temp, move.type);
				child.parent = this;
				child.move = tempMove;
				validMoves.add(child);
				System.out.println("added right arrow move");
				System.out.println(temp.toString());
				System.out.println(++count);

			} else {
				break;
			}
		}
		// check up-right
		for (int i = move.newX + 1, j = move.newY + 1; i < state.getBoard().length && j < state.getBoard().length; i++, j++) {
			if (!occupied(i, j, move.x, move.y, state)) {
				// for each position, add all possible arrow shots to move list
				GameMove tempMove = new GameMove(move.x, move.y, move.newX, move.newY, i, j, type);
				State temp = state.result(state, tempMove);
				Node child = new Node(temp, move.type);
				child.parent = this;
				child.move = tempMove;
				validMoves.add(child);
				System.out.println("added up right arrow move");
				System.out.println(temp.toString());
				System.out.println(++count);


			} else {
				break;
			}
		}
		// check up-left
		for (int i = move.newX - 1, j = move.newY + 1; i >= 0 && j < state.getBoard().length; i--, j++) {
			if (!occupied(i, j, move.x, move.y, state)) {
				// for each position, add all possible arrow shots to move list
				GameMove tempMove = new GameMove(move.x, move.y, move.newX, move.newY, i, j, type);
				State temp = state.result(state, tempMove);
				Node child = new Node(temp, move.type);
				child.parent = this;
				child.move = tempMove;
				validMoves.add(child);
				System.out.println("added up left arrow move");
				System.out.println(temp.toString());
				System.out.println(++count);


			} else {
				break;
			}
		}
		// check down-right
		for (int i = move.newX + 1, j = move.newY - 1; i < state.getBoard().length && j >= 0; i++, j--) {
			if (!occupied(i, j, move.x, move.y, state)) {
				// for each position, add all possible arrow shots to move list
				GameMove tempMove = new GameMove(move.x, move.y, move.newX, move.newY, i, j, type);
				State temp = state.result(state, tempMove);
				Node child = new Node(temp, move.type);
				child.parent = this;
				child.move = tempMove;
				validMoves.add(child);
				System.out.println("added down right arrow move");
				System.out.println(temp.toString());
				System.out.println(++count);


			} else {
				break;
			}
		}
		// check down left
		for (int i = move.newX - 1, j = move.newY - 1; i >= 0 && j >= 0; i--, j--) {
			if (!occupied(i, j, move.x, move.y, state)) {
				// for each position, add all possible arrow shots to move list
				GameMove tempMove = new GameMove(move.x, move.y, move.newX, move.newY, i, j, type);
				State temp = state.result(state, tempMove);
				Node child = new Node(temp, move.type);
				child.parent = this;
				child.move = tempMove;
				validMoves.add(child);
				System.out.println("added down left arrow move");
				System.out.println(temp.toString());
				System.out.println(++count);


			} else {
				break;
			}
		}

		return validMoves;
	}

	public boolean occupied(int x, int y, int currX, int currY, State game) {
		/*check for currX, currY to see if the square is the current location of queen.
		 * if position is marked with anything other than available, it is occupied
		 */ 
		
		if ((currX == x && currY == y)||game.getBoard()[x][y].equalsIgnoreCase(State.POS_AVAILABLE)) {
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
