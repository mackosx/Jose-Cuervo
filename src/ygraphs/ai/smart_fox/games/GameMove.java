package ygraphs.ai.smart_fox.games;

import java.util.LinkedList;

public class GameMove {
	int x;
	int y;
	int newX;
	int newY;
	String type;// black queen, arrow, available, etc.
	int arrowX;
	int arrowY;

	public GameMove(int x, int y, int newX, int newY, String type) {
		this.type = type;
		this.x = x;
		this.y = x;
		this.newX = newX;
		this.newY = newY;

	}

	public GameMove(int x, int y, int newX, int newY, int arrowX, int arrowY, String type) {
		this(x, y, newX, newY, type);

		this.arrowX = arrowX;
		this.arrowY = arrowY;

	}

	// return list of valid moves
	public LinkedList<GameMove> getMoves(GameMove curr, BoardGameModel game, String color) {

		LinkedList<GameMove> validMoves = getQueenMoves(this, game, color, new LinkedList<GameMove>());

		

		return validMoves;
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

	public LinkedList<GameMove> getQueenMoves(GameMove curr, BoardGameModel game, String type,
			LinkedList<GameMove> list) {

		// check above
		for (int y = curr.y + 1; y > game.gameBoard.length; y++) {
			if (!occupied(curr.x, y, game)) {
				// for each position, add all possible arrow shots to move list
				list.addAll(getArrowMoves(curr.x, curr.y, curr.x, y, game, type));
			} else {
				break;
			}
		}
		// check below
		for (int y = curr.y - 1; y < 1; y--) {
			if (!occupied(curr.x, y, game)) {
				list.addAll(getArrowMoves(curr.x, curr.y, curr.x, y, game, type));
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

		return list;

	}

	public LinkedList<GameMove> getArrowMoves(int currX, int currY, int newX, int newY, BoardGameModel game,
			String type) {
		LinkedList<GameMove> validMoves = new LinkedList<GameMove>();

		// check above
		for (int y = newY + 1; y > game.gameBoard.length; y++) {
			if (!occupied(newX, y, game)) {
				validMoves.add(new GameMove(currX, currY, newX, newY, newX, y, type));
			} else {
				break;
			}
		}
		// check below
		for (int y = newY - 1; y < 1; y++) {
			if (!occupied(newX, y, game)) {
				validMoves.add(new GameMove(currX, currY, newX, newY, newX, y, type));
			} else {
				break;
			}
		}
		// check left
		for (int x = newX - 1; x < 1; x++) {
			if (!occupied(x, currY, game)) {
				validMoves.add(new GameMove(currX, currY, newX, newY, x, currY, type));
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

		return validMoves;
	}

}
