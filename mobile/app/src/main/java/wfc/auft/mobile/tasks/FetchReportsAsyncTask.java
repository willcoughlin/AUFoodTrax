package wfc.auft.mobile.tasks;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.Map;

import wfc.auft.mobile.MainDelegate;
import wfc.auft.mobile.data.Reports;

public class FetchReportsAsyncTask extends AsyncTask<Void, Void, Map<String, Map<String, String>>> {

    private MainDelegate activity;
    private ProgressDialog progressDialog;

    public FetchReportsAsyncTask(MainDelegate activity) {
        this.activity = activity;
        progressDialog = new ProgressDialog(activity);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.setMessage("Fetching Reports");
        progressDialog.setIndeterminate(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(true);
        progressDialog.show();
    }


    @Override
    protected Map<String, Map<String, String>> doInBackground(Void... voids) {
        Map<String, Map<String, String>> reports;
        try {
            reports = Reports.getAllReports();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(activity, "Failed to fetch reports. Check your internet connection.",
                    Toast.LENGTH_LONG).show();
            return null;
        }

        return reports;
    }
    @Override
    protected void onPostExecute(Map<String, Map<String, String>> result) {
        super.onPostExecute(result);
        progressDialog.dismiss();
        if (result == null || result.isEmpty()) {
            Toast.makeText(activity, "No new reports",
                    Toast.LENGTH_LONG).show();
        }
        activity.updateReports(result);
    }
}
