package wfc.auft.webservice.reports;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.springframework.stereotype.Repository;
import wfc.auft.webservice.common.DataSource;

import java.util.HashMap;
import java.util.Map;

/** This class interfaces with the currentreports collection */
@Repository
public class ReportsDao {

    private MongoCollection<Document> reportsCollection =
            DataSource.mongoDatabase().getCollection("currentreports");

    Map<String,Document> getAllReports() {
        Map<String, Document> locationsMap = new HashMap<>();

        try (MongoCursor<Document> cursor = reportsCollection.find().iterator()) {
            while (cursor.hasNext()) {
                Document currentLocation = cursor.next();
                locationsMap.put(currentLocation.getString("_id"), currentLocation);
            }
        }

        return locationsMap;
    }
}
