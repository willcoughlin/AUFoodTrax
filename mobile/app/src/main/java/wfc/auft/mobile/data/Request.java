package wfc.auft.mobile.data;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * This class provides methods to interface with the REST service.
 */
public class Request {

    /** @return result of GET as string */
    public static String get(String urlString) throws Exception {
        StringBuilder result = new StringBuilder();
        URL url = new URL(urlString);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        rd.close();

        return result.toString();
    }

    /** @return result of a request which responds with JSON as a Map */
    static Map<String, Object> getWithMapResponse(String urlString) throws Exception {
        String textResponse = get(urlString);
        Gson gson = new Gson();

        Map<String, Object> map = new HashMap<>();
        map =  (Map<String, Object>) gson.fromJson(textResponse, map.getClass());

        return map;
    }
}
