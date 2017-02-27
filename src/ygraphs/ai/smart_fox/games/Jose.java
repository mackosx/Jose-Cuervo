package ygraphs.ai.smart_fox.games;

import java.awt.BorderLayout;
import java.awt.Container;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

import javax.swing.Box;
import javax.swing.JFrame;

import ygraphs.ai.smart_fox.GameMessage;
import ygraphs.ai.smart_fox.games.AmazonsGameMessage;
import ygraphs.ai.smart_fox.games.GameClient;
import ygraphs.ai.smart_fox.games.GamePlayer;

public class Jose extends GamePlayer {
	private GameClient gameClient;
	private JFrame guiFrame = null;
	private GameBoard board = null;
	private boolean gameStarted = false;
	public String usrName = null;
	int turnCount;

	public Jose(String name, String password) {
		turnCount = 0;

		this.usrName = name;
		setupGUI();

		connectToServer(name, passwd); // server connection
	}

	// depth limited search
	/*
	 * function IDDFS(root) for depth from 0 to inf found = DLS(root, depth) if
	 * found != null return found
	 * 
	 * function DLS(node, depth) if depth = 0 and node is a goal return node if
	 * depth > 0 foreach child of node found = DLS(child, depth - 1) if found !=
	 * null return found return null
	 */

	// Iterative Deepening based on number of turns
	LinkedList<GameMove> found = new LinkedList<GameMove>();

	public LinkedList<GameMove> IDDLS(GameMove root) {
		GameMove currentBest;
		double bestValue;
		for (int i = 0; i <= turnCount; i++) {
			for (GameMove pos : DLS(root, i)) {
				found.add(pos);
				// keeps track of best mindist eval
				if(evaluate(pos) > bestValue){
					currentBest = pos;
				}
			}
		}
		return found;
	}

	// Depth Limited Search
	public LinkedList<GameMove> DLS(GameMove curr, int depth) {
		if (depth == 0)
			return found;
		else {
			for (GameMove pos : curr.getMoves(curr, game, color)) {
				found.addAll(DLS(pos, depth - 1));
				if (!found.equals(null))
					return found;
			}
		}
		return null;

	}

	// create interface
	private void setupGUI() {
		guiFrame = new JFrame();

		guiFrame.setSize(800, 700);
		guiFrame.setTitle("Game of the Amazons (COSC 322, UBCO)");

		guiFrame.setLocation(200, 200);
		guiFrame.setVisible(true);
		guiFrame.repaint();
		guiFrame.setLayout(null);

		Container contentPane = guiFrame.getContentPane();
		contentPane.setLayout(new BorderLayout());

		contentPane.add(Box.createVerticalGlue());

		board = createGameBoard();
		contentPane.add(board, BorderLayout.CENTER);
	}

	private GameBoard createGameBoard() {
		return new GameBoard(this);
	}

	@Override
	public void onLogin() {
		ArrayList<String> rooms = gameClient.getRoomList();
		this.gameClient.joinRoom(rooms.get(6));
	}

	@Override
	public boolean handleGameMessage(String messageType, Map<String, Object> msgDetails) {
		if (messageType.equals(GameMessage.GAME_ACTION_START)) {

			if (((String) msgDetails.get("player-black")).equals(this.userName())) {
				System.out.println("Game State: " + msgDetails.get("player-black"));
			}

		} else if (messageType.equals(GameMessage.GAME_ACTION_MOVE)) {
			handleOpponentMove(msgDetails);
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	private void handleOpponentMove(Map<String, Object> msgDetails) {
		System.out.println("OpponentMove(): " + msgDetails.get(AmazonsGameMessage.QUEEN_POS_CURR));
		ArrayList<Integer> qcurr = (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.QUEEN_POS_CURR);
		ArrayList<Integer> qnew = (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.Queen_POS_NEXT);
		ArrayList<Integer> arrow = (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.ARROW_POS);
		System.out.println("QCurr: " + qcurr);
		System.out.println("QNew: " + qnew);
		System.out.println("Arrow: " + arrow);

		
		// mark position on gui (board)
		board.markPosition(qnew.get(0), qnew.get(1), arrow.get(0), arrow.get(1), qcurr.get(0), qcurr.get(1), true);

	}

	@Override
	public String userName() {
		return usrName;
	}
}
