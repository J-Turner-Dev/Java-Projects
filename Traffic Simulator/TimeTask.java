/*
 * Name: Joshua Turner
 * Date: 5-11-2021
 * File: TimeTask.java
 * Class: CMSC 335 Object Oriented and Concurrent Programming
 * Professor: Amanda Yu
 * Assignment: Project 3 
 * Purpose: Runnable task used to notify the Time thread to continue operation. 
 */
package trafficsimulator;

public class TimeTask implements Runnable {
    
    private Time timer;
    
    TimeTask(Time timer) {
        this.timer = timer;
    }

    // Notifies the Time thread to no longer wait.
    @Override
    public void run() {
        synchronized(timer.getLock()) {
            timer.getLock().notify();
        }
    }
    
}
