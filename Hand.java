/****************************************************************************
 *
 * Created by: Heejo Suh
 * Created on: Mar 2018
 * Created for: learning
 * 
 * This class is a 'blueprint' for a stack of Hands
 *
 ****************************************************************************/

import java.util.ArrayList;

//****************************************************************************

public class Hand extends DeckOfCards {
	//Hand holds cards
	

	private int userBetted = 0;
	private int insuranceBet = 0;
	private boolean ableToDouble = true;
	private boolean ableToDraw = true;

	private int handCounts = 0;

	private ArrayList<Cards> handCards = new ArrayList<Cards>();
	
	//-----------------------
	public Hand() {
		//basic constructor for first left hand
	}
	//-----------------------
	public Hand(Cards defaultCard, int userBetted, Cards newCard) {
		//basic constructor for a hand after split
		this.userBetted = userBetted;
		handCards.add(defaultCard);
		
		if (defaultCard.getCardRank()==1 ) {
			//With a pair of aces, the player is given one card for each ace and may not draw again. 
			//Also, if a ten-card is dealt to one of these aces, the payoff is equal to the bet
			addCard(newCard, false, true);
			ableToDraw = false;
		} else {
			putCardBack(newCard);
		}
	}
	
	//-----------------------
	public int bet (boolean insuranceOrNot, int userChipCount) {
		//Allows user to bet money
		//"\nYour cards total: "+handCounts+
		System.out.println("\nYour bet: "+userBetted+"\nYour #chips: "+userChipCount+"\n");
		int betChips;
		if (insuranceOrNot){
			//insurance
			//can bet up to half of original bet
			betChips = getIntInput("Dealer has an ace! How many chips would you like to bet for insurance?\n", true, 0, userBetted/2);
			insuranceBet += betChips;
		} else {
			//not insurance
			betChips = getIntInput("How many chips would you like to bet?\n", true, 0, userChipCount);
			userBetted += betChips;
		}
		return userChipCount -= betChips;
		
	}
	

	//-----------------------
	public int getUserBetted() {
		//return bet money
		return userBetted;
	}

	//-----------------------
	public int getUserInsurance() {
		//return insurance bet money
		return insuranceBet;
	}
	//-----------------------
	public void addCard(Cards newCard, boolean FacedDown, boolean forUser) {
		//add a card to hand
		if (ableToDraw) {
			handCards.add(newCard);
			if (FacedDown) {
				//turn face down
				topCard().faceDown();
			}
			//set ace
			setAce(forUser);
			handCounts += newCard.getCardValue();
		} else {
			System.out.println("You cannot draw any more cards!");
			putCardBack(newCard);
		}
	}
	
	//-----------------------
	public int getRank(int index) {
		//return rank
		return handCards.get(index).getCardRank();
	}
	//-----------------------
	public void turnCardUp(int index) {
		//turn card up 
		handCards.get(index).faceUp(); //face the card up
	}
	

	//-----------------------
	public Cards topCard() {
		//return top card
		return handCards.get(handCards.size()-1);
	}
		

	//-----------------------
	public void printStats(boolean forUser) {
		//prints current stats of hand
		if (forUser) {
			System.out.println("\nBetted:  "+userBetted);
		}
		System.out.println("Cards total:  "+handCounts+"\n------------");
	}
		
	//-----------------------
	public Cards cardAt(int index) {
		//return card at index
		return handCards.get(index);
	}	
	//-----------------------
	public int getTotal() {
		//return total
		return handCounts;
	}

	//-----------------------
	private void setAce(boolean forUser) {
		//checks and sets ace
		if (forUser) {
			//for user
			if (topCard().isAce()) {
				int decideVal = getIntInput("You have an ace! Would you like to use the ace card as a 1 or a 11?", false, 1, 11);
				topCard().setAceValue(decideVal); //set to decided value
			}
		} else {
			//for dealer
			if (topCard().isAce()) {
				if ((handCounts+11) <=21 && (handCounts+11)>=17) {
					//if total does not go over 21 with ace set as 11
					//and total is greater or equal to 17
					topCard().setAceValue(11); //set to 11
				} else {
					topCard().setAceValue(1); //set to 11
				}
			}
		}
	}
	


	//-----------------------
	public boolean checkForBlackjack() {	
		//check for blackjack
		//for user
		if (handCounts == 21) {
			return true;
		} else {
			return false;
		}
	}

	//-----------------------
	public boolean checkForBust() {	
		//check for bust
		//for user
		if (handCounts > 21) {
			return true;
		} else {
			return false;
		}
	}
	//-----------------------
	public void hit(Cards newCard) {
		//Player draws another card (and more if wishes). 
		//If this card causes the player's total points to exceed 21 (known as "breaking" or "busting") then he loses.
		addCard(newCard, false, true);
		showCards("");
		printStats(true);
	}


	//-----------------------
	public void doubleDown(int chipsCount, Cards newCard) {
		//doubles the bet on the cards
		
		if ( ableToDouble && (handCounts==9 || handCounts==10 || handCounts==11)) {
			//player can double the bet under given conditions
			//can place a bet equal to the original bet, and the dealer gives him just one card 
			//which is placed face-down and is not turned up until the bets are settled at the end of the hand.
	
			if (chipsCount>=userBetted) {
				userBetted += userBetted;
				addCard(newCard, true, true);
				ableToDouble=false;
				chipsCount-=userBetted;
				showCards("");
				topCard().faceUp();
				showCards("");
			}

		} else {
			System.out.println("ERROR: Your total must be 9, 10, or 11 to double down!");
			putCardBack(newCard);
		}		
	}


	//-----------------------
	public void showCards(String userName) {
		//shows the cards

		if (userName!="") {
			System.out.println(userName+":");
		}
		for(int index = 0 ; index<handCards.size() ; index++) {
			String outputSymbol= handCards.get(index).getCardUnicode();
			//print symbol
			if (handCards.get(index).isFacedDown()) {
				//show faced-down card
				System.out.print("\uD83C\uDCA0");
			} else {
				//convert string to unicode and print
				//===============================================
				//Referenced from 
				//https://stackoverflow.com/questions/11145681/how-to-convert-a-string-with-unicode-encoding-to-a-string-of-letters
				String toUnicode = outputSymbol.split(" ")[0];
				toUnicode = toUnicode.replace("\\","");
				String[] unicodeArray = toUnicode.split("u");
				
				String outPutUnicode = "";
				for(int i = 1; i < unicodeArray.length; i++){
				    int hexVal = Integer.parseInt(unicodeArray[i], 16);
				    outPutUnicode += (char)hexVal;
				}
				//===============================================
				System.out.print(outPutUnicode);
			}
			System.out.print("  ");
		}
	}
	
}
