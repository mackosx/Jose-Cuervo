package ygraphs.ai.smart_fox.games;

public class Evaluator {

	public Evaluator() {
		// Constructor
	}
	
	/**
	 * @param n = node to examine
	 * @return returns 2d array mapping each point to its closest queen
	 */
	public int minDistance(Node n) {
		// 1. for each point p, compare db = dist(p,Black) and dw =
		// dist(p,White);
		// 2. if db < dw: Black point;
		// 3. else if db > dw: White point;
		// 4. else point is neutral.
		State boardstate = n.state();
		String[][] board = boardstate.getBoard();
		String opposite = n.getType() == "black"? "white" : "black";
		int[][] black = n.getQueens("black");
		int[][] white = n.getQueens("white");
		//String[][] map = board.clone();
		
		// Evaluate board for each queen
		int opponentScore = 0;
		int ourScore = 0;
		int row = 0;
		for (String[] i : board) {
			int column = 0;
			for (String j : i) {
				if (j.equalsIgnoreCase("available")) {
					String ownership = dist(row, column, black, white);
					if(ownership.equals(n.getType())){
						//increment board for for our colour
						ourScore++;
					}
					else if(ownership.equals(opposite)){
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
	 * @param cr = row idx of current point (current row)
	 * @param cc = column idx of current point (current column)
	 * @param black = 2d array supplying row and column of all black queens
	 * @param white = 2d array supplying row and column of all white queens
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

}