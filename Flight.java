package assignment13;

import java.util.HashSet;

/**
 * An edge class that represents a Flight.
 * 
 * @author Andy Dao, uID: u0692334
 * @author Casey Yip, uID: u1025709
 */
public class Flight {

    public Airport destination;
    public double delay, canceled, time, distance, cost;
    public HashSet<String> carriers;
    public int count;

    /**
     * Constructor for the flight class. Holds data such as the delay, canceled, time, distance, and
     * cost for a flight, as well as all carriers, origin, and destination.
     * 
     * @param destination - destination Airport (the end/goal point)
     * @param carrier - the airline carrier
     * @param delay - value of delay for this flight
     * @param canceled - 0 if not canceled, and 1 if canceled
     * @param time - The flight time
     * @param distance - the distance from the origin to the destination
     * @param cost - the weight (cost) of the flight
     */
    public Flight(Airport destination, String carrier, double delay, double canceled, double time, double distance,
            double cost) {
        this.destination = destination;
        this.delay = delay;
        this.canceled = canceled;
        this.time = time;
        this.distance = distance;
        this.cost = cost;
        carriers = new HashSet<String>();
        carriers.add(carrier);
        count = 1;
    }

    /**
     * Averages out duplicate flights of flight A -> B.
     *
     * @param carrier - the airline carrier name
     * @param delay - the delay value
     * @param canceled - 0 or 1, depending if canceled or not. (0 for not canceled)
     * @param time - the total time for the flight
     * @param distance - the total distance for the flight
     * @param cost - the total (USD) cost for the flight
     */
    public void averageData(String carrier, double delay, double canceled, double time, double distance, double cost) {
        this.delay = ((this.delay * count) + delay) / (count + 1);
        this.canceled = ((this.canceled * count) + canceled) / (count + 1);
        this.time = ((this.time * count) + time) / (count + 1);
        this.distance = ((this.distance * count) + distance) / (count + 1);
        this.cost = ((this.cost * count) + cost) / (count + 1);
        carriers.add(carrier);
        count++;
    }

    /**
     * Returns the cost data based on the type of criteria specified by the user
     * 
     * @param criteria - the type of cost criteria (DISTANCE, DELAY, COST, etc...)
     * @return - the value (type double) of the specified criteria
     */
    public double getCriteriaData(FlightCriteria criteria) {
        switch (criteria) {
            case DELAY:
                return delay;
            case CANCELED:
                return canceled;
            case TIME:
                return time;
            case DISTANCE:
                return distance;
            case COST:
                return cost;
            default:
                return 0; // just return 0 if none specified
        }
    }

}
