package playerObjects.Animal;

import boardView.Simulation;
import environment.Hideout;
import environment.Resources.Plant;
import environment.Resources.WaterSource;
import playerObjects.Animal.Predator;
import playerObjects.Animal.Prey;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

/**
 * This class is used to replenish the supply of all resources according to number of animals,
 * Calculates how many predators and preys there are and increases each resource supply by that amount every 10s
 */
public class Replenisher implements Runnable{
    /**
     * List of predators
     */
    private CopyOnWriteArrayList<Predator> predators;
    /**
     * List of preys
     */
    private CopyOnWriteArrayList<Prey> preys;
    /**
     * List of water sources
     */
    private ArrayList<WaterSource> waterSources;
    /**
     * List of plants
     */
    private ArrayList<Plant> plants;

    public Replenisher(Simulation simulation){
        this.predators = simulation.getPreds();
        this.preys = simulation.getPreys();
        this.waterSources = simulation.getWaterSources();
        this.plants = simulation.getPlants();
    }

    @Override
    public void run(){
        while (!Thread.interrupted()) {
            int replenishingSpeed = this.preys.size() + this.predators.size();
            for (WaterSource ws : this.waterSources) {
                ws.setWaterSupply(ws.getWaterSupply() + replenishingSpeed);
            }
            for (Plant p : this.plants) {
                p.setFoodSupply(p.getFoodSupply() + replenishingSpeed);
            }
            try {
                TimeUnit.MILLISECONDS.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
