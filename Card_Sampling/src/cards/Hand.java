/**
 * Mark Van der Merwe and Thomas Oh
 */
package cards;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.TreeSet;

/**
 * Hand represents a hand of cards that has a rank and 5 or 7 cards associated
 * with it.
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

		/**
		 * Constructor adds a value to an enum.
		 * 
		 * @param rank
		 *            - val of enum.
		 */
		Rank(int rank) {
			rankNum = rank;
		}

		/**
		 * Get the value of an enum object.
		 * 
		 * @return - val of enum.
		 */
		public int getRankNum() {
			return rankNum;
		}
	}

	private Card[] hand;
	private Deck deck;

	/**
	 * Set size of hand when constructing object.
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

	/**
	 * Creates a hand by using an array with positions that correlate to a cards
	 * location in the deck.
	 * 
	 * @param cardLocations
	 *            array containing the locations of the cards
	 */
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
	 * Generates 5 random cards to compare both hands. Assumes both hands are
	 * size seven and are part of the same deck. Used to analyze Texas Hold'em
	 * 
	 * @param generator
	 *            random generator to pick cards
	 * @param handOne
	 * @param handTwo
	 */
	public static void getTwoRandomHands(Random_Generator generator, Hand handOne, Hand handTwo, int[] handOneCards,
			int[] handTwoCards) {
		Integer[] nums = new Integer[9];

		// Use sets to make sure there are no duplicates.
		LinkedHashSet<Integer> numbers = new LinkedHashSet<>();

		// Set first cards in hands based on the provided card indices. Also add
		// them to set so no duplicates occur.
		for (int index = 0; index < 2; index++) {
			handOne.hand[index] = handOne.deck.getCard(handOneCards[index]);
			numbers.add(handOneCards[index]);
			handTwo.hand[index] = handTwo.deck.getCard(handTwoCards[index]);
			numbers.add(handTwoCards[index]);
		}

		// Randomly generate and place the five cards to go with the two hands.
		while (numbers.size() < 9) {
			numbers.add(generator.next_int(52));
		}
		nums = numbers.toArray(nums);

		for (int index = 4; index < 9; index++) {
			Card card = handOne.deck.getCard(nums[index]);
			handOne.hand[index - 2] = card;
			handTwo.hand[index - 2] = card;
		}
	}

	/**
	 * Compares the cards between two hands and determines which one wins. A
	 * positive value will be returned if hand one wins, negative if hand two
	 * wins, and zero for a draw. If ranks are a draw, kickers or high card will
	 * be checked if possible.
	 * 
	 * @param handOne
	 * @param handTwo
	 * @return result of one hand versus the other
	 */
	public static int compareTwoHands(Hand handOne, Hand handTwo) {
		// Get rank and compare.
		int diff = handTwo.getRank().getRankNum() - handOne.getRank().getRankNum();

		if (diff > 0) {
			// If the difference between rank is positive, hand one wins, return
			// >0.
			return 1;
		} else if (diff < 0) {
			// If negative, hand one loses.
			return -1;
		} else {
			// Given a tie, we handle several ways.
			switch (handOne.getRank()) {
			// Royal flush just ties.
			case ROYAL_FLUSH:
				return 0;
			// Straight flush - compare the highest card in the straight.
			case STRAIGHT_FLUSH:
				return handOne.valStraightStart - handTwo.valStraightStart;
			// Four of a kind - compare the four of a kind by the val of which
			// each has four.
			case FOUR_OF_A_KIND:
				return handOne.fourOfAKindStart - handTwo.fourOfAKindStart;
			// Compare first the three of a kind and then the pair for a tied
			// full house.
			case FULL_HOUSE:
				diff = handOne.threeOfAKindStart - handTwo.threeOfAKindStart;
				if (diff != 0) {
					return diff;
				} else {
					return handOne.highPairStart = handTwo.highPairStart;
				}
				// Compare the flush high card.
			case FLUSH:
				return handOne.flushStart - handTwo.flushStart;
			// Compare the straight high card.
			case STRAIGHT:
				return handOne.valStraightStart - handTwo.valStraightStart;
			// Compare the three of a kind high card.
			case THREE_OF_A_KIND:
				return handOne.threeOfAKindStart - handTwo.threeOfAKindStart;
			// Compare the high pair high card.
			case TWO_PAIRS:
				diff = handOne.highPairStart - handTwo.highPairStart;
				if (diff != 0) {
					return diff;
				} else {
					return handOne.lowPairStart - handTwo.lowPairStart;
				}
				// Compare the highest pair.
			case SINGLE_PAIR:
				diff = handOne.highPairStart - handTwo.highPairStart;
				if (diff != 0) {
					return diff;
				} else {
					for (int index = 13; index >= 0; index--) {
						if (handOne.valueCount[index] == 1 && handTwo.valueCount[index] == 0) {
							return 1;
						} else if (handOne.valueCount[index] == 0 && handTwo.valueCount[index] == 1) {
							return -1;
						}
					}
					return 0;
				}
				// Compare the highest card.
			default:
				for (int index = 13; index >= 0; index--) {
					if (handOne.valueCount[index] == 1 && handTwo.valueCount[index] == 0) {
						return 1;
					} else if (handOne.valueCount[index] == 0 && handTwo.valueCount[index] == 1) {
						return -1;
					}
				}
			}
		}
		return 0;
	}

	// Instance variables to help determine rank.
	private int[] suitCount;
	private int[] valueCount;
	private int numThreeOfAKind = 0;
	private int numPairs = 0;

	private int valStraightStart;
	private int flushStart = -1;
	private int fourOfAKindStart;
	private int threeOfAKindStart;
	private int highPairStart;
	private int lowPairStart;

	/**
	 * Find the rank of the hand
	 * 
	 * @return Rank of the hand
	 */
	public Rank getRank() {
		//Count the number of each suit and value.
		suitCount();
		valueCount();

		// This is all pretty self explanatory by the name of the functions.
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
			if (hand[index].getValue() == 14) {
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
				for (int ind = 0; ind < this.hand.length; ind++) {
					if (hand[ind].getSuit().getSuitNum() == index) {
						if (hand[ind].getValue() > flushStart) {
							flushStart = hand[ind].getValue();
						}
					}
				}
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
				for (int straightCount = index - 1; straightCount >= index - 4 && straightCount >= 0; straightCount--) {
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
	 * Checks for four of a kind
	 * 
	 * @return result of check
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

	/**
	 * Tracks numbers of three of kinds and pairs. Also keeps track of the
	 * largest three of a kind and high pair.
	 */
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
	 * 
	 * @return string for hand
	 */
	public String toString() {
		String toString = "";
		for (int index = 0; index < hand.length; index++) {
			toString += hand[index].toString() + "\n";
		}
		return toString;
	}

}
