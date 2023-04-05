package boardView;

/**
 * This class serves as a baseline for all classes and objects that will exist on the board.
 * It's used to keep track of objects coordinates as well as some simple distance calculating.
 */
public abstract class Drawable {
    /**
     * X coordinate of drawable object
     */
    private int x_pos;
    /**
     * Y coordinate of drawable object
     */
    private int y_pos;
    protected Drawable(int x, int y){
        this.x_pos = x;
        this.y_pos = y;
    }

    /**
     * @return returns X value of drawable object
     */
    public int getX_pos() {
        return x_pos;
    }

    /**
     * @return returns Y value of drawable object
     */
    public int getY_pos() {
        return y_pos;
    }

    /**
     * @param x_pos sets drawable objects X to x_pos
     */
    public void setX_pos(int x_pos) {
        this.x_pos = x_pos;
    }

    /**
     * @param y_pos sets drawable objects Y to y_pos
     */
    public void setY_pos(int y_pos) {
        this.y_pos = y_pos;
    }

    /**
     * @param coords array of ints as a string
     * @return returns an actual array of ints
     */
    public int[] isolateInts(String coords){
        String[] isolated = coords.substring(1, coords.length()-1).split(",");
        int[] ints = new int[isolated.length];
        for (int i = 0; i < isolated.length; i++) {
            ints[i] = Integer.parseInt(isolated[i].trim());
        }
        return ints;
    }

    /**
     * @param xt Targets X coordinate
     * @param yt Targets Y coordinate
     * @return returns simple manhattan distance of this drawable object from it's target
     */
    public int manhattanDistance(int xt, int yt){
        return Math.abs(this.getX_pos() - xt) + Math.abs(this.getY_pos() - yt);
    }
}
