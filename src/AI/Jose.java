package AI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ygraphs.ai.smart_fox.GameMessage;
//import ygraphs.ai.smart_fox.games.Amazon;
import ygraphs.ai.smart_fox.games.AmazonsGameMessage;
import ygraphs.ai.smart_fox.games.GameClient;
import ygraphs.ai.smart_fox.games.GamePlayer;

/**
 * 
 * 
 * @author mackosx
 */
public class Jose extends GamePlayer {

	private GameClient gameClient;
	private JFrame guiFrame = null;
	private GameBoard board = null;
	public String usrName = null;
	public String colour = "white";
	public int turnCount = 0;

	/**
	 * Sets up server connection with name and pass, starts gui
	 * 
	 * @param name
	 * @param passwd
	 * @throws IOException
	 */
	public Jose(String name, String passwd) {
		connectToServer(name, passwd);

		this.usrName = name;
		try {
			setupGUI(gameClient);
			guiFrame.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					System.exit(0);
				}
			});
		} catch (Exception e) {
			System.out.println(e);
		}
		guiFrame.repaint();
	}

	/**
	 * Makes a move for the AI
	 */
	public void makeMove(int turns) {
		System.out.println("initializing state.");
		Node n = new Node(board.getState(), colour);
		StateSpace s = new StateSpace(n, turns);
		System.out.println("Starting search.");
		s.search();
		board.getState().positionMarked(s.bestMove);
		int[][] p = { { s.bestMove.row, s.bestMove.col }, { s.bestMove.newRow, s.bestMove.newCol },
				{ s.bestMove.arrowRow, s.bestMove.arrowCol } };
		p = convertCoords(p, true);
		this.gameClient.sendMoveMessage(p[0], p[1], p[2]);

		guiFrame.repaint();
	}

	/**
	 * create a client and use "this" class (a GamePlayer) as the delegate. the
	 * client will take care of the communication with the server.
	 * 
	 * @param name
	 *            User name to connect with
	 * @param passwd
	 *            Password to connect with
	 */
	private void connectToServer(String name, String passwd) {
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
		// 6 and 7 do NOT call handle game message OYAMA LAKE AND WOOD LAKE
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
			} else {
				this.colour = "white";
				System.out.println("Jose's Move.");
				makeMove(++turnCount);

			}

		} else if (messageType.equals(GameMessage.GAME_ACTION_MOVE)) {
			handleOpponentMove(msgDetails);
			turnCount++;
			System.out.println("Jose's Move.");
			makeMove(++turnCount);
		}
		return true;
	}

	/**
	 * Gets informations for an opponents move and marks it for the player
	 * 
	 * @param msgDetails
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
		// should call convertCoords
		int[][] p = { { qcurr.get(0), qcurr.get(1) }, { qnew.get(0), qnew.get(1) }, { arrow.get(0), arrow.get(1) } };
		p = convertCoords(p, false);

		board.markPosition(new GameMove(p[0][0], p[0][1], p[1][0], p[1][1], p[2][0], p[2][1]));
		System.out.println(board.getState().toString());

	}

	/**
	 * Make a gui for human viewing
	 * 
	 * @param client
	 *            GameClient to handle messages
	 * @throws IOException
	 */
	private void setupGUI(GameClient client) throws IOException {
		// setup fram
		guiFrame = new JFrame();
		guiFrame.setSize(700, 600);
		guiFrame.setTitle("Game of the Amazons (COSC 322, UBCO)");
		guiFrame.setLocation(200, 200);
		guiFrame.setVisible(true);
		guiFrame.setLayout(null);
		// set up main container
		Container background = new JLabel(
				new ImageIcon(ImageIO.read(new File("resources/jungle_by_ego_trap_graphics.jpg"))));
		guiFrame.setContentPane(background);
		background.setLayout(new BorderLayout());
		background.setSize(700, 600);
		//background.add(Box.createVerticalGlue());
		// background.add(background);
		board = createGameBoard();
		board.setOpaque(false);

		background.add(board, BorderLayout.CENTER);
		background.setVisible(true);
		// add button to calculate AI move
		JButton btn = new JButton();
		JPanel pnl = new JPanel();
		btn.setSize(20, 20);
		btn.setAlignmentX(Component.CENTER_ALIGNMENT);
		btn.setAlignmentY(Component.CENTER_ALIGNMENT);
		pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS));
		pnl.add(btn);
		pnl.setOpaque(false);

		btn.setVisible(true);
		btn.setText("Jose Move");
		background.add(pnl, BorderLayout.EAST);
		btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// Handles the calculate move button for the AI
				System.out.println("initializing state.");
				Node n = new Node(board.getState(), colour);
				StateSpace s = new StateSpace(n, ++turnCount);
				System.out.println("Starting search.");
				s.search();
				board.markPosition(s.bestMove);

				int[][] p = { { s.bestMove.row, s.bestMove.col }, { s.bestMove.newRow, s.bestMove.newCol },
						{ s.bestMove.arrowRow, s.bestMove.arrowCol } };
				// convert to 1-10 xy to send to server
				p = convertCoords(p, true);
				client.sendMoveMessage(p[0], p[1], p[2]);
			}

		});
		guiFrame.repaint();

	}

	private GameBoard createGameBoard() {
		return new GameBoard(this);
	}

	public boolean handleMessage(String msg) {
		System.out.println("Time Out ------ " + msg);
		return true;
	}

	@Override
	public String userName() {
		return usrName;
	}

	/**
	 * 
	 * @param positions
	 *            2d array of the move coordinates in order: qOld1, qOld2,
	 *            qNew1, qNew2, aNew1, aNew2
	 * @param toServerMessage
	 *            if true: coordinates convert to 1-10 xy format if false:
	 *            coordinates convert to row/column basic 2d array format
	 * @return proper coordinates
	 */
	private int[][] convertCoords(int[][] positions, boolean toServerMessage) {
		int qFormer1, qFormer2, qNew1, qNew2, a1, a2;
		if (toServerMessage) {
			// TODO: implement conversion from 1-10 Cartesian to our 0-9 array
			// form
			// rc to xy
			qFormer1 = positions[0][1] + 1;
			qFormer2 = 10 - positions[0][0];
			qNew1 = positions[1][1] + 1;
			qNew2 = 10 - positions[1][0];
			a1 = positions[2][1] + 1;
			a2 = 10 - positions[2][0];

		} else {
			// TODO: implement conversion form 0-9 array to 1-10 Cartesian
			// xy to rc
			qFormer1 = 10 - positions[0][1];
			qFormer2 = positions[0][0] - 1;
			qNew1 = 10 - positions[1][1];
			qNew2 = positions[1][0] - 1;
			a1 = 10 - positions[2][1];
			a2 = positions[2][0] - 1;
		}
		int[][] newCoords = { { qFormer1, qFormer2 }, { qNew1, qNew2 }, { a1, a2 } };
		return newCoords;
	}

	// end of GameBoard

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		// uncomment second Amazon for the ai to play against itself
		// Amazon game = new Amazon("JoseW", "cosc322");
		Jose game2 = new Jose("JoseB", "cosc");

	}

}// end of Amazon
