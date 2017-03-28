package AI;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Random;

public class StateSpace {
	private Node root = null;
	ArrayList<Node> frontier = new ArrayList<Node>();;
	int depth = 0;
	Node best;
	int turnCount;
	long start;
	String currentTurn;

	public StateSpace(Node r, int turns, long start) {
		this.start = start;
		root = r;
		turnCount = turns;
	}

	public void startAlphaBeta() {
		System.out.println("Starting Alpha Beta Pruning/Minimax.");
		alphaBeta();
		System.out.println("Done searching.");
		if (root.getChildren().size() <= 0) {
			System.out.println("Game Over.\n" + root.opposite.toUpperCase() + "' WINS!");
			System.out.println(root.state().toString());
			System.exit(1);
		}
		State st = new State(root.state().getBoard());
		System.out.println(st.result(root.state(), best.getMove()).toString());
	}

	/**
	 * 
	 * @param s
	 *            starting node being evaluated
	 * @param limit
	 *            depth to search at
	 * @return best move from a-B pruning
	 */
	public void alphaBeta() { // returns an action
		depth = 1;
		Node tempBest = null;
		do {
			if (tempBest != null) {
				System.out.println("Best move so far:\n" + tempBest.toString() + " \nVALUE:" + tempBest.hValue);
				best = tempBest;
			}
			System.out.println("Now searching depth = " + depth + "...");
			tempBest = maxValue(root, Integer.MIN_VALUE, Integer.MAX_VALUE, depth++);
			if (root.getChildren().size() <= 0) {
				System.out.println("No moves left. Game Over.\n" + root.opposite.toUpperCase() + "' WINS!");
				System.out.println(root.state().toString());
				System.exit(1);
			}
		} while (timeLeft());

	}

	/**
	 * 
	 * @return max value of a node, based on alpha-beta
	 */
	int bestScore;

	public Node maxValue(Node s, int alpha, int beta, int limit) {
		Evaluator eval = new Evaluator(root);
		if (limit == 0) {
			boolean kings = false;
			// heuristic switch over to start making territory
			if (turnCount > 15)
				kings = true;
			s.hValue = eval.minDist(s, kings);
			return s;

		}

		Node v = new Node(null, null, Integer.MIN_VALUE);
		for (Node child : s.generateChildren()) {
			if (timeLeft()) {

				Node min = minValue(child, alpha, beta, limit - 1);
				if (min.hValue > v.hValue) {
					v = child;
					s.hValue = v.hValue;
				} else {

				}

				alpha = Math.max(alpha, v.hValue);

				if (beta <= alpha) {
					break;
				}
			} else {
				return v;
			}

		}

		return v;

	}

	/**
	 * 
	 * @param s
	 * @param alpha
	 * @param beta
	 * @param limit
	 * @return returns utility value for minimizing player
	 */
	public Node minValue(Node s, int alpha, int beta, int limit) {
		Evaluator eval = new Evaluator(root);
		if (limit == 0) {
			boolean kings = false;
			// heuristic switch over to start making territory
			if (turnCount > 15)
				kings = true;
			s.hValue = eval.minDist(s, kings);
			return s;
		}

		Node v = new Node(null, null, Integer.MAX_VALUE);
		for (Node child : s.generateChildren()) {

			if (timeLeft()) {
				if (maxValue(child, alpha, beta, limit - 1).hValue < v.hValue) {
					v = child;
					s.hValue = v.hValue;
				} else {

				}
				beta = Math.min(beta, v.hValue);

				if (beta <= alpha) {
					break;
				}
			} else {
				return v;
			}

		}
		return v;

	}

	// checks if there is time left
	public boolean timeLeft() {
		// adjust time limit
		int timeLimit = 15;
		long startTime = this.start;
		long now = System.currentTimeMillis();
		if ((now - startTime) / 1000 < timeLimit)
			return true;
		else {
			System.out.println("Times up. Time: " + (now - startTime) / 1000.0);
			return false;
		}

	}

}
