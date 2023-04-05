package environment.Resources;

/**
 * Plant is a subclass of abstract class Resources,
 * Objects of this class are used by Prey objects to increase their food level
 */
public class Plant extends Resources {
    /**
     * How much food is currently available in plant
     */
    private int foodSupply;
    public Plant(int x, int y, String name, int replenishingSpeed, int maxNumber){
        super(x, y, name, replenishingSpeed, maxNumber, 2);
        this.foodSupply = Integer.MAX_VALUE;
    }

    /**
     * @param amount How much food the prey object will eat
     * @return how much the object ate
     * Prey objects use this function to eat
     */
    synchronized
    public int takeFood(int amount){
        if(this.getFoodSupply() > amount){
            this.setFoodSupply(this.getFoodSupply() - amount);
            return amount;
        }
        return 0;
    }

    /**
     * @return Returns how much food is left in the plant
     */
    synchronized
    public int getFoodSupply() {
        return foodSupply;
    }

    /**
     * @param foodSupply sets the food amount in the plant
     */
    synchronized
    public void setFoodSupply(int foodSupply) {
        this.foodSupply = foodSupply;
    }
}

