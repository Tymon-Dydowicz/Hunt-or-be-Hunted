package boardView;

import GUI.GUI;
import environment.*;
import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.*;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import environment.Resources.*;
import playerObjects.Animal.Predator;
import playerObjects.Animal.Prey;
import playerObjects.Animal.Replenisher;


/**
 * This is the main class of the application, it extends application. Is responsible for creating the simulation board, new Preys
 * , new Predators and refreshing GUI.
 */
public class Simulation extends Application {
    /**
     * 2D array which represents the board on which the simulation happens, it consists of environment objects
     */
    private static Environment[][] board;
    /**
     * This is a 2d String array which represents how many paths and to what should come out from one hideout
     */
    private String[][] mode;
    /**
     * Simple randomizer object used for randomly generated numbers
     */
    private Random randomizer;
    /**
     * List of already used coordinates not to create objects in the same place;
     */
    private ArrayList<int[]> taken;
    /**
     * List of all existing water sources;
     */
    private ArrayList<WaterSource> waterSources;
    /**
     * List of all existing plants
     */
    private ArrayList<Plant> plants;
    /**
     * List of all existing hideouts;
     */
    private ArrayList<Hideout> hideouts;
    /**
     * List of all alive preys;
     */
    private CopyOnWriteArrayList<Prey> preys;
    /**
     * List of all alive predators
     */
    private CopyOnWriteArrayList<Predator> predators;
    /**
     * Replenisher object that is responsible for replenishing the resources
     */
    private Replenisher replenisher;
    /**
     * GUI class that is used to display the simulation
     */
    private GUI simulation_view;
    /**
     * Size of the map
     */
    int size = 40;
    /**
     * Number of water sources on the map
     */
    int noWS = 5;
    /**
     * Number of plants on the map
     */
    int noPL = 5;
    /**
     * Number of hideouts on the map
     */
    int noHO = 10;
    /**
     * Size of the GUI window in pixels
     */
    int GUISize = 800;


