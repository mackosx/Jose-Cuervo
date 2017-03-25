package AI;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Random;

public class StateSpace {
	private Node root = null;
	ArrayList<Node> frontier = new ArrayList<Node>();;
	int depth = 0;
	GameMove bestMove;
	int turnCount;
	long start;
	String currentTurn;

	public StateSpace(Node r, int turns, long start) {
		this.start = start;
		root = r;
		turnCount = turns;
	}

	public void startAlphaBeta() {
		getDepth();
		System.out.println("ALPHA BETA DEPTH: " + depth);
		root.hValue = alphaBeta();
		ArrayList<Node> bestMoves = new ArrayList<Node>();
		int max = Integer.MIN_VALUE;
		for (Node s : root.getChildren()) {
			if (s.hValue >= max) {
				max = s.hValue;

			}
		}
		for (Node s : root.getChildren()) {
			if (s.hValue >= max) {
				bestMoves.add(s);
			}
		}
		if (root.getChildren().size() <= 0) {
			System.out.println("Game Over.\n" + root.opposite.toUpperCase() + "' WINS!");
			System.out.println(root.state().toString());
			System.exit(1);
		}
		Random r = new Random();
		int randomIndex = r.nextInt(bestMoves.size());
		bestMove = bestMoves.get(randomIndex).getMove();
		State st = new State(root.state().getBoard());
		System.out.println(st.result(root.state(), bestMove).toString());
	}

	public void search() {
		// right now is static level generation
		// TODO: could convert to iterative deepening
		// yolo static
		add();
		if (turnCount > 70) {
			for (int i = 0; i < 4; i++) {
				add();
				clean();
			}
			add();
		} else if (turnCount > 40) {
			for (int i = 0; i < 2; i++) {
				clean();
				add();
			}
		} else if (turnCount > 20) {
			clean();
			add();

		}

		System.out.println("Starting Alpha Beta Pruning/Minimax.");
		startAlphaBeta();
		System.out.println("Done searching.");

	}

	/**
	 * removes nodes whose value is less than average
	 */
	public void clean() {
		int avg = 0;
		for (Node s : frontier) {
			avg += s.hValue;

		}
		if (frontier.size() != 0)
			avg /= frontier.size();
		ArrayList<Node> toBeRemoved = new ArrayList<Node>();
		for (Node s : frontier) {
			if (s.hValue < avg) {
				toBeRemoved.add(s);
			}
		}

		LinkedHashSet<Node> map = new LinkedHashSet<Node>(frontier);
		map.removeAll(toBeRemoved);
		frontier = new ArrayList<Node>(map);
	}

	public void add() {
		getDepth();
		// initialize new frontier to old size
		ArrayList<Node> nextLevel = new ArrayList<Node>(frontier.size());
		Node temp = null;
		if (depth != 0) {
			for (Node n : frontier) {
				temp = n;
				nextLevel.addAll(temp.generateChildren());

			}

		} else {
			nextLevel.addAll(root.generateChildren());

		}

		frontier.clear();
		frontier.addAll(nextLevel);
		System.out.println(depth);
		depth++;

	}

	public int getDepth() {
		int d = 0;
		Node s = root;
		while (s != null) {
			System.out.println(s.getType());
			if (!s.getChildren().isEmpty()) {
				s = s.getChildren().get(0);
				d++;

			} else {
				break;
			}

		}
		depth = d;
		return depth;

	}

	/**
	 * 
	 * @param s
	 *            starting node being evaluated
	 * @param limit
	 *            depth to search at
	 * @return best move from a-B pruning
	 */
	public int alphaBeta() { // returns an action
		// getDepth();
		return maxValue(root, Integer.MIN_VALUE, Integer.MAX_VALUE, depth);
	}

	/**
	 * 
	 * @return max value of a node, based on alpha-beta
	 */
	int bestScore;

	public int maxValue(Node s, int alpha, int beta, int limit) {
		Evaluator eval = new Evaluator(root);
		if (limit == 0 || s.getChildren().size()==0) {
			// TODO: use king moves or queen moves based on turn count
			if (turnCount > 15) {
				s.hValue = eval.newMinDist(s, true);
			} else {
				s.hValue = eval.newMinDist(s, false);
			}
			return s.hValue;

		}

		int v = Integer.MIN_VALUE;
		for (Node child : s.getChildren()) {
			if (timeLeft()) {
				v = Math.max(v, minValue(child, alpha, beta, limit - 1));
				alpha = Math.max(alpha, v);

				if (alpha >= beta) {
					break;
				}
			}

			else {
				s.hValue = v;
				return v;
			}

		}
		s.hValue = v;
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
	public int minValue(Node s, int alpha, int beta, int limit) {
		Evaluator eval = new Evaluator(root);
		if (limit == 0|| s.getChildren().size() == 0) {
			// TODO: use king moves or queen moves based on turn count
			if (turnCount > 10) {
				s.hValue = eval.newMinDist(s, true);
			} else {
				s.hValue = eval.newMinDist(s, false);
			}
			return s.hValue;
		}

		int v = Integer.MAX_VALUE;
		for (Node child : s.getChildren()) {
			if (timeLeft()) {
				v = Math.min(v, maxValue(child, alpha, beta, limit - 1));
				beta = Math.min(beta, v);

				if (beta <= alpha) {
					break;
				}
			} else {
				s.hValue = v;
				return v;
			}

		}
		s.hValue = v;
		return v;

	}

	public boolean timeLeft() {
		long startTime = this.start;
		long now = System.currentTimeMillis();
		if ((now - startTime) / 1000 < 25)
			return true;
		else {
			System.out.println("Times up.");
			return false;
		}

	}

}
