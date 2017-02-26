package wfc.auft.webservice.locations;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.springframework.stereotype.Repository;
import wfc.auft.webservice.common.DataSource;

import java.util.HashMap;
import java.util.Map;

/** This class interfaces with the DB regarding locations. */
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
        BasicDBObject query = new BasicDBObject("_id", id);
        try (MongoCursor<Document> cursor = locationsCollection.find(query).iterator()) {
            while (cursor.hasNext())
                target = cursor.next();
        }

        return target;
    }

}
