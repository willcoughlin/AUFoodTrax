package wfc.auft.webservice.trucks;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


/**
 * Maps trucks collection related requests.
 */
@RestController
@RequestMapping("/trucks")
public class TrucksController {

    private final TrucksService trucksService;

    @Autowired
    public TrucksController(TrucksService trucksDao) {
        this.trucksService = trucksDao;
    }

    /** @return a Map of all food trucks */
    @RequestMapping(method = RequestMethod.GET)
    public Map<String, Document> getAllTrucks() {
        return trucksService.getAllTrucks();
    }

    /** @return the Document of the target truck if found, null otherwise */
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public Document getTruckById(@PathVariable("id") String id) {
        return trucksService.getTruckById(id);
    }
}
