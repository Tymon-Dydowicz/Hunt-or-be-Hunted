package environment;

/**
 * Wilderness is a subclass of abstract class Environment,
 * This class is another filler object of the board, it can be used by predators to traverse the board but does not allow prey to use them
 */
public class Wilderness extends Environment{
    public Wilderness(int x, int y){
        super(x, y, 0, "WD", Integer.MAX_VALUE);
    }
}
