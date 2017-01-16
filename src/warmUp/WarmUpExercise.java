package warmUp;

import java.util.ArrayList;
import java.util.Map;

import ygraphs.ai.smart_fox.GameMessage;
import ygraphs.ai.smart_fox.games.AmazonsGameMessage;
import ygraphs.ai.smart_fox.games.GameClient;
import ygraphs.ai.smart_fox.games.GamePlayer;
/**
 * An example showing how to implement a GamePlayer 
 * @author emerson
 */
public class WarmUpExercise extends GamePlayer{

    private GameClient gameClient;
	
    private String userName = null;
 
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {				 
		 WarmUpExercise player_01 = new WarmUpExercise("emerson", "kirby");  		 
	}
	
	/**
	 * Any name and passwd 
	 * @param userName
	 * @param passwd
	 */
	public WarmUpExercise(String userName, String passwd) {
		this.userName = userName;
		gameClient = new GameClient(userName, passwd, this);	 
	}
 
 
 
	@Override
	public String userName() { 
		return userName;
	}

	@Override
	public boolean handleGameMessage(String messageType, Map<String, Object> msgDetails) {
 		//This method will be called by the GameClient when it receives a game-related message
		//from the server.
		return true;
	}

	@Override
	public void onLogin() {
		System.out.println("I am called because the server said I am logged in successfully");
		ArrayList<String> Rooms = gameClient.getRoomList();
		System.out.println(Rooms);
	}

}