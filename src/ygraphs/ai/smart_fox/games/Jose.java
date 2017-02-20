package ygraphs.ai.smart_fox.games;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

import javax.swing.JFrame;


import ygraphs.ai.smart_fox.GameMessage;


public class Jose extends GamePlayer{
	private GameClient gameClient;
	private JFrame guiFrame = null;
	private GameBoard board = null;
	private boolean gameStarted = false;
	public String usrName = null;
	
	public Jose(String name, String password){
		int turnCount = 0;
		
		
		this.usrName = name;
		//setupGUI(); //TODO: wait until gui is implemented

		//connectToServer(name, passwd); //server connection
	}
	
	//depth limited search
	/*
	 * function IDDFS(root)
    for depth from 0 to inf
        found = DLS(root, depth)
        if found != null
            return found

function DLS(node, depth)
    if depth = 0 and node is a goal
        return node
    if depth > 0
        foreach child of node
            found = DLS(child, depth - 1)
            if found != null
                return found
    return null
	 */
	LinkedList<Position> found = new LinkedList<Position>();
	public LinkedList<Position> iterativeDeepening(Position root,int turn){
		for(int i = 0; i <= turn; i++){
			for (Position pos:DLS(root, i)){
				found.add(pos);
			}
		}
		return found;
	}
	//Depth Limited Search
	public LinkedList<Position> DLS(Position curr, int depth){
		if(depth == 0)
			return found;
		else{
			for (Position pos: curr.getMoves()) {
				found.addAll(DLS(pos, depth - 1));
				if(!found.equals(null))
					return found;
			}
		}
		return null;
		
	}
	private GameBoard createGameBoard() {
		return new GameBoard(this);
	}
	@Override
	public void onLogin() {
		ArrayList<String> rooms = gameClient.getRoomList();
		this.gameClient.joinRoom(rooms.get(6));
	}
	@Override
	public boolean handleGameMessage(String messageType, Map<String, Object> msgDetails) {
		if (messageType.equals(GameMessage.GAME_ACTION_START)) {

			if (((String) msgDetails.get("player-black")).equals(this.userName())) {
				System.out.println("Game State: " + msgDetails.get("player-black"));
			}

		} else if (messageType.equals(GameMessage.GAME_ACTION_MOVE)) {
			handleOpponentMove(msgDetails);
		}
		return true;
	}
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

	@Override
	public String userName() {
		return usrName;
	}
}
