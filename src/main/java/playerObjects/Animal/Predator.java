package playerObjects.Animal;

import environment.Environment;

import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * Predator is a subclass of abstract class Animal, objects of this class hunt for Prey objects in order to get food
 */
public class Predator extends Animal implements Runnable{
    /**
     * This parameter determines whether Predator is currently interested in hunting Prey objects
     * If it's hunting Prey objects are in danger. If it's relaxing, Predators are not going to ensue in a pursuit
     */
    private String mode;
    /**
     * List of rest of the predators
     */
    private List<Predator> predators;
    /**
     * List of all preys used to know which one to attack
     */
    private List<Prey> preys;
    /**
     * The board it traverses
     */
    private Environment[][] board;

    public Predator(int x, int y, Environment[][] board, List<Predator> predators, List<Prey> preys){
        super(x, y, "Predator", "Necro", 70, 10);
        this.mode = "Hunt";
        this.board = board;
        this.predators = predators;
        this.preys = preys;

    }

    /**
     * @return Returns current mode of the predator
     */
    public String getMode() {
        return mode;
    }

    /**
     * Predator does random moves for a random amount of time
     */
    public void randomWalk(){
        Random randomizer = new Random();
        int r = randomizer.nextInt(4);
        int r2 = randomizer.nextInt(5);
        if (r == 0 && !Objects.equals(board[this.getX_pos() + 1][this.getY_pos()].getName(), "MT")) {
            this.setX_pos(this.getX_pos() + 1);
        }
        if (r == 1 && !Objects.equals(board[this.getX_pos() - 1][this.getY_pos()].getName(), "MT")) {
            this.setX_pos(this.getX_pos() - 1);
        }
        if (r == 2 && !Objects.equals(board[this.getX_pos()][this.getY_pos() + 1].getName(), "MT")) {
            this.setY_pos(this.getY_pos() + 1);
        }
        if (r == 3 && !Objects.equals(board[this.getX_pos() + 1][this.getY_pos() - 1].getName(), "MT")){
            this.setY_pos(this.getY_pos() - 1);
        }

        if (r2 == 0){
            this.setMode("Hunt");
        }
        wait(2000);
    }

    /**
     * @param mode Sets the mode
     */
    public void setMode(String mode) {
        this.mode = mode;
    }

    /**
     * Predator Objects attacks Prey Object if they are 1 cell away, decreasing its health by Predators strength
     * @param attackedPrey Object of class Prey that was close enough to this Predator object
     */
    public void attack(Prey attackedPrey){
        attackedPrey.setHealth(Math.max(attackedPrey.getHealth() - Math.max(this.getStrength()-attackedPrey.getStrength(), 0), 0));
    }

    /**
     * This method is called when Predator is hunting, it selects the closest non hidden prey object and moves towards it,
     * if it's sufficiently close it will start dealing damage
     */
    public void Hunt(){
        int x, y, d, min;
        x = this.getX_pos();
        y = this.getY_pos();
        Prey hunted = null;
        boolean broken = false;
        min = Integer.MAX_VALUE;
        for (Prey prey : preys) {
            if (!prey.isHidden() && prey.getHealth() > 0){
                d = Math.abs(x - prey.getX_pos()) + Math.abs(y - prey.getY_pos());
                if (d < min){
                    min = d;
                    hunted = prey;
                }
            }
        }
        if (hunted == null) {
            return;
        }
        while(hunted != null) {
            while (Math.abs(x - hunted.getX_pos()) + Math.abs(y - hunted.getY_pos()) > 1) {
                int xHunted = hunted.getX_pos();
                int yHunted = hunted.getY_pos();
                x = this.getX_pos();
                y = this.getY_pos();
                if (hunted.isHidden()){
                    broken = true;
                    break;
                }
                if (x < xHunted) {
                    this.setX_pos(x + 1);
                } else if (x > xHunted) {
                    this.setX_pos(x - 1);
                } else if (y < yHunted) {
                    this.setY_pos(y + 1);
                } else if (y > yHunted) {
                    this.setY_pos(y - 1);
                }
                wait(1000/this.getSpeed());
            }
            this.attack(hunted);
            if (broken){
                break;
            }
            if(hunted.getHealth() <= 0){
                hunted = null;
                this.setMode("Relax");
            }
            wait(1000/this.getSpeed());
        }
    }

    @Override
    public void run() {
        while (true) {
            if (Objects.equals(this.getMode(), "Relax")) {
                this.randomWalk();
            } else if (Objects.equals(this.getMode(), "Hunt")) {
                this.Hunt();
            }
            if(preys.isEmpty()){
                System.out.println("DIED FROM HUNGER");
                predators.remove(this);
                Thread.currentThread().interrupt();
                break;
            }
            wait(1000/this.getSpeed());
        }

    }
}
