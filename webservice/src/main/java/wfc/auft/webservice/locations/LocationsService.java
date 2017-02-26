package wfc.auft.webservice.locations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
