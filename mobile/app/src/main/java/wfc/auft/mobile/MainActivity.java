package wfc.auft.mobile;

import android.animation.LayoutTransition;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import wfc.auft.mobile.tasks.MapPopulateAsyncTask;

public class MainActivity extends MainDelegate implements OnMapReadyCallback, View.OnClickListener,
        GoogleMap.OnMarkerClickListener {

    /*
     * TODO document code
     * TODO Trucks class w requests
     * TODO change title in marker info windows from truck id to truck name
     * TODO add list view
     * TODO add report form
     * TODO clean commented code
     */

    // Context definitions
    final int CONTEXT_MAP   = 0;
    final int CONTEXT_INFO  = 1;
    final int CONTEXT_LIST  = 2;

    private int currentContext;
    private View contentView;

    private View mapView;
    private GoogleMap map;
    private MapPopulateAsyncTask mapPopulateAsyncTask;
    private Map<String, Marker> mapMarkers;
    private Map<String, Map<String, String>> locations;

    private View infoView;

    private Boolean isFabOpen = false;
    private FloatingActionButton fabMain, fab1, fab2, fab3;

    private Animation fabMainRotateRight, fabMainRotateLeft;
    private Animation fab1SlideUp, fab1SlideDown;
    private Animation fab2SlideUp, fab2SlideDown;
    private Animation fab3SlideUp, fab3SlideDown;
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
        fab3 = (FloatingActionButton)findViewById(R.id.fab_3);

        fabMain.setOnClickListener(this);
        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);
        fab3.setOnClickListener(this);

        fab3.setImageResource(R.drawable.ic_refresh); // fab3 (top) is always refresh
        setFabContext(currentContext);

        // Define animations
        fabMainRotateRight = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_main_rotate_right);
        fabMainRotateLeft = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_main_rotate_left);

        fab1SlideUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_1_slide_up);
        fab1SlideDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_1_slide_down);

        fab2SlideUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_2_slide_up);
        fab2SlideDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_2_slide_down);

        fab3SlideUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_3_slide_up);
        fab3SlideDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_3_slide_down);

        //fabChangeShrink = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_shrink);
        //fabChangeExpand = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_expand);

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
            else if (id == R.id.fab_2) {
                if (currentContext == CONTEXT_LIST) {
                    currentContext = CONTEXT_MAP;
                }
                else {
                    currentContext = CONTEXT_LIST;
                }
            }
            else if(id == R.id.fab_3) {
                fab3Rotate.setRepeatCount(-1);
                fab3.startAnimation(fab3Rotate);
                new MapPopulateAsyncTask(this).execute();
            }

            //v.startAnimation(fabChangeShrink);
            setFabContext(currentContext);
            //v.startAnimation(fabChangeExpand);
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

    public void updateMap(Map<String, Map<String, String>> locations) {

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
        //this.locations = locations; // will this be needed?
        //Map<String, LatLng> pinLocations = new HashMap<>();
       // Map<String, MarkerOptions> mapMarkerOptions = new HashMap<>();

        for (String id : locations.keySet()) {
            Double lat = Double.parseDouble(locations.get(id).get("lat"));
            Double lng = Double.parseDouble(locations.get(id).get("lng"));

            //pinLocations.put(id, new LatLng(lat, lng));

            LatLng position = new LatLng(lat, lng);
            String title = locations.get(id).get("truck");
            String snippet = locations.get(id).get("desc");
            MarkerOptions options =
                    new MarkerOptions().position(position).title(title).snippet(snippet);
            //mapMarkerOptions.put(id, options);

            if (title.length() > 1)
                mapMarkers.put(id, map.addMarker(options));
        }



//        Marker rsevltW1 = map.addMarker(new MarkerOptions()
//                .position(pinLocations.get("rsevlt-w1"))
//                .title("Test")
//                .snippet(locations.get("rsevlt-w1").get("desc")));
//
//        Marker rsevltW2 = map.addMarker(new MarkerOptions()
//                .position(pinLocations.get("rsevlt-w2"))
//                .title("Test")
//                .snippet(locations.get("rsevlt-w2").get("desc")));
//
//        Marker transcSw = map.addMarker(new MarkerOptions()
//                .position(pinLocations.get("transc-sw"))
//                .title("Test")
//                .snippet(locations.get("transc-sw").get("desc")));
//
//        Marker hlytchSw = map.addMarker(new MarkerOptions()
//                .position(pinLocations.get("hlytch-sw"))
//                .title("Test")
//                .snippet(locations.get("hlytch-sw").get("desc")));
//
//        Marker hlytchNw = map.addMarker(new MarkerOptions()
//                .position(pinLocations.get("hlytch-nw"))
//                .title("Test")
//                .snippet(locations.get("hlytch-nw").get("desc")));
//
//        Marker hlytchNe = map.addMarker(new MarkerOptions()
//                .position(pinLocations.get("hlytch-ne"))
//                .title("Test")
//                .snippet(locations.get("hlytch-ne").get("desc")));
    }

    private void animateFAB(){
        if(isFabOpen){ // do animations in reverse order when closing
            fab2.bringToFront();
            fab3.startAnimation(fab3SlideDown);

            fab1.bringToFront();
            fab2.startAnimation(fab2SlideDown);

            fabMain.bringToFront();
            fab1.startAnimation(fab1SlideDown);

            fabMain.startAnimation(fabMainRotateLeft);

            fab1.setClickable(false);
            fab2.setClickable(false);
            fab3.setClickable(false);

            isFabOpen = false;
        }
        else {
            fabMain.startAnimation(fabMainRotateRight);

            fab1.startAnimation(fab1SlideUp);
            fab1.bringToFront();

            fab2.startAnimation(fab2SlideUp);
            fab2.bringToFront();

            fab3.startAnimation(fab3SlideUp);
            fab3.bringToFront();

            fab1.setClickable(true);
            fab2.setClickable(true);
            fab3.setClickable(true);

            isFabOpen = true;
        }
    }

    private void setFabContext(int CONTEXT) {
        if (CONTEXT == CONTEXT_MAP) {
            changeView(mapView);
            fab1.setImageResource(R.drawable.ic_info);
            fab2.setImageResource(R.drawable.ic_reports_list);
            //fab3.setImageResource(R.drawable.ic_refresh);

        }
        else if (CONTEXT == CONTEXT_INFO) {
            changeView(infoView);
            fab1.setImageResource(R.drawable.ic_map);
            fab2.setImageResource(R.drawable.ic_reports_list);
            //fab3.setImageResource(R.drawable.ic_refresh);
        }
        else if (CONTEXT == CONTEXT_LIST) {
            fab1.setImageResource(R.drawable.ic_info);
            fab2.setImageResource(R.drawable.ic_map);
            //fab3.setImageResource(R.drawable.ic_refresh);
        }
    }

    private void changeView(View newView) {
        ((RelativeLayout) contentView).removeAllViews();
        ((RelativeLayout) contentView).addView(newView);
        if (newView.equals(infoView)) {
            ((TextView)findViewById(R.id.textView4)).setMovementMethod(new ScrollingMovementMethod());
        }
    }

}
