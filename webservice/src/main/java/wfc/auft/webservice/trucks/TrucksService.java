package wfc.auft.webservice.trucks;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TrucksService {

    private TrucksDao trucksDao;

    @Autowired
    public TrucksService(TrucksDao trucksDao) {
        this.trucksDao = trucksDao;
    }


    public void updateTruckById(String id, String field, String value) {
        trucksDao.updateTruckById(id, field, value);
    }

    public Map<String,Document> getAllTrucks() {
        return trucksDao.getAllTrucks();
    }

    public Document getTruckById(String id) {
        return trucksDao.getTruckById(id);
    }
}
