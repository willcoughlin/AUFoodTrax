package wfc.auft.mobile.data;


import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Map;

public class MapState {

    private Map<String, Map<String, String>> locations;
    private Map<String, MarkerOptions> markerOptions;

    public Map<String, MarkerOptions> getMarkerOptions() {
        return markerOptions;
    }

    public void loadMarkerOptions() {
        markerOptions = new HashMap<>();

        for (String id : locations.keySet()) {
            Map<String, String> location = locations.get(id);
            LatLng coordinates = new LatLng(
                    Double.parseDouble(location.get("lat")),
                    Double.parseDouble(location.get("lng"))
            );

            markerOptions.put(id, new MarkerOptions().position(coordinates));
        }
    }

    public void setLocations(Map<String, Map<String, String>> locations) {
        this.locations = new HashMap<>(locations);
    }
}
