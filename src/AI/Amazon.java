package AI;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import AI.GameBoard;
import ygraphs.ai.smart_fox.GameMessage;
//import ygraphs.ai.smart_fox.games.Amazon;
import ygraphs.ai.smart_fox.games.AmazonsGameMessage;
import ygraphs.ai.smart_fox.games.GameClient;
import ygraphs.ai.smart_fox.games.GamePlayer;

/**
 * For testing and demo purposes only. An GUI Amazon client for human players
 * 
 * @author yong.gao@ubc.ca
 */
public class Amazon extends GamePlayer {

	private GameClient gameClient;
	private JFrame guiFrame = null;
	private GameBoard board = null;
	private boolean gameStarted = false;
	public String usrName = null;
	public String colour = "white";

	/**
	 * Constructor
	 * 
	 * @param name
	 * @param passwd
	 */
	public Amazon(String name, String passwd) {
		connectToServer(name, passwd);

		this.usrName = name;
		setupGUI(gameClient);
		guiFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}
	public void makeMove(){
		System.out.println("initializing state.");
		Node n = new Node(board.getState(), colour);
		StateSpace s = new StateSpace(n);
		System.out.println("Starting search.");
		s.search();
		board.getState().positionMarked(s.bestMove);
		//int[][] properLocations = convertCoords(s.bestMove, false);
		int[] qf = {s.bestMove.row, s.bestMove.col};
		int[] qn = {s.bestMove.newRow, s.bestMove.newCol};
		int[] a = {s.bestMove.arrowRow, s.bestMove.arrowCol};
		this.gameClient.sendMoveMessage(qf, qn, a);

		guiFrame.repaint();
	}

	private void connectToServer(String name, String passwd) {
		// create a client and use "this" class (a GamePlayer) as the delegate.
		// the client will take care of the communication with the server.
		gameClient = new GameClient(name, passwd, this);
	}

	@Override
	/**
	 * Implements the abstract method defined in GamePlayer. Will be invoked by
	 * the GameClient when the server says the login is successful
	 */
	public void onLogin() {

		// once logged in, the gameClient will have the names of available game
		// rooms
		ArrayList<String> rooms = gameClient.getRoomList();
		System.out.println(rooms.toString());
		this.gameClient.joinRoom(rooms.get(5));
		//6 and 7 do NOT call handle game message OYAMA LAKE AND WOOD LAKE
		System.out.println("Player successfully joined room.");
	}

	/**
	 * Implements the abstract method defined in GamePlayer. Once the user joins
	 * a room, all the game-related messages will be forwarded to this method by
	 * the GameClient.
	 * 
	 * See GameMessage.java
	 * 
	 * @param messageType
	 *            - the type of the message
	 * @param msgDetails
	 *            - A HashMap info and data about a game action
	 */
	public boolean handleGameMessage(String messageType, Map<String, Object> msgDetails) {
		System.out.println("Handle Game Message Called: " + messageType.toString());
		if (messageType.equals(GameMessage.GAME_ACTION_START)) {

			if (((String) msgDetails.get("player-black")).equals(this.userName())) {
				System.out.println("Game State: " + msgDetails.get("player-black"));
				this.colour = "black";
				makeMove();
			}
			else{
				this.colour = "white";
			}

		} else if (messageType.equals(GameMessage.GAME_ACTION_MOVE)) {
			handleOpponentMove(msgDetails);
			makeMove();
		}
		return true;
	}

	// handle the event that the opponent makes a move.
	@SuppressWarnings("unchecked")
	private void handleOpponentMove(Map<String, Object> msgDetails) {
		System.out.println("OpponentMove(): " + msgDetails.get(AmazonsGameMessage.QUEEN_POS_CURR));
		ArrayList<Integer> qcurr = (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.QUEEN_POS_CURR);
		ArrayList<Integer> qnew = (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.Queen_POS_NEXT);
		ArrayList<Integer> arrow = (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.ARROW_POS);
		System.out.println("QCurr: " + qcurr);
		System.out.println("QNew: " + qnew);
		System.out.println("Arrow: " + arrow);

		board.markPosition(qnew.get(0), qnew.get(1), arrow.get(0), arrow.get(1), qcurr.get(0), qcurr.get(1), true);

	}

	/**
	 * handle a move made by this player --- send the info to the server.
	 * 
	 * @param x
	 *            queen row index
	 * @param y
	 *            queen col index
	 * @param arow
	 *            arrow row index
	 * @param acol
	 *            arrow col index
	 * @param qfr
	 *            queen original row
	 * @param qfc
	 *            queen original col
	 */
	public void playerMove(int x, int y, int arow, int acol, int qfr, int qfc) {

		int[] qf = new int[2];
		qf[0] = qfr;
		qf[1] = qfc;

		int[] qn = new int[2];
		qn[0] = x;
		qn[1] = y;

		int[] ar = new int[2];
		ar[0] = arow;
		ar[1] = acol;

		// To send a move message, call this method with the required data
		this.gameClient.sendMoveMessage(qf, qn, ar);

	}

	// set up the game board
	private void setupGUI(GameClient client) {
		guiFrame = new JFrame();

		guiFrame.setSize(800, 600);
		
		guiFrame.setTitle("Game of the Amazons (COSC 322, UBCO)");

		guiFrame.setLocation(200, 200);
		guiFrame.setVisible(true);
		guiFrame.setLayout(null);

		Container contentPane = guiFrame.getContentPane();
		contentPane.setLayout(new BorderLayout());

		contentPane.add(Box.createVerticalGlue());

		board = createGameBoard();
		contentPane.add(board, BorderLayout.CENTER);
		JButton btn = new JButton();
		contentPane.add(btn, BorderLayout.EAST);
		btn.setSize(20, 20);
		btn.setLocation(750, 100);
		btn.setVisible(true);
		btn.setText("Calculate Best Move");
		btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("initializing state.");
				Node n = new Node(board.getState(), colour);
				StateSpace s = new StateSpace(n);
				System.out.println("Starting search.");
				s.search();
				board.getState().positionMarked(s.bestMove);
				//int[][] properLocations = convertCoords(s.bestMove, false);
				int[] qf = {s.bestMove.row, s.bestMove.col};
				int[] qn = {s.bestMove.newRow, s.bestMove.newCol};
				int[] a = {s.bestMove.arrowRow, s.bestMove.arrowCol};
				client.sendMoveMessage(qf, qn, a);

				guiFrame.repaint();

			}

			private int[][] convertCoords(GameMove bestMove, boolean opponent) {
				if(opponent){
					//TODO: implement conversion from 1-10 Cartesian to our 0-9 array form
				}
				else{
					//TODO: implement conversion form 0-9 array to 1-10 Cartesian
				}
				return null;
			}

		});
		guiFrame.repaint();

	}

	private GameBoard createGameBoard() {
		GameBoard g = new GameBoard(this);
		return g;
	}

	public boolean handleMessage(String msg) {
		System.out.println("Time Out ------ " + msg);
		return true;
	}

	@Override
	public String userName() {
		return usrName;
	}

// end of GameBoard

	/**
	 * Constructor
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Amazon game = new Amazon("yong.gao", "cosc322");
		Amazon game2 = new Amazon("play2", "cosc");

	}

}// end of Amazon
