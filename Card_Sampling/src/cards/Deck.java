/**
 * Mark Van der Merwe and Thomas Oh
 */
package cards;

/**
 * @author Mark Van der Merwe and Thomas Oh
 *
 */
public class Deck {
	
	private boolean nullify = true;

	private final static Card[] deck = new Card[52];
	static {
		for (int i = 0; i < 52; i++) {
			deck[i] = new Card(i);
		}
	}
	
	public Deck(boolean nullify) {
		this.nullify = nullify;
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
		do {
			index = generator.next_int(52);
			card = deck[index];
		} while (card == null);

		if(nullify) deck[index] = null;
			
		return card;
	}

	public Card getCard(int position) throws Exception {
		Card card = deck[position];

		if (card == null) {
			// Die a horrible death.
			throw new Exception();
		}

		if(nullify) deck[position] = null;
		
		return card;
	}

	// Maybe later.
	public void Shuffle() {

	}

}
