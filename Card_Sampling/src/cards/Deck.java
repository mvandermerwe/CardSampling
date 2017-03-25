/**
 * Mark Van der Merwe and Thomas Oh
 */
package cards;

/**
 * Creates the deck of 52 cards to be used for testing.
 * 
 * @author Mark Van der Merwe and Thomas Oh
 */
public class Deck {

	/**
	 * Card array with 52 cards.
	 */
	private final static Card[] deck = new Card[52];
	static {
		for (int i = 0; i < 52; i++) {
			deck[i] = new Card(i);
		}
	}

	/**
	 * Grabs card from the deck based on position in deck.
	 * 
	 * @param position
	 *            - position of wanted card in deck
	 * @return - Card at position.
	 */
	public Card getCard(int position) {
		Card card = deck[position];

		return card;
	}

}
