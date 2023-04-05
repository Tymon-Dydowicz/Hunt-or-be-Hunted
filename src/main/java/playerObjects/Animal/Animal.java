package playerObjects.Animal;

import boardView.Drawable;

import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Animal class is our main class for containing living objects that will interact on the board.
 */
public abstract class Animal extends Drawable {
    /**
     * This parameter defines the name of the object
     */
    private String name;
    /**
     * This parameter defines the species of the object
     */
    private String species;
    /**
     * This parameter defines how much health the object has, if this parameter falls below 0 the object is deleted
     */
    private int health;
    /**
     * If two objects of this class fight, this parameter will determine how much health each of them will lose
     */
    private int strength;
    /**
     * This parameter determines how fast is the object able to traverse the board
     */
    private int speed;

    Animal(int x, int y, String name, String specie, int health, int strength){
        super(x, y);
        Random randomizer = new Random();
        this.name = name;
        this.species = specie;
        this.health = health;
        this.strength = strength;
        if(Objects.equals(name, "Prey")){
            this.speed = randomizer.nextInt(2, 5);
        } else if (Objects.equals(name, "Predator")) {
            this.speed = randomizer.nextInt(7, 10);
        }
    }

    /**
     * @return Returns current health
     */
    public int getHealth() {
        return health;
    }

    /**
     * @param health Sets the health of the object
     */
    public void setHealth(int health) {
        this.health = health;
    }

    /**
     * @return Returns strength of the object
     */
    public int getStrength() {
        return strength;
    }

    /**
     * @return Returns the speed of the object
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * @param time How much we want the thread to wait
     */
    public void wait(int time){
        try {
            TimeUnit.MILLISECONDS.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
