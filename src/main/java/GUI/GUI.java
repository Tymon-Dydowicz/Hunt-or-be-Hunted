package GUI;

import boardView.Simulation;
import javafx.application.Platform;
import javafx.scene.layout.HBox;

/**
 * GUI class that is responsible for containing all visuals regarding the simulation
 */
public class GUI extends HBox {
    /**
     * This is where the environment board and preys and predators are displayed
     */
    private MainView mainView;
    /**
     * This is where you can check what are current values of given object
     */
    private InfoPanel infoPanel;
    /**
     * This is a reference to our main class so that we can use its functions
     */
    private Simulation simulation;
    /**
     * How big the GUI is
     */
    private int size;

    public GUI(int size, Simulation simulation){
        this.simulation = simulation;
        this.size = size;
        this.mainView = new MainView(this.size, simulation);
        this.infoPanel = new InfoPanel(this.size, simulation);

        this.mainView.getMapCanvas().setOnMouseClicked(mouseEvent -> {
            Platform.runLater(()->{
                int x = (int) Math.floor(mouseEvent.getX() / this.size * this.simulation.getBoard().length);
                int y = (int) Math.floor(mouseEvent.getY() / this.size * this.simulation.getBoard().length);
                this.getInfoPanel().chooseTarget(x, y);
            });
        });

        this.getChildren().addAll(this.mainView, this.getInfoPanel());
    }

    /**
     * @return Returns a pointer to mainView
     */
    public MainView getMainView(){
        return this.mainView;
    }

    /**
     * @return Returns a pointer to infoPanel
     */
    public InfoPanel getInfoPanel() {
        return infoPanel;
    }
}
