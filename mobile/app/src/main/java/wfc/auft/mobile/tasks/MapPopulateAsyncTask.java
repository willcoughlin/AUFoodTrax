package wfc.auft.mobile.tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.Map;

import wfc.auft.mobile.MainDelegate;
import wfc.auft.mobile.data.Locations;


public class MapPopulateAsyncTask extends AsyncTask<Void, Void, Map<String, Map<String, String>>>
{
    private MainDelegate activity;
    private ProgressDialog progressDialog;

    private Map<String, Map<String, String>> locations;

    public MapPopulateAsyncTask(MainDelegate activity)
    {
        this.activity = activity;
        this.progressDialog = new ProgressDialog(activity);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.setMessage("Loading");
        progressDialog.setIndeterminate(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    @Override
    protected Map<String, Map<String, String>> doInBackground(Void... voids) {
        try {
            locations = Locations.getAllLocations();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return locations;
    }


    protected void onPostExecute(Map<String, Map<String, String>> result) {
        super.onPostExecute(result);
        progressDialog.dismiss();
        if (result == null || result.isEmpty()) {
            Toast.makeText(activity, "Failed to update. Check your internet connection.",
                    Toast.LENGTH_LONG).show();
        } else {
            activity.updateMap(result);
        }
    }
}