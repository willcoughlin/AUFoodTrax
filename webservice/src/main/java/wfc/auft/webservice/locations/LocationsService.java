package wfc.auft.webservice.locations;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class LocationsService {

    LocationsDao locationsDao;

    @Autowired
    public LocationsService(LocationsDao locationsDao) {
        this.locationsDao = locationsDao;
    }

    public void updateLocationById(String id, String field, String value) {
        locationsDao.updateLocationById(id, field, value);
    }

    public Map<String,Document> getAllLocations() {
        return locationsDao.getAllLocations();
    }

    public Document getLocationById(String id) {
        return locationsDao.getLocationById(id);
    }
}
