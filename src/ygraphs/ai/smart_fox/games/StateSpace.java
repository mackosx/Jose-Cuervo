package ygraphs.ai.smart_fox.games;

import java.util.ArrayList;
import java.util.LinkedHashSet;

public class StateSpace {
	ArrayList<Node> frontier;
	Evaluator eval;

	public StateSpace() {
		eval = new Evaluator();
	}

	public void search() {
		State s = new State(10, 10);
		s.init(true);
		Node root = new Node(s, "white");
		frontier = new ArrayList<Node>(3100000);
		frontier.addAll(root.getChildren());
		int initialSize = root.validMoves.size();
		System.out.println("Cleaning...");
		clean();
		System.out.println("From " + initialSize + " nodes to " + frontier.size());
		System.out.println("Adding...");
		add();
		int current = frontier.size();
		System.out.println("Node generated: " + current);
		//System.out.println("Cleaning 2...");
		//clean();
		System.out.println("From " + current + " nodes to " + frontier.size());
	}

	/**
	 * removes nodes whose value is less than the average
	 */
	public void clean() {
		int avg = 0;
		System.out.println("Evaluation in progress...");
		for (Node s : frontier) {
			s.hValue = eval.minDistance(s);
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

		for (int i = 0; i < frontier.size(); i++) {
			nextLevel.addAll(frontier.get(i).getChildren());
			//System.out.println(nextLevel.size());
		}

		frontier.clear();
		frontier.addAll(nextLevel);
	}

}
