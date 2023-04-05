package environment;

/**
 * Path is a subclass of abstract class Environment,
 * This class of objects is used by Prey objects to traverse the map
 */
public class Path extends Environment{
    /**
     * This parameter determines how much the speed of moveable objects is increased/decreased
     */
    private String type;
    public Path(int x, int y, String type){
        super(x,  y, 0, "PA", Integer.MAX_VALUE);
        this.type = type;
    }
}
