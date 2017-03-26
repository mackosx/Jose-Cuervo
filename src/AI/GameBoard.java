package AI;

import java.awt.BasicStroke;
import java.awt.Color;
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
	GameEventHandler ev = new GameEventHandler();
	public Jose game = null;
	private State gameModel = null;

	public GameBoard(Jose amazon) {
		addMouseListener(ev);
		this.game = amazon;
		gameModel = new State(this.rows, this.cols);
		init();
	}

	public State getState() {
		return gameModel;
	}

	/**
	 * setup initial board state with amazons
	 * 
	 * 
	 */
	public void init() {
		String tagB = null;
		String tagW = null;

		tagB = State.POS_MARKED_BLACK;
		tagW = State.POS_MARKED_WHITE;

		gameModel.setBoardLocation(0, 3, tagB);
		gameModel.setBoardLocation(0, 6, tagB);
		gameModel.setBoardLocation(3, 0, tagB);
		gameModel.setBoardLocation(3, 9, tagB);

		gameModel.setBoardLocation(6, 0, tagW);
		gameModel.setBoardLocation(6, 9, tagW);
		gameModel.setBoardLocation(9, 3, tagW);
		gameModel.setBoardLocation(9, 6, tagW);

	}

	/**
	 * paint game board
	 */
	protected void paintComponent(Graphics gg) {
		super.paintComponent(gg);
		Graphics2D g = (Graphics2D) gg.create();
		g.setStroke(new BasicStroke(3));
		// color of lines
		g.setColor(new Color(0, 0, 0));
		//g.setStroke();
		
			g.drawLine(0 * cellDim + offset - 1, offset, 0 * cellDim + offset - 1, rows * cellDim + offset);
			g.drawLine(offset, 0 * cellDim + offset -1, cols * cellDim + offset, 0 * cellDim + offset -1);
			g.drawLine(10 * cellDim + offset + 2, offset, 10 * cellDim + offset + 2, rows * cellDim + offset);
			g.drawLine(offset, 10 * cellDim + offset + 2, cols * cellDim + offset, 10 * cellDim + offset + 2);
		boolean dark = true;
		for (int r = 0; r < rows; r++) {
			dark = !dark;
			for (int c = 0; c < cols; c++) {
				dark=!dark;
				posX = c * cellDim + offset;
				posY = r * cellDim + offset;
				g.setStroke(new BasicStroke(1));

				
				if(dark)
					g.setColor(new Color(118, 65, 52, 170));
				else
					g.setColor(new Color(173, 131, 80, 170));
				if (gameModel.getBoard()[r][c].equalsIgnoreCase(State.POS_AVAILABLE)) {
					
					g.fillRect(posX + 1, posY + 1, 50, 50);
				}

				if (gameModel.getBoard()[r][c].equalsIgnoreCase(State.POS_MARKED_BLACK)) {
					g.fillRect(posX + 1, posY + 1, 50, 50);
					ImageObserver n = null;
					BufferedImage img = null;
					try {
						img = ImageIO.read(new File("resources/blackQueen.png"));
					} catch (IOException e) {
						e.printStackTrace();
					}
					g.drawImage(img, posX + 5, posY + 5, n);
				} else if (gameModel.getBoard()[r][c].equalsIgnoreCase(State.POS_MARKED_ARROW)) {
					g.fillRect(posX + 1, posY + 1,50, 50);

					ImageObserver n = null;
					BufferedImage img = null;
					try {
						img = ImageIO.read(new File("resources/arrow.png"));
					} catch (IOException e) {
						e.printStackTrace();
					}
					g.drawImage(img, posX + 5, posY + 5, n);
				} else if (gameModel.getBoard()[r][c].equalsIgnoreCase(State.POS_MARKED_WHITE)) {
					g.fillRect(posX + 1, posY + 1, 50, 50);
					ImageObserver n = null;
					BufferedImage img = null;
					try {
						img = ImageIO.read(new File("resources/whiteQueen.png"));
					} catch (IOException e) {
						e.printStackTrace();
					}
					g.drawImage(img, posX + 5, posY + 5, n);
				}

			}
		}

	}

	// JComponent method
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
				if (gameModel.getBoard()[row][col].equals("white") || gameModel.getBoard()[row][col].equals("black")) {
					qfr = row;
					qfc = col;
					counter++;
					System.out.println("CLICK COUNT: " + counter);
				}

			} else if (counter == 1) {
				if (qfr == row && qfc == col) {
					counter = 0;
					System.out.println("CLICK COUNT: " + counter);

				} else {
					qrow = row;
					qcol = col;
					counter++;
					System.out.println("CLICK COUNT: " + counter);

				}

			} else if (counter == 2) {
				if ((row == qrow && col == qcol)) {
					counter = 0;
					System.out.println("CLICK COUNT: " + counter);

				} else {
					arow = row;
					acol = col;
					counter++;
					System.out.println("CLICK COUNT: " + counter);

				}
			}

			if (counter == 3) {
				if (arow == qrow && acol == qcol) {
					counter = 0;
					System.out.println("CLICK COUNT: " + counter);
				} else {
					System.out.println("CLICK COUNT: " + counter);
					counter = 0;
					System.out.println(qrow + " " + " " + qcol);
					markPosition(new GameMove(qfr, qfc, qrow, qcol, arow, acol));
					game.turnCount++;

					qrow = 0;
					qcol = 0;
					arow = 0;
					acol = 0;
					
				}
			}
		}

	}// end of GameEventHandler

	public void markPosition(GameMove gameMove) {
		gameModel.positionMarked(gameMove);
		repaint();
	}

}// end
