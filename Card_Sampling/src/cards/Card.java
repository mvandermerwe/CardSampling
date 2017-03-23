/**
 * Mark Van der Merwe and Thomas Oh
 */
package cards;

/**
 * Card class. Each card has a suit and a value.
 * 
 * @author markvandermerwe
 */
public class Card {

	/**
	 * Enumerated type for our suits.
	 * 
	 * @author markvandermerwe
	 */
	public enum Suit {
		CLUBS(0), DIAMONDS(1), HEARTS(2), SPADES(3);

		private int suitNum;

		Suit(int suitNum) {
			this.suitNum = suitNum;
		}

		public int getSuitNum() {
			return suitNum;
		}

		public String toString() {
			switch (this.getSuitNum()) {
			case 0:
				return "Clubs";
			case 1:
				return "Diamonds";
			case 2:
				return "Hearts";
			case 3:
				return "Spades";
			default:
				return "";
			}
		}
	}

	// State vars for card.
	private Suit suit;
	private int value;

	/**
	 * Constructor for card
	 * 
	 * @param suit
	 * @param value
	 */
	public Card(Suit suit, int value) {
		this.suit = suit;
		this.value = value;
	}

	/**
	 * Constructor based on position.
	 * 
	 * @param position position in the deck
	 */
	public Card(int position) {
		// Determining the suit of the card.
		switch (position / 13) {
		case 0:
			this.suit = Suit.CLUBS;
			break;
		case 1:
			this.suit = Suit.DIAMONDS;
			break;
		case 2:
			this.suit = Suit.HEARTS;
			break;
		case 3:
			this.suit = Suit.SPADES;
			break;
		}

		// Determine value of the card.
		this.value = position % 13 + 2;
	}

	/**
	 * Returns true if provided card has the same value.
	 * 
	 * @param otherCard
	 *            - card to compare to.
	 * @return true/false if same value.
	 */
	public boolean sameValue(Card otherCard) {
		return value == otherCard.value;
	}

	/**
	 * Determine if suits are same.
	 * 
	 * @param otherCard
	 *            - card to compare to.
	 * @return true/false if same suit.
	 */
	public boolean sameSuit(Card otherCard) {
		return suit.equals(otherCard.suit);
	}
	
	/**
	 * Getter for suit.
	 * 
	 * @return returns this card's suit.
	 */
	public Suit getSuit() {
		return suit;
	}
	
	/**
	 * Getter for value.
	 * 
	 * @return returns this card's value.
	 */
	public int getValue() {
		return value;
	}

	/**
	 * Return string representation of the current card object.
	 * 
	 * @return string of card
	 */
	public String toString() {
		return value + " of " + suit.toString();
	}

}
