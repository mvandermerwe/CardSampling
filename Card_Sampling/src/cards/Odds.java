/**
 * Mark Van der Merwe and Thomas Oh
 */
package cards;

import java.io.FileWriter;
import java.io.IOException;

/**
 * 
 * @author markvandermerwe
 *
 */
public class Odds {

	private static Random_Generator randomGenerator;
	
	private static double[] exhaustivePercentages;
	
	final static int START_TEST_COUNT = 100;
	final static int END_TEST_COUNT = 10_000_000;
	final static int INCREMENT = 500_000;

	public static void main(String[] args) {
		// Define our randomGenerator to use.
		randomGenerator = new My_Best_Random_Generator();

		performExhaustiveTests();
		performStochasticTests();
		
		//System.out.println(odds_to_win(13,14,0,1, 1_000_000));
	}

	private static void performExhaustiveTests() {
		exhaustivePercentages = percentage_per_hand_category_exhaustive(5);

//		System.out.println("Exhaustive Tests:");
//		for (double percent : exhaustivePercentages) {
//			System.out.println(percent);
//		}
	}
	
	private static void performStochasticTests() {
		StringBuilder stochasticData = new StringBuilder();
		
		for(int runs = START_TEST_COUNT; runs < END_TEST_COUNT; runs+=INCREMENT) {
			double error = 0;
			
			for(int test = 0; test < 100; test++) {
				double[] percentages = percentage_per_hand_category_stochastic(5, runs);
				
				for(int rank = 0; rank < 10; rank++) {
					error += Math.abs(exhaustivePercentages[rank] - percentages[rank]);
				}
			}
			
			stochasticData.append(runs + "," + error);
			System.out.println(runs + "," + error);
		}

		sendToFile(stochasticData, "stochastic.csv");
	}

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

			Hand.getTwoRandomHands(randomGenerator, hand1, hand2, handOneCards, handTwoCards);
			if (Hand.compareTwoHands(hand1, hand2) > 0) {
				handsWon++;
			}
		}

		return (double) handsWon / samples;
	}

	static double[] percentage_per_hand_category_exhaustive(int hand_size) {
		Deck deck = new Deck();
		double[] ranks = new double[10];
		Hand hand = null;

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
								hand.createHandFromNumbers(cards);
								ranks[hand.getRank().getRankNum()]++;
							} else if (hand_size == 7) {
								for (int index6 = index5 + 1; index6 < 52; index6++) {
									for (int index7 = index6 + 1; index7 < 52; index7++) {
										Integer[] cards = new Integer[] { index1, index2, index3, index4, index5,
												index6, index7 };
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
			ranks[index] = ranks[index] / 2598960;
		}

		return ranks;
	}

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
