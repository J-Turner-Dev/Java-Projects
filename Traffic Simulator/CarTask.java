/*
 * Name: Joshua Turner
 * Date: 5-11-2021
 * File: CarTask.java
 * Class: CMSC 335 Object Oriented and Concurrent Programming
 * Professor: Amanda Yu
 * Assignment: Project 3 
 * Purpose: Runnable task used to notify the Car thread to continue operation. 
 */
package trafficsimulator;

public class CarTask implements Runnable {
    
    private Car cars;
    
    CarTask(Car cars) {
        this.cars = cars;
    }

    // Notifies the Car thread to no longer wait.
    @Override
    public void run() {
        synchronized(cars.getLock()) {
            cars.getLock().notify();
        }
    }
    
}
