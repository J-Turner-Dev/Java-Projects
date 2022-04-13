/*
 * Name: Joshua Turner
 * Date: 5-11-2021
 * File: TrafficLight.java
 * Class: CMSC 335 Object Oriented and Concurrent Programming
 * Professor: Amanda Yu
 * Assignment: Project 3 
 * Purpose: Creates a thread that handles all the data for the traffic lights
 *          It also creates and data panel to display the light conditions
 */
package trafficsimulator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class TrafficLight extends Thread {
    
    private Time time;
    private JPanel dataPanel = new JPanel();
    
    //The lists that contain the data
    private ArrayList<JTextField> lightData = new ArrayList<>();
    private ArrayList<Integer> lightCount = new ArrayList<>();
    private ArrayList<Integer> lightCondition = new ArrayList<>();
    
    //Lock object used to pause thread with the wait method.
    private final Object lock = new Object();
    
    public TrafficLight(String name, Time time) {
        super.setName(name);
        this.time = time;
        
        //Builds the data panel with the three starting lights
        dataPanel.setLayout(new GridLayout(6, 6));
        for(int i = 0; i < 3; i++) {
            JLabel nameLabel = new JLabel("Light " + (i+1));
            nameLabel.setPreferredSize(new Dimension(45, 24));
            lightData.add(new JTextField());
            lightData.get(i).setPreferredSize(new Dimension(60, 24));
            lightData.get(i).setEditable(false);
            lightData.get(i).setBackground(Color.white);
            lightCount.add(0);
            lightCondition.add(2);
            JPanel midPanel = new JPanel();
            midPanel.add(nameLabel);
            midPanel.add(lightData.get(i));
            dataPanel.add(midPanel);
        }
        
    }

    public JPanel getDataPanel() {
        return dataPanel;
    }

    public ArrayList<Integer> getLightCondition() {
        return lightCondition;
    }
    
    public Object getLock() {
        return lock;
    }
    
    // Method for adding more lights to the simulator
    public void addLight(JPanel panel) {

        int size = lightData.size();
        JPanel newPanel = new JPanel();
        JLabel newLabel = new JLabel("Light " + (size+1));
        newLabel.setPreferredSize(new Dimension(45, 24));
        newPanel.add(newLabel);
        lightData.add(new JTextField());
        lightData.get(size).setPreferredSize(new Dimension(60, 24));
        lightData.get(size).setEditable(false);
        lightData.get(size).setBackground(Color.white);
        lightCount.add(0);
        lightCondition.add(2);
        newPanel.add(lightData.get(size));
        dataPanel.add(newPanel);
        panel.revalidate();
        
    }
    
    @Override
    public void run() {
        while(true) {
            
            //If the timer is stopped it resets the data
            if(time.getRunClock()==0) {
                for(int i = 0; i < lightData.size(); i++) {
                    lightData.get(i).setForeground(Color.black);
                    lightData.get(i).setText("Stopped");
                    lightCount.set(i, 0);
                    lightCondition.set(i, 2);
                }
            }
            //If the timer is active it will update the light conditions
            else if(time.getRunClock()==1) {
                
                for(int i = 0; i < lightData.size(); i++) {
                    
                    //adjusts the light counter
                    if(lightCount.get(i) == 40) {
                        lightCount.set(i, 0);
                    }
                    else {
                        lightCount.set(i, lightCount.get(i) + 1);
                    }
                    //Changes the light to green
                    if(lightCount.get(i) == 1) {
                        lightData.get(i).setForeground(Color.green);
                        lightData.get(i).setText("GREEN");
                        lightCondition.set(i, 2);
                    }
                    //Changes the light to yellow
                    else if(lightCount.get(i) == 16) {
                        lightData.get(i).setForeground(Color.yellow);
                        lightData.get(i).setText("YELLOW");
                        lightCondition.set(i, 1);
                    }
                    //Changes the light to red
                    else if(lightCount.get(i) == 21) {
                        lightData.get(i).setForeground(Color.red);
                        lightData.get(i).setText("RED");
                        lightCondition.set(i, 0);
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
