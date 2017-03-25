/**
 * Mark Van der Merwe and Thomas Oh
 */
package cards;

/**
 * Implementation of a generator that produces a very non-random sequence of
 * numbers. Attempts to use Linear Congruential method.
 * 
 * @author Erin Parker
 * @date February 23, 2007
 */
public class Poor_Random_Generator implements Random_Generator {

	private long multiplier = 6;
	private long increment = 1135;
	
	private long seed;
	
	/**
	 * Finds next integer based on formula above.
	 * 
	 * @param max The largest the next integer can be
	 * @return next random integer
	 */
	@Override
	public int next_int(int max) {
		seed = (multiplier * seed + increment) % max;
		return (int) seed;
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
