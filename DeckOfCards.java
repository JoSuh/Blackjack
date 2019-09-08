/****************************************************************************
 * Created by: Heejo Suh
 * Created on: Mar 2018
 * Created for: learning
 * 
 * This class is a 'blueprint' for a stack for deck of cards
 * 
 * 
 ****************************************************************************/


import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;
import java.util.Collections;


public class DeckOfCards {
	//class for Deck of cards
	
	//Private Fields
	
	private ArrayList<Cards> deck = new ArrayList<Cards>();

	//-----------------------
	public void newDeckOfCards() {
		//this is the constructor for deck
		
		getNewStack();
		shuffleDeck();
		cutCard();
	}
	//-----------------------
	public static int getIntInput(String askQ, boolean range, int rangeStartOrPossibleValue, int rangeEndORPossilbeValue) {
		//gets input in correct range from user then returns it
		Scanner scanner = new Scanner(System.in);
		
		
		while (true) {
			System.out.println(askQ);
			
			String input = scanner.nextLine();
			try {
				//check if response is a number
				Integer inputNumber = Integer.valueOf(input);
				if (range) {
					//check if input is in range
					if ( inputNumber>=rangeStartOrPossibleValue && inputNumber<=rangeEndORPossilbeValue) {
						return inputNumber;
					} else {
						System.out.println("ERROR: Invalid input!");
					}
			
				} else {
				//check if input matches value
					if (  inputNumber==rangeStartOrPossibleValue || inputNumber==rangeEndORPossilbeValue ) {
						return inputNumber;
					} else {
						System.out.println("ERROR: Invalid input!");
					}
				}
			} catch (IllegalArgumentException x) { 
				//not an integer
				System.out.println("ERROR: Input an integer value!");
			}
		}
	}				
	//-----------------------
	private void cutCard(){
		//inserts the plastic card to cut the deck into 2
		int cutIndex = getIntInput("Where would you like to place the plastic card at?(In front of which card)", true, 1, deck.size());
		System.out.println("Placing the card at " + cutIndex + "\n");
		deck.subList(cutIndex, deck.size()).clear(); //remove everything after index
	}
	
	//-----------------------
	private void shuffleDeck(){
		//shuffles the whole deck
		Collections.shuffle(this.deck, new Random());
	}

	//-----------------------
	private void getNewStack(){
		//shuffles the whole deck

		//create deck (312 cards)
		for(int decks = 1 ; decks<7 ; decks++) {
			//for 6 decks
			for(int suit = 1 ; suit<5 ; suit++) {
				//4 suits
				for(int rank = 1 ; rank<14 ; rank++) {
					//13 ranks
					//add each card to deck
					Cards newCard = new Cards(suit, rank, false); //all cards are not faced down
					deck.add(newCard);
				}
			}
		}
	}
	//-----------------------
	public Cards drawCard(){
		//pops the last value from the stack and return it
		if(deck.size() == 0) {
			System.out.println("Plastic card found! Reshuffling new deck...");
			newDeckOfCards();
		}
		
		Cards topCard = deck.get(deck.size()-1);
		deck.remove(topCard);
		return topCard;
	}
	//-----------------------
	public void putCardBack(Cards cardToPutBack){
		//adds the popped card back onto stack
		deck.add(0, cardToPutBack);
	}
	
	
	
}//closing for class
