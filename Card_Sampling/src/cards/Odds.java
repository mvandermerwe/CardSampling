/**
 * Mark Van der Merwe and Thomas Oh
 */
package cards;

/**
 * 
 * @author markvandermerwe
 *
 */
public class Odds {

	public static void main(String[] args) {
		double total = 0;
		double[] percentages = percentage_per_hand_category_exhaustive(5);
		
		for(double percent: percentages) {
			System.out.println(percent);
			total += percent;
		}
		System.out.println("Total: " + total);
	}

	static double odds_to_win(int h1c1, int h1c2, int h2c1, int h2c2, int samples) {
		return -1;
	}

	static double[] percentage_per_hand_category_exhaustive(int hand_size) {
		Deck deck = new Deck(false);
		double[] ranks = new double[10];

		for (int index1 = 0; index1 < 52; index1++) {
			for (int index2 = index1 + 1; index2 < 52; index2++) {
				for (int index3 = index2 + 1; index3 < 52; index3++) {
					for (int index4 = index3 + 1; index4 < 52; index4++) {
						for (int index5 = index4 + 1; index5 < 52; index5++) {
							Hand hand = new Hand(index1, index2, index3, index4, index5, deck);

							ranks[hand.getRank().getRankNum()]++;
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
		return null;
	}
}
