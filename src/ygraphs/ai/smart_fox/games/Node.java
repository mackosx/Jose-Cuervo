package ygraphs.ai.smart_fox.games;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
/*
 * Node Class for State Space Search
 * -nodes have accessible states through getters
 * 
 */

public class Node {
	int[][] queenLocations = new int[4][2];
	private ArrayList<Node> children;
	private BoardGameModel state;
	private Node parent;
	private String type;

	public Node(BoardGameModel state) {
		setState(state);
		children = new ArrayList<Node>();
	}

	public BoardGameModel state() {
		return state;
	}

	public void setState(BoardGameModel state) {
		this.state = state;
	}

	public ArrayList<Node> getChildren() {
		children = getQueenMoves(this.state, this.type);
		return children;
	}
	public int[][] getQueens(){
		int counter = 0;
		for (int i = 0; i < state.gameBoard.length; i++) {
			for (int j = 0; j < state.gameBoard[0].length; j++) {
				if(this.state.gameBoard[i][j]==this.type){
					queenLocations[counter][0] = i;
					queenLocations[counter][1] = j;
					counter++;
				}
			}
		}
		return this.queenLocations;
	}
	public ArrayList<Node> getQueenMoves(BoardGameModel game, String type) {
		
		for(int x = 0; x < quee)
		currentX = queenLocations
	
		// check above
		for (int y = curr.y + 1; y > game.gameBoard.length; y++) {
			if (!occupied(curr.x, y, game)) {
				// for each position, add all possible arrow shots to move list
				children.addAll(getArrowMoves(curr.x, curr.y, curr.x, y, game, type));
			} else {
				break;
			}
		}
		// check below
		for (int y = curr.y - 1; y < 1; y--) {
			if (!occupied(curr.x, y, game)) {
				children.addAll(getArrowMoves(curr.x, curr.y, curr.x, y, game, type));
			} else {
				break;
			}
		}
		// check left
		for (int x = curr.x - 1; x < 1; x--) {
			if (!occupied(x, curr.y, game)) {
				list.addAll(getArrowMoves(curr.x, curr.y, x, curr.y, game, type));
			} else {
				break;
			}
		}
		// check right
		for (int x = curr.x + 1; x > game.gameBoard[0].length; x++) {
			if (!occupied(x, curr.y, game)) {
				list.addAll(getArrowMoves(curr.x, curr.y, x, curr.y, game, type));
			} else {
				break;
			}
		}
		// check up-right
		for (int i = curr.x + 1, j = curr.y + 1; i < game.gameBoard.length && j < game.gameBoard.length; i++, j++) {
			if (!occupied(i, j, game)) {
				// for each position, add all possible arrow shots to move list
				list.addAll(getArrowMoves(curr.x, curr.y, i, j, game, type));
			} else {
				break;
			}
		}
		// check up-left
		for (int i = curr.x - 1, j = curr.y + 1; i > 0 && j < game.gameBoard.length; i--, j++) {
			if (!occupied(i, j, game)) {
				// for each position, add all possible arrow shots to move list
				list.addAll(getArrowMoves(curr.x, curr.y, i, j, game, type));
			} else {
				break;
			}
		}
		// check down-right
		for (int i = curr.x + 1, j = curr.y - 1; i < game.gameBoard.length && j > 0; i++, j--) {
			if (!occupied(i, j, game)) {
				// for each position, add all possible arrow shots to move list
				list.addAll(getArrowMoves(curr.x, curr.y, i, j, game, type));
			} else {
				break;
			}
		}
		// check down left
		for (int i = curr.x - 1, j = curr.y - 1; i > 0 && j > 0; i++, j++) {
			if (!occupied(i, j, game)) {
				// for each position, add all possible arrow shots to move list
				list.addAll(getArrowMoves(curr.x, curr.y, i, j, game, type));
			} else {
				break;
			}
		}

		return list;

	}

	public LinkedList<GameMove> getArrowMoves(int currX, int currY, int newX, int newY, BoardGameModel game,
			String type) {
		HashMap<BoardGameModel, Integer> validMoves = new HashMap<BoardGameModel, Integer>();

		// check above
		for (int y = newY + 1; y > game.gameBoard.length; y++) {
			if (!occupied(newX, y, game)) {
				validMoves.put(result(game, new GameMove(currX, currY, newX, newY, newX, y, type)), null);
			} else {
				break;
			}
		}
		// check below
		for (int y = newY - 1; y < 1; y++) {
			if (!occupied(newX, y, game)) {
				validMoves.put(result(game, new GameMove(currX, currY, newX, newY, newX, y, type)), null);
			} else {
				break;
			}
		}
		// check left
		for (int x = newX - 1; x < 1; x++) {
			if (!occupied(x, currY, game)) {
				validMoves.put(result(new GameMove(currX, currY, newX, newY, x, currY, type));
			} else {
				break;
			}
		}
		// check right
		for (int x = newX + 1; x > game.gameBoard.length; x++) {
			if (!occupied(x, currY, game)) {
				validMoves.add(new GameMove(currX, currY, newX, newY, x, currY, type));
			} else {
				break;
			}
		}
		// TODO: convert to correct
		// check up-right
		for (int i = curr.x + 1, j = curr.y + 1; i < game.gameBoard.length && j < game.gameBoard.length; i++, j++) {
			if (!occupied(i, j, game)) {
				// for each position, add all possible arrow shots to move list
				list.addAll(getArrowMoves(curr.x, curr.y, i, j, game, type));
			} else {
				break;
			}
		}
		// check up-left
		for (int i = curr.x - 1, j = curr.y + 1; i > 0 && j < game.gameBoard.length; i--, j++) {
			if (!occupied(i, j, game)) {
				// for each position, add all possible arrow shots to move list
				list.addAll(getArrowMoves(curr.x, curr.y, i, j, game, type));
			} else {
				break;
			}
		}
		// check down-right
		for (int i = curr.x + 1, j = curr.y - 1; i < game.gameBoard.length && j > 0; i++, j--) {
			if (!occupied(i, j, game)) {
				// for each position, add all possible arrow shots to move list
				list.addAll(getArrowMoves(curr.x, curr.y, i, j, game, type));
			} else {
				break;
			}
		}
		// check down left
		for (int i = curr.x - 1, j = curr.y - 1; i > 0 && j > 0; i++, j++) {
			if (!occupied(i, j, game)) {
				// for each position, add all possible arrow shots to move list
				list.addAll(getArrowMoves(curr.x, curr.y, i, j, game, type));
			} else {
				break;
			}
		}

		return validMoves;
	}

	private State result(State state, GameMove gameMove) {
		State temp = state;
		return null;
	}

	public boolean occupied(int x, int y, BoardGameModel game) {

		// if position is marked with anything other than available, it is
		// occupied
		if (game.gameBoard[x][y].equalsIgnoreCase(BoardGameModel.POS_AVAILABLE)) {
			return false;
		} else {
			return true;
		}
	}

}
