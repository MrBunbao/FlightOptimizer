package assignment13;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * JUnit test Class for NetworkGraph.
 * 
 * Tests dealing with the cost has a delta value of .01 to have a decimal precision of 3, which is
 * our margin of error (0.000 decimal places)
 * 
 * @author Andy Dao, uID: u0692334
 * @author Casey Yip, uID: u1025709
 */
public class JUnitTests {
    private static NetworkGraph testFile;
    private static NetworkGraph flights2015File;
    private static NetworkGraph noAirportsFile;
    private static NetworkGraph secondTestFile;

    // Our margin or error of a decimal precision of 3
    private static final double marginOfError = 0.009;

    @BeforeClass
    public static void setupOnce() throws Exception {
        testFile = new NetworkGraph("testfile.csv");
        noAirportsFile = new NetworkGraph("noAirports.csv"); // CSV file with no airports besides
                                                             // headers
        secondTestFile = new NetworkGraph("second_testfile.csv");

        // That way we setup the large file only once
        flights2015File = new NetworkGraph("flights-2015-q3.csv");
    }

    @AfterClass
    public static void tearDownOnce() throws Exception {
        testFile = null;
        noAirportsFile = null;
        secondTestFile = null;
        flights2015File = null;
    }

    // ===== Tests provided to us ===== //

    @Test
    public void MOB_ACV_Distance() {
        BestPath shortestDistance = flights2015File.getBestPath("MOB", "ACV", FlightCriteria.DISTANCE);
        assertEquals(2253.0, shortestDistance.pathLength, marginOfError);
    }

    @Test
    public void SFO_DFW_Distance_DL() {
        BestPath shortestDistance = flights2015File.getBestPath("SFO", "DFW", FlightCriteria.DISTANCE, "DL");
        assertEquals(1588.0, shortestDistance.pathLength, marginOfError);
    }

    @Test
    public void MOB_SLC_TIME() {
        BestPath shortestDistance = flights2015File.getBestPath("MOB", "SLC", FlightCriteria.TIME);
        assertEquals(269.25, shortestDistance.pathLength, marginOfError);
    }

    @Test
    public void LAS_LAX_COST() {
        BestPath shortestDistance = flights2015File.getBestPath("LAS", "LAX", FlightCriteria.COST);
        assertEquals(138.39, shortestDistance.pathLength, marginOfError);
    }

    // ========== *** OUR TESTS *** ========== //

    // ===== ~ SMALL TESTS ~ =====//

    // * Unique Airports:

    @Test
    public void addedUniqueAirports() {
        HashMap<String, Airport> allAirports = testFile.getAirports();
        ArrayList<String> airportsInGraphArray = new ArrayList<String>();
        airportsInGraphArray.add("PAX");
        airportsInGraphArray.add("ENI");
        airportsInGraphArray.add("BLW");
        airportsInGraphArray.add("OTC");
        airportsInGraphArray.add("UMN");
        airportsInGraphArray.add("SFO");
        airportsInGraphArray.add("SLC");

        // 6 unique airports total from the file
        assertEquals(7, allAirports.size());

        // Check for specific names
        if (allAirports.size() != airportsInGraphArray.size()) {
            fail("The hashmap doesn't have the right amount of airports.");
        } else {
            for (String str : airportsInGraphArray) {
                assertTrue(allAirports.containsKey(str));
            }
        }
    }

    // * Unique Flights:

    @Test
    public void addedUniqueFlights() {
        HashMap<String, Airport> airports = testFile.getAirports();
        // Only 1 unique flight for SFO, which goes to SFO
        assertEquals(2, airports.get("SFO").flights.size());
        // 3 unique flights from PAX. They go to the following destinations: ENI, BLW, OTC.
        assertEquals(3, airports.get("PAX").flights.size());
        // Only 1 unique flight for BLW, which they all go to: PAX
        assertEquals(1, airports.get("BLW").flights.size());
    }

    // ===== ~ * EXTENSIVE TESTS * ~ =====//

    // ===== ~ BLW TO PAX ~ ===== //

    @Test
    public void BLW_PAX_COST() {
        BestPath shortestDistance = testFile.getBestPath("BLW", "PAX", FlightCriteria.COST);
        assertEquals(596.75, shortestDistance.pathLength, marginOfError);
    }

    @Test
    public void BLW_PAX_COST_PATH() {
        ArrayList<String> path = new ArrayList<String>();
        path.add("BLW");
        path.add("PAX");
        BestPath shortestDistance = testFile.getBestPath("BLW", "PAX", FlightCriteria.COST);
        assertEquals(path, shortestDistance.path);
    }

    @Test
    public void BLW_PAX_COST_AA() {
        BestPath shortestDistance = testFile.getBestPath("BLW", "PAX", FlightCriteria.COST, "AA");
        assertEquals(596.75, shortestDistance.pathLength, marginOfError);
    }

