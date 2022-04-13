/*
 * Name: Joshua Turner
 * Date: 7-13-2021
 * File: GeneticAlgorithm.java
 * Class: CMSC 427 Artificial Intelligence Foundations
 * Professor: Dr. Charles Lively
 * Assignment: Genetic Algorithm Assignment 
 * Purpose: This class was modified for the GeneticAlgorithmExample.java class
 *          that was used for the Genetic Algorithm Assignment in CMSC 427 
 *          course at UMGC.  The Tournament Selection method was adapted from
 *          The pseudocode in "Essential Metaheuristics" by Sean Luke, 2016, 
 *          pg. 45.
 *          This Genetic algorithm estimates the omptimal water system size
 *          for a Diamondback Terrapin.  You have the choice of to selection 
 *          types - Roulette style or Tournament sytle.
 */
package geneticalgorithm;

import java.util.Date;
import java.util.Random;
import java.util.Scanner;

public class GeneticAlgorithm {

    // number of genes (1's and 0's) in each individual
    final static int genesPerIndividual = 21;
    // number of individual in each generation
    final static int individualsPerGeneration = 50;
    // odds that a gene will mutate
    static float mutationRate = 0.03f;
    // number of generations
    final static int numberOfGenerations = 150;

    public static String[] currentGeneration = new String[individualsPerGeneration];
    public static String[] nextGeneration = new String[individualsPerGeneration];

    public static Random rand = new Random();
    public static Date d = new Date();
    public static java.text.DateFormat df = new java.text.SimpleDateFormat("ssHHMMmm");

    public static void main(String[] args) {

        rand.setSeed(Long.parseLong(df.format(d)));

        // create initial generation
        currentGeneration = createInitialGeneration();

        String selectionType;
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter '1' for Tournament Selection");
        System.out.println("Enter anything else for Roulette Selection");
        selectionType = scan.next();
        System.out.println(selectionType);

        // loop for each generation
        for (int x = 0; x < numberOfGenerations; x++) {
            // print current generation
            System.out.println("\nGeneration " + (x + 1));
            printGeneration(currentGeneration);

            // get the some of the fitnesses
            double totalFitness = totalFitness(currentGeneration);

            // loop for (number of individuals per generation)/2
            for (int y = 0; y < individualsPerGeneration - 1; y = y + 2) {

                String individual1;
                String individual2;

                // get two individuals based upon their fitness
                if (selectionType.equals("1")) {
                    individual1 = new String(getIndividualTournament(currentGeneration, 2));
                    individual2 = new String(getIndividualTournament(currentGeneration, 2));
                } else {
                    mutationRate = 0.015f;
                    individual1 = new String(getIndividualRoulette(currentGeneration, totalFitness));
                    individual2 = new String(getIndividualRoulette(currentGeneration, totalFitness));
                }

                // select crossover point
                int crossoverPoint = rand.nextInt(genesPerIndividual - 2);
                // add two new individual to next generation
                nextGeneration[y] = new String(individual1.substring(0, crossoverPoint) +
                        individual2.substring(crossoverPoint, genesPerIndividual));
                nextGeneration[y + 1] = new String(individual2.substring(0, crossoverPoint) +
                        individual1.substring(crossoverPoint, genesPerIndividual));
            }

            // mutation - check each gene for mutation
            // loop through each individual
            for (int z = 0; z < individualsPerGeneration; z++) {
                // loop through each gene
                for (int a = 0; a < genesPerIndividual; a++) {

                    if (rand.nextDouble() <= mutationRate) { // mutate the gene

                        // change gene 1 to 0 and 0 to 1
                        if (nextGeneration[z].charAt(a) == '1')
                            nextGeneration[z] = nextGeneration[z].substring(0, a)
                                    + '0' + nextGeneration[z].substring(a + 1,
                                            genesPerIndividual);
                        else
                            nextGeneration[z] = nextGeneration[z].substring(0, a)
                                    + '1' + nextGeneration[z].substring(a + 1,
                                            genesPerIndividual);
                    }
                }
            }
            // the next generation becomes the current generation
            currentGeneration = nextGeneration;
            // create new next generation
            nextGeneration = new String[individualsPerGeneration];
        }

    }

    public static String getIndividualRoulette(String[] generation, double totalFitness) {

        // place holder for selected individual
        String tempString = new String();
        // pick random # between 0 and total fitness
        double fitnessNumber = rand.nextDouble() * totalFitness;
        // index to array
        int x = 0;

        // loop until fitness number is 0
        while (fitnessNumber >= 0.0) {
            // holds individual to return
            tempString = generation[x];
            // submit individuals fitness from fitness number
            fitnessNumber = fitnessNumber - fitness(tempString);
            // add 1 to array index
            x = x + 1;
        }
        return tempString; // return selected individual
    }

    public static String getIndividualTournament(String[] generation, int size) {
        String bestIndividual = null;
        for (int i = 0; i < size; i++) {
            int nextInt = rand.nextInt(individualsPerGeneration);
            if (bestIndividual == null) {
                bestIndividual = generation[nextInt];
            } else {
                if (fitness(generation[nextInt]) >= fitness(bestIndividual)) {
                    bestIndividual = generation[nextInt];
                }
            }
        }
        return bestIndividual;
    }

    public static double totalFitness(String[] generation) {

        // temporary variable to hold fitness
        double temp = 0.0f;
        // loop through each individual
        for (int x = 0; x < individualsPerGeneration; x++) {
            // add individual's fitness to temp variable
            temp = temp + fitness(generation[x]);
        }
        return temp; // return temp variable.
    }

    public static String[] createInitialGeneration() {

        String[] tempArray = new String[individualsPerGeneration];
        // loop once for each individual needed
        for (int x = 0; x < individualsPerGeneration; x++) {
            String temp = new String();
            // for for each gene needed.
            for (int y = 0; y < genesPerIndividual; y++) {
                // randomly decide on 0 or 1
                if (rand.nextFloat() > 0.5)
                    temp = temp.concat("1"); // append 1 to individual
                else
                    temp = temp.concat("0"); // append 0 to individual
            }
            tempArray[x] = temp; // add individual to array
        }
        return tempArray; // return array
    }

    public static void printGeneration(String[] generation) {
        // loop through each individual in generation and prints the info
        for (int x = 0; x < individualsPerGeneration; x++) {
            String individualX = generation[x]; // get individual
            System.out.println("Water System " + (x + 1) + " " + individualX +
                    " equals " + individualToValue(individualX) +
                    " Kiloliters and has fitness " + fitness(individualX));
        }
    }

    public static double individualToValue(String individual) {

        // converts the 0's and 1's in individual to domain value
        // equation x = LB + ((sum bi * 2^i)/ (2^n - 1)) * (UB - LB)
        double LowerBound = 0.0f;
        double UpperBound = 65536.0f;
        double sumValues = 0;

        // loop through each character of individual
        for (int i = 0; i < individual.length(); i++) {
            if (individual.charAt(i) == '1')
                sumValues = sumValues + java.lang.StrictMath.pow(2.0, i);
        }

        // return equation
        return LowerBound + (sumValues / (java.lang.StrictMath.pow(2.0,
                genesPerIndividual) - 1)) * (UpperBound - LowerBound);
    }

    public static double fitness(String individual) {

        // piecewise function that is used to determine the health of the
        // turtles depending on water size this is used to determine the
        // fitness of the individuals in the algorithm.
        if (individualToValue(individual) < 1000) {
            return 0.0;
        } else if (individualToValue(individual) > 10000) {
            return 0.5;
        } else {
            return (individualToValue(individual) + 150) / 140;
        }
    }

}
