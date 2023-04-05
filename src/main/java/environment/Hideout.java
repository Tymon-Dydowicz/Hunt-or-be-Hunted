package environment;

/**
 * A Hideout is subclass of abstract class Environment,
 * This object is used by Prey objects to hide from Predator objects and reproduce
 */
public class Hideout extends Environment {
    /**
     * This parameter determines how many living objects can be in hideout if the number of living objects is the same
     * as this parameter the parameter of parent class full gets set to True not allowing more living objects enter
     */
    public Hideout(int x, int y,String name, int capacity){
        super(x, y, 0, name, capacity);
    }
}
