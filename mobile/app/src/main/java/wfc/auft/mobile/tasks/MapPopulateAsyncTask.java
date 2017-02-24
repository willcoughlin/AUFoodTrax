package wfc.auft.mobile.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.Map;

import wfc.auft.mobile.data.Locations;


public class MapPopulateAsyncTask extends AsyncTask<Void, Void, Map<String, LatLng>>
{
    private Context context;
    private ProgressDialog progressDialog;

    public MapPopulateAsyncTask(Context context)
    {
        this.context = context;
        this.progressDialog = new ProgressDialog(context);
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
    protected Map<String, LatLng> doInBackground(Void... voids) {

        Map<String, Map<String, String>> locations;
        try {
            locations = Locations.getAllLocations();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        Map<String, LatLng> pinLocations = new HashMap<>();

        for (String id : locations.keySet()) {
            Double lat = Double.parseDouble(locations.get(id).get("lat"));
            Double lng = Double.parseDouble(locations.get(id).get("lng"));

            pinLocations.put(id, new LatLng(lat, lng));
        }

        return pinLocations;
    }


    protected void onPostExecute(Map<String, LatLng> result) {
        super.onPostExecute(result);
        progressDialog.dismiss();
        if (result == null || result.isEmpty())
            Toast.makeText(context, "Failed to update. Check your internet connection.",
                    Toast.LENGTH_LONG).show();
    }
}