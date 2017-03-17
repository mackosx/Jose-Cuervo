package AI;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Random;

public class StateSpace {
	private Node root = null;
	ArrayList<Node> frontier = new ArrayList<Node>();;
	int depth = 0;
	GameMove bestMove;
	public StateSpace(Node r) {
		root = r;
	}

	public void makeMove(){
		System.out.println(++Jose.turnCount);
		
	}
	
	public void startAlphaBeta(){
		Evaluator eval = new Evaluator();
		System.out.println("AlphaBeta depth: " + depth);
		root.hValue = alphaBeta();
		ArrayList<GameMove> bestMoves = new ArrayList<GameMove>();
		int max = Integer.MIN_VALUE;
		for (Node s : root.getChildren()) {
			if (max <= s.hValue) {
				max = s.hValue;
				
			}
		}
		for (Node s : root.getChildren()) {
			if (max <= s.hValue) {
				bestMoves.add(s.getMove());
			}
		}
		System.out.println("Child nodes: " + root.getChildren().size());
		if(root.getChildren().size() <= 0){
			System.out.println("Game Over.");
			System.exit(1);
		}
		Random r = new Random();
		int randomIndex = r.nextInt(bestMoves.size());
		System.out.println("Index selected: " + randomIndex);
		bestMove = bestMoves.get(randomIndex);
		System.out.println("****BEST MOVE*****\nTotal moves found: " + bestMoves.size() + "\n" + bestMove.toString());
		System.out.println(State.result(root.state(), bestMove).toString());
		System.out.println(eval.newMinDist(new Node(State.result(root.state(), bestMove), "white")));
		makeMove();
	}
	public void search() {
		//right now is static level generation
		// could convert to iterative deepening
		System.out.println("Expanding nodes...");
		add();
		System.out.println("Done.");
		if (Jose.turnCount > 70) {
			for(int i = 0; i < 4; i++){
				add();
				clean();
			}
			add();
		} else if (Jose.turnCount >= 40){
			for(int i = 0; i < 2; i++){
				clean();
				add();
			}
		} else if (Jose.turnCount >= 20){
			clean();
			add();

		}
		
		
		startAlphaBeta();
		
	}

	/**
	 * removes nodes whose value is less than the average
	 */
	public void clean() {
		Evaluator eval = new Evaluator();
		int avg = 0;
		System.out.println("Evaluation in progress...");
		for (Node s : frontier) {
			s.hValue = eval.newMinDist(s);
			avg += s.hValue;

		}
		System.out.println("Done evaluations.");
		if (frontier.size() != 0)
			avg /= frontier.size();
		System.out.println(avg);
		System.out.println("Preparing for removal...");
		ArrayList<Node> toBeRemoved = new ArrayList<Node>();
		for (Node s : frontier) {
			if (s.hValue < avg) {
				toBeRemoved.add(s);
			}
		}
		System.out.println("Done removing.");

		LinkedHashSet<Node> map = new LinkedHashSet<Node>(frontier);
		map.removeAll(toBeRemoved);
		frontier = new ArrayList<Node>(map);
	}

	public void add() {
		// initialize new frontier to old size
		ArrayList<Node> nextLevel = new ArrayList<Node>(frontier.size());
		
		if (depth != 0) {
			for (int i = 0; i < frontier.size(); i++) {
				nextLevel.addAll(frontier.get(i).generateChildren());
				// System.out.println(nextLevel.size());
			}
		} else {
			nextLevel.addAll(root.getQueenMoves(root.state(), root.getType()));
		}

		frontier.clear();
		frontier.addAll(nextLevel);
		depth++;
	}

	public int getDepth() {
		int d = 0;
		Node s = root;
		while (s != null) {
			if (!s.getChildren().isEmpty()) {
				s = s.getChildren().get(0);
			} else
				break;
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
	public int alphaBeta() { // returns an action
		//getDepth();
		System.out.println("Calculated depth: " + depth);
		return maxValue(root, Integer.MIN_VALUE, Integer.MAX_VALUE, depth);
	}

	/**
	 * 
	 * @return max value of a node, based on alpha-beta
	 */
	int bestScore;

	public int maxValue(Node s, int alpha, int beta, int limit) {
		Evaluator eval = new Evaluator();
		if (limit == 0) {
			s.hValue = eval.newMinDist(s);
			return s.hValue;
		}

		int v = Integer.MIN_VALUE;
		for (Node child : s.getChildren()) {
			v = Math.max(v, minValue(child, alpha, beta, limit - 1));
			alpha = Math.max(alpha, v);
			if (beta <= alpha) {
				break;
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
		Evaluator eval = new Evaluator();
		if (limit == 0){
			s.hValue = eval.newMinDist(s);
			return s.hValue;
		}
			
		int v = Integer.MAX_VALUE;
		for (Node child : s.getChildren()) {
			v = Math.min(v, maxValue(child, alpha, beta, limit - 1));
			beta = Math.min(beta, v);
			if (beta <= alpha) {
				break;
			}
		}
		s.hValue = v;
		return v;

	}

}
