 /****************************************************************************
 * Created by: Heejo Suh
 * Created on: Mar 2018
 * Created for: learning
 * 
 * This class is a 'blueprint' for a stack of Cards
 *
 ****************************************************************************/
import java.util.HashMap;

public class Cards{
	//class for cards
	
	//public  StringBuffer unicodeValue = new StringBuffer(  );
	private  String unicodeValue = "\\uD83C";
	private boolean facedDown;
	private int cardValue;
	
	//13 ranks of 2-9, 10, A(11), J(12), Q(13), K(14)
	private int rank;
	//4 suits of Spade(1), Heart(2), Diamond(3), Club(4)
	private int suit;
	
   	
	//---------------------------------------------
	public Cards (int suit, int rank, boolean facedDown) {
		//basic constructor for a card

		//dictionary for suit S, H, D, C
	   	HashMap<Integer, Character> suitToString = new HashMap<Integer, Character>();
	   	suitToString.put(1, 'A'); //Spade
	   	suitToString.put(2, 'B'); //Hearts
	   	suitToString.put(3, 'C'); //Diamond
	   	suitToString.put(4, 'D'); //Clubs
	   	
		//dictionary for rank (J, Q, K)
	   	HashMap<Integer, Character> rankToCode = new HashMap<Integer, Character>();
	   	rankToCode.put(10, 'A'); //10
	   	rankToCode.put(11, 'B'); //J
	   	rankToCode.put(12, 'D'); //Q
	   	rankToCode.put(13, 'E'); //K
	   	
		this.suit = suit;
		this.rank = rank;
		this.facedDown = facedDown; //card faced up as default
		//unicode
		if (rankToCode.get(rank)!=null) {
			//see if card is one that is NOT convertible directly from number
			unicodeValue += ("\\uDC" + suitToString.get(suit) + rankToCode.get(rank));
		} else { 
			//is convertible directly from number
			unicodeValue += ("\\uDC" + suitToString.get(suit) + rank);
		}
		//unicodeValue = String.valueOf(Character.toChars(0x0001F0A0));

		if (rank<10) {
			//for numbers
			cardValue = rank;
			//set ace to default 10, too, for convenience
		} else {
			cardValue = 10;
		}
		
		
	}
	
	//--------------------------
	public int getCardValue(){
		//returns the value of card
		return cardValue;
	}

	//--------------------------
	public int getCardRank(){
		//returns the rank of card
		return rank;
	}
	//--------------------------
	public void faceUp() {
		//make a card face up
		facedDown = false;
	}
	//--------------------------
	public void faceDown() {
		//make a card face down
		facedDown = true;
	}
	//--------------------------
	public boolean isFacedDown() {
		//returns if card is faced down or not
		return facedDown;
	}
	
	//--------------------------
	public boolean isAce() {
		//returns if card is an ace
		if (rank==1) {
			return true;
		} else {
			return false;
		}
	}
	//--------------------------
	public void setAceValue(int setValue) {
		//set the value of an ace card
		cardValue = setValue;
	}
	
	//--------------------------
	public String getCardUnicode() {
		//return card unicode
		return unicodeValue;
	}

}	
	
