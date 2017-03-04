package ygraphs.ai.smart_fox.games;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

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

	Amazon game = null;
	private State gameModel = null;

	boolean playerAMove;
<<<<<<< HEAD

	public GameBoard(Jose game) {
=======


>>>>>>> a4f2484cd50b215f9bf1fd175a22f27219fbeec5
		this.game = game;
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

		boolean valid = gameModel.positionMarked(new GameMove(qfr, qfc, qrow, qcol, arow, acol, null));
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

				posY = (9 - r) * cellDim + offset;

				if (gameModel.getBoard()[r][c].equalsIgnoreCase(State.POS_AVAILABLE)) {
					g.clearRect(posX + 1, posY + 1, 49, 49);
				}

				if (gameModel.getBoard()[r][c].equalsIgnoreCase(State.POS_MARKED_BLACK)) {
					g.clearRect(posX + 1, posY + 1, 49, 49);
					g.fillOval(posX, posY, 50, 50);
				} else if (gameModel.getBoard()[r][c].equalsIgnoreCase(State.POS_MARKED_ARROW)) {
					g.clearRect(posX + 1, posY + 1, 49, 49);
					g.drawLine(posX, posY, posX + 50, posY + 50);
					g.drawLine(posX, posY + 50, posX + 50, posY);
				} else if (gameModel.getBoard()[r][c].equalsIgnoreCase(State.POS_MARKED_WHITE)) {
					g.clearRect(posX + 1, posY + 1, 49, 49);
					g.drawOval(posX, posY, 50, 50);
				}
			}
		}

	}


	// JComponent method
	public Dimension getPreferredSize() {
		return new Dimension(750, 500);
	}

}// end
