/**
 * Mark Van der Merwe and Thomas Oh
 */
package cards;

/**
 * Creates a generator that follow the formula Xnext = (a * X + b) % m
 * Where X is the integer, a is the multiplier, b is the increment, and m is the mask.
 */
public class My_Best_Random_Generator implements Random_Generator {

	private long multiplier = 0x5DEECE66DL;
	private long increment = 0xBL;
	
	private static final long mask = (1L << 48) - 1;
	
	private long seed;
	
	/**
	 * Finds next integer based on formula above.
	 * 
	 * @param max The largest the next integer can be
	 * @return next random integer
	 */
	@Override
	public int next_int(int max) {
		seed = ((multiplier * seed + increment) & mask);
		return (int) (seed % max);
	}

	/**
	 * Sets the seed of the generator
	 * 
	 * @param seed the seed to set
	 */
	@Override
	public void set_seed(long seed) {
		this.seed = seed;
	}

	/**
	 * sets the constants used in the equation to calculate the next int
	 * 
	 * @param multiplier
	 * @param increment
	 */
	@Override
	public void set_constants(long multiplier, long increment) {
		this.multiplier = multiplier;
		this.increment = increment;
	}

}
