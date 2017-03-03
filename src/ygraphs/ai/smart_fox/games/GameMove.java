package ygraphs.ai.smart_fox.games;

public class GameMove {
	int x;
	int y;
	int newX;
	int newY;
	String type;// black queen, arrow, available, etc.
	int arrowX;
	int arrowY;

	public GameMove(int x, int y, int newX, int newY, String type) {
		this.type = type;
		this.x = x;
		this.y = x;
		this.newX = newX;
		this.newY = newY;

	}

	public GameMove(int x, int y, int newX, int newY, int arrowX, int arrowY, String type) {
		this(x, y, newX, newY, type);

		this.arrowX = arrowX;
		this.arrowY = arrowY;

	}

}
