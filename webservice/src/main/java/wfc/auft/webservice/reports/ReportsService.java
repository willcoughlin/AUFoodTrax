package wfc.auft.webservice.reports;

import org.springframework.stereotype.Service;

/** Operates on report data before delivery */

@Service
public class ReportsService {

    private ReportsDao reportsDao;

    public ReportsService(ReportsDao reportsDao) {
        this.reportsDao = reportsDao;
    }

    /** Parses json string */
    void voteOnReport(String voteJson) {
        String alphaNumeric = voteJson.replaceAll(("[^A-Za-z0-9:,]"), "");
        String[] alphaNumericSplit = alphaNumeric.split(":|,");

        String reportId = alphaNumericSplit[1];
        String vote = alphaNumericSplit[3];

        reportsDao.voteOnReport(reportId, vote);
    }
}
