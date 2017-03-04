package ygraphs.ai.smart_fox.games;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
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
	public String colour;
	int turnCount;
	public GameMove bestMove;
	int limit;
	long startTime;

	public Jose(String name, String password) {
		startTime = System.currentTimeMillis();
		turnCount = 0;
		this.usrName = name;

		setupGUI();
		guiFrame.addWindowListener(new WindowAdapter() {
			  public void windowClosing(WindowEvent e) {
				  System.exit(0);
			  }
			});
		connectToServer(name, password); // server connection
	}

	/**
	 * Sends the best move to the server
	 * 
	 * @param move
	 *            best move picked
	 */
	public void aiMove(GameMove move) {
		int[] current = { move.x, move.y };
		int[] newPos = { move.newX, move.newY };
		int[] arrowPos = { move.arrowX, move.arrowY };

		this.gameClient.sendMoveMessage(current, newPos, arrowPos);
	}

	public int minDistance(Node s) {
		// placeholder function for eval
		return 0;
	}

	
	public boolean timeLeft(){
		if(startTime - System.currentTimeMillis() < 29000){
			return true;
		}
		else{
			return false;
		}
	}
	/**
	 * iterative deepening
	 */
	
	public void ID() {
		System.out.println("started ID.");
		int depth = 1;
		this.colour = "white";
		bestMove = alphaBeta(new Node(this.board.getState(), this.colour), depth);
		System.out.println(bestMove.toString());
		aiMove(bestMove);
	}

	/**
	 * 
	 * @param s 	starting node being evaluated
	 * @param limit	depth to search at
	 * @return		best move from a-B pruning
	 */
	public GameMove alphaBeta(Node s, int limit) { // returns an action
		maxValue(s, Integer.MIN_VALUE, Integer.MAX_VALUE, limit);
		return s.getMove();
	}

	/**
	 * 
	 * @return max value of a node, based on alpha-beta
	 */
	int bestScore;

	public int maxValue(Node s, int alpha, int beta, int limit) {
		if (limit == 0){
			s.hValue = minDistance(s);
			return s.hValue;
		}
			
		int v = Integer.MIN_VALUE;
		limit--;
		for (Node child : s.getChildren()) {
			v = Math.max(v, minValue(child, alpha, beta, limit));
			if (v >= alpha) {
				bestMove = child.getMove();
				return v;
			}
			alpha = Math.max(alpha, v);
			bestMove = child.getMove();
			return v;
		}
		return -1;
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
		if (limit == 0)
			return minDistance(s);
		int v = Integer.MAX_VALUE;
		limit--;
		for (Node child : s.getChildren()) {
			v = Math.min(v, maxValue(child, alpha, beta, limit));
			if (v <= alpha) {
				bestMove = child.getMove();
				return v;
			}
			beta = Math.min(beta, v);
			bestMove = child.getMove();
			return v;
		}
		return -1;
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
		System.out.println("Logged in");
	}

	/**
	 * server starts timer the same time it calls this method
	 * 
	 * @param messageType
	 *            - can be start or move
	 * @param msgDetails
	 *            - contains whose turn it is
	 */
	@Override
	public boolean handleGameMessage(String messageType, Map<String, Object> msgDetails) {
		System.out.println("handle game message called: " + messageType);
		if (messageType.equals(GameMessage.GAME_ACTION_START)) {
			if (((String) msgDetails.get("player-black")).equals(this.userName())) {
				System.out.println("Game State: " + msgDetails.get("player-black"));
				this.colour = "black";
				ID();
			} else {
				this.colour = "white";
			}

		} else if (messageType.equals(GameMessage.GAME_ACTION_MOVE)) {
			handleOpponentMove(msgDetails);
			ID();
		}
		return true;
	}

	/**
	 * 
	 * @param msgDetails
	 *            - contains information from the server
	 */

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

	private void connectToServer(String name, String passwd) {
		// create a client and use "this" class (a GamePlayer) as the delegate.
		// client takes care of the communication with the server.
		gameClient = new GameClient(name, passwd, this);
	}

	@Override
	public String userName() {
		return usrName;
	}

	public static void main(String[] args)  {
		
		Jose jose = new Jose("mack", "pass");
	
		
	}
	



}