    @Test
    public void BLW_PAX_COST_AA_PATH() {
        ArrayList<String> path = new ArrayList<String>();
        path.add("BLW");
        path.add("PAX");
        BestPath shortestDistance = testFile.getBestPath("BLW", "PAX", FlightCriteria.COST, "AA");
        assertEquals(path, shortestDistance.path);
    }

    // ===== ~ PAX TO BLW ~ ===== //

    @Test
    public void PAX_BLW_COST() {
        BestPath shortestDistance = testFile.getBestPath("PAX", "BLW", FlightCriteria.COST);
        assertEquals(765.87, shortestDistance.pathLength, marginOfError);
    }


    // ===== ~ PTX TO [DESTINATION] ~ ===== //

    @Test
    public void PTX_ENI_DELAYED() {
        BestPath shortestDELAY = testFile.getBestPath("PAX", "ENI", FlightCriteria.DELAY);
        ArrayList<String> flightPath = new ArrayList<String>();
        flightPath.add("PAX");
        flightPath.add("ENI");

        assertEquals(286, shortestDELAY.pathLength, marginOfError);
        assertEquals(flightPath, shortestDELAY.path);
    }

    @Test
    public void PTX_ENI_CANCELED() {
        BestPath shortestCANCELED = testFile.getBestPath("PAX", "ENI", FlightCriteria.CANCELED);
        ArrayList<String> flightPath = new ArrayList<String>();
        flightPath.add("PAX");
        flightPath.add("ENI");

        assertEquals(0.5, shortestCANCELED.pathLength, marginOfError);
        assertEquals(flightPath, shortestCANCELED.path);
    }

    @Test
    public void PTX_ENI_TIME() {
        BestPath shortestTIME = testFile.getBestPath("PAX", "ENI", FlightCriteria.TIME);
        ArrayList<String> flightPath = new ArrayList<String>();
        flightPath.add("PAX");
        flightPath.add("ENI");

        assertEquals(94, shortestTIME.pathLength, marginOfError);
        assertEquals(flightPath, shortestTIME.path);
    }

    @Test
    public void PTX_ENI_DISTANCE() {
        BestPath shortestDISTANCE = testFile.getBestPath("PAX", "ENI", FlightCriteria.DISTANCE);
        ArrayList<String> flightPath = new ArrayList<String>();
        flightPath.add("PAX");
        flightPath.add("ENI");

        assertEquals(655, shortestDISTANCE.pathLength, marginOfError);
        assertEquals(flightPath, shortestDISTANCE.path);
    }

    @Test
    public void PTX_ENI_COST() {
        BestPath shortestCOST = testFile.getBestPath("PAX", "ENI", FlightCriteria.COST);
        ArrayList<String> flightPath = new ArrayList<String>();
        flightPath.add("PAX");
        flightPath.add("ENI");

        assertEquals(421.14, shortestCOST.pathLength, marginOfError);
        assertEquals(flightPath, shortestCOST.path);
    }

    // * with airline

    @Test
    public void PTX_ENI_DELAYED_DL() {
        BestPath shortestDELAY = testFile.getBestPath("PAX", "ENI", FlightCriteria.DELAY, "DL");
        ArrayList<String> flightPath = new ArrayList<String>();
        flightPath.add("PAX");
        flightPath.add("ENI");

        assertEquals(286, shortestDELAY.pathLength, marginOfError);
        assertEquals(flightPath, shortestDELAY.path);
    }

    @Test
    public void PTX_ENI_CANCELED_WN() {
        BestPath shortestCANCELED = testFile.getBestPath("PAX", "ENI", FlightCriteria.CANCELED, "WN");
        ArrayList<String> flightPath = new ArrayList<String>();
        flightPath.add("PAX");
        flightPath.add("ENI");

        assertEquals(0.5, shortestCANCELED.pathLength, marginOfError);
        assertEquals(flightPath, shortestCANCELED.path);
    }

    @Test
    public void PTX_ENI_TIME_DL() {
        BestPath shortestTIME = testFile.getBestPath("PAX", "ENI", FlightCriteria.TIME, "DL");
        ArrayList<String> flightPath = new ArrayList<String>();
        flightPath.add("PAX");
        flightPath.add("ENI");

        assertEquals(94, shortestTIME.pathLength, marginOfError);
        assertEquals(flightPath, shortestTIME.path);
    }

    @Test
    public void PTX_ENI_DISTANCE_DL() {
        BestPath shortestDISTANCE = testFile.getBestPath("PAX", "ENI", FlightCriteria.DISTANCE, "DL");
        ArrayList<String> flightPath = new ArrayList<String>();
        flightPath.add("PAX");
        flightPath.add("ENI");

        assertEquals(655, shortestDISTANCE.pathLength, marginOfError);
        assertEquals(flightPath, shortestDISTANCE.path);
    }

    @Test
    public void PTX_ENI_COST_DL() {
        BestPath shortestCOST = testFile.getBestPath("PAX", "ENI", FlightCriteria.COST, "DL");
        ArrayList<String> flightPath = new ArrayList<String>();
        flightPath.add("PAX");
        flightPath.add("ENI");

        assertEquals(421.14, shortestCOST.pathLength, marginOfError);
        assertEquals(flightPath, shortestCOST.path);
    }

