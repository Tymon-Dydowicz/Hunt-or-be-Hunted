# Hunt-or-be-Hunted

Java project that features wildlife simulator with Heroes of Might & Magic references.\
Every individual entity is a different thread with many capabilities amongst them:
* moving
* finding shortest path to food/water sources or hideouts using A* algorithm 
* identyfing type of resource and whether it has any space left to occupy
* replenishing resources
* reproducing which result in a creation of a new thread

Code is synchronized on the critcal sections as to avoid possible deadlocks between threads.\
There is a working GUI that allows visualization of the simulation as well as some interaction with it.
