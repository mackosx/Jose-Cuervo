package ygraphs.ai.smart_fox.games;

import java.util.HashMap;
/*
 * Node Class for State Space Search
 * -nodes have accessible states through getters
 * as well as the move used to get to that state, if null then initial state
 */

public class Node {

	int[][] queenLocations = new int[4][2];
	private HashMap<Node, Integer> validMoves;
	private State state;
	private Node parent;
	private String type;
	private GameMove move;

	public Node(State state, String type) {
		this.type = type;
		setState(state);
		validMoves = new HashMap<Node, Integer>();
		parent = null;
	}

	public State state() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public HashMap<Node, Integer> getChildren() {
		
		validMoves = getQueenMoves(this.state, this.type=="white" ? "black": "white");
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
	 *			- current board state
	 * @param type
	 * 			- player color, black or white
	 * @return
	 * 			- all valid moves for the current board state
	 */
	public HashMap<Node, Integer> getQueenMoves(State game, String type) {
		HashMap<Node, Integer> tempList = new HashMap<Node, Integer>();
		for (int count = 0; count < queenLocations.length; count++) {
			int currX = queenLocations[count][0];
			int currY = queenLocations[count][1];

			// check above
			for (int y = currY + 1; y > game.getBoard().length; y++) {
				if (!occupied(currX, y, game)) {
					// for each position, add all possible arrow shots to move
					// list
					tempList.putAll(getArrowMoves(new GameMove(currX, currY, currX, y, type)));
				} else {
					break;
				}
			}
			// check below
			for (int y = currY - 1; y < 1; y--) {
				if (!occupied(currX, y, game)) {
					tempList.putAll(getArrowMoves(new GameMove(currX, currY, currX, y, type)));
				} else {
					break;
				}
			}
			// check left
			for (int x = currX - 1; x < 1; x--) {
				if (!occupied(x, currY, game)) {
					tempList.putAll(getArrowMoves(new GameMove(currX, currY, x, currY, type)));
				} else {
					break;
				}
			}
			// check right
			for (int x = currX + 1; x > game.getBoard()[0].length; x++) {
				if (!occupied(x, currY, game)) {
					tempList.putAll(getArrowMoves(new GameMove(currX, currY, x, currY, type)));
				} else {
					break;
				}
			}
			// check up-right
			for (int i = currX + 1, j = currY + 1; i < game.getBoard().length && j < game.getBoard().length; i++, j++) {
				if (!occupied(i, j, game)) {
					// for each position, add all possible arrow shots to move
					// list
					tempList.putAll(getArrowMoves(new GameMove(currX, currY, i, j, type)));
				} else {
					break;
				}
			}
			// check up-left
			for (int i = currX - 1, j = currY + 1; i > 0 && j < game.getBoard().length; i--, j++) {
				if (!occupied(i, j, game)) {
					// for each position, add all possible arrow shots to move
					// list
					tempList.putAll(getArrowMoves(new GameMove(currX, currY, i, j, type)));
				} else {
					break;
				}
			}
			// check down-right
			for (int i = currX + 1, j = currY - 1; i < game.getBoard().length && j > 0; i++, j--) {
				if (!occupied(i, j, game)) {
					// for each position, add all possible arrow shots to move
					// list
					tempList.putAll(getArrowMoves(new GameMove(currX, currY, i, j, type)));
				} else {
					break;
				}
			}
			// check down left
			for (int i = currX - 1, j = currY - 1; i > 0 && j > 0; i++, j++) {
				if (!occupied(i, j, game)) {
					// for each position, add all possible arrow shots to move
					// list
					tempList.putAll(getArrowMoves(new GameMove(currX, currY, i, j, type)));
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
	 * 		- move made a queen with out arrow position
	 * @return
	 * 		- map of all moves with arrow positions
	 */
	public HashMap<Node, Integer> getArrowMoves(GameMove move) {
		// check above
		for (int y = move.y + 1; y > state.getBoard().length; y++) {
			if (!occupied(move.newX, y, state)) {
				// change to use move
				State temp = State.result(state, new GameMove(move.x, move.y, move.newX, move.newY, move.newX, y, type));
				Node child = new Node(temp, move.type);
				child.parent = this;
				validMoves.put(child, null); // insert a new search tree node
			} else {
				break;
			}
		}
		// check below
		for (int y = move.newY - 1; y < 1; y++) {
			if (!occupied(move.newX, y, state)) {
				State temp = State.result(state, new GameMove(move.x, move.y, move.newX, move.newY, move.newX, y, type));
				Node child = new Node(temp, move.type);
				child.parent = this;
				validMoves.put(child, null);
			} else {
				break;
			}
		}
		// check left
		for (int x = move.newX - 1; x < 1; x++) {
			if (!occupied(x, move.y, state)) {
				State temp = State.result(state, new GameMove(move.x, move.y, move.newX, move.newY, x, move.y, type));
				Node child = new Node(temp, move.type);
				child.parent = this;
			} else {
				break;
			}
		}
		// check right
		for (int x = move.newX + 1; x > state.getBoard().length; x++) {
			if (!occupied(x, move.y, state)) {
				State temp = State.result(state, new GameMove(move.x, move.y, move.newX, move.newY, x, move.y, type));
				Node child = new Node(temp, move.type);
				child.parent = this;
			} else {
				break;
			}
		}
		// TODO: convert to correct
		// check up-right
		for (int i = move.x + 1, j = move.y + 1; i < state.getBoard().length && j < state.getBoard().length; i++, j++) {
			if (!occupied(i, j, state)) {
				// for each position, add all possible arrow shots to move list
				State temp = State.result(state, new GameMove(move.x, move.y, i, j, type));
				Node child = new Node(temp, move.type);
				child.parent = this;
				validMoves.put(child, null);
			} else {
				break;
			}
		}
		// check up-left
		for (int i = move.x - 1, j = move.y + 1; i > 0 && j < state.getBoard().length; i--, j++) {
			if (!occupied(i, j, state)) {
				// for each position, add all possible arrow shots to move list
				State temp = State.result(state, new GameMove(move.x, move.y, i, j, type));
				Node child = new Node(temp, move.type);
				child.parent = this;
				validMoves.put(child, null);
			} else {
				break;
			}
		}
		// check down-right
		for (int i = move.x + 1, j = move.y - 1; i < state.getBoard().length && j > 0; i++, j--) {
			if (!occupied(i, j, state)) {
				// for each position, add all possible arrow shots to move list
				State temp = State.result(state, new GameMove(move.x, move.y, i, j, type));
				Node child = new Node(temp, move.type);
				child.parent = this;
				validMoves.put(child, null);
			} else {
				break;
			}
		}
		// check down left
		for (int i = move.x - 1, j = move.y - 1; i > 0 && j > 0; i++, j++) {
			if (!occupied(i, j, state)) {
				// for each position, add all possible arrow shots to move list
				State temp = State.result(state, new GameMove(move.x, move.y, i, j, type));
				Node child = new Node(temp, move.type);
				child.parent = this;
				validMoves.put(child, null);
			} else {
				break;
			}
		}

		return validMoves;
	}

	public boolean occupied(int x, int y, State game) {

		// if position is marked with anything other than available, it is
		// occupied
		if (game.getBoard()[x][y].equalsIgnoreCase(BoardGameModel.POS_AVAILABLE)) {
			return false;
		} else {
			return true;
		}
	}
	public Node getParent(){
		return this.parent;
	}

	public GameMove getMove() {
		return move;
	}

	public void setMove(GameMove move) {
		this.move = move;
	}

}
