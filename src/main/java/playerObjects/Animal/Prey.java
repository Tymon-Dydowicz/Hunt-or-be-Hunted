package playerObjects.Animal;

import environment.Environment;
import environment.Hideout;
import environment.Resources.Plant;
import environment.Resources.WaterSource;

import java.util.*;


/**
 * Prey is a subclass of abstract class Animal, objects of this class can not fight against Predators.
 * Prey objects use Resource objects to drink and eat, and Hideout objects to hide from predators and reproduce
 */
public class Prey extends Animal implements Runnable {
    /**
     * This parameter determines how much the thirst level increases with each turn
     */
    private int waterNeed;
    /**
     * This parameter determines how much the hunger level increases with each turn
     */
    private int foodNeed;
    /**
     * This parameter defines how thirsty the Prey object is. If it's too high it will die.
     */
    private int thirst;
    /**
     * This parameter defines how hungry the Prey object is. If it's too high it will die.
     */
    private int hunger;
    /**
     * This parameter determines whether the Prey object is visible to Predator objects and thus susceptible to attack
     */
    private boolean hidden;
    /**
     * This is max health of the animal
     */
    private int maxHealth;

    /**
     * The board it traverses
     */
    private Environment[][] board;
    /**
     * Rest of the preys
     */
    private List<Prey> preys;
    /**
     * How much time has passed since last reproduce
     */
    private int timer;

    public Prey(int x, int y, Environment[][] board, List<Prey> preys){
        super(x, y, "Prey", "Necro", 100, 2);
        Random randomizer = new Random();
        this.waterNeed = randomizer.nextInt(2, 10);
        this.foodNeed = randomizer.nextInt(2, 10);
        this.thirst = 10;
        this.hunger = 10;
        this.hidden = false;
        this.board = board;
        this.maxHealth = randomizer.nextInt(30, 70);
        this.preys = preys;
        this.timer = 150;
    }

    /**
     * @return Retruns max Health
     */
    public int getMaxHealth() {
        return maxHealth;
    }

    /**
     * @return Retns current water level
     */
    public int getThirst() {
        return thirst;
    }

    /**
     * @param thirst Sets current water level
     */
    public void setThirst(int thirst) {
        this.thirst = thirst;
    }

    /**
     * @param amount Decreases the water level by amount
     */
    public void decreaseThirst(int amount){
        this.setThirst(Math.max(this.getThirst() - amount, -10));
    }

    /**
     * @return Retruns food level
     */
    public int getHunger() {
        return hunger;
    }

    /**
     * @param hunger Sets food level
     */
    public void setHunger(int hunger) {
        this.hunger = hunger;
    }

    /**
     * @param amount Decreases food level by amount
     */
    public void decreaseHunger(int amount){
        this.setHunger(Math.max(this.getHunger() - amount, -10));
    }

    /**
     * @return Returns whether the object is hidden or not
     */
    public boolean isHidden() {
        return hidden;
    }

    /**
     * @param hidden Sets the object hidden value
     */
    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    /**
     * If prey is nearby a WaterSource it can drink to lower its thirst
     */
    public void drink(){
        while (!Thread.interrupted() && this.getThirst() < 100 && this.getHealth() > 0){
            WaterSource WS = (WaterSource) this.board[this.getX_pos()][this.getY_pos()];
            this.setThirst(this.getThirst() + WS.takeWater(2*this.waterNeed));

            this.wait(1000/this.getSpeed());
        }
    }

    /**
     * If prey is nearby a Plant it can eat it to lower its hunger
     */
    public void eat(){
        while (!Thread.interrupted() && this.getHunger() < 100 && this.getHealth() > 0){
            Plant plant = (Plant) this.board[this.getX_pos()][this.getY_pos()];
            this.setHunger(this.getHunger() + plant.takeFood(2*this.foodNeed));

            this.wait(1000/this.getSpeed());
        }
    }

    /**
     * While prey is not hungry and thirsty and is hidden inside a Hideout it can reproduce making another copy of itsels
     */
    public void reproduce(){
        System.out.println("reproducing");
        Prey prey = new Prey(this.getX_pos(), this.getY_pos(), board, preys);
        this.board[this.getX_pos()][this.getY_pos()].increaseCurrentlyIn();
        preys.add(prey);
        Thread t = new Thread(prey);
        t.start();
        this.timer = 0;
    }

