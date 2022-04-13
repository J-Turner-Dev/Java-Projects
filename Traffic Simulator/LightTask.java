/*
 * Name: Joshua Turner
 * Date: 5-11-2021
 * File: LightTask.java
 * Class: CMSC 335 Object Oriented and Concurrent Programming
 * Professor: Amanda Yu
 * Assignment: Project 3 
 * Purpose: Runnable task used to notify the TrafficLight thread to continue 
 *          operation. 
 */
package trafficsimulator;

public class LightTask implements Runnable {
    
    private TrafficLight lights;
    
    LightTask(TrafficLight lights) {
        this.lights = lights;
    }

    // Notifies the TrafficLight thread to no longer wait.
    @Override
    public void run() {
        synchronized(lights.getLock()) {
            lights.getLock().notify();
        }
    }
    
}
