package wfc.auft.mobile;

import android.animation.LayoutTransition;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import wfc.auft.mobile.data.Trucks;
import wfc.auft.mobile.tasks.MapPopulateAsyncTask;

public class MainActivity extends MainDelegate implements OnMapReadyCallback, View.OnClickListener,
        GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener {

    /*
     * TODO implement send report
     * TODO implement send vote
     * TODO clean commented code
     */

    // Context definitions
    final int CONTEXT_MAP   = 0;
    final int CONTEXT_INFO  = 1;
    //final int CONTEXT_LIST  = 2;

    private int currentContext;
    private View contentView;

    private View mapView;
    private GoogleMap map;
    private MapPopulateAsyncTask mapPopulateAsyncTask;
    private Map<String, Marker> mapMarkers;
    private Map<String, Map<String, String>> locations;
    private Map<String, Map<String, String>> trucks;

    private View infoView;

    private Boolean isFabOpen = false;
    private FloatingActionButton fabMain, fab1, fab2;

    private Animation fabMainRotateRight, fabMainRotateLeft;
    private Animation fab1SlideUp, fab1SlideDown;
    private Animation fab2SlideUp, fab2SlideDown;
    private Animation fab3Rotate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        String deviceID = sharedPref.getString("deviceID", "NO_ID");

        if (deviceID.equals("NO_ID")) {
            deviceID = UUID.randomUUID().toString();
        }

        Log.d("UUID", deviceID);

        contentView = findViewById(R.id.content_main);
        mapView = getLayoutInflater().inflate(R.layout.map_view, (ViewGroup)contentView, false);
        infoView = getLayoutInflater().inflate(R.layout.info_view, (ViewGroup)contentView, false);

        // Create FABS
        fabMain = (FloatingActionButton) findViewById(R.id.fab_main);
        fab1 = (FloatingActionButton)findViewById(R.id.fab_1);
        fab2 = (FloatingActionButton)findViewById(R.id.fab_2);

        fabMain.setOnClickListener(this);
        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);

        fab2.setImageResource(R.drawable.ic_refresh); // fab2 (top) is always refresh
        setFabContext(currentContext);

        // Define animations
        fabMainRotateRight = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_main_rotate_right);
        fabMainRotateLeft = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_main_rotate_left);

        fab1SlideUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_1_slide_up);
        fab1SlideDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_1_slide_down);

        fab2SlideUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_2_slide_up);
        fab2SlideDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_2_slide_down);

        fab3Rotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_3_rotate);

        LayoutTransition layoutTransition = new LayoutTransition();
        ((ViewGroup) contentView).setLayoutTransition(layoutTransition);

        // Create map
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mapPopulateAsyncTask = new MapPopulateAsyncTask(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;

        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
       // map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(32.6025, -85.4865)));
        //map.moveCamera(CameraUpdateFactory.zoomTo(17.5f));
        map.setMinZoomPreference(17.0f);
        map.setLatLngBoundsForCameraTarget(new LatLngBounds(
                new LatLng(32.598, -85.490), new LatLng(32.609, -85.481)
        ));
        map.setOnMarkerClickListener(this);
        map.setOnInfoWindowClickListener(this);

        mapPopulateAsyncTask.execute();
    }

    @Override
    public void onClick(View v) {
        Log.d("onClick", "marker not clicked");
        int id = v.getId();
        if (id == R.id.fab_main) {
            animateFAB();
            // removed unnecessary return
        }
        else {
            if (id == R.id.fab_1 ) {
                if (currentContext == CONTEXT_INFO) {
                    currentContext = CONTEXT_MAP;
                }
                else {
                    currentContext = CONTEXT_INFO;
                }
            }
            else if(id == R.id.fab_2) {
                fab3Rotate.setRepeatCount(-1);
                fab2.startAnimation(fab3Rotate);
                new MapPopulateAsyncTask(this).execute();
            }

            setFabContext(currentContext);
        }
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        CameraPosition newPosition = new CameraPosition.Builder()
                .target(marker.getPosition())
                .zoom(17.7f).build();

        map.animateCamera(CameraUpdateFactory.newCameraPosition(newPosition));
        marker.showInfoWindow();
        return true;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        if (marker.getTitle().equals("Tap to report truck at this location")) {
            reportTruckDialog(true);
        } else  {
            AlertDialog.Builder voteDialog = new AlertDialog.Builder(this);
            voteDialog.setMessage("Is the currently reported truck accurate?");
            voteDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // send vote
                }
            });
            voteDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    reportTruckDialog(false);
                }
            });

            voteDialog.show();
        }
    }

    public void updateMap(Map<String, Map<String, Map<String, String>>> results) {

        locations = results.get("locations");
        trucks = results.get("trucks");

        map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(32.6025, -85.4865)));
        map.moveCamera(CameraUpdateFactory.zoomTo(17.5f));

        if (isFabOpen) {
            // If fab is open when this is called then it was called because the refresh button was
            // pressed.  Stop the spin animation and show the fab3 in the place it was.
            fab3Rotate.setRepeatCount(0);
            //fab3.clearAnimation();
            //fab3.setVisibility(View.VISIBLE);
        }

        if (locations == null || locations.isEmpty())
            return; // no data to load to map


        map.clear();
        mapMarkers = new HashMap<>();

        for (String id : locations.keySet()) {
            Double lat = Double.parseDouble(locations.get(id).get("lat"));
            Double lng = Double.parseDouble(locations.get(id).get("lng"));

            //pinLocations.put(id, new LatLng(lat, lng));

            LatLng position = new LatLng(lat, lng);

            MarkerOptions options =
                    new MarkerOptions().position(position);

            String truckId = locations.get(id).get("truck");

            if (truckId.length() > 1) {
                Map<String, String> currentTruck = trucks.get(truckId);

                if (currentTruck != null)
                    options.title(currentTruck.get("name"));
                else
                    options.title("No truck at this location");

                options.snippet("Tap to confirm or report an error");

                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            } else {
                options.title("Tap to report truck at this location");
                options.snippet(locations.get(id).get("desc"));
            }

            mapMarkers.put(id, map.addMarker(options));
        }

    }

    private void animateFAB(){
        if(isFabOpen){ // do animations in reverse order when closing
            fab1.bringToFront();
            fab2.startAnimation(fab2SlideDown);

            fabMain.bringToFront();
            fab1.startAnimation(fab1SlideDown);

            fabMain.startAnimation(fabMainRotateLeft);

            fab1.setClickable(false);
            fab2.setClickable(false);

            isFabOpen = false;
        }
        else {
            fabMain.startAnimation(fabMainRotateRight);

            fab1.startAnimation(fab1SlideUp);
            fab1.bringToFront();

            fab2.startAnimation(fab2SlideUp);
            fab2.bringToFront();

            fab1.setClickable(true);
            fab2.setClickable(true);

            isFabOpen = true;
        }
    }

    private void setFabContext(int CONTEXT) {
        if (CONTEXT == CONTEXT_MAP) {
            changeView(mapView);
            fab1.setImageResource(R.drawable.ic_info);

        }
        else if (CONTEXT == CONTEXT_INFO) {
            changeView(infoView);
            fab1.setImageResource(R.drawable.ic_map);
        }
    }

    private void changeView(View newView) {
        ((RelativeLayout) contentView).removeAllViews();
        ((RelativeLayout) contentView).addView(newView);
        if (newView.equals(infoView)) {
            ((TextView)findViewById(R.id.textView4)).setMovementMethod(new ScrollingMovementMethod());
        }
    }

    private void reportTruckDialog(boolean vacant) {
        String[] truckNames = {"Chick in a Box", "Firetruck Bar-B-Que",  "General Lee Hibachi",
                "Philly Connection", "Smooth-N-Groove"};

        AlertDialog.Builder reportDialog = new AlertDialog.Builder(this);
        reportDialog.setTitle("Who's here now?");
        reportDialog.setItems(truckNames, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                // implement send report based on vacant or not
            }
        });
        reportDialog.show();
    }
}
