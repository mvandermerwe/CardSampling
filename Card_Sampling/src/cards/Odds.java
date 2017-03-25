/**
 * Mark Van der Merwe and Thomas Oh
 */
package cards;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;

/**
 * 
 * @author markvandermerwe
 *
 */
public class Odds {

	private static Random_Generator randomGenerator;

	// Store exhaustively calculated percentages for comparison.
	private static double[] exhaustivePercentages;

	// Some variables that define test iteration amounts.
	final static int START_TEST_COUNT = 100;
	final static int END_TEST_COUNT = 2_000_000;
	final static int INCREMENT = 200_000;

	public static void main(String[] args) {
		// Define our randomGenerator to use.
		randomGenerator = new My_Best_Random_Generator();

		// Below we have our tests to determine error given different stochastic
		// samples.
		performExhaustiveTests(7);
		performStochasticTests(7);

		// Test timing of stochastic tests.
		// timeStochasticTests(5);
		// timeExhaustiveTests(7);

		// Try all possible hands within scope against some random ones.
		//testExhaustiveTexasHand(39, 52, 39, 52, false);
		//testExhaustiveTexasHand(39, 52, 26, 39, true);

		// Test out Texas Hold'Em card examples from Analysis doc.
		//Ace,Ace v. King, King
		//System.out.println(odds_to_win(12,24,11,23, 1_000_000));
		//Ace,Ace v. Ace, King
		//System.out.println(odds_to_win(12,24,36,35, 1_000_000));
		//2,8 v. Ace, Ace
		//System.out.println(odds_to_win(0,6,12,24, 1_000_000));
	}

	/**
	 * Prints the data in a double array to console - used for debugging.
	 * 
	 * @param data
	 *            - double array to print.
	 */
	public static void printData(double[] data) {
		for (int index = 0; index < data.length; index++) {
			System.out.println(data[index]);
		}
	}

	/**
	 * Checks and ranks every possible combination of cards and calculates the
	 * probability of each rank.
	 */
	private static void performExhaustiveTests(int handSize) {
		exhaustivePercentages = percentage_per_hand_category_exhaustive(handSize);

		// System.out.println("Exhaustive Tests:");
		// for (double percent : exhaustivePercentages) {
		// System.out.println(percent);
		// }
	}

	/**
	 * Runs a set number of tests to find the probability of each rank using
	 * stochastic sampling.
	 * 
	 * @param handSize
	 *            - size of the hand to test on.
	 */
	private static void performStochasticTests(int handSize) {
		StringBuilder stochasticData = new StringBuilder();

		for (int runs = START_TEST_COUNT; runs < END_TEST_COUNT; runs += INCREMENT) {
			double error = 0.0;

			// At each size, run analysis 100 times to determine average.
			for (int test = 0; test < 100; test++) {
				double[] percentages = percentage_per_hand_category_stochastic(handSize, runs);

				// Sum up the error by finding the difference between the exact
				// percentage and estimated percentage.
				for (int rank = 0; rank < 10; rank++) {
					error += Math.abs(exhaustivePercentages[rank] - percentages[rank]) / 100;
				}
			}

			stochasticData.append(runs + "," + error + "\n");
			System.out.println(runs + "," + error);
		}

		sendToFile(stochasticData, "stochasticPoor.csv");
	}

	/**
	 * Times a set number of tests to find the probability of each rank
	 */
	private static void timeStochasticTests(int cardSize) {
		StringBuilder timeData = new StringBuilder();

		// At each test count size, time how long the test takes to run.
		for (int runs = START_TEST_COUNT; runs < END_TEST_COUNT; runs += INCREMENT) {
			long startTime = System.nanoTime();
			percentage_per_hand_category_stochastic(cardSize, runs);
			long endTime = System.nanoTime();
			double time = (endTime - startTime) / Math.pow(10, 9);

			timeData.append(runs + "," + time + "\n");
			System.out.println(runs + "," + time);
		}
		// Send data to file.
		sendToFile(timeData, "time" + cardSize + ".csv");
	}

	/**
	 * Times a set number of tests to find the probability of each rank
	 */
	private static void timeExhaustiveTests(int cardSize) {
		double time = 0.0;

		long startTime = System.nanoTime();
		percentage_per_hand_category_exhaustive(cardSize);
		long endTime = System.nanoTime();
		time += (endTime - startTime) / Math.pow(10, 9);

		System.out.println("Exhaustive: " + time);
	}

	private static void testExhaustiveTexasHand(int card1Start, int card1End, int card2Start, int card2End,
			boolean diffSuite) {
		StringBuilder cardData = new StringBuilder();
		Integer[] nums = new Integer[4];

		// Using the parameters, go through the card possibilites.
		for (int card1 = card1Start; card1 < card1End; card1++) {
			for (int card2 = card2Start; card2 < card2End; card2++) {
				// No repeats, lets use reuse code.
				if (!diffSuite && (card2 <= card1)) {
					continue;
				}
				// Use sets to make sure there are no duplicates.
				LinkedHashSet<Integer> numbers = new LinkedHashSet<>();
				double oddsToWin = 0;

				for (int test = 0; test < 1500; test++) {
					numbers.add(card1);
					numbers.add(card2);

					// Generate our other random hand to test against.
					while (numbers.size() < 4) {
						numbers.add(randomGenerator.next_int(52));
					}

					nums = numbers.toArray(nums);
					// For the given random hand compared to the set hand,
					// generate odds of winning.
					oddsToWin += odds_to_win(card1, card2, nums[2], nums[3], 10_000) / 1500;

					numbers.clear();
				}
				System.out.println(card1 + "," + card2 + ": " + oddsToWin);
				cardData.append(String.valueOf(card1) + String.valueOf(card2) + "," + oddsToWin + "\n");

			}
		}
		if (diffSuite) {
			sendToFile(cardData, "card" + "Heart" + ".csv");
		} else {
			sendToFile(cardData, "card" + "Spades" + ".csv");
		}

	}

