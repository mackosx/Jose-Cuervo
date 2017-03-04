package ygraphs.ai.smart_fox.games;

public class GameMove {
	int x; // current queen x
	int y; // current queen y
	int newX; // new queen x
	int newY; // new queen y
	String type;// black queen, arrow, available, etc.
	int arrowX; // arrow x location
	int arrowY;// arrow y location

	public GameMove(int x, int y, int newX, int newY, String type) {
		this.type = type;
		this.x = x;
		this.y = y;
		this.newX = newX;
		this.newY = newY;

	}

	public GameMove(int x, int y, int newX, int newY, int arrowX, int arrowY, String type) {
		this(x, y, newX, newY, type);

		this.arrowX = arrowX;
		this.arrowY = arrowY;

	}
	public String toString(){
		return "Current pos: ("+x+"," + y+")\nNew pos:("+newX+"," + newY+")\nArrow pos: ("+arrowX+","+arrowY+")\n";
	}

}
