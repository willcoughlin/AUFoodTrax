package wfc.auft.mobile.data;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
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

    public static void postJsonToUrl(JSONObject jsonObject, String urlString) throws Exception {


        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestMethod("POST");

        OutputStream os = conn.getOutputStream();
        os.write(jsonObject.toString().getBytes("UTF-8"));
        os.close();

        // read the response
        InputStream in = new BufferedInputStream(conn.getInputStream());
        String result = org.apache.commons.io.IOUtils.toString(in, "UTF-8");
        JSONObject response = new JSONObject(result);


        in.close();
        conn.disconnect();
        //return jsonObject;
    }

}
