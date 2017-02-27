package wfc.auft.webservice.reports;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.util.JSON;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import wfc.auft.webservice.common.DataSource;
import wfc.auft.webservice.locations.LocationsService;
import wfc.auft.webservice.trucks.TrucksService;

import java.util.HashMap;
import java.util.Map;

/** This class interfaces with the currentreports collection */
@Repository
public class ReportsDao {

    private LocationsService locationsService;
    private TrucksService   trucksService;

    private MongoCollection<Document> reportsCollection =
            DataSource.mongoDatabase().getCollection("currentreports");

    @Autowired public ReportsDao(LocationsService locationsService, TrucksService trucksService) {
        this.locationsService = locationsService;
        this.trucksService = trucksService;
    }

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

    Document getReportById(String id) {
        Document target = null;
        BasicDBObject query = new BasicDBObject("_id", id);
        try (MongoCursor<Document> cursor = reportsCollection.find(query).iterator()) {
            while (cursor.hasNext())
                target = cursor.next();
        }

        return target;
    }

    void voteOnReport(String reportId, String vote) {
        Document report = getReportById(reportId);

        if (vote.equals("no")) {
            reportsCollection.deleteOne(report);
        } else if (vote.equals("yes")) {
            String truckId = report.getString("truck");
            String locationId = report.getString("location");

            trucksService.updateTruckById(truckId, "location", locationId);
            locationsService.updateLocationById(locationId, "truck", truckId);

            reportsCollection.deleteOne(report);
        }
    }

    public void newReport(String jsonString) {
        Document document = Document.parse(jsonString);

        String truck = document.getString("truck");
        String location = document.getString("location");

        Document truckDoc = trucksService.getTruckById(truck);
        Document locationDoc = locationsService.getLocationById(location);

        if (truckDoc != null && locationDoc != null && !truckDoc.isEmpty() && !locationDoc.isEmpty())
            reportsCollection.insertOne(document);
    }
}
