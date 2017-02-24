package wfc.auft.mobile;

import org.json.JSONObject;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import wfc.auft.mobile.data.Locations;
import wfc.auft.mobile.data.Request;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void getLocationById() {
        try {
            assertEquals(Locations.getLocationByID("hlytch-sw").get("_id"), "hlytch-sw");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void getAllLocations() {
        try {
            String expected = "hlytch-sw";
            Map<String, Map<String, String>> allLocations = Locations.getAllLocations();
            Map<String, String> location = allLocations.get(expected);
            String id = location.get("_id");
            assertEquals(expected, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}