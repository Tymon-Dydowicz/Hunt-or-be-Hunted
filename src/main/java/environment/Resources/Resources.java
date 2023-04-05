package environment.Resources;

import environment.Environment;

/**
 * Resources class is an abstract class for resource types that are used by Prey to feed or drink
 */
public abstract class Resources extends Environment {
    Resources(int x, int y, String name, int replenishingSpeed, int maxNumber, int resType){
        super(x, y, resType, name, maxNumber);
    }
}
