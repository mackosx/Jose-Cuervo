package AI;

import java.util.LinkedList;

import junit.framework.*;

@SuppressWarnings("unused")
public class Test extends TestCase{

//	public void testMoveGen(){
//		State state = new State(10, 10);
//		Node root = new Node(state, "white");
//		
//		state.setBoardLocation(0, 3, State.POS_MARKED_WHITE);
//		state.setBoardLocation(0, 6, State.POS_MARKED_WHITE);
//		state.setBoardLocation(2, 0, State.POS_MARKED_WHITE);
//		state.setBoardLocation(2, 9, State.POS_MARKED_WHITE);
//
//		state.setBoardLocation(7, 0, State.POS_MARKED_BLACK);
//		state.setBoardLocation(7, 9, State.POS_MARKED_BLACK);
//		state.setBoardLocation(9, 3, State.POS_MARKED_BLACK);
//		state.setBoardLocation(9, 6, State.POS_MARKED_BLACK);
//		LinkedList<Node> children = root.getChildren();
//		int count = 0;
//		for (Node item:children) {
//			System.out.println(item.getMove().toString());
//			System.out.println(item.state().toString());
//			System.out.println(++count);
//		}
//
//		System.out.println(children.size() + " thats a lot");
//
//
//
//	}
	public void test1(){
		long curr = System.currentTimeMillis();
		State st = new State(10, 10);
		st.init(true);
		Node n = new Node(st, "white");
		Evaluator e = new Evaluator();
		
		System.out.println(n.state().toString());
		System.out.println(e.newMinDist(n));
		System.out.println(e.str);
		System.out.println("Time: " + (System.currentTimeMillis() - curr) + "ms");
	}
//	public void test2 (){
//		Amazon p = new Amazon("mack", "pass");
//		long curr = System.currentTimeMillis();
//		
//	
//		System.out.println((System.currentTimeMillis() - curr)/1000.0);
//		
//		
//	}
	
}
