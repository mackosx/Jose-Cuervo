package AI;

public class GameMove {
	int row = -1; // current queen row
	int col = -1; // current queen y
	int newRow =-1; // new queen x
	int newCol = -1; // new queen y
	//String type;// black queen, arrow, available, etc.
	int arrowRow = -1; // arrow x location initialized to -1 indicates it hasnt been set
	int arrowCol = -1;// arrow y location same

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
	public GameMove(int row, int col, int newRow, int newCol) {
		//this.type = type;
		this.row = row;
		this.col = col;
		this.newRow = newRow;
		this.newCol = newCol;

		
	}
	public GameMove(int row, int col){
		this.row = row;
		this.col = col;
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
	public GameMove(int row, int col, int newRow, int newCol, int arrowRow, int arrowCol) {
		this(row, col, newRow, newCol);

		this.arrowRow = arrowRow;
		this.arrowCol = arrowCol;

	}
	public String toString(){
		return "Current pos: ("+row+"," + col+")\nNew pos:("+newRow+"," + newCol+")\nArrow pos: ("+arrowRow+","+arrowCol+")\n";
	}

}
