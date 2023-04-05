package GUI;

import boardView.Simulation;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;

/**
 * This class contains buttons used to add prey and predators
 */
public class Toolbar extends ToolBar {
    /**
     * Button to add Prey objects
     */
    private Button addPrey;
    /**
     * Button to add Predator objects
     */
    private Button addPred;
    public Toolbar(Simulation simulation){
        this.addPrey = new Button("Add Prey");
        this.addPrey.setPrefHeight(30);
        this.addPrey.setOnAction(actionEvent -> {
            simulation.addPrey(1);
        });
        this.addPred = new Button("Add Predator");
        this.addPred.setPrefHeight(30);
        this.addPred.setOnAction(actionEvent -> {
            simulation.addPredator(1);
        });
        this.getItems().addAll(this.addPrey, this.addPred);
    }
}
