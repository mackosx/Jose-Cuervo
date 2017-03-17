package AI;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import ygraphs.ai.smart_fox.games.Amazon;
import ygraphs.ai.smart_fox.games.GamePlayer;


/**
 * @author mackosx
 */
public class GameBoard extends JPanel {
	private static final long serialVersionUID = 1L;
	private int rows = 10;
	private int cols = 10;

	int width = 500;
	int height = 500;
	int cellDim = width / 10;
	int offset = width / 20;

	int posX = -1;
	int posY = -1;

	int r = 0;
	int c = 0;

	GamePlayer game = null;
	private State gameModel = null;

	boolean playerAMove;

	
	public GameBoard(AI.Amazon amazon) {
		addMouseListener(new  GameEventHandler());
		this.game = amazon;
		gameModel = new State(this.rows, this.cols);
		init(true);
	}
	public GameBoard(Jose amazon) {


		this.game = amazon;
		gameModel = new State(this.rows, this.cols);
		init(true);
	}



	
	public State getState() {
		return gameModel;
	}

	public void init(boolean isPlayerA) {
		String tagB = null;
		String tagW = null;

		tagB = State.POS_MARKED_BLACK;
		tagW = State.POS_MARKED_WHITE;

		gameModel.setBoardLocation(0, 3, tagW);
		gameModel.setBoardLocation(0, 6, tagW);
		gameModel.setBoardLocation(2, 0, tagW);
		gameModel.setBoardLocation(2, 9, tagW);

		gameModel.setBoardLocation(7, 0, tagB);
		gameModel.setBoardLocation(7, 9, tagB);
		gameModel.setBoardLocation(9, 3, tagB);
		gameModel.setBoardLocation(9, 6, tagB);
	}

	/**
	 * repaint the part of the board
	 *
	 * @param qrow
	 *            queen row index
	 * @param qcol
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
	public boolean markPosition(int qrow, int qcol, int arow, int acol, int qfr, int qfc, boolean opponentMove) {

		System.out.println(qrow + ", " + qcol + ", " + arow + ", " + acol + ", " + qfr + ", " + qfc);

		boolean valid = gameModel.positionMarked(new GameMove(qfr, qfc, qrow, qcol, arow, acol));
		repaint();
		return valid;
	}

	// JCmoponent method
	protected void paintComponent(Graphics gg) {
		Graphics g = (Graphics2D) gg;

		for (int i = 0; i < rows + 1; i++) {
			g.drawLine(i * cellDim + offset, offset, i * cellDim + offset, rows * cellDim + offset);
			g.drawLine(offset, i * cellDim + offset, cols * cellDim + offset, i * cellDim + offset);
		}

		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {

				posX = c * cellDim + offset;
				posY = r * cellDim + offset;

				//posY = (9 - r) * cellDim + offset;

				if (gameModel.getBoard()[r][c].equalsIgnoreCase(State.POS_AVAILABLE)) {
					g.clearRect(posX + 1, posY + 1, 49, 49);
				}

				if (gameModel.getBoard()[r][c].equalsIgnoreCase(State.POS_MARKED_BLACK)) {
					g.clearRect(posX + 1, posY + 1, 49, 49);
					ImageObserver n = null;
					BufferedImage img = null;
					try {
						img = ImageIO.read(new File("resources/rsz_bq.png"));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					g.drawImage(img, posX + 5, posY + 5, n);
				} else if (gameModel.getBoard()[r][c].equalsIgnoreCase(State.POS_MARKED_ARROW)) {
					g.clearRect(posX + 1, posY + 1, 49, 49);
					//g.drawLine(posX, posY, posX + 50, posY + 50);
					//g.drawLine(posX, posY + 50, posX + 50, posY);
					ImageObserver n = null;
					BufferedImage img = null;
					try {
						img = ImageIO.read(new File("resources/rsz_arrow.png"));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					g.drawImage(img, posX + 5, posY + 5, n);
				} else if (gameModel.getBoard()[r][c].equalsIgnoreCase(State.POS_MARKED_WHITE)) {
					g.clearRect(posX + 1, posY + 1, 49, 49);
					ImageObserver n = null;
					BufferedImage img = null;
					try {
						img = ImageIO.read(new File("resources/rsz_wq.png"));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					g.drawImage(img, posX + 5, posY + 5, n);
				}
			}
		}

	}


	// JComponent method
	public Dimension getPreferredSize() {
		return new Dimension(750, 500);
	}
	public class GameEventHandler extends MouseAdapter {

		int counter = 0;

		int qrow = 0;
		int qcol = 0;

		int qfr = 0;
		int qfc = 0;

		int arow = 0;
		int acol = 0;

		public void mousePressed(MouseEvent e) {



			int x = e.getX();
			int y = e.getY();

			if (((x - offset) < -5) || ((y - offset) < -5)) {
				return;
			}

			int row = (y - offset) / cellDim;
			int col = (x - offset) / cellDim;

			if (counter == 0) {
				qfr = row;
				qfc = col;

				//qfr = 9 - qfr;
				counter++;
			} else if (counter == 1) {
				qrow = row;
				qcol = col;

				//qrow = 9 - qrow;
				counter++;
			} else if (counter == 2) {
				arow = row;
				acol = col;

				//arow = 9 - arow;
				counter++;
			}

			if (counter == 3) {
				counter = 0;
				System.out.println(qrow+" "+" "+qcol);
				boolean validMove = markPosition(qrow, qcol, arow, acol, qfr, qfc, false); // update itself
				Jose.turnCount++;


				qrow = 0;
				qcol = 0;
				arow = 0;
				acol = 0;

			}
		}
	}// end of GameEventHandler


}// end
