package wfc.auft.mobile.tasks;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import wfc.auft.mobile.MainDelegate;
import wfc.auft.mobile.data.Reports;

public class SendReportAsyncTask extends AsyncTask <Void, Void, Void> {

    private ProgressDialog progressDialog;
    private MainDelegate activity;
    private String deviceId;
    private String locationId;
    private String truckId;

    public SendReportAsyncTask(MainDelegate activity, String deviceId, String locationId, String truckId) {
        this.activity = activity;
        this.deviceId = deviceId;
        this.locationId = locationId;
        this.truckId = truckId;
        progressDialog = new ProgressDialog(activity);
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            Reports.sendReport(deviceId, locationId, truckId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.setMessage("Sending Report");
        progressDialog.setIndeterminate(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(Void unused) {
        super.onPostExecute(unused);
        progressDialog.dismiss();
        Toast.makeText(activity, "Report sent", Toast.LENGTH_SHORT).show();
        new MapPopulateAsyncTask(activity).execute(); // refresh map
    }
}
