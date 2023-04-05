package environment;

import java.util.ArrayList;
import java.util.LinkedList;

public class Memory {
    private int[] coordinates;
    private LinkedList<int[]> path;

    public Memory(int[] coordinates, LinkedList<int[]> path){
        this.coordinates = coordinates;
        this.path = path;
    }

    public int[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(int[] coordinates) {
        this.coordinates = coordinates;
    }

    public LinkedList<int[]> getPath() {
        return path;
    }

    public void setPath(LinkedList<int[]> path) {
        this.path = path;
    }
}
