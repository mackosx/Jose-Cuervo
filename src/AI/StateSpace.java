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
		alphaBeta();		
		if (root.getChildren().size() <= 0) {
			System.out.println("Game Over.\n" + root.opposite.toUpperCase() + "' WINS!");
			System.out.println(root.state().toString());
			System.exit(1);
		}		
		State st = new State(root.state().getBoard());
		System.out.println(st.result(root.state(), best.getMove()).toString());
	}

	public void search() {
		// right now is static level generation
		// TODO: could convert to iterative deepening
		// yolo static
		//add();
//		if (turnCount > 70) {
//			for (int i = 0; i < 3; i++) {
//				add();
//				clean();
//			}
//			add();
//			}else if (turnCount > 50) {
//			for (int i = 0; i < 2; i++) {
//				clean();
//				add();
//			}
//			}
//			else if (turnCount > 25) {
//			clean();
//			add();
//
//		}

		System.out.println("Starting Alpha Beta Pruning/Minimax.");
		startAlphaBeta();
		System.out.println("Done searching.");

	}

	/**
	 * removes nodes whose value is less than average
	 */
	

	public int getDepth() {
		int d = 0;
		Node s = root;
		while (s != null) {
			if (!s.getChildren().isEmpty()) {
				s = s.getChildren().get(0);

			} else {
				break;
			}
			d++;


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
	public void alphaBeta() { // returns an action
		// getDepth();
		depth = 1;
		Node tempBest = null;
		do{
			best = tempBest;
			System.out.println("Now searching depth = " + depth + "...");
			tempBest = maxValue(root, Integer.MIN_VALUE, Integer.MAX_VALUE, depth++);
			System.out.println("Best move so far:\n" + tempBest.toString() + " \nVALUE:" + tempBest.hValue);
			//best = tempBest;
		}while(timeLeft());
		
	}

	/**
	 * 
	 * @return max value of a node, based on alpha-beta
	 */
	int bestScore;

	public Node maxValue(Node s, int alpha, int beta, int limit) {
		Evaluator eval = new Evaluator(root);
		//System.out.println("new evaluator for " + root.getType());
		if (limit == 0) {
			// TODO: use king moves or queen moves based on turn count
//			if (turnCount > 15) {
//				s.hValue = eval.newMinDist(s, true);
//			} else {
				s.hValue = eval.minDist(s, false);
//			}
				//System.out.println("Evaluation: " + s.hValue);
			return s;

		}

		Node v = new Node(null,null, Integer.MIN_VALUE);
		for (Node child : s.generateChildren()) {
			if (timeLeft()) {
				Node min = minValue(child, alpha, beta, limit - 1);
				if(min.hValue > v.hValue){
					v = child;
					//best = child;
				}else{
					
				}
				
				alpha = Math.max(alpha, v.hValue);

				if (beta <= alpha) {
					break;
				}
			} else {
				//s.hValue = v.hValue;
				return v;
			}

		}
		//s.hValue = v.hValue;
		//best = v;
		//System.out.println("Max of Evaluations: " + v);
		//best = v;
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
		//System.out.println("new evaluator for " + root.getType());

		if (limit == 0) {
			// TODO: use king moves or queen moves based on turn count
//			if (turnCount > 10) {
//				s.hValue = eval.newMinDist(s, true);
//			} else {
				s.hValue = eval.minDist(s, false);
//			}
				//System.out.println("Evaluation: " + s.hValue);
			return s;
		}

		Node v = new Node(null, null, Integer.MAX_VALUE);
		for (Node child : s.generateChildren()) {

			if (timeLeft()) {
				if(maxValue(child, alpha, beta, limit - 1).hValue < v.hValue){
					v = child;
				}else{
					
				}
				beta = Math.min(beta, v.hValue);

				if (beta <= alpha) {
					break;
				}
			} else {
				//s.hValue = v.hValue;
				return v;
			}

		}
		//s.hValue = v.hValue;
//		System.out.println("Min of evaluations: " + v);
		//best = v;
		return v;

	}

	public boolean timeLeft() {
		long startTime = this.start;
		long now = System.currentTimeMillis();
		if ((now - startTime) / 1000 < 7)
			return true;
		else {
			System.out.println("Times up.");
			return false;
		}

	}

}
