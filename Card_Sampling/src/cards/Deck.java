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

	/**
	 * Get a card based on its index;
	 * 
	 * @param index
	 * @return
	 */
	public Card getCard(Random_Generator generator) {
		Card card;
		int index;
		do{
			index = generator.next_int(52);
			card = deck[index];
		} while(card == null);
		
		deck[index] = null;
		return card;
	}

	// Maybe later.
	public void Shuffle() {

	}

}
