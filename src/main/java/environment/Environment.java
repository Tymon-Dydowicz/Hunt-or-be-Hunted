package environment;

import boardView.Drawable;

/**
 * Every non-living object that is present on the map is an environment, board is created of objects of this type
 */
public abstract class Environment extends Drawable {
    /**
     *  This parameter determines whether another living object can enter this spot on the map which is by default set to false while generating
     */
    private boolean full;
    /**
     * This parameter allows Prey objects to differentiate between normal environment objects and objects they can use to feed or drink
     */
    private int isResource;
    private final String name;
    /**
     * Determines how many animal objects can enter this environment object
     */
    private int capacity;
    /**
     * Specifies how many animal objects are currently in this enviroment object
     */
    private int currentlyIn;

    protected Environment(int x, int y, int isResource, String name, int capacity){
        super(x, y);
        this.name = name;
        this.full = false;
        this.isResource = isResource;
        this.capacity = capacity;
        this.currentlyIn = 0;
    }

    /**
     * @return returns whether there are more animals in this environment object than the capacity allows
     */
    synchronized
    public boolean isFull() {
        return full;
    }

    /**
     * @return used to differentiate between different environment objects
     */
    public String getName(){
        return this.name;
    }
    /**
     * @return Returns what is the capacity of given environment object
     */
    public int getCapacity() {
        return capacity;
    }
    /**
     * @param capacity Sets the capacity
     */
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    /**
     * @return Returns how many animal objects are currently in environment object
     */
    synchronized
    public int getCurrentlyIn() {
        return currentlyIn;
    }

    /**
     * @param currentlyIn Sets how many animal objects are currently in environment object
     */
    synchronized
    public void setCurrentlyIn(int currentlyIn) {
        this.currentlyIn = currentlyIn;
    }
    /**
     * A method that increases currently in by one
     */
    synchronized
    public void increaseCurrentlyIn(){
        this.setCurrentlyIn(this.getCurrentlyIn() + 1);
    }

    /**
     * A method that decreases currently in by one
     */
    synchronized
    public void decreaseCurrentlyIn(){
        this.setCurrentlyIn(this.getCurrentlyIn() - 1);
    }

    /**
     * @return Returns true or false
     * This method is synchronized and very important it lets the prey know whether it's possible to move to this tile
     */
    synchronized
    public boolean checkAndOccupy(){
        if (this.currentlyIn < this.capacity){
            this.increaseCurrentlyIn();
            return true;
        }
        return false;
    }
}