    private void tick(){
        this.wait(1000/this.getSpeed());
        if(this.getHealth() <= 0){
            System.out.println("INTERUTPTED");
            Thread.currentThread().interrupt();
        }
        if (this.getHunger() > 50 && this.getThirst() > 50){
            this.setHealth(Math.min(this.getHealth() + 1, getMaxHealth()));
        }
        if(this.getThirst() <= 0 || this.getHunger() <= 0){
            this.setHealth(this.getHealth() - 1);
        }
        decreaseHunger(this.foodNeed);
        decreaseThirst(this.waterNeed);
    }

    /**
     * @param target Prey moves towards target using only path files
     */
    public void move(String target){
        int[] coords;
        Stack<int []> path = optimizedCreatePath(target);
        this.setHidden(false);
        while(!path.isEmpty()){
            coords = path.pop();
            while (!Thread.interrupted() && !this.board[coords[0]][coords[1]].checkAndOccupy() && this.getHealth() > 0){
                this.wait(1000/this.getSpeed());
            }
            this.board[coords[0]][coords[1]].decreaseCurrentlyIn();
            this.setX_pos(coords[0]);
            this.setY_pos(coords[1]);
            this.tick();
            if(Thread.interrupted()){
                Thread.currentThread().interrupt();
                break;
            }
            this.wait(1000/this.getSpeed());
        }
    }

    /**
     * @param target Where the prey wants to get
     * @return returns the path to the target
     * Generates a path to the target from current position using DFS approach
     */
    public Stack<int[]> optimizedCreatePath(String target) {
        int x, y;
        int[] start;
        Stack<int []> finalPath = new Stack<>();
        ArrayList<int[]> neighbours;
        Queue<int[]> toCheck = new LinkedList<>();
        HashSet<String> visited = new HashSet<String>();
        HashMap<String, String> paths = new HashMap<String, String>();

        x = this.getX_pos();
        y = this.getY_pos();
        start = new int[] {x, y};
        toCheck.add(start);
        paths.put(Arrays.toString(start), null);
        visited.add(Arrays.toString(start));

        while (!toCheck.isEmpty()) {
            int[] coords;
            coords = toCheck.remove();
            x = coords[0];
            y = coords[1];
            if (board[x][y].getName().equals(target)) {
                String current = Arrays.toString(coords);
                while (paths.get(current) != null){
                    finalPath.add(isolateInts(current));
                    current = paths.get(current);
                }
                return finalPath;
            }

            neighbours = new ArrayList<>();
            neighbours.add(new int[]{x - 1, y});
            neighbours.add(new int[]{x + 1, y});
            neighbours.add(new int[]{x, y - 1});
            neighbours.add(new int[]{x, y + 1});
            for (int[] neighbour : neighbours) {
                if (!board[neighbour[0]][neighbour[1]].getName().equals("WD")) {
                    String idx = Arrays.toString(neighbour);
                    if (!visited.contains(idx)) {
                        toCheck.add(neighbour);
                        paths.put(idx, Arrays.toString(coords));
                        visited.add(idx);
                    }
                }
            }
        }
        return null;
    }


    @Override
    public void run(){
        int x, y;
        while (!Thread.interrupted() && this.getHealth() > 0){
            x = this.getX_pos();
            y = this.getY_pos();
            if (Objects.equals(this.board[x][y].getName(), "HO")){
                Hideout hideout = (Hideout) this.board[x][y];
                if (!hideout.checkAndOccupy()){
                    this.setHidden(true);
                }
            }
            else{
                this.setHidden(false);
            }

//            if (this.getThirst() > 90 && this.getHunger() > 90 && this.getHealth() > this.maxHealth*0.9 && this.isHidden() && this.timer > 300){
//                this.reproduce();
//            }

            if (this.getThirst() < 20){
                this.move("WS");

                if(Thread.interrupted()){
                    break;
                }
                this.drink();
            }

            if (this.getHunger() < 20){
                this.move("PL");
                if(Thread.interrupted()){
                    break;
                }
                this.eat();
            }
            if(!Objects.equals(this.board[this.getX_pos()][this.getY_pos()].getName(), "HO")) {
                this.move("HO");
            }
            this.tick();
            this.timer++;
        }
        Thread.currentThread().interrupt();
        preys.remove(this);
        System.out.println("DEAD");
    }
}