    // ===== ~ SFO TO SLC ~ ===== //

    @Test
    public void SFO_SLC_COST() {
        // There are 2 negative values for cost for SFO -> SLC. Shouldn't calculate these into
        // average
        BestPath shortestCOST = testFile.getBestPath("SFO", "SLC", FlightCriteria.COST);
        assertEquals(223, shortestCOST.pathLength, marginOfError);
    }

    @Test
    public void SFO_SLC_DELAY() {
        // There are 2 negative values for cost for SFO -> SLC. Shouldn't calculate these into
        // average
        BestPath shortestDELAY = testFile.getBestPath("SFO", "SLC", FlightCriteria.DELAY);
        assertEquals(527, shortestDELAY.pathLength, marginOfError);
    }

    // ====== * SLC TO PHX * ===== //

    @Test
    public void SLC_PHX_COST() {
        ArrayList<String> path = new ArrayList<String>();
        path.add("SLC");
        path.add("BLW");
        path.add("PAX");
        path.add("PHX");

        BestPath shortestDistance = secondTestFile.getBestPath("SLC", "PHX", FlightCriteria.COST);
        assertEquals(1965.60, shortestDistance.pathLength, marginOfError);
        assertEquals(path, shortestDistance.path);
    }

    // ====== * ENI TO [DESTINATION] * ===== //

    @Test
    public void ENI_BLW_DELAY_GB_PATH() {
        ArrayList<String> path = new ArrayList<String>();
        path.add("ENI");
        path.add("OTC");
        path.add("BLW");
        BestPath shortestDistance = secondTestFile.getBestPath("ENI", "BLW", FlightCriteria.DELAY, "GB");
        assertEquals(path, shortestDistance.path);
    }

    @Test
    public void ENI_OTC_CANCEL_GB_PATH() {
        ArrayList<String> path = new ArrayList<String>();
        path.add("ENI");
        path.add("OTC");
        BestPath shortestDistance = secondTestFile.getBestPath("ENI", "OTC", FlightCriteria.CANCELED, "GB");
        assertEquals(path, shortestDistance.path);
    }

    @Test
    public void ENI_PAX_COST_WN_NEAGTIVEVALUE() {
        BestPath shortestDistance = secondTestFile.getBestPath("ENI", "PAX", FlightCriteria.COST, "WN");
        assertEquals(new ArrayList<String>(), shortestDistance.path);
    }


    // ====== * PDD TO GOK * ===== //

    @Test
    public void PDD_GOK_COST_TS_NEAGTIVEVALUE() {
        BestPath shortestDistance = secondTestFile.getBestPath("PDD", "GOK", FlightCriteria.COST, "TS");
        assertEquals(new ArrayList<String>(), shortestDistance.path);
        assertEquals(0, shortestDistance.pathLength, marginOfError);
    }

    @Test
    public void PDD_GOK_COST_NEAGTIVEVALUE() {
        ArrayList<String> path = new ArrayList<String>();
        path.add("PDD");
        path.add("THX");
        path.add("UOY");
        path.add("ABC");
        path.add("GOK");

        BestPath shortestDistance = secondTestFile.getBestPath("PDD", "GOK", FlightCriteria.COST);
        assertEquals(path, shortestDistance.path);
        assertEquals(1429, shortestDistance.pathLength, marginOfError);
    }

    @Test
    public void PDD_GOK_COST_WN_NEAGTIVEVALUE() {
        BestPath shortestDistance = secondTestFile.getBestPath("PDD", "GOK", FlightCriteria.COST, "WN");
        assertEquals(new ArrayList<String>(), shortestDistance.path);
    }

    @Test
    public void PDD_GOK_COST_TZ_NEAGTIVEVALUE() {
        ArrayList<String> path = new ArrayList<String>();
        path.add("PDD");
        path.add("THX");
        path.add("UOY");
        path.add("ABC");
        path.add("GOK");

        BestPath shortestDistance = secondTestFile.getBestPath("PDD", "GOK", FlightCriteria.COST, "TZ");
        assertEquals(path, shortestDistance.path);
        assertEquals(1429, shortestDistance.pathLength, marginOfError);
    }

    // ===== ~ No AIRPORTS IN CSV ~ ===== //

    @Test
    public void noAirports_in_file() {
        BestPath noAirportsPath = noAirportsFile.getBestPath("BLW", "PAX", FlightCriteria.COST);
        assertEquals(new ArrayList<String>(), noAirportsPath.path);
        assertEquals(0.0, noAirportsPath.pathLength, marginOfError);
    }

    // ===== ~ SAME ORIGIN IN CSV ~ ===== //

    @Test
    public void originDestination_same() {
        BestPath sameOriginDest = testFile.getBestPath("SFO", "SFO", FlightCriteria.COST);
        assertEquals(0.0, sameOriginDest.pathLength, marginOfError);
    }



}
