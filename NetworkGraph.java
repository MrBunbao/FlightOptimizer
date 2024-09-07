package assignment13;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Queue;


/**
 * <p>
 * This class represents a graph of flights and airports along with specific data about those
 * flights. It is recommended to create an airport class and a flight class to represent nodes and
 * edges respectively. There are other ways to accomplish this and you are free to explore those.
 * </p>
 * 
 * <p>
 * Testing will be done with different criteria for the "best" path so be sure to keep all
 * information from the given file. Also, before implementing this class (or any other) draw a
 * diagram of how each class will work in relation to the others. Creating such a diagram will help
 * avoid headache and confusion later.
 * </p>
 * 
 * <p>
 * Be aware also that you are free to add any member variables or methods needed to completed the
 * task at hand
 * </p>
 * 
 * @author CS2420 Teaching Staff - Spring 2016
 * @author Andy Dao, uID: u0692334
 * @author Casey Yip, uID: u1025709
 */
public class NetworkGraph {

    private HashMap<String, Airport> allAirports; // All airports in the graph (origin and
    // destination)

    /**
     * <p>
     * Constructs a NetworkGraph object and populates it with the information contained in the given
     * file. See the sample files or a randomly generated one for the proper file format.
     * </p>
     * 
     * <p>
     * You will notice that in the given files there are duplicate flights with some differing
     * information. That information should be averaged and stored properly. For example some times
     * flights are canceled and that is represented with a 1 if it is and a 0 if it is not. When
     * several of the same flights are reported totals should be added up and then reported as an
     * average or a probability (value between 0-1 inclusive).
     * </p>
     * 
     * @param flightInfoPath - The path to the file containing flight data. This should be a
     *        *.csv(comma separated value) file
     * 
     * @throws FileNotFoundException The only exception that can be thrown in this assignment and
     *         only if a file is not found.
     */
    public NetworkGraph(String flightInfoPath) throws FileNotFoundException {
        allAirports = new HashMap<String, Airport>();
        readCSVFile(flightInfoPath);
    }

