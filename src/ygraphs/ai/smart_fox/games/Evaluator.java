package ygraphs.ai.smart_fox.games;

import java.util.HashMap;

import ygraphs.ai.smart_fox.games.Amazon;
import ygraphs.ai.smart_fox.games.BoardGameModel;
import ygraphs.ai.smart_fox.games.GameBoard;

//import p1.BoardGameModel;
//import p1.GameBoard;

public class Evaluator {

	public static void main(String[] args) {
		// Create the state
		State maz = new State(10, 10);

		// Initialize the starting positions
		maz.init(false);

		// Test node, white
		Node testNode = new Node(maz, "black");

		// Test of the evaluator class
		Evaluator ev = new Evaluator();

		// Passing evaluator a test node
		ev.minDistance(testNode);
	}

	public Evaluator() {
		// Constructor
	}

	public int minDistance(Node n) {
		// 1. for each point p, compare db = dist(p,Black) and dw =
		// dist(p,White);
		// 2. if db < dw: Black point;
		// 3. else if db > dw: White point;
		// 4. else point is neutral.

		State boardstate = n.state();
		String[][] board = boardstate.getBoard();
		int[][] ourQueenies = n.getQueens(false);
		int[][] theirQueenies = n.getQueens(true);

		// Evaluate board for each queen
		int db = 0;
		int dw = 0;
		int column = 0;
		for (String[] i : board) {
			int row = 0;
			for (String j : i) {
				// If string = "available" compute the distance (number of moves) to the nearest queen of each colour
				if (j.equalsIgnoreCase("available")) {
					// Current board location
					System.out.println("["+row+"]"+"["+column+"]");
					
				}
				row++;
			}
			column++;
		}
		return 0;
	}
	
	public int dist(int qr, int qc, int qfr, int qfc){
		//Ignoring arrows to 
		return 0;
	}

}