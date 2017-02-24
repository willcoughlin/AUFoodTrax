package wfc.auft.mobile.data;

import java.util.HashMap;
import java.util.Map;

/**
 * This class requests any or all set locations from the specified URL.
 */
public class Locations {
    public static final String REQUEST_URL =
            "http://aufoodtrax.azurewebsites.net/webservice/locations";


    public static Map<String, Map<String, String>> getAllLocations() throws Exception {

        Map<String, Map<String, String>> result = new HashMap<>();
        Map<String, Object> responseResult = Request.getWithMapResponse(REQUEST_URL);

        for (String key : responseResult.keySet()) {
            Map<String, String> entry = new HashMap<>();
            Map<String, Object> responseEntry = (Map<String, Object>) responseResult.get(key);
            for (String field : responseEntry.keySet())
                entry.put(field, (String) responseEntry.get(field));

            result.put(key, entry);
        }

        return result;
    }

    public static Map<String, String> getLocationByID(String id) throws Exception {

        Map<String, String> result = new HashMap<>();
        Map<String, Object> mapResponse = Request.getWithMapResponse(REQUEST_URL + "/" + id);

        for (String key : mapResponse.keySet())
            result.put(key, (String) mapResponse.get(key));

        return result;
    }

}

