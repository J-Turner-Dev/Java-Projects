/*
 * Name: Joshua Turner
 * Date: 5-11-2021
 * File: Time.java
 * Class: CMSC 335 Object Oriented and Concurrent Programming
 * Professor: Amanda Yu
 * Assignment: Project 3 
 * Purpose: Creates a thread that manages the timer.  It creates two displays
 *          One that has the current time that updates every second.  And the 
 *          has the timer which can be paused, continued, or stopped.  The clock
 *          will continue regardless of the timer.
 */
package trafficsimulator;

import java.awt.Color;
import java.awt.Dimension;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JTextField;

public class Time extends Thread {
    
    private JTextField timeStamp;
    private JTextField timerStamp;
    private int runClock = 0;
    private int secondCount = 0;
    private int minuteCount = 0;
    private int hourCount = 0;
    
    //Lock object used to pause thread with the wait method.
    private final Object lock = new Object();
    
    Time(String name){
        super.setName(name);
        
        //Creates the displays for the clock and timer
        timeStamp = new JTextField(name);
        timeStamp.setPreferredSize(new Dimension(100, 24));
        timeStamp.setEditable(false);
        timeStamp.setBackground(Color.white);
        timerStamp = new JTextField(" 0 : 0 : 0 ");
        timerStamp.setPreferredSize(new Dimension(100, 24));
        timerStamp.setEditable(false);
        timerStamp.setBackground(Color.white);
    }

    public JTextField getTimeStamp() {
        return timeStamp;
    }
    
    public JTextField getTimerStamp() {
        return timerStamp;
    }

    public int getRunClock() {
        return runClock;
    }

    public void setRunClock(int runClock) {
        this.runClock = runClock;
    }
    
    public Object getLock() {
        return lock;
    }

    public void setSecondCount(int secondCount) {
        this.secondCount = secondCount;
    }

    public void setMinuteCount(int minuteCount) {
        this.minuteCount = minuteCount;
    }

    public void setHourCount(int hourCount) {
        this.hourCount = hourCount;
    }
    
    @Override
    public void run() {
        while(true) {
            
            //Retrieves current time
            SimpleDateFormat fmt = new SimpleDateFormat(" hh : mm : ss");
            String currentTime = fmt.format(new Date());
            timeStamp.setText(currentTime);
            
            //If timer is stopped it displays zeros
            if(runClock == 0) {
                timerStamp.setText(" 0 : 0 : 0 ");
            }
            //If timer is running will update the timer every second
            else if (runClock == 1) {
                secondCount++;
                int seconds = secondCount;
                if(seconds >= 60) {
                    seconds = 0;
                    secondCount = 0;
                    minuteCount++;
                }
                int minutes = minuteCount;
                if(minutes >= 60) {
                    minutes = 0;
                    minuteCount = 0;
                    hourCount++;
                }
                int hours = hourCount;
                String timerString = String.format(" " + hours + " : " + minutes
                        + " : " + seconds + " ");
                timerStamp.setText(timerString);
            }
            //Now the thread will wait until notified
            try {
                synchronized(lock) {
                    lock.wait();
                }
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
            
        } 
    }
    
}
