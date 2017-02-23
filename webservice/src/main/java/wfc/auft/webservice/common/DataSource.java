package wfc.auft.webservice.common;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

/**
 * This class defines the MongoDatabase that will be used and connects to it.
 */
public final class DataSource {

    private static final MongoClientURI connectionString = new MongoClientURI("mongodb://auft:tLOocuWXENvsJX97b5DvibMlMXVa8rdFpocMVeDuGaha09uKMGDT0BaJVqN6c68UImwwuSVKMUdBmr20uxR27Q==@auft.documents.azure.com:10250/?ssl=true&sslInvalidHostNameAllowed=true");
    private static final MongoClient mongoClient = new MongoClient(connectionString);
    private static final MongoDatabase mongoDatabase = mongoClient.getDatabase("auft");

    public static MongoDatabase mongoDatabase() {
        return mongoDatabase;
    }
}
