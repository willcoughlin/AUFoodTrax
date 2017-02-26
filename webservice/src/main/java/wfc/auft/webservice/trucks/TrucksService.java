package wfc.auft.webservice.trucks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
