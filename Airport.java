package assignment13;

import java.util.ArrayList;
import java.util.HashMap;

/***
 * A vertex class that represents an Airport.
 * 
 * @author Andy Dao, uID:u0692334*
 * @author Casey Yip, uID:u1025709
 */
public class Airport implements Comparable<Airport> {

    public String airportName; // Name of the airport
    public Airport cameFrom; // Previous airport that correlates to the shortest path
    public boolean visited; // Whether this vertex/node has been visited yet
    public double cost; // The cost/weight
    public HashMap<String, Flight> flights; // The flights coming out of this specific airport

    /**
     * Constructor for the airport class. Makes a new airport.
     *
     * @param name - name of the airport
     */
    public Airport(String name) {
        airportName = name;
        cameFrom = null;
        visited = false;
        cost = Double.MAX_VALUE; // Infinity as the starting weight until we visit it
        flights = new HashMap<String, Flight>();
    }

    /**
     * Creates a new flight for this specific airport. Also averages out the values if it is already
     * contained.
     *
     * @param destination - The destination (goal) airport
     * @param carrier - the specific airline carrier
     * @param delay - delay of the flight
     * @param canceled - 0 or 1, 0 if the flight is not canceled, 1 if it is canceled
     * @param time - the total time of the flight
     * @param distance - the distance of the flight
     * @param cost - the cost (USD) of the flight
     */
    public void addFlight(Airport destination, String carrier, double delay, double canceled, double time,
            double distance, double cost) {
        // Add new flights to the data graph. If the flight of A -> B already exists, aggregate.
        if (!flights.containsKey(destination.airportName)) {
            flights.put(destination.airportName,
                    new Flight(destination, carrier, delay, canceled, time, distance, cost));
        } else { // If already contained, average out the data
            flights.get(destination.airportName).averageData(carrier, delay, canceled, time, distance, cost);
        }
    }

    /**
     * Helper method to obtain the best optimal path from the destination, and trace back to the
     * start/origin
     * 
     * @param arrayList - list that will hold the path from origin to destination
     * @return - the flight path from origin to destination
     */
    public ArrayList<String> drawSolutionPath(ArrayList<String> pathList) {
        // If we have no more nodes to go back to, just end at the current airport and return the
        // path list
        if (cameFrom == null) {
            pathList.add(airportName);
            return pathList;
        }
        // If we made it passed the base case, that means we can still go back further
        cameFrom.drawSolutionPath(pathList);
        pathList.add(airportName);
        return pathList;
    }


    @Override
    public int compareTo(Airport rhs) {
        return (this.cost < rhs.cost ? -1 : (this.cost == rhs.cost ? 0 : 1));
    }
}
