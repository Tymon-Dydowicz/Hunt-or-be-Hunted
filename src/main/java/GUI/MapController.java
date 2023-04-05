package GUI;

import boardView.Simulation;
import environment.Environment;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import playerObjects.Animal.Predator;
import playerObjects.Animal.Prey;

import java.util.List;
import java.util.Objects;

public class MapController {
    private Simulation world;
    @FXML
    private Canvas mapCanvas;

    public MapController(Simulation world){
        this.world = world;
    }

    @FXML
    public void initialize() {
        System.out.println("Dupa");
//        GraphicsContext graphics = this.mapCanvas.getGraphicsContext2D();
//
//        for (int i = 0; i < this.world.getBoard().length; i++) {
//            for (int j = 0; j < this.world.getBoard().length; j++) {
//
//                Environment env = this.world.getBoard()[i][j];
//                if (Objects.equals(env.getName(), "HO")) {
//                    graphics.setFill(Color.BLUE);
//                } else if (Objects.equals(env.getName(), "WS")) {
//                    graphics.setFill(Color.SADDLEBROWN);
//                } else if (Objects.equals(env.getName(), "PL")) {
//                    graphics.setFill(Color.DARKGRAY);
//                } else if (Objects.equals(env.getName(), "PA")) {
//                    graphics.setFill(Color.YELLOW);
//                } else if (Objects.equals(env.getName(), "MT")) {
//                    graphics.setFill(Color.DIMGRAY);
//                } else {
//                    graphics.setFill(Color.GREEN);
//                }
//                graphics.fillRect(i, j, 1, 1);
//
//            }
//        }
//        graphics.setFill(Color.MAGENTA);
//        for (Prey prey : this.world.getPreys()) {
//            graphics.fillOval(prey.getX_pos(), prey.getY_pos(), 1, 1);
//        }
//        graphics.setFill(Color.CRIMSON);
//        for (Predator predator : this.world.getPreds()) {
//            graphics.fillOval(predator.getX_pos(), predator.getY_pos(), 1, 1);
//        }
    }
}