    /**
     * Reads in a CSV (comma separated value)file and parses its info.
     * 
     * @param inputCSV - the .csv file being parsed
     * 
     * @throws FileNotFoundException The only exception that can be thrown in this assignment and
     *         only if a file is not found.
     */
    private void readCSVFile(String inputCSV) throws FileNotFoundException {
        String origin, destination, carrier;
        double delay, canceled, time, distance, cost;

        // Read in the csv file with a BufferedReader for speed
        try (BufferedReader brFile = new BufferedReader(new FileReader(inputCSV))) {
            String[] lineData; // Array to read the line data
            // Read the first line to skip the headers
            brFile.readLine();

            // Continue reading each line one by one until there is no more
            while (brFile.ready()) {
                // Parse the line read in the csv file, split by commas
                lineData = brFile.readLine().split(",");
                // Obtain the correct data and set them to their respective variables to add into
                // our data graph
                origin = lineData[0];
                destination = lineData[1];
                carrier = lineData[2];
                delay = Double.parseDouble(lineData[3]);
                canceled = Double.parseDouble(lineData[4]);
                time = Double.parseDouble(lineData[5]);
                distance = Double.parseDouble(lineData[6]);
                cost = Double.parseDouble(lineData[7]);

                // Add the airport info if it hasn't been added yet
                if (!allAirports.containsKey(origin)) {
                    allAirports.put(origin, new Airport(origin));
                }
                if (!allAirports.containsKey(destination)) {
                    allAirports.put(destination, new Airport(destination));
                }
                // Add flight. Will aggregate if duplicate flight paths
                allAirports.get(origin).addFlight(allAirports.get(destination), carrier, delay, canceled, time,
                        distance, cost);
            }
            brFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * This method returns a BestPath object containing information about the best way to fly to the
     * destination from the origin. "Best" is defined by the FlightCriteria parameter
     * <code>enum</code>. This method should throw no exceptions and simply return a BestPath object
     * with information dictating the result. If a destination or origin is not contained in this
     * instance of NetworkGraph simply return a BestPath object with no path (not a
     * <code>null</code> path). If origin or destination are <code>null</code>, also return a
     * BestPath object with no path.
     * 
     * @param origin - The starting location to find a path from. This should be a 3 character long
     *        string denoting an airport.
     * 
     * @param destination - The destination location from the starting airport. Again, this should
     *        be a 3 character long string denoting an airport.
     * 
     * @param criteria - This enum dictates the definition of "best". Based on this value a path
     *        should be generated and return.
     * 
     * @return - An object containing path information including origin, destination, and everything
     *         in between.
     */
    public BestPath getBestPath(String origin, String destination, FlightCriteria criteria) {
        return getBestPath(origin, destination, criteria, null);
    }

    /**
     * <p>
     * This overloaded method should do the same as the one above only when looking for paths skip
     * the ones that don't match the given airliner.
     * </p>
     *
     * @param origin - The starting location to find a path from. This should be a 3 character long
     *        string denoting an airport.
     *
     * @param destination - The destination location from the starting airport. Again, this should
     *        be a 3 character long string denoting an airport.
     *
     * @param criteria - This enum dictates the definition of "best". Based on this value a path
     *        should be generated and return.
     *
     * @param airliner - a string dictating the airliner the user wants to use exclusively. Meaning
     *        no flights from other airliners will be considered.
     *
     * @return - An object containing path information including origin, destination, and everything
     *         in between.
     */
    public BestPath getBestPath(String origin, String destination, FlightCriteria criteria, String airliner) {
        Queue<Airport> PQ = new PriorityQueue<Airport>();

        // Create the starting (node) airport
        Airport nextAirport = allAirports.get(origin);

        // Make sure the starting airport is actually in the graph
        if (nextAirport != null) {
            nextAirport.cost = 0.0;
        } else {
            return new BestPath(); // auto return an empty path with no cost if no origin airport
        }

        PQ.add(nextAirport); // ENQUEUE

        // Perform Dijkstra's Algorithm
        while (!PQ.isEmpty()) {
            Airport currentAirport = PQ.poll(); // DEQUEUE

            // Done if found destination/goal!
            if (currentAirport.airportName.equals(destination)) {
                ArrayList<String> path = currentAirport.drawSolutionPath(new ArrayList<String>());
                double pathLength = currentAirport.cost; // We need to do this so we can return it
                                                         // after resetting all the values
                resetAirports();
                return new BestPath(path, pathLength);
            }
            // If yet we have not found the destination we're searching for...
            currentAirport.visited = true; // Set visitation status to true

            // Search through all neighbors of the current airport
            for (Flight flightEl : currentAirport.flights.values()) {
                nextAirport = flightEl.destination;

                // If the edge weight is a negative value, disregard it and move on to possible next
                // edge
                if (flightEl.getCriteriaData(criteria) < 0) {
                    continue;
                } else {
                    // Make sure it hasn't been visited yet
                    // Check if user specified for an Airliner
                    if (!nextAirport.visited && (airliner == null || flightEl.carriers.contains(airliner))) {
                        // Check if the "nextAirport" has a higher cost than our current
                        // flight(criteria) + current.cost
                        if (nextAirport.cost > flightEl.getCriteriaData(criteria) + currentAirport.cost) {
                            // remove this airport since the cost is higher than our lowest cost so
                            // far
                            PQ.remove(nextAirport); // DEQUEUE
                            nextAirport.cost = flightEl.getCriteriaData(criteria) + currentAirport.cost; // update
                            nextAirport.cameFrom = currentAirport; // Set where the next airport its
                            // previous airport
                            PQ.add(nextAirport); // ENQUEUE the new next airport
                        }
                    }
                }
            }
        }
        // Return empty BestPath if no solution and reset all values
        resetAirports();
        return new BestPath();
    }

    /**
     * Resets all Airport values to original state as if they weren't visited
     */
    private void resetAirports() {
        for (Airport airportEl : allAirports.values()) {
            airportEl.visited = false;
            airportEl.cameFrom = null;
            airportEl.cost = Double.MAX_VALUE;
        }
    }

    /**
     * Helper method for JUnit testing. Returns the HashMap of allAirports.
     * 
     * @return - the HashMap object of allAirports, containing all the airports in the graph.
     */
    public HashMap<String, Airport> getAirports() {
        return allAirports;
    }

}
