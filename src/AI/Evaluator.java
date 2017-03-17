package AI;

import java.util.LinkedList;
import java.util.Queue;

public class Evaluator {

	public Evaluator() {
		// Constructor
	}

	/**
	 * @param n
	 *            = node to examine
	 * @return returns 2d array mapping each point to its closest queen
	 */
	int[][] black;
	int[][] white;
	String opposite;
	State s;

	public int minDistance(Node n) {
		// 1. for each point p, compare db = dist(p,Black) and dw =
		// dist(p,White);
		// 2. if db < dw: Black point;
		// 3. else if db > dw: White point;
		// 4. else point is neutral.
		State boardstate = n.state();
		String[][] board = boardstate.getBoard();
		opposite = n.getType() == "black" ? "white" : "black";
		black = n.getQueens("black");
		white = n.getQueens("white");
		// String[][] map = board.clone();

		// Evaluate board for each queen
		int opponentScore = 0;
		int ourScore = 0;
		int row = 0;
		for (String[] i : board) {
			int column = 0;
			for (String j : i) {
				if (j.equalsIgnoreCase("available")) {
					String ownership = dist(row, column, black, white);
					if (ownership.equals(n.getType())) {
						// increment board for for our colour
						ourScore++;
					} else if (ownership.equals(opposite)) {
						opponentScore++;
					}
				}
				column++;
			}
			row++;
		}
		return ourScore - opponentScore;
	}

	/**
	 * @param cr
	 *            = row idx of current point (current row)
	 * @param cc
	 *            = column idx of current point (current column)
	 * @param black
	 *            = 2d array supplying row and column of all black queens
	 * @param white
	 *            = 2d array supplying row and column of all white queens
	 * @return returns the color of the queen closest to the supplied point
	 */
	public String dist(int cr, int cc, int[][] black, int[][] white) {
		int dw, minDW, db, minDB;
		dw = minDW = db = minDB = Integer.MAX_VALUE;

		// Determine nearest white
		for (int[] i : white) {
			int qr = i[0];
			int qc = i[1];
			if (!((qc == cc) && (cr == qr))) {
				if (qc == cc) {
					dw = Math.abs(qr - cr);
				} else if (qr == cr) {
					dw = Math.abs(qc - cc);
				} else if (((qr + qc) == (cr + cc)) || ((Math.abs(qr - qc)) == (Math.abs(cr - cc)))) {
					dw = Math.abs(qc - cc);
				}
			}

			if (dw < minDW) {
				minDW = dw;
			}
		}

		// Determine nearest black
		for (int[] j : black) {
			int qr = j[0];
			int qc = j[1];
			if (!((qc == cc) && (cr == qr))) {
				if (qc == cc) {
					db = Math.abs(qr - cr);
				} else if (qr == cr) {
					db = Math.abs(qc - cc);
				} else if ((qr + qc) == (cr + cc)) {
					db = Math.abs(qc - cc);
				}
			}

			if (db < minDB) {
				minDB = db;
			}
		}

		if (minDB < minDW) {
			return "black";
		} else if (minDB > minDW) {
			return "white";
		} else {
			return "neutral";
		}
	}

	// test evaluator
	int ourScore = 0;
	int theirScore = 0;
	String str="";
	public int newMinDist(Node n) {
		String[][] s = n.state().getBoard();
		ourScore = 0;
		theirScore = 0;
		for (int i = 0; i < s.length; i++) {
			for (int j = 0; j < s[0].length; j++) {
				//System.out.println(n.state().toString(i, j));
				if (s[i][j].equals("available"))
					findNearestQueen(i, j, n);
			}
			str+="\n";
		}

		return ourScore - theirScore;
	}

