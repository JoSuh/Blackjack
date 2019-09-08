/****************************************************************************
 /****************************************************************************
 *
 * Created by: Heejo Suh
 * Created on: Mar 2018
 * Created for: learning
 * 
 * This class is a 'blueprint' for a stack
 *
 ****************************************************************************/


import java.util.Scanner;

//****************************************************************************

public class Blackjack {
	//Plays the blackjack game(21)

	
	private int userChipCount = 500;

	//yes-> 0	already have-> 1	lost chance-> 2
	private int ableToSplit = 0;
	private boolean playingGame = true;

	private Hand leftHandCards = new Hand();
	private Hand rightHandCards = new Hand();
	private Hand dealerCards = new Hand();

	private DeckOfCards newDeck = new DeckOfCards();

	//---------------------------------------------------------------------------------------------------------------
	//-----------------------
	public Blackjack() {
		//basic constructor for game
		newDeck.newDeckOfCards();
		while (userChipCount>=0) {
			playBlackjack();
		}
	}

	//---------------------------------------------------------------------------------------------------------------
	//-----------------------
	private void playBlackjack() {
		//basic constructor for game
		System.out.println("\n===================================================\n============New Game!============");
		//default
		ableToSplit = 0;
		playingGame = true;
		leftHandCards = new Hand();
		rightHandCards = new Hand();
		dealerCards = new Hand();
		
		//leftHandCards.bet(insuranceOrNot, userChipCount)
		userChipCount= leftHandCards.bet(false, userChipCount); //bet money
		giveTwoCards();
		
		System.out.println("\n-----------------------------------------");
		dealerCards.showCards("Dealer");
		
		//while (playingGame) {
		//---------------------------------
			System.out.println("\n-----------------------------------------");
			leftHandCards.showCards("Left Hand");
			userTurn(leftHandCards);

			//---------------------------------
			if (ableToSplit== 1) {
				//right hand
				System.out.println("\n-----------------------------------------");
				rightHandCards.showCards("Right Hand");
				userTurn(rightHandCards);
			}
			//---------------------------------
			System.out.println("\n-----------------------------------------");
			dealerTurn(); 
			//---------------------------------
		//}
	}
	//-----------------------
	private void giveTwoCards() {
		//Gives user and dealer two cards each

		//addCard(newCard, FacedDown, forUser);
		
		//1st card-------------------
		leftHandCards.addCard(newDeck.drawCard(), false, true);
		dealerCards.addCard(newDeck.drawCard(), false, false);
		
		//2nd card-------------------
		leftHandCards.addCard(newDeck.drawCard(), false, true);
		dealerCards.addCard(newDeck.drawCard(), true, false);
		
		if ( dealerCards.getRank(0)==1 || dealerCards.getRank(0)>=10) {
			//If face-up card is a ten-card or an ace, and check if blackjack
			if ( dealerCards.getRank(0)==1) {
				//Insurance
				//can make a bet up to half of original bet
				leftHandCards.bet(true, userChipCount); //ask for insurance betting
				if (dealerCards.getRank(1)>=10) {
					//If face-down card is a ten-card, it is turned up
					System.out.println("Dealer has a blackjack!");
					dealerCards.turnCardUp(1);
					userChipCount += leftHandCards.getUserInsurance(); //receive double the bet
					checkIfGameEnded(false);
				} else {
					//not a ten card
					System.out.println("Dealer does not have a blackjack!");
				}
			}
		}
		checkIfGameEnded(false);
	}


	//-----------------------
	private void dealerTurn() {
		//dealer's turn
		dealerCards.showCards("Dealer");
		dealerCards.printStats(false);
		if (ableToSplit==0) {
			ableToSplit = 2; //user's chance to split is gone
		}
		//When the dealer has served every player, his face-down card is turned up. 
		dealerCards.turnCardUp(1);
		if (dealerCards.getTotal() < 17) {
			//If the total is 17 or more, he must stand. If the total is 16 or under, he must take a card. 
			dealerCards.addCard(newDeck.drawCard(), false, false);
			dealerCards.showCards("Dealer");
			dealerCards.printStats(false);
		}
		checkIfGameEnded(true);
	}
	//-----------------------
	private void userTurn(Hand leftOrRight) {
		//user's turn
		leftOrRight.printStats(true);
		
		Scanner scanner = new Scanner(System.in);
		String input = "";
		while (!input.equals("S")) {
			System.out.print("What would you like to do?\nS: stand     H: hit     D: double");
			if (ableToSplit==0) {
				System.out.println("     SP: split\n");
			}
			
			input = scanner.nextLine().toUpperCase();
			
			if (input.equals("S")) {
				//Stand
			} else if (input.equals("H")) {
				//Hit
				leftOrRight.hit(newDeck.drawCard());
				
			} else if (input.equals("D")) {
				//Double
				leftOrRight.doubleDown(userChipCount, newDeck.drawCard());		
			} else if (input.equals("SP") && ableToSplit==0) {
				//Split
				split();
			} else {
				System.out.println("Invalid input! Try again.");
			}
		}
	}

