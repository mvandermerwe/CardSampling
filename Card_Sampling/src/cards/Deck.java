/**
 * Mark Van der Merwe and Thomas Oh
 */
package cards;

/**
 * @author Mark Van der Merwe and Thomas Oh
 *
 */
public class Deck {

	private final static Card[] deck = new Card[52];
	static {
		for (int i = 0; i < 52; i++) {
			deck[i] = new Card(i);
		}
	}

	public Card getCard(int position) {
		Card card = deck[position];
		
		return card;
	}

}
