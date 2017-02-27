package wfc.auft.webservice.reports;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/** Maps reporting operations */

@RestController
@RequestMapping("/reports")
public class ReportsController {

    private final ReportsDao reportsDao;
    private final ReportsService reportsService;

    @Autowired
    public ReportsController(ReportsDao reportsDao, ReportsService reportsService) {
        this.reportsDao = reportsDao;
        this.reportsService = reportsService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public Map<String, Document> getAllReports() {
        return reportsDao.getAllReports();
    }

    @RequestMapping(value ="/vote", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void voteOnReport(@RequestBody String voteJson) {
        reportsService.voteOnReport(voteJson);
    }

    @RequestMapping(value = "/new", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void newReport(@RequestBody String reportJson) {
        reportsService.newReport(reportJson);
    }
}