	//-----------------------
	private void split() {
		//splits the cards into two hands
		
		if (leftHandCards.getRank(0) == leftHandCards.getRank(1)) {
			//If a player's first two cards are of the same denomination, may choose to treat them as two separate hands 
			//Original bet gets placed on one hand, and the same amount should be placed on the other.
			//First plays left hand, then the right
			//Two hands are treated separately

			if (userChipCount>=leftHandCards.getUserBetted()) {
				//if can pay the same bet
				rightHandCards = new Hand(leftHandCards.cardAt(1), leftHandCards.getUserBetted(), newDeck.drawCard());
				leftHandCards = new Hand(leftHandCards.cardAt(0), leftHandCards.getUserBetted(), newDeck.drawCard());
				ableToSplit = 1;
			} else {
				//not enough chips
				System.out.println("You don't have enough chips!");
			}
		} else {
			System.out.println("ERROR: You need to have two cards of the same denomination to split!");
		}

	}	
	
	//-----------------------
	private void checkIfGameEnded(boolean forceEnd) {
		//checks if game should be ended
		if (forceEnd || leftHandCards.checkForBlackjack() || leftHandCards.checkForBust() || rightHandCards.checkForBlackjack() || rightHandCards.checkForBust() || dealerCards.checkForBlackjack() || dealerCards.checkForBust()) {
			//get win
			playingGame = false;
		
			System.out.println("=============================================================\n\nFinal Results:");
			//---------------------------------
			dealerCards.turnCardUp(1);
			dealerCards.showCards("Dealer");
			System.out.println(dealerCards.getTotal());
			//---------------------------------
			compareUserAndDealer(leftHandCards, "Left Hand");
			if (ableToSplit== 1) {
				//right hand
				compareUserAndDealer(rightHandCards, "Right Hand");
			}
		}
	}

	//-----------------------
	private void compareUserAndDealer(Hand leftOrRight, String userName) {
	//Calculates who wins
		
		leftOrRight.showCards(userName);
		System.out.println(leftOrRight.getTotal());
		
		//------------------------------------------------------------------------------
		//blackjack?
		if ( dealerCards.checkForBlackjack() && leftOrRight.checkForBlackjack()) {
			//both have blackjack
			//stand-off -> the player takes back his chips
			System.out.println("\n=> Tie");
			userChipCount += leftHandCards.getUserBetted();
		} else if ( !dealerCards.checkForBlackjack() && leftOrRight.checkForBlackjack()) {
			//only user has blackjack
			//user receives 1.5 times the bet
			System.out.println("\n=> Win");
			userChipCount += leftHandCards.getUserBetted()*1.5;
		} else if ( dealerCards.checkForBlackjack() && !leftOrRight.checkForBlackjack()) {
			//only dealer has blackjack
			//dealer takes the chips
			System.out.println("\n=> Lose");
		}
		//------------------------------------------------------------------------------
		//bust?
		else if ( dealerCards.checkForBust() && leftOrRight.checkForBust()) {
			//both are bust
			//stand-off -> the player takes back his chips
			System.out.println("\n=> Tie");
			userChipCount += leftHandCards.getUserBetted();
		} else if ( !dealerCards.checkForBust() && leftOrRight.checkForBust()) {
			//only user is bust
			//dealer takes the chips
			System.out.println("\n=> Lose");
		} else if ( dealerCards.checkForBust() && !leftOrRight.checkForBust()) {
			//only dealer is bust
			//user receives 1.5 times the bet
			System.out.println("\n=> Win");
			userChipCount += leftHandCards.getUserBetted()*1.5;
		}
		//------------------------------------------------------------------------------
		//compare?
		else if ( dealerCards.getTotal() < leftOrRight.getTotal()) {
			//user closer to 21
			//user receives 1.5 times the bet
			System.out.println("\n=> Win");
			userChipCount += leftHandCards.getUserBetted()*1.5;
		} else if ( dealerCards.getTotal() > leftOrRight.getTotal()) {
			//dealer closer to 21
			//dealer takes the chips
			System.out.println("\n=> Lose");
		} else if ( dealerCards.getTotal() == leftOrRight.getTotal()) {
			//exact same value
			System.out.println("\n=> Tie");
		} 
		
		
		playBlackjack();
	}
	
}//closing for class



