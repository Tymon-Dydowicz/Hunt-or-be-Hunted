package environment.Resources;

/**
 * This class is a subclass of abstract class Resources,
 * It is used by Prey objects to decrease their thirst level
 */
public class WaterSource extends Resources {
    /**
     *How much water is currently available in water source
     */
    private int waterSupply;

    public WaterSource(int x, int y, String name, int replenishingSpeed, int maxNumber){
        super(x, y, name, replenishingSpeed, maxNumber, 1);
        this.waterSupply = Integer.MAX_VALUE;
    }

    /**
     * @param amount how much the prey object will drink
     * @return how much the prey object drank
     */
    synchronized
    public int takeWater(int amount){
        if(this.getWaterSupply() > amount){
            this.setWaterSupply(this.getWaterSupply() - amount);
            return amount;
        }
        return 0;
    }

    /**
     * @return Returns how much water is left in water source
     */
    synchronized
    public int getWaterSupply() {
        return waterSupply;
    }

    /**
     * @param waterSupply Sets the amount of water in water source
     */
    synchronized
    public void setWaterSupply(int waterSupply) {
        this.waterSupply = waterSupply;
    }
}
