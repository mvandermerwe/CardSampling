/**
 * Mark Van der Merwe and Thomas Oh
 */
package cards;

import java.util.HashSet;

/**
 * 
 * @author markvandermerwe
 *
 */
public class Hand {

	/**
	 * Enumerated type for Hand rankings.
	 * 
	 * @author markvandermerwe
	 */
	public enum Rank {
		ROYAL_FLUSH(0), STRAIGHT_FLUSH(1), FOUR_OF_A_KIND(2), FULL_HOUSE(3), FLUSH(4), STRAIGHT(5), THREE_OF_A_KIND(
				6), TWO_PAIRS(7), SINGLE_PAIR(8), HIGH_CARD(9);

		private int rankNum;

		Rank(int rank) {
			rankNum = rank;
		}

		public int getRankNum() {
			return rankNum;
		}
	}

	private Card[] hand;
	private Deck deck;

	/**
	 * Set size of hand.
	 * 
	 * @param sizeOfHand
	 *            - must be either 5 or 7!
	 * @throws Exception
	 *             - if not 5 or 7 die a horrible death.
	 */
	public Hand(int sizeOfHand, Deck deck) throws Exception {
		if (sizeOfHand != 5 && sizeOfHand != 7) {
			// Die a horrible death.
			throw new Exception();
		}
		hand = new Card[sizeOfHand];
		this.deck = deck;
	}

	public void createHandFromNumbers(Integer[] cardLocations) {
		for (int card = 0; card < cardLocations.length; card++) {
			try {
				hand[card] = deck.getCard(cardLocations[card]);
			} catch (Exception e) {
				System.out.println("Card requested doesn't work.");
				e.printStackTrace();
			}
		}
	}

	/**
	 * Populates a hand randomly for the given length.
	 * 
	 * @param generator
	 *            - the random generator used to pick cards.
	 */
	public void getRandomHand(Random_Generator generator) {
		Integer[] nums = new Integer[hand.length];

		// Use sets to make sure there are no duplicates.
		HashSet<Integer> numbers = new HashSet<>();
		while (numbers.size() < hand.length) {
			numbers.add(generator.next_int(52));
		}

		createHandFromNumbers(numbers.toArray(nums));
	}

	/**
	 * Assumes both hands are size seven and are part of the same deck.
	 * 
	 * @param generator
	 * @param handOne
	 * @param handTwo
	 */
	public static void getTwoRandomHands(Random_Generator generator, Hand handOne, Hand handTwo) {
		Integer[] nums = null;

		// Use sets to make sure there are no duplicates.
		HashSet<Integer> numbers = new HashSet<>();
		while (numbers.size() < 9) {
			numbers.add(generator.next_int(52));
		}
		nums = numbers.toArray(nums);

		for (int index = 4; index < 9; index++) {
			Card card = handOne.deck.getCard(nums[index]);
			handOne.hand[index] = card;
			handTwo.hand[index] = card;
		}

		for (int index = 0; index < 2; index++) {
			handOne.hand[index] = handOne.deck.getCard(nums[index]);
			handTwo.hand[index] = handTwo.deck.getCard(nums[index+2]);
		}
	}

	// Instance variables to help determine rank.
	private int[] suitCount;
	private int[] valueCount;
	private int valStraightStart;
	private int fourOfAKindStart;
	private int numThreeOfAKind = 0;
	private int numPairs = 0;
	private int threeOfAKindStart;
	private int highPairStart;
	private int lowPairStart;

	public Rank getRank() {
		suitCount();
		valueCount();

		if (isFlush()) {
			if (isStraight()) {
				if (isRoyalFlush()) {
					return Rank.ROYAL_FLUSH;
				} else {
					return Rank.STRAIGHT_FLUSH;
				}
			} else {
				return Rank.FLUSH;
			}
		} else {
			if (isFourOfAKind()) {
				return Rank.FOUR_OF_A_KIND;
			}

			countOfThreesAndPairs();

			if (numThreeOfAKind >= 2 || (numThreeOfAKind == 1 && numPairs >= 1)) {
				return Rank.FULL_HOUSE;
			}

			if (isStraight()) {
				return Rank.STRAIGHT;
			}

			if (numThreeOfAKind == 1) {
				return Rank.THREE_OF_A_KIND;
			}

			if (numPairs >= 2) {
				return Rank.TWO_PAIRS;
			}

			if (numPairs == 1) {
				return Rank.SINGLE_PAIR;
			}

			return Rank.HIGH_CARD;
		}
	}

	/******************* Helper Methods for Rank **************************/

	/**
	 * Find the number of times each suit occurs in the hand.
	 * 
	 * @return num for each suit.
	 */
	private void suitCount() {
		suitCount = new int[4];
		for (int index = 0; index < hand.length; index++) {
			suitCount[hand[index].getSuit().getSuitNum()]++;
		}
	}

	/**
	 * Find the number of times each value occurs in the hand.
	 * 
	 * @return num for each val.
	 */
	private void valueCount() {
		valueCount = new int[14];
		for (int index = 0; index < hand.length; index++) {
			valueCount[hand[index].getValue() - 1]++;
			if (hand[index].getValue() == 13) {
				valueCount[0]++;
			}
		}
	}

	/**
	 * Check if flush is present.
	 * 
	 * @return if flush.
	 */
	private boolean isFlush() {
		for (int index = 0; index < 4; index++) {
			if (suitCount[index] >= 5) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Check if straight is present.
	 * 
	 * @return if straight present.
	 */
	private boolean isStraight() {
		boolean isStraight = false;

		for (int index = 13; index >= 4; index--) {
			if (valueCount[index] != 0) {
				for (int straightCount = index-1; straightCount >= index - 4 && straightCount >= 0; straightCount--) {
					if (valueCount[straightCount] == 0) {
						isStraight = false;
						break;
					}
					isStraight = true;
				}
			}

			if (isStraight) {
				valStraightStart = index;
				return true;
			}
		}
		return false;
	}

	/**
	 * Check if royal straight is present.
	 * 
	 * @return if royal straight.
	 */
	private boolean isRoyalFlush() {
		return valStraightStart == 13;
	}

	/**
	 * 
	 * @return
	 */
	private boolean isFourOfAKind() {
		for (int index = 1; index < 14; index++) {
			if (valueCount[index] >= 4) {
				fourOfAKindStart = index;
				return true;
			}
		}
		return false;
	}

	private void countOfThreesAndPairs() {
		for (int index = 1; index < 14; index++) {
			if (valueCount[index] == 3) {
				numThreeOfAKind++;
				threeOfAKindStart = index;
			} else if (valueCount[index] == 2) {
				numPairs++;
				lowPairStart = highPairStart;
				highPairStart = index;
			}
		}
	}

	/**
	 * To string for hand.
	 */
	public String toString() {
		String toString = "";
		for (int index = 0; index < hand.length; index++) {
			toString += hand[index].toString() + "\n";
		}
		return toString;
	}

}
