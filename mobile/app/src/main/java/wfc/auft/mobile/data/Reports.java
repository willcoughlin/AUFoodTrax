package wfc.auft.mobile.data;


import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Reports {

    private static final String REQUEST_URL =
            "http://aufoodtrax.azurewebsites.net/webservice/reports";


    public static Map<String, Map<String, String>> getAllReports() throws Exception {

        Map<String, Map<String, String>> result = new HashMap<>();
        Map<String, Object> responseResult = Request.getWithMapResponse(REQUEST_URL);

        for (String key : responseResult.keySet()) {
            Map<String, String> entry = new HashMap<>();

            @SuppressWarnings("unchecked") // We know this will always be a Map<String,Object>
                    Map<String, Object> responseEntry = (Map<String, Object>) responseResult.get(key);
            for (String field : responseEntry.keySet())
                entry.put(field, (String) responseEntry.get(field));

            result.put(key, entry);
        }

        return result;
    }

    public static void voteOnReport(String id, boolean vote) throws Exception {

        JSONObject voteJson = new JSONObject();
        voteJson.put("_id", id);
        voteJson.put("vote", vote ? "yes" : "no");

        Request.postJsonToUrl(voteJson, REQUEST_URL + "/vote");
    }

    public static void sendReport(String deviceId, String location, String truck) throws Exception {
        Random random = new Random();
        String reportId = Integer.toHexString(random.nextInt(255));
        reportId = reportId.length() < 2 ? "0" + reportId : reportId;

        JSONObject reportJson = new JSONObject();
        reportJson.put("_id", reportId);
        reportJson.put("user", deviceId);
        reportJson.put("location", location);
        reportJson.put("truck", truck);

        Request.postJsonToUrl(reportJson, REQUEST_URL + "/new");
    }
}
