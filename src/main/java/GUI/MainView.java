package GUI;

import java.util.List;
import java.util.Objects;

import boardView.Simulation;
import environment.Environment;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import playerObjects.Animal.Predator;
import playerObjects.Animal.Prey;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;

/**
 * This class contains our map
 */
public class MainView extends VBox {
    /**
     * Scale of our map so that it fits GUI perfectly
     */
    private Affine affine;
    /**
     * Map
     */
    private Environment[][] board;
    /**
     * Pointer to our main class so we can use it's functions
     */
    private Simulation simulation;
    /**
     * Environment map and Predator/Prey map stacked on top of eachother to avoid the need to redraw entire map
     */
    private StackPane entireCanvas;
    /**
     * Environment map
     */
    private Canvas mapCanvas;
    /**
     * Mask used to show where the predators and preys are
     */
    private Canvas animalCanvas;

    public MainView(int size, Simulation simulation){
        this.mapCanvas = new Canvas(size, size);
        this.animalCanvas = new Canvas(size, size);
        this.board = simulation.getBoard();
        this.simulation = simulation;

        Toolbar buttons = new Toolbar(simulation);
        buttons.setPrefHeight(50);
        this.entireCanvas = new StackPane();
        this.entireCanvas.getChildren().addAll(this.mapCanvas, this.animalCanvas);


        this.affine = new Affine();
        this.affine.appendScale((double) size/board.length, (double) size/board.length);
        this.drawEnvironment();

        this.getChildren().addAll(buttons, this.entireCanvas);
    }

    /**
     * @return Returns our main map
     */
    public StackPane getMapCanvas(){
        return this.entireCanvas;
    }

    /**
     * Draws the environment onto the bottom of our entireCanvas
     */
    public void drawEnvironment(){
        GraphicsContext graphics = this.mapCanvas.getGraphicsContext2D();
        graphics.setTransform(this.affine);

        for (int i = 0; i < this.board.length; i++) {
            for (int j = 0; j < this.board.length; j++) {

                Environment env = this.board[i][j];
                if (Objects.equals(env.getName(), "HO")) {
                    graphics.setFill(Color.BLUE);
                } else if (Objects.equals(env.getName(), "WS")) {
                    graphics.setFill(Color.SADDLEBROWN);
                } else if (Objects.equals(env.getName(), "PL")) {
                    graphics.setFill(Color.DARKGRAY);
                } else if (Objects.equals(env.getName(), "PA")) {
//                    Image image = new Image("/trdg050.png");
//                    graphics.drawImage(image, i, j, 1, 1);
                    graphics.setFill(Color.YELLOW);
                } else if (Objects.equals(env.getName(), "MT")) {
                    graphics.setFill(Color.DIMGRAY);
                } else {
                    graphics.setFill(Color.GREEN);
                }
                graphics.fillRect(i, j, 1, 1);
                graphics.setStroke(Color.BLACK);
                graphics.setLineWidth(0.05);
                graphics.strokeLine(i, 0, i, board.length);
                graphics.strokeLine(0, j, board.length, j);
            }
        }
    }

    /**
     * Constantly draws position of preys and predators
     */
    public void draw() {
        GraphicsContext graphics = this.animalCanvas.getGraphicsContext2D();
        graphics.setTransform(this.affine);
        graphics.clearRect(0, 0, this.animalCanvas.getWidth(), this.animalCanvas.getHeight());
        for (Prey prey : this.simulation.getPreys()) {
            Image image = new Image("/PeasantC.png");
            graphics.drawImage(image, prey.getX_pos(), prey.getY_pos(), 1, 1);
        }
        for (Predator predator : this.simulation.getPreds()) {
            Image image = new Image("/SkeletonC.png");
            graphics.drawImage(image, predator.getX_pos(), predator.getY_pos(), 1, 1);
        }
    }
}