    /**
     * @param primaryStage
     * Main function that starts the whole simulation
     */
    @Override
    public void start(Stage primaryStage){
        randomizer = new Random();
        taken = new ArrayList<>();
        waterSources = new ArrayList<>();
        plants = new ArrayList<>();
        hideouts = new ArrayList<>();
        board = new Environment[size][size];
        preys = new CopyOnWriteArrayList<>();
        predators = new CopyOnWriteArrayList<>();
        mode = new String[][]{{"HO", "1"}, {"WS", "2"}, {"PL", "2"}};
        replenisher = new Replenisher(this);
        Thread rep = new Thread(replenisher);
        rep.start();

        generateMap(size, noWS, noPL, noHO);



        simulation_view = new GUI(GUISize, this);
        Scene scene = new Scene(simulation_view);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setOnCloseRequest(e -> {rep.interrupt(); System.exit(0);});
        primaryStage.setResizable(false);
        primaryStage.setTitle("Heroes 3 Simulator");
        Image logo = new Image("/logo.png");
        Image cursor = new Image("/CursorC.png");
        Cursor customCursor = new ImageCursor(cursor);
        primaryStage.getIcons().add(logo);
        simulation_view.getMainView().draw();
        simulation_view.getMainView().setCursor(customCursor);
        simulation_view.getInfoPanel().setCursor(customCursor);



        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                update();
                updateInfo();
            }
            }, 0, 50);
    }

    /**
     * @return Retruns board, 2D array of Environment objects
     */
    public Environment[][] getBoard() {
        return this.board;
    }

    /**
     * @return Returns predators list
     */
    public CopyOnWriteArrayList<Predator> getPreds(){
        return this.predators;
    }

    /**
     * @return Returns preys list
     */
    public CopyOnWriteArrayList<Prey> getPreys(){
        return this.preys;
    }

    /**
     * @return Returns water sources list
     */
    public ArrayList<WaterSource> getWaterSources() {
        return waterSources;
    }

    /**
     * @return Returns plants list
     */
    public ArrayList<Plant> getPlants() {
        return plants;
    }

    /**
     * @param size How big is the map we want to sample our coordinates on
     * @param number How many random coordinates we need
     * @param coordinates Coordinates which we don't want to reuse
     * @return returns an array list with specified amount of non repeated coordinates
     */
    public ArrayList<int []> generateRandomCoordinates(int size, int number, ArrayList<int []> coordinates){
        int x, y;
        int[] coor;
        boolean in;
        int counter = 0;
        ArrayList<int []> returned = new ArrayList<>();
        if (coordinates == null) {
            coordinates = new ArrayList<>();
        }

        while(counter < number) {
            in = false;
            coor = new int[2];
            x = randomizer.nextInt(2, size-2);
            y = randomizer.nextInt(2, size-2);

            for (int[] coordinate : coordinates) {
                if (x == coordinate[0] && y == coordinate[1]) {
                    in = true;
                }
            }
            if (!in) {
                coor[0] = x;
                coor[1] = y;
                coordinates.add(coor);
                returned.add(coor);
                counter++;
            }
        }
        return returned;
    }

    /**
     * @param size size of the map
     * @param noWS how many waters sources we want
     *             Generates noWS water sources in random places on the board
     */
    public void generateWaterSources(int size, int noWS){
        ArrayList<int[]> coordinates = generateRandomCoordinates(size, noWS, taken);
        for (int[] coordinate: coordinates) {
            int x, y, replenishingSpeed, maxNumber;
            x = coordinate[0];
            y = coordinate[1];
            replenishingSpeed = randomizer.nextInt(100);
            maxNumber = randomizer.nextInt(2,5);
            WaterSource waterSource = new WaterSource(x, y, "WS", replenishingSpeed, maxNumber);
            getWaterSources().add(waterSource);
        }
    }

    /**
     * @param size size of the map
     * @param noPL how many plants we want
     *             Generates noPL plants in random places on the board
     */
    public void generatePlants(int size, int noPL){
        ArrayList<int[]> coordinates = generateRandomCoordinates(size, noPL, taken);
        for (int[] coordinate: coordinates) {
            int x, y, replenishingSpeed, maxNumber;
            x = coordinate[0];
            y = coordinate[1];
            replenishingSpeed = randomizer.nextInt(100);
            maxNumber = randomizer.nextInt(2,5);
//            System.out.println(x + y + replenishingSpeed + maxNumber);
            Plant plant = new Plant(x, y, "PL", replenishingSpeed, maxNumber);
            getPlants().add(plant);
        }
    }

    /**
     * @param size size of the map
     * @param noHO how many hideouts sources we want
     *             Generates noHO hideouts in random places on the board
     */
    public void generateHideouts(int size, int noHO){
        ArrayList<int[]> coordinates = generateRandomCoordinates(size, noHO, taken);
        for (int[] coordinate: coordinates) {
            int x, y, capacity;
            x = coordinate[0];
            y = coordinate[1];
            capacity = randomizer.nextInt(2) + 1;
//            System.out.println(x + y + replenishingSpeed + maxNumber);
            Hideout hideout = new Hideout(x, y,"HO", capacity);
            hideouts.add(hideout);
        }
    }

    /**
     * @param source One object on the map
     * @param target Second object on the map
     *               Generates a path between the two
     */
    public void generatePath(Environment source, Environment target){
        int xSource, ySource, xTarget, yTarget;
        xSource = source.getX_pos();
        ySource = source.getY_pos();
        xTarget = target.getX_pos();
        yTarget = target.getY_pos();
//        System.out.println(xSource +" "+ySource+" "+xTarget+" "+yTarget);
//        System.out.println(board[xSource][ySource].getName());
        if (xSource < xTarget){
            while(xSource < xTarget){
//                System.out.println(xSource +" "+ySource+" "+xTarget+" "+yTarget);
                xSource++;
                if (board[xSource][ySource] == null){
                    board[xSource][ySource] = new Path(xSource, ySource, "type");
                }
            }
        }
        else{
            while(xSource > xTarget) {
                xSource--;
                if (board[xSource][ySource] == null) {
                    board[xSource][ySource] = new Path(xSource, ySource, "type");
                }
//                System.out.println(xSource +" "+ySource+" "+xTarget+" "+yTarget);
            }
        }

        if (ySource < yTarget){
            while(ySource < yTarget){
//                System.out.println(xSource +" "+ySource+" "+xTarget+" "+yTarget);
                ySource++;
                if (board[xSource][ySource] == null){
                    board[xSource][ySource] = new Path(xSource, ySource, "type");
                }
            }
        }
        else{
            while(ySource > yTarget) {
//                System.out.println(xSource +" "+ySource+" "+xTarget+" "+yTarget);
                ySource--;
                if (board[xSource][ySource] == null) {
                    board[xSource][ySource] = new Path(xSource, ySource, "type");
                }
            }
        }


    }

    /**
     * Generates all paths according to mode field
     */
