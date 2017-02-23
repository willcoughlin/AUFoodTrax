package wfc.auft.mobile.data;

import java.util.Map;

public class Locations {
    private static Map<String, Map<String, String>> locationsMap;

    static {

    }

    static Map<String, Map<String, String>> getAllLocations() {
        return locationsMap;
    }

    static Map<String, String> getLocationByID(String id) {
        return null;
    }

}
