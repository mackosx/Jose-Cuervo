package ygraphs.ai.smart_fox.games;

public class GameMove {
	int row; // current queen row
	int col; // current queen y
	int newRow; // new queen x
	int newCol; // new queen y
	String type;// black queen, arrow, available, etc.
	int arrowRow; // arrow x location
	int arrowCol;// arrow y location

	/**
	 * 
	 * @param row
	 * 		current row of queen
	 * @param col
	 * 		current column of queen
	 * @param newRow
	 * 		new row of queen
	 * @param newCol
	 * 		new column of queen
	 * @param type
	 */
	public GameMove(int row, int col, int newRow, int newCol, String type) {
		this.type = type;
		this.row = row;
		this.col = col;
		this.newRow = newRow;
		this.newCol = newCol;

	}

	/**
	 * 
	 * @param row
	 * 		current row of queen
	 * @param col
	 * 		current column of queen
	 * @param newRow
	 * 		new row of queen
	 * @param newCol
	 * 		new column of queen
	 * @param arrowRow
	 * 		arrow row
	 * @param arrowCol
	 * 		arrow column
	 * @param type
	 */
	public GameMove(int row, int col, int newRow, int newCol, int arrowRow, int arrowCol, String type) {
		this(row, col, newRow, newCol, type);

		this.arrowRow = arrowRow;
		this.arrowCol = arrowCol;

	}
	public String toString(){
		return "Current pos: ("+row+"," + col+")\nNew pos:("+newRow+"," + newCol+")\nArrow pos: ("+arrowRow+","+arrowCol+")\nType: "+type;
	}

}
