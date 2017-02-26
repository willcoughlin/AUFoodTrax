package wfc.auft.mobile.tasks;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import wfc.auft.mobile.MainDelegate;
import wfc.auft.mobile.data.Reports;

public class VoteAsyncTask extends AsyncTask<Void, Void, Void> {

    private MainDelegate activity;
    private ProgressDialog progressDialog;
    private String reportId;
    private boolean vote;

    public VoteAsyncTask(MainDelegate activity, String reportId, boolean vote) {
        this.activity = activity;
        this.progressDialog = new ProgressDialog(activity);

        this.reportId = reportId;
        this.vote = vote;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.setMessage("Sending vote");
        progressDialog.setIndeterminate(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            Reports.voteOnReport(reportId, vote);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        progressDialog.dismiss();
        Toast.makeText(activity, "Vote sent", Toast.LENGTH_SHORT).show();
        new MapPopulateAsyncTask(activity).execute(); // refresh map
    }
}