	/**
	 * Takes in two specific hands and runs multiple games of random cards
	 * against the same hand and returns the percentage of times hand one wins
	 * over hand two
	 * 
	 * @param h1c1
	 *            Card one to hand one
	 * @param h1c2
	 *            Card two to hand one
	 * @param h2c1
	 *            Card one to hand two
	 * @param h2c2
	 *            Card two to hand two
	 * @param samples
	 *            number of tests
	 * @return chance one hand winning over the other
	 */
	static double odds_to_win(int h1c1, int h1c2, int h2c1, int h2c2, int samples) {
		int[] handOneCards = new int[] { h1c1, h1c2 };
		int[] handTwoCards = new int[] { h2c1, h2c2 };
		Deck deck = new Deck();

		int handsWon = 0;

		for (int run = 0; run < samples; run++) {
			Hand hand1 = null;
			Hand hand2 = null;
			try {
				hand1 = new Hand(7, deck);
				hand2 = new Hand(7, deck);
			} catch (Exception e) {
				e.printStackTrace();
			}

			// Get the random cards (in addition to the four defined).
			Hand.getTwoRandomHands(randomGenerator, hand1, hand2, handOneCards, handTwoCards);
			// Compare the hands, determine if the hand won.
			if (Hand.compareTwoHands(hand1, hand2) > 0) {
				handsWon++;
			}
		}

		return (double) handsWon / samples;
	}

	/**
	 * Runs through every hand and calculates the ratio of the amount of times
	 * that rank appears compared to the total number of possible of hands.
	 * 
	 * @param hand_size
	 *            size of hand
	 * @return The probability of each rank
	 */
	static double[] percentage_per_hand_category_exhaustive(int hand_size) {
		Deck deck = new Deck();
		double[] ranks = new double[10];
		Hand hand = null;

		// Go thorugh each possible hand combination.
		for (int index1 = 0; index1 < 52; index1++) {
			for (int index2 = index1 + 1; index2 < 52; index2++) {
				for (int index3 = index2 + 1; index3 < 52; index3++) {
					for (int index4 = index3 + 1; index4 < 52; index4++) {
						for (int index5 = index4 + 1; index5 < 52; index5++) {
							if (hand_size == 5) {
								Integer[] cards = new Integer[] { index1, index2, index3, index4, index5 };
								try {
									hand = new Hand(hand_size, deck);
								} catch (Exception e) {
									System.out.println("Illegal hand size.");
									e.printStackTrace();
								}
								// Create our hands, determine rank, adjust
								// histogram.
								hand.createHandFromNumbers(cards);
								ranks[hand.getRank().getRankNum()]++;
							} else if (hand_size == 7) {
								for (int index6 = index5 + 1; index6 < 52; index6++) {
									for (int index7 = index6 + 1; index7 < 52; index7++) {
										Integer[] cards = new Integer[] { index1, index2, index3, index4, index5,
												index6, index7 };
										try {
											hand = new Hand(hand_size, deck);
										} catch (Exception e) {
											System.out.println("Illegal hand size.");
											e.printStackTrace();
										}
										// Create our hands, determine rank, adjust
										// histogram.
										hand.createHandFromNumbers(cards);
										ranks[hand.getRank().getRankNum()]++;
									}
								}
							}
						}
					}
				}
			}
		}

		// Divide each count by the number of possible hands.
		for (int index = 0; index < 10; index++) {
			if (hand_size == 5) {
				ranks[index] = ranks[index] / 2598960;
			} else {
				ranks[index] = ranks[index] / 133784560;
			}
		}

		return ranks;
	}

	/**
	 * Calculates the ratio of each rank based on multiple stochastic tests.
	 * 
	 * @param hand_size
	 *            size of hand
	 * @param random_samples
	 *            number of random hands to create
	 * @return The probability of each rank stochastically
	 */
	static double[] percentage_per_hand_category_stochastic(int hand_size, int random_samples) {
		Deck deck = new Deck();
		double[] ranks = new double[10];
		Hand hand = null;

		for (int test = 0; test < random_samples; test++) {
			try {
				hand = new Hand(hand_size, deck);
			} catch (Exception e) {
				e.printStackTrace();
			}
			hand.getRandomHand(randomGenerator);
			ranks[hand.getRank().getRankNum()]++;
		}

		// Divide each count by the number of possible hands.
		for (int index = 0; index < 10; index++) {
			ranks[index] = ranks[index] / random_samples;
		}

		return ranks;
	}

	/**
	 * Helper method for writing our data to a CSV file.
	 * 
	 * @param fileData
	 *            - the data to be put into the file.
	 * @param filename
	 *            - the name of the file to write to.
	 */
	private static void sendToFile(StringBuilder fileData, String filename) {
		try {
			FileWriter csvWriter = new FileWriter(filename);
			csvWriter.write(fileData.toString());
			csvWriter.close();
		} catch (IOException e) {
			System.out.println("Unable to write to file. Here is the test data, though:");
			System.out.print(fileData.toString());
		}
	}
}
