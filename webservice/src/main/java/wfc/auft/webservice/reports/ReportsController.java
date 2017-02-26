package wfc.auft.webservice.reports;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/reports")
public class ReportsController {

    private final ReportsDao reportsDao;

    @Autowired
    public ReportsController(ReportsDao reportsDao) {
        this.reportsDao = reportsDao;
    }

    @RequestMapping(method = RequestMethod.GET)
    public Map<String, Document> getAllReports() {
        return reportsDao.getAllReports();
    }
}
