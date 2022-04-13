/*
 * Name: Joshua Turner
 * Date: 5-11-2021
 * File: Car.java
 * Class: CMSC 335 Object Oriented and Concurrent Programming
 * Professor: Amanda Yu
 * Assignment: Project 3 
 * Purpose:  Creates a thread that tracks all the different cars as well as 
 *           their locations and speeds, also displays which traffic lights
 *           the cars are stopped at.
 */
package trafficsimulator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Car extends Thread {
    
    private int locX = 50;
    private Time time;
    private TrafficLight lights;
    private JPanel dataPanel = new JPanel();
    
    //The lists that contain the data
    private ArrayList<JTextField> carData = new ArrayList<>();
    private ArrayList<Integer> carSpeed = new ArrayList<>();
    private ArrayList<Integer> locY = new ArrayList<>();
    private ArrayList<Integer> lightCondition = new ArrayList<>();
    
    //Lock object used to pause thread with the wait method.
    private final Object lock = new Object();
    
    Car(String name, Time time, TrafficLight lights) {
        super.setName(name);
        this.time = time;
        this.lights = lights;
        
        // Build the dataPanel with the three starting cars
        dataPanel.setLayout(new GridLayout(6, 3));
        for(int i = 0; i < 3; i++) {
            JLabel nameLabel = new JLabel("Car " + (i+1));
            nameLabel.setPreferredSize(new Dimension(36, 24));
            carData.add(new JTextField());
            carData.get(i).setPreferredSize(new Dimension(250, 24));
            carData.get(i).setEditable(false);
            carData.get(i).setBackground(Color.white);
            carSpeed.add(54);
            locY.add(i*25);
            JPanel midPanel = new JPanel();
            midPanel.add(nameLabel);
            midPanel.add(carData.get(i));
            dataPanel.add(midPanel);
        }
    }
    
    public JPanel getDataPanel() {
        return dataPanel;
    }
    
    public Object getLock() {
        return lock;
    }
    
    // Method for adding more cars to the simulator
    public void addCar(JPanel panel) {

        int size = carData.size();
        JPanel newPanel = new JPanel();
        JLabel newLabel = new JLabel("Car " + (size+1));
        newLabel.setPreferredSize(new Dimension(36, 24));
        newPanel.add(newLabel);
        carData.add(new JTextField());
        carData.get(size).setPreferredSize(new Dimension(250, 24));
        carData.get(size).setEditable(false);
        carData.get(size).setBackground(Color.white);
        carSpeed.add(54);
        locY.add(0);
        newPanel.add(carData.get(size));
        dataPanel.add(newPanel);
        panel.revalidate();
        
    }
    
    @Override
    public void run() {
        while(true) {
            
            //If the timer is stopped it resets the data
            if(time.getRunClock()==0) {
                for(int i = 0; i < carData.size(); i++) {
                    carSpeed.set(i, 0);
                    locY.set(i, (i*25));
                    carData.get(i).setText("Loc: (" + locX + ", " + locY.get(i) 
                            + ") -- " + carSpeed.get(i) + " km/h -- Stopped");
                }  
            }
            //If the timer is active it updates location and speed
            else if(time.getRunClock()==1) {
                lightCondition = lights.getLightCondition();
                ArrayList<Integer> redLights = new ArrayList<>();
                //Used to find the red lights
                for(int j = 0; j < lightCondition.size(); j++) {
                    if (lightCondition.get(j) == 0){
                        redLights.add((j+1)*1000);
                    }
                }
                //If the car is within a red light then it stops
                for(int i = 0; i < carData.size(); i++) {
                    boolean stopped = false;
                    int lightStop = 0;
                    for(int j = 0; j < redLights.size(); j++) {
                        if(locY.get(i) >= (redLights.get(j)-15) && locY.get(i) 
                                <= redLights.get(j)) {
                            locY.set(i, redLights.get(j));
                            carSpeed.set(i,0);
                            stopped = true;
                            lightStop = redLights.get(j)/1000;
                        }
                        
                    }
                    //If the car did not stop then its location is updated
                    if(!stopped) {
                        carSpeed.set(i, 54);
                        int newLoc = locY.get(i) + 15;
                        if(newLoc > lightCondition.size()*1000) {
                            locY.set(i, newLoc -lightCondition.size()*1000);
                        }
                        else {
                            locY.set(i, newLoc);
                        }
                        
                    }
                    //Updates the display panel to show current data
                    if(stopped) {
                        carData.get(i).setText("Loc: (" + locX + ", " + 
                                locY.get(i) + ") -- "+ carSpeed.get(i) + " km/h"
                                        + " -- Stopped at Light " + lightStop);
                    }
                    else {
                        carData.get(i).setText("Loc: (" + locX + ", " + 
                                locY.get(i) + ") -- "+ carSpeed.get(i) + " km/h"
                                        + " -- Moving");
                    }   
                }   
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