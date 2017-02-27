package wfc.auft.webservice.locations;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Maps locations collection related requests.
 */
@RestController
@RequestMapping("/locations")
public class LocationsController {

    //private final LocationsDao locationsDao;
    private final LocationsService locationsService;

    @Autowired
    public LocationsController(LocationsService locationsService) {
        this.locationsService = locationsService;
    }

    /** @return a Map of all locations */
    @RequestMapping(method = RequestMethod.GET)
    public Map<String, Document> getAllLocations() {
        return locationsService.getAllLocations();
    }

    /** @return the Document of the target truck if found, null otherwise */
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public Document getLocationById(@PathVariable("id") String id) {
        return locationsService.getLocationById(id);
    }
}
