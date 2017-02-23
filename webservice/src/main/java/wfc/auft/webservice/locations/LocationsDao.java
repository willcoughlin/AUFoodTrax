package wfc.auft.webservice.locations;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.springframework.stereotype.Repository;
import wfc.auft.webservice.common.DataSource;

import java.util.HashMap;
import java.util.Map;

/** This class handles all DB requests regarding locations. */
@Repository
public class LocationsDao {
    private MongoCollection<Document> locationsCollection = DataSource.mongoDatabase().getCollection("locations");

    /** @return a Map of all locations. */
    Map<String, Document> getAllLocations() {
        Map<String, Document> locationsMap = new HashMap<>();

        try (MongoCursor<Document> cursor = locationsCollection.find().iterator()) {
            while (cursor.hasNext()) {
                Document currentLocation = cursor.next();
                locationsMap.put(currentLocation.getString("_id"), currentLocation);
            }
        }

        return locationsMap;
    }

    /** @return a target location as specified, if found, null otherwise */
    Document getLocationById(String id) {
        Document target = null;
        try (MongoCursor<Document> cursor = locationsCollection.find().iterator()) {
            while (cursor.hasNext()) {
                Document currentLocation = cursor.next();
                String currentTruckId = currentLocation.getString("_id");

                if (currentTruckId.equals(id)) {
                    target = currentLocation;
                    break;
                }
            }
        }

        return target;
    }

}
