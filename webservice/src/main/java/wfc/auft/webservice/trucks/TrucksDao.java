package wfc.auft.webservice.trucks;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.springframework.stereotype.Repository;
import wfc.auft.webservice.common.DataSource;

import java.util.HashMap;
import java.util.Map;

/**
 * This class handles all interfacing with MongoDB regarding trucks.
 */
@Repository
public class TrucksDao {
    private MongoCollection<Document> trucksCollection = DataSource.mongoDatabase().getCollection("trucks");

    /** @return a Map of all food trucks. */
    Map<String, Document> getAllTrucks() {
        Map<String, Document> trucksMap = new HashMap<>();

        try (MongoCursor<Document> cursor = trucksCollection.find().iterator()) {
            while (cursor.hasNext()) {
                Document currentTruck = cursor.next();
                trucksMap.put(currentTruck.getString("_id"), currentTruck);
            }
        }

        return trucksMap;
    }

    /** @return the Document of the target truck if found, null otherwise. */
    Document getTruckById(String id) {
        Document target = null;

        try (MongoCursor<Document> cursor = trucksCollection.find().iterator()) {
            while (cursor.hasNext()) {
                Document currentTruck = cursor.next();
                String currentTruckId = currentTruck.getString("_id");

                if (currentTruckId.equals(id)) {
                    target = currentTruck;
                    break;
                }
            }
        }

        return target;
    }
}
