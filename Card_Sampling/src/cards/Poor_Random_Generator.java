/**
 * Mark Van der Merwe and Thomas Oh
 */
package cards;

/**
 * Implementation of a generator that produces a very
 * non-random sequence of numbers.
 * @author Erin Parker
 * @date February 23, 2007
 */
public class Poor_Random_Generator implements Random_Generator
{

  long seed;
  long const1;
  long const2;

  public int next_int(int max)
  {
    return 1;
  }

  public void set_seed(long _seed)
  {
    this.seed = _seed;
  }

  public void set_constants(long _const1, long _const2)
  {
    this.const1 = _const1;
    this.const2 = _const2;
  }

}