//    public void generatePathWithAStar(Environment source, Environment target){
//        int xCurrent, yCurrent, xTarget, yTarget, cost, heuristic;
//        int[] start;
//        int[] tuple;
//        Stack<int []> finalPath = new Stack<>();
//        ArrayList<int[]> neighbours;
////        LinkedList<int []> path;
//        PriorityQueue<int[]> toCheck = new PriorityQueue<>();
//        xCurrent = source.getX_pos();
//        yCurrent = source.getY_pos();
//        xTarget = target.getX_pos();
//        yTarget = target.getY_pos();
//        start = new int[] {0, source.manhattanDistance(xTarget, yTarget), xCurrent, yCurrent}; //{cost, heuristic, x, y}
////        path = new LinkedList<>();
////        Memory startingState = new Memory(start, path);
//        HashSet<String> visited = new HashSet<String>();
//        HashMap<String, String> paths = new HashMap<String, String>();
//
//        toCheck.add(start);
//        int[] coords = Arrays.copyOfRange(start, start.length-2, start.length);
//        paths.put(Arrays.toString(coords), null);
//        visited.add(Arrays.toString(coords));
//
//
//        while (!toCheck.isEmpty()) {
//            tuple = toCheck.remove();
//            coords = Arrays.copyOfRange(start, start.length-2, start.length);
//            cost = tuple[0];
//            heuristic = tuple[1];
//            xCurrent = coords[0];
//            yCurrent = coords[1];
//            if (xCurrent == xTarget && yCurrent == yTarget){
//                while (!finalPath.isEmpty()){
//                    if (board[xCurrent][yCurrent] == null) {
//                        board[xCurrent][yCurrent] = new Path(xCurrent, yCurrent, "type");
//                    }
//                    coords = finalPath.pop();
//                    xCurrent = coords[0];
//                    yCurrent = coords[1];
//                }
//            }
//
//            neighbours  = new ArrayList<>();
//            neighbours.add(new int[]{xCurrent - 1, yCurrent});
//            neighbours.add(new int[]{xCurrent + 1, yCurrent});
//            neighbours.add(new int[]{xCurrent, yCurrent - 1});
//            neighbours.add(new int[]{xCurrent, yCurrent + 1});
//            for (int[] neighbour : neighbours) {
//                if (!Objects.equals(board[neighbour[0]][neighbour[1]].getName(), "WD")) {
//                    int idx = neighbour[0]*board[0].length+neighbour[1];
//                    if(!visited[idx]) {
//                        LinkedList<int[]> newPath = new LinkedList<>(path);
//                        newPath.add(neighbour);
//                        Memory nextState = new Memory(neighbour, newPath);
//                        toCheck.add(nextState);
//                    }
//                }
//            }
//        }
//    }
    public void generatePaths(){
        ArrayList<Environment> target;
        for (Hideout h : hideouts) {
            for (String[] s : mode) {
                target = findClosest(h, s);
                for (Environment t : target) {
                    generatePath(h, t);
                }
            }
        }
    }

    /**
     * @param s Hideout from which we search
     * @param t targeted type of object
     * @return returns the closes objects of type t
     */
    public ArrayList<Environment> findClosest(Hideout s, String[] t){
        int xSource = s.getX_pos();
        int ySource = s.getY_pos();
        String type = t[0];
        int number = Integer.parseInt(t[1]);
        ArrayList<? extends Environment> targets = null;

        if (Objects.equals(type, "HO")){
            targets = (ArrayList<? extends Environment>) hideouts.clone();
        } else if (Objects.equals(type, "WS")) {
            targets = getWaterSources();
        } else if (Objects.equals(type, "PL")) {
            targets = getPlants();
        }

        targets.sort(new Comparator<Environment>() {
            @Override
            public int compare(Environment o1, Environment o2) {
                int d1 = Math.abs(xSource - o1.getX_pos()) + Math.abs(ySource - o1.getY_pos());
                int d2 = Math.abs(xSource - o2.getX_pos()) + Math.abs(ySource - o2.getY_pos());
                if (d1 == 0) {
                    d1 = Integer.MAX_VALUE;
                }
                if (d2 == 0) {
                    d2 = Integer.MAX_VALUE;
                }
                return d1 - d2;
            }
        });
        ArrayList<Environment> result = new ArrayList<>(targets.subList(0, number));
//        for (Environment e : result) {
//            System.out.println(xSource + " " + ySource + " Source");
//            System.out.println(e.getX_pos() + " " + e.getY_pos() + " Target");
//        }
        return result;
    }

    /**
     * @param size size of the map
     * @param noWS number of water sources
     * @param noPL number of plants
     * @param noHO number of hideouts
     *             Generates the entire map using the aforementioned functions
     */
    public void generateMap(int size, int noWS, int noPL, int noHO){
        generateWaterSources(size, noWS);
        generatePlants(size, noPL);
        generateHideouts(size, noHO);

        for (WaterSource w: getWaterSources()) {
            board[w.getX_pos()][w.getY_pos()] = w;
        }
        for (Plant p: getPlants()) {
            board[p.getX_pos()][p.getY_pos()] = p;
        }
        for (Hideout h: hideouts) {
            board[h.getX_pos()][h.getY_pos()] = h;
        }

        generatePaths();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j] == null) {
                    board[i][j] = new Wilderness(i, j);
                }
                if (i == 0 || j == 0 || i == board.length - 1 || j == board.length - 1){
                    board[i][j] = new Mountain(i, j);
                }
                else if (Objects.equals(board[i][j].getName(), "PA")){
                    int counter = 0;
                    boolean highway = false;
                    if (!(board[i + 1][j + 1] == null) && Objects.equals(board[i + 1][j + 1].getName(), "PA")){
                        highway = true;
                    }
                    if (!(board[i - 1][j + 1] == null) && Objects.equals(board[i - 1][j + 1].getName(), "PA")){
                        highway = true;
                    }
                    if (!(board[i + 1][j - 1] == null) && Objects.equals(board[i + 1][j - 1].getName(), "PA")){
                        highway = true;
                    }
                    if (!(board[i - 1][j - 1] == null) && Objects.equals(board[i - 1][j - 1].getName(), "PA")){
                        highway = true;
                    }
                    if (!(board[i + 1][j] == null) && Objects.equals(board[i + 1][j].getName(), "PA")){
                        counter++;
                    }
                    if (!(board[i - 1][j] == null) && Objects.equals(board[i - 1][j].getName(), "PA")){
                        counter++;
                    }
                    if (!(board[i][j + 1] == null) && Objects.equals(board[i][j + 1].getName(), "PA")){
                        counter++;
                    }
                    if (!(board[i][j - 1] == null) && Objects.equals(board[i][j - 1].getName(), "PA")){
                        counter++;
                    }
                    if (counter >= 3 && !highway){
                        board[i][j].setCapacity(1);
                    }
                }
            }

        }
    }
    public void printMap(){
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
//                System.out.print(board[i][j].getName() + " ");
                if (board[i][j].getCapacity() > 100){
                    System.out.print(" M:" + board[i][j].getName() + " ");
                }
                else {
                    System.out.print(" " + board[i][j].getCapacity() + ":" + board[i][j].getName() + " ");
                }
            }
            System.out.println();
        }
    }

    /**
     * @param number how many Prey objects we want to add
     *               Adds this much prey objects
     */
    public void addPrey(int number){
        Hideout hideout;
        Random randomizer = new Random();
        for (int i = 0; i < number; i++) {
            Collections.shuffle(hideouts);
            hideout = hideouts.get(0);
            Prey prey = new Prey(hideout.getX_pos(), hideout.getY_pos(), board, preys);
            hideout.increaseCurrentlyIn();
            preys.add(prey);
            Thread t = new Thread(prey);
            t.start();
        }
    }

    /**
     * @param number how many Prey objects we want to add
     *               Adds this much prey objects
     */
    public void addPredator(int number){
        ArrayList<int []> coordinates;
        coordinates = generateRandomCoordinates(size, number, null);
        for (int i = 0; i < number; i++){
            int[] coords;
            coords = coordinates.get(i);
            Predator predator = new Predator(coords[0], coords[1], board, predators, preys);
            predators.add(predator);
            Thread t = new Thread(predator);
            t.start();
        }
    }
    private void update() {
        this.simulation_view.getMainView().draw();
    }
    private void updateInfo(){
        this.simulation_view.getInfoPanel().refresh();
    }
    public static void main(String[] args) {
        launch(args);
    }

}