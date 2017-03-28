package AI;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ygraphs.ai.smart_fox.GameMessage;
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
	public long startTime;

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
		System.out.println(colour + "'s MOVE");
		Node n = new Node(board.getState(), colour);
		StateSpace s = new StateSpace(n, turns, startTime);
		System.out.println("Starting search.");
		s.search();
		GameMove bestMove = s.best.getMove();
		board.getState().positionMarked(bestMove);
		int[][] p = { { bestMove.row, bestMove.col }, { bestMove.newRow, bestMove.newCol },
				{ bestMove.arrowRow, bestMove.arrowCol } };
		p = convertCoords(p, true);
		System.out.println("Jose moving Queen at " + "[" + p[0][0] + ", " + p[0][1] + "]");
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
		int roomNum =5;
		// once logged in, the gameClient will have the names of available game
		// rooms
		ArrayList<String> rooms = gameClient.getRoomList();
		System.out.println(rooms.toString());
		this.gameClient.joinRoom(rooms.get(roomNum));
		// 6 and 7 do NOT call handle game message OYAMA LAKE AND WOOD LAKE
		// jk maybe they work
		System.out.println(this.usrName + " successfully joined " + rooms.get(roomNum) + ".");
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
		// System.out.println("Game Message Called: " + messageType.toString());
		if (messageType.equals(GameMessage.GAME_ACTION_START)) {
			System.out.println("GAME STARTING.");
			if (((String) msgDetails.get("player-black")).equals(this.userName())) {
				System.out.println(msgDetails.get("player-black") + " playing as black.");
				System.out.println(msgDetails.get("player-white") + " playing as white.");
				this.colour = "black";
			} else {
				startTime = System.currentTimeMillis();
				System.out.println(msgDetails.get("player-white") + " playing as white.");
				System.out.println(msgDetails.get("player-black") + " playing as black.");
				this.colour = "white";
				System.out.println("Jose's Move.");
				// make a move
				System.out.println("Turn " + turnCount + ".");

				makeMove(turnCount += 2);

			}

		} else if (messageType.equals(GameMessage.GAME_ACTION_MOVE)) {
			startTime = System.currentTimeMillis();
			handleOpponentMove(msgDetails);
			turnCount++;
			System.out.println("Jose's Move.");
			// make a move
			System.out.println("Turn " + turnCount + ".");

			makeMove(turnCount++);
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
		System.out.println("Opponent moved Queen at " + msgDetails.get(AmazonsGameMessage.QUEEN_POS_CURR));
		ArrayList<Integer> qcurr = (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.QUEEN_POS_CURR);
		ArrayList<Integer> qnew = (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.Queen_POS_NEXT);
		ArrayList<Integer> arrow = (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.ARROW_POS);
		// System.out.println("QCurr: " + qcurr);
		// System.out.println("QNew: " + qnew);
		// System.out.println("Arrow: " + arrow);
		// should call convertCoords
		int[][] p = { { qcurr.get(0), qcurr.get(1) }, { qnew.get(0), qnew.get(1) }, { arrow.get(0), arrow.get(1) } };
		p = convertCoords(p, false);
		if (!(board.getState().getBoard()[p[0][0]][p[0][1]].equals("white")
				|| board.getState().getBoard()[p[0][0]][p[0][1]].equals("black"))) {
			System.out.println("WAS NOT A QUEEN");

		}
		board.markPosition(new GameMove(p[0][0], p[0][1], p[1][0], p[1][1], p[2][0], p[2][1]));
		// System.out.println(board.getState().toString());

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
		guiFrame.setSize(700, 575);
		guiFrame.setIconImage(ImageIO.read(new File("resources/icon.png")));
		guiFrame.setTitle("Game of the Amazons - Jose Cuervo");
		guiFrame.setLocation(350, 150);
		guiFrame.setVisible(true);
		guiFrame.setLayout(null);
		// set up main container
		Container background = new JLabel(new ImageIcon(ImageIO.read(new File("resources/jungleBg.jpg"))));
		guiFrame.setContentPane(background);
		background.setLayout(new BorderLayout());

		board = createGameBoard();
		board.setOpaque(false);

		background.add(board, BorderLayout.CENTER);
		background.setVisible(true);
		// add button to calculate AI move
		JButton btn = new JButton();
		JPanel pnl = new JPanel();
		JButton btn2 = new JButton();
		btn2.setText("Reset Player Move");
		btn2.setVisible(true);

		btn.setAlignmentY(Component.CENTER_ALIGNMENT);
		pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS));
		pnl.add(btn);
		pnl.setOpaque(false);

		btn.setVisible(true);
		btn.setText("Jose Move");
		background.add(pnl, BorderLayout.EAST);

		pnl.add(btn2);
		btn2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				board.ev.counter = 0;
			}
		});
		btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// Handles the calculate move button for the AI
				Node n = new Node(board.getState(), colour);
				startTime = System.currentTimeMillis();
				StateSpace s = new StateSpace(n, ++turnCount, startTime);
				System.out.println("Starting search.");
				s.search();
				GameMove bestMove = s.best.getMove();
				board.markPosition(bestMove);

				int[][] p = { { bestMove.row, bestMove.col }, { bestMove.newRow, bestMove.newCol },
						{ bestMove.arrowRow, bestMove.arrowCol } };
				// convert to 1-10 xy to send to server
				p = convertCoords(p, true);
				client.sendMoveMessage(p[0], p[1], p[2]);
				System.out.println(turnCount++);
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
			// form
			// rc 0-9 to rc 1-10
			qFormer1 = 10 - positions[0][0];
			qFormer2 = positions[0][1] + 1;
			qNew1 = 10 - positions[1][0];
			qNew2 = positions[1][1] + 1;
			a1 = 10 - positions[2][0];
			a2 = positions[2][1] + 1;

		} else {
			// xy to rc
			qFormer1 = 10 - positions[0][0];
			qFormer2 = positions[0][1] - 1;
			qNew1 = 10 - positions[1][0];
			qNew2 = positions[1][1] - 1;
			a1 = 10 - positions[2][0];
			a2 = positions[2][1] - 1;
		}
		int[][] newCoords = { { qFormer1, qFormer2 }, { qNew1, qNew2 }, { a1, a2 } };
		return newCoords;
	}

	// end of GameBoard

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		// uncomment second Amazon for the ai to play against itself
		Jose game = new Jose("Jose", "pass123");
		Jose game2 = new Jose("JoseB", "pass123");

	}

}// end of Amazon
