/**
 * Mark Van der Merwe and Thomas Oh
 */
package cards;

/**
 * Javas_Random_Generator - build a Random_Generator implemented
 * by simplying wrapping the java.util.Random class
 *
 * @author germain
 * @date   Sep 2005
 * Course  CS2020
 *
 * Class Description:
 *
 * This class uses Java's implementation of a random number
 * generator to implement the Random_Generator interface.
 * It is simply a wrapper to make sure the API for our
 * Random_Generator class meshes with the API for the
 * java.util.Random class.
 * 
 */
public class Javas_Random_Generator implements Random_Generator
{
  /*
   * Create a Java.util.Random object to do the actual "work"
   * of this class.
   */
  private java.util.Random random_generator_ = new java.util.Random();

  /**
   * This function returns a random integer between [0,max)
   * @param max the upper bound for the range of the random number,
   * non-inclusive.
   * @return an integer between [0, max).
   */
  public int next_int(int max)
  {
    return this.random_generator_.nextInt(max);
  }

  /**
   * This function sets a seed for the random number
   * generator.  A random number generator should return
   * the same sequence of numbers for the same seed.
   * @param seed the seed parameter used to initialize the
   * random number generator.
   */
  public void set_seed(long seed)
  {
    this.random_generator_.setSeed(seed);
  }

  /**
   * Not used for Java's Random Generator
   */
  public void set_constants(long _const1, long _const2)
  {
    // not needed
  }
}