	public void findNearestQueen(int row, int col, Node n) {
		State b = n.state();
		opposite = n.getType() == "black" ? "white" : "black";
		boolean[][] checked = new boolean[10][10]; // 2d board representation of
													// if a spot has been
													// checked
		checked[row][col] = true; // Mark starting tile as checked
		boolean isFound = false;
		// Initialize the queue
		Queue<GameMove> q = new LinkedList<>();
		q = getQueenMoves(row, col, b, checked);
		while (!isFound) {
			Queue<GameMove> tempQ = new LinkedList<>();
			int index = q.size();
			// If empty tile is trapped, break
			if (index == 0) {
				isFound = true;
				break;
			}
			for (int i = 0; i < index; i++) {
				GameMove currentTile = q.poll();
				// If queen is found, increase count and break
				String current = b.getBoard()[currentTile.row][currentTile.col];
				if (current.equals("white")||current.equals("black")) {
					isFound = true;
					String currentOpposite = current.equals("white")?"black":"white";
					boolean contested = false;
					// Check if tile is contested
					for (GameMove remainder : q) {
						// If a queen is found in the rest of the queue, tile is
						// contested
						if (b.getBoard()[remainder.row][remainder.col].equals(currentOpposite))
							contested = true;
					}
					if (contested){
						//System.out.println("neutral.");
						str+="|N";
						break;
					}
					// Determine who's queen it is, increase count accordingly
					if (b.getBoard()[currentTile.row][currentTile.col].equals(opposite)) {
						theirScore++;
						str+="|2";
						//System.out.println("opponent controlled." + theirScore);
	
					} else {
						str+="|1";

						ourScore++;
						//System.out.println("our control." + ourScore);
					}
				
					// If element is the last in the queue (ie: current 'layer')
					// then break
					break;
				}
				// If not a queen, mark location as checked
				else
					checked[currentTile.row][currentTile.col] = true;
				// If free square (not arrow), add neighbours of it to tempQ
				if (b.getBoard()[currentTile.row][currentTile.col].equals("available")) {
					tempQ.addAll(getQueenMoves(currentTile.row, currentTile.col, b, checked));
				}
			}
			q = tempQ;
		}
	}

	public LinkedList<GameMove> getQueenMoves(int currRow, int currCol, State boardstate, boolean[][] checked) {
		// check above
		LinkedList<GameMove> validMoves = new LinkedList<GameMove>();
		for (int i = currRow - 1; i >= 0; i--) {
			if (!checked[i][currCol])
				validMoves.add(new GameMove(i, currCol));
			if (occupied(i, currCol, boardstate)) {
				break;
			}
		}

		// check below
		for (int i = currRow + 1; i < boardstate.getBoard().length; i++) {
			if (!checked[i][currCol])
				validMoves.add(new GameMove(i, currCol));
			if (occupied(i, currCol, boardstate)) {
				break;
			}
		}

		// check left
		for (int j = currCol - 1; j >= 0; j--) {
			if (!checked[currRow][j])
				validMoves.add(new GameMove(currRow, j));
			if (occupied(currRow, j, boardstate)) {
				break;
			}
		}

		// check right
		for (int j = currCol + 1; j < boardstate.getBoard().length; j++) {
			if (!checked[currRow][j])
				validMoves.add(new GameMove(currRow, j));
			if (occupied(currRow, j, boardstate)) {
				break;
			}
		}

		// check up-right
		for (int i = currRow + 1, j = currCol - 1; i < boardstate.getBoard().length && j >= 0; i++, j--) {
			if (!checked[i][j])
				validMoves.add(new GameMove(i, j));
			if (occupied(i, j, boardstate)) {
				break;
			}
		}

		// check up-left
		for (int i = currRow - 1, j = currCol - 1; i >= 0 && j >= 0; i--, j--) {
			if (!checked[i][j])
				validMoves.add(new GameMove(i, j));
			if (occupied(i, j, boardstate)) {
				break;
			}
		}

		// check down-right
		for (int i = currRow + 1, j = currCol + 1; i < boardstate.getBoard().length
				&& j < boardstate.getBoard().length; i++, j++) {
			if (!checked[i][j])
				validMoves.add(new GameMove(i, j));
			if (occupied(i, j, boardstate))
				break;

		}

		// check down left
		for (int i = currRow - 1, j = currCol + 1; i >= 0 && j < boardstate.getBoard().length; i--, j++) {
			if (!checked[i][j])
				validMoves.add(new GameMove(i, j));
			if (occupied(i, j, boardstate))
				break;

		}
		return validMoves;

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
	public boolean occupied(int row, int col, State game) {
		/*
		 * check for currX, currY to see if the square is the current location
		 * of queen. if position is marked with anything other than available,
		 * it is occupied
		 */

		if (game.getBoard()[row][col].equalsIgnoreCase(State.POS_AVAILABLE)) {
			return false;
		} else {
			return true;
		}
	}

}