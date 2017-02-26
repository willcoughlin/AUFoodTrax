package wfc.auft.mobile.tasks;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import wfc.auft.mobile.MainDelegate;
import wfc.auft.mobile.data.Locations;
import wfc.auft.mobile.data.Trucks;

public class MapPopulateAsyncTask extends AsyncTask<Void, Void, Map<String, Map<String, Map<String, String>>>>
{
    private MainDelegate activity;
    private ProgressDialog progressDialog;

    public MapPopulateAsyncTask(MainDelegate activity)
    {
        this.activity = activity;
        this.progressDialog = new ProgressDialog(activity);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.setMessage("Populating map");
        progressDialog.setIndeterminate(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    @Override
    protected Map<String, Map<String, Map<String, String>>> doInBackground(Void... voids) {
        Map<String, Map<String, String>> locations;
        try {
            locations = Locations.getAllLocations();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        Map<String, Map<String, String>> trucks;
        try {
            trucks = Trucks.getAllTrucks();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        Map<String, Map<String, Map<String, String>>> result = new HashMap<>();
        result.put("locations", locations);
        result.put("trucks", trucks);

        return result;
    }


    protected void onPostExecute(Map<String, Map<String, Map<String, String>>> result) {
        super.onPostExecute(result);
        progressDialog.dismiss();
        if (result == null || result.isEmpty()) {
            Toast.makeText(activity, "Failed to populate map. Check your internet connection.",
                    Toast.LENGTH_LONG).show();
        }
        activity.updateMap(result);
    }
}