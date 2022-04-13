/*
 * Name: Joshua Turner
 * Date: 5-11-2021
 * File: TrafficSimulator.java
 * Class: CMSC 335 Object Oriented and Concurrent Programming
 * Professor: Amanda Yu
 * Assignment: Project 3 
 * Purpose: This contains the main method as well as the code for building the 
 *          GUI.  The GUI has a start, stop, pause, and continue functionality
 *          as well as adding more traffic lights or cars.  An executor is run
 *          from the main method to run tasks the keep the other threads
 *          updating every second.
 */
package trafficsimulator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

public class TrafficSimulator extends JFrame {
    
    private Color lightBlue = new Color(0, 200, 245);
    
    private static int lightCount = 3;
    private static int carCount = 3;
    
    //Creates the thread objects
    private static Time timer = new Time("Clock");
    private static TrafficLight lights = new TrafficLight("Lights", timer);
    private static Car cars = new Car("Cars", timer, lights);
    
    //Constructor for the GUI
    TrafficSimulator(String title) {
        super(title);
        
        //Starts all the threads
        timer.start();
        lights.start();
        cars.start();
        
        //Building the specific GUI Panels and features
        JPanel display = new JPanel();
        JPanel dataPanel = new JPanel();
        JPanel timePanel = new JPanel();
        display.setPreferredSize( new Dimension(1000,600) ); 
        display.setLayout(new BorderLayout());
        display.add(timePanel, BorderLayout.NORTH);
        display.add(dataPanel, BorderLayout.CENTER);
        JPanel bottom = new JPanel();
        bottom.setLayout(new GridLayout(2,1));
        bottom.setPreferredSize(new Dimension(500,80));
        
        JPanel upperPanel = new JPanel();
        upperPanel.setLayout(new FlowLayout());
        bottom.add(upperPanel);
        upperPanel.setBackground(lightBlue);
        
        JButton startButton = new JButton("Start");
        JButton pauseButton = new JButton("Pause");
        JButton continueButton = new JButton("Continue");
        JButton stopButton = new JButton("Stop");
        
        upperPanel.add(startButton);
        upperPanel.add(pauseButton);
        upperPanel.add(continueButton);
        upperPanel.add(stopButton);
        
        JPanel lowerPanel = new JPanel();
        lowerPanel.setLayout(new FlowLayout());
        bottom.add(lowerPanel);
        lowerPanel.setBackground(lightBlue);
        
        JLabel carsLabel = new JLabel("New Car", JLabel.LEFT);
        JLabel lightsLabel = new JLabel("New Traffic Light", JLabel.LEFT);
        
        JButton carsButton = new JButton("Add");
        JButton lightsButton = new JButton("Add");
        
        JTextField carsText = new JTextField(" Cars: " + carCount + " ");
        carsText.setPreferredSize(new Dimension(100, 24));
        carsText.setEditable(false);
        carsText.setBackground(Color.white);
        JTextField lightsText = new JTextField(" Lights: " + lightCount + " ");
        lightsText.setPreferredSize(new Dimension(100, 24));
        lightsText.setEditable(false);
        lightsText.setBackground(Color.white);
        
        lowerPanel.add(carsLabel);
        lowerPanel.add(carsButton);
        lowerPanel.add(carsText);
        lowerPanel.add(lightsLabel);
        lowerPanel.add(lightsButton);
        lowerPanel.add(lightsText);
        display.add(bottom, BorderLayout.SOUTH);
        
        JLabel clockLabel = new JLabel("Clock", JLabel.LEFT);
        JLabel timerLabel = new JLabel("Timer", JLabel.LEFT);
        
        timePanel.setBackground(lightBlue);
        timePanel.add(clockLabel);
        timePanel.add(timer.getTimeStamp());
        timePanel.add(timer.getTimerStamp());
        timePanel.add(timerLabel);
        
        dataPanel.setLayout(new GridLayout(2,1));
        JPanel carData = new JPanel();
        carData.setLayout(new FlowLayout());
        carData.setBorder(new TitledBorder("Car Data"));
        dataPanel.add(carData);
        JPanel lightData = new JPanel();
        lightData.setBorder(new TitledBorder("Traffic Light Data"));
        dataPanel.add(lightData);
        dataPanel.add(lightData);
        
        lightData.add(lights.getDataPanel());
        carData.add(cars.getDataPanel());
        
        this.setContentPane(display);
        this.pack();
        this.setLocation(50,50);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //Starts the timer and resets counts
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(timer.getRunClock() == 0) {
                    timer.setRunClock(1);
                    timer.setSecondCount(0);
                    timer.setMinuteCount(0);
                    timer.setHourCount(0);
                }
            }
        });
        
        //Pauses the timer
        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(timer.getRunClock()==1) {
                    timer.setRunClock(2);
                }    
            }
        });
        
        //Only button that continues a paused timer
        continueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(timer.getRunClock()==2) {
                    timer.setRunClock(1);
                }
            }
        });
        
        //Stops the timer
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timer.setRunClock(0);
            }
        });
        
        //Used to add more traffic lights to the simulation
        lightsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lights.addLight(dataPanel);
                lightCount++;
                lightsText.setText(" Lights: " + lightCount + " ");
            }
        });
        
        //Used to add more cars to the simulation
        carsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cars.addCar(dataPanel);
                carCount++;
                carsText.setText(" Cars: " + carCount + " ");
            }
        });   
    }

    public static void main(String[] args) {
        
        TrafficSimulator mySimulator = new TrafficSimulator("Traffic Light "
                + "Simulator");
        mySimulator.setVisible(true);
        
        //Executor with scheduled threadpool
        ScheduledExecutorService executor= Executors.newScheduledThreadPool(20);
        
        //Runnable tasks
        TimeTask myTimeTask = new TimeTask(timer);
        LightTask myLightTask = new LightTask(lights);
        CarTask myCarTask = new CarTask(cars);
        
        //Executes each of the task 1 per second continuously
        executor.scheduleAtFixedRate(myTimeTask, 0, 1, TimeUnit.SECONDS);
        executor.scheduleAtFixedRate(myLightTask, 0, 1, TimeUnit.SECONDS);
        executor.scheduleAtFixedRate(myCarTask, 0, 1, TimeUnit.SECONDS);
        
    }
    
}
