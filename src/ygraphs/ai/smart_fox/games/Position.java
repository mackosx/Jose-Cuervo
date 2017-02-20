package ygraphs.ai.smart_fox.games;

import java.util.LinkedList;

public class Position {
	int x;
	int y;
	String content;// black queen, arrow, available, etc.

	public Position(int x, int y, String content) {
		this.content = content;
		this.x = x;
		this.y = y;

	}

	// return list of valid moves
	// one thread timer, other thread determine best val so far,
	public LinkedList<Position> getMoves(Position curr, BoardGameModel game) {

		String[][] board = game.gameBoard;
		LinkedList<Position> validQueenMoves = getQueenMoves(curr, game);
		//list of moves with arrow shots
		LinkedList<Position> validMoves = new LinkedList<Position>();
		// get moves if piece is black
		if (this.content.equalsIgnoreCase(BoardGameModel.POS_MARKED_BLACK)) {
			// check up
			for (int x = y + 1; x < board.length; x++) {
				if (!occupied(x, y, game)) {
					validQueenMoves.add(new Position(x, y, BoardGameModel.POS_MARKED_BLACK));
				}
			}
		}

		return validMoves;
	}

	public boolean occupied(int x, int y, BoardGameModel game) {
		// if position is marked with anything other than available, it is occupied
		if (game.gameBoard[x][y].equalsIgnoreCase(BoardGameModel.POS_AVAILABLE)) {
			return false;
		} else {
			return true;
		}
	}

	public LinkedList<Position> getQueenMoves(Position curr, BoardGameModel game) {
		
		LinkedList<Position> validQueenMoves = new LinkedList<Position>();
		// get moves if piece is black
		if (this.content.equalsIgnoreCase(BoardGameModel.POS_MARKED_BLACK)) {
			// check above
			for (int x = y + 1; x < game.gameBoard.length; x++) {
				if (!occupied(x, y, game)) {
					validQueenMoves.add(new Position(x, y, BoardGameModel.POS_MARKED_BLACK));
				}else{
					break;
				}
			}
		}
		return validQueenMoves;

	}
}
