package assignment13;

/**
 * <p>
 * An example of how a user will use your best flight API.
 * </p>
 * <p>
 * You will still be required to writed JUnit tests to test your program.
 * </p>
 *
 * @author CS2420 Teaching Staff - Spring 2016
 * @author Andy Dao, uID: u0692334
 * @author Casey Yip, uID: u1025709
 */
public class FindBestPathTester {

    public static void main(String[] args) {
        NetworkGraph airportGraph = null;
        try {
            airportGraph = new NetworkGraph("flights-2015-q3.csv");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Returns the shortest distance path of flights from MOB to ACV
        // Solution: a path of ['MOB', 'DFW', 'SFO', 'ACV'] and distance of 2253
        BestPath shortestDistancePath = airportGraph.getBestPath("MOB", "ACV", FlightCriteria.DISTANCE);
        System.out.println(shortestDistancePath.toString());

        // Returns the shortest distance path of flights from SFO to DWF when flying with DL
        // Solution: a path of ['SFO', 'SLC', 'DFW'] and distance of 1588
        BestPath shortestDistancePath2 = airportGraph.getBestPath("SFO", "DFW", FlightCriteria.DISTANCE, "DL");
        System.out.println(shortestDistancePath2.toString());

        // TODO - This method currently doesn't output the correct path (close), and time (but close
        // as well)
        // Returns the shortest flight time path from MOB to SLC
        // Solution: a path of ['MOB', 'DFW', 'SLC'] and time of ~269.25
        BestPath shortestTimePath = airportGraph.getBestPath("MOB", "SLC", FlightCriteria.TIME);
        System.out.println(shortestTimePath.toString());

        // Returns the fiscally cheapest path of flights from LAS to LAX
        // Solution: a path of ['LAS', 'LAX'] and cost of ~138.39
        BestPath cheapestPath = airportGraph.getBestPath("LAS", "LAX", FlightCriteria.COST);
        System.out.println(cheapestPath.toString());

        // Solution: a path of ['SFO', 'SLC', 'DFW'] and time of ~257.8726
        BestPath shortestDistancePath3 = airportGraph.getBestPath("SFO", "DFW", FlightCriteria.TIME, "DL");
        System.out.println(shortestDistancePath3.toString());
    }

}
