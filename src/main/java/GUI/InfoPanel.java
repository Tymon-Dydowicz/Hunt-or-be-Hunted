package GUI;

import boardView.Drawable;
import boardView.Simulation;
import environment.Hideout;
import environment.Mountain;
import environment.Path;
import environment.Resources.Plant;
import environment.Resources.WaterSource;
import environment.Wilderness;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import playerObjects.Animal.Animal;
import playerObjects.Animal.Predator;
import playerObjects.Animal.Prey;

import java.util.concurrent.Callable;

/**
 * This is our info panel where you can see details about all objects present in the simulation
 */
public class InfoPanel extends VBox {
    /**
     * Pointer to our main class so we can use its functions
     */
    private Simulation simulation;
    /**
     * Title of our infoPanel
     */
    private Label title;
    /**
     * Currently selected object we are displaying
     */
    private Drawable target;
    /**
     * Type of the object we are currently displaying
     */
    private String type;
    /**
     * Button that lets us drop the selected object
     */
    private Button leave;
    /**
     * Button used to kill selected prey
     */
    private Button kill;
    public InfoPanel(int size, Simulation simulation){
        this.simulation = simulation;
        this.setPadding(new Insets(10));
        this.setSpacing(20);
        this.setPrefWidth(size/3);

        title = new Label("Info Panel");
        this.setAlignment(Pos.CENTER);
        title.setFont(new Font("Eurostile", 30));


        leave = new Button("");
        leave.setPrefHeight(100);
        leave.setPrefWidth(200);
        leave.setOnAction(actionEvent -> {
            this.target = null;
            leave.setVisible(false);
        });
        leave.setVisible(false);
        Image image = new Image("/Button.png");
        BackgroundImage bgImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
        Background background = new Background(bgImage);
        leave.setBackground(background);

        kill = new Button("KILL");
        kill.setOnAction(actionEvent -> {
            if (this.type == "Predator"){
                this.simulation.getPreds().remove(this.target);
                this.target = null;
            } else if (this.type == "Prey") {
                this.simulation.getPreys().remove(this.target);
                this.target = null;
            }
            kill.setVisible(false);
            leave.setVisible(false);
        });
        kill.setVisible(false);


        this.getChildren().addAll(title, leave, kill);
    }

    /**
     * Function that refreshes our details so that they are up to date
     */
    public void refresh(){
        Platform.runLater(()->{
            ObservableList<Node> children= this.getChildren();
            children.removeIf(child -> child != title && child != leave && child != kill);
            if (this.target != null) {
                leave.setVisible(true);
                if (this.type == "Prey") {
                    kill.setVisible(true);
                    Prey prey = (Prey) this.target;
                    Label type = new Label("Prey");
                    Label coordinates = new Label("Coordinates: " + prey.getX_pos() + " " + prey.getY_pos());
                    Label health = new Label("Health: " + prey.getHealth() + "/" + prey.getMaxHealth());
                    Label strength = new Label("Strength: " + prey.getStrength());
                    Label needs = new Label("Water: " + prey.getThirst() + " | Food: " + prey.getHunger());
                    Label hidden = new Label("Hidden: " + prey.isHidden());
                    this.getChildren().addAll(type, coordinates, health, strength, needs, hidden);
                } else if (this.type == "Predator") {
                    kill.setVisible(true);
                    Predator predator = (Predator) this.target;
                    Label type = new Label("Predator");
                    Label coordinates = new Label("Coordinates: " + predator.getX_pos() + " " + predator.getY_pos());
                    Label health = new Label("Health: " + predator.getHealth());
                    Label strength = new Label("Strength: " + predator.getStrength());
                    Label mode = new Label("Current mode: " + predator.getMode());
                    this.getChildren().addAll(type, coordinates, health, strength, mode);
                } else if (this.type == "PA") {
                    Path path = (Path) this.target;
                    Label type = new Label("Path");
                    Label coordinates = new Label("Coordinates: " + path.getX_pos() + " " + path.getY_pos());
                    Label capacity = new Label("Currently in: " + path.getCurrentlyIn() + "/" + path.getCapacity());
                    Label full = new Label("Is full: " + path.isFull());
                    this.getChildren().addAll(type, coordinates, capacity, full);
                } else if (this.type == "MT") {
                    Label type = new Label("Mountains");
                    Label coordinates = new Label("Coordinates: " + this.target.getX_pos() + " " + this.target.getY_pos());
                    Label descp = new Label("Unreachable to all!");
                    descp.setFont(new Font("Eurostile", 20));
                    descp.setTextFill(Color.RED);
                    this.getChildren().addAll(type, coordinates, descp);
                } else if (this.type == "HO") {
                    Hideout hideout = (Hideout) this.target;
                    Label type = new Label("Hideout");
                    Label coordinates = new Label("Coordinates: " + hideout.getX_pos() + " " + hideout.getY_pos());
                    Label capacity = new Label("Currently in: " + hideout.getCurrentlyIn() + "/" + hideout.getCapacity());
                    Label full = new Label("Is full: " + hideout.isFull());
                    this.getChildren().addAll(type, coordinates, capacity, full);
                } else if (this.type == "WD") {
                    Wilderness wilderness = (Wilderness) this.target;
                    Label type = new Label("Wilderness");
                    Label coordinates = new Label("Coordinates: " + wilderness.getX_pos() + " " + wilderness.getY_pos());
                    Label descp = new Label("Only predators can walk here");
                    this.getChildren().addAll(type, coordinates, descp);
                } else if (this.type == "WS") {
                    WaterSource waterSource = (WaterSource) this.target;
                    Label type = new Label("Water Source");
                    Label coordinates = new Label("Coordinates: " + waterSource.getX_pos() + " " + waterSource.getY_pos());
                    Label resource = new Label("Water: " + waterSource.getWaterSupply());
                    Label capacity = new Label("Currently in: " + waterSource.getCurrentlyIn() + "/" + waterSource.getCapacity());
                    Label full = new Label("Is full: " + waterSource.isFull());
                    this.getChildren().addAll(type, coordinates, resource, capacity, full);
                } else if (this.type == "PL") {
                    Plant plant = (Plant) this.target;
                    Label type = new Label("Plant");
                    Label coordinates = new Label("Coordinates: " + plant.getX_pos() + " " + plant.getY_pos());
                    Label resource = new Label("Food: " + plant.getFoodSupply());
                    Label capacity = new Label("Currently in: " + plant.getCurrentlyIn() + "/" + plant.getCapacity());
                    Label full = new Label("Is full: " + plant.isFull());
                    this.getChildren().addAll(type, coordinates, resource, capacity, full);
                }
            }
        });
    }

    /**
     * @param x X coordinate we have clicked on
     * @param y Y coodrinate we have clicked on
     *          returns object that is currently occupying these coorindates, priotizes animals
     */
    public void chooseTarget(int x, int y){
        Platform.runLater(() -> {
            boolean chosen = false;
            for (Predator predator: this.simulation.getPreds()) {
                if (predator.getX_pos() == x && predator.getY_pos() == y){
                    this.target = predator;
                    this.type = "Predator";
                    chosen = true;
                }
            }
            for (Prey prey : this.simulation.getPreys()) {
                if (prey.getX_pos() == x && prey.getY_pos() == y){
                    this.target = prey;
                    this.type = "Prey";
                    chosen = true;
                }
            }
            if (!chosen){
                this.target = this.simulation.getBoard()[x][y];
                this.type = this.simulation.getBoard()[x][y].getName();
            }
        });
    }
}
