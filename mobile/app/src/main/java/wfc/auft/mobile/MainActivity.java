package wfc.auft.mobile;

import android.animation.LayoutTransition;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.UUID;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {

    // TODO create list view

    // Context definitions
    final int CONTEXT_MAP   = 0;
    final int CONTEXT_INFO  = 1;
    final int CONTEXT_NEW   = 2;
    final int CONTEXT_LIST  = 3;

    private int currentContext;
    private View contentView;
    private View mapView, infoView;
    private Boolean isFabOpen = false;

    private FloatingActionButton fabMain, fab1, fab2, fab3;

    private Animation fabMainRotateRight, fabMainRotateLeft;
    private Animation fab1SlideUp, fab1SlideDown;
    private Animation fab2SlideUp, fab2SlideDown;
    private Animation fab3SlideUp, fab3SlideDown;
    private Animation fabChangeShrink, fabChangeExpand;

    private String deviceID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        deviceID = sharedPref.getString("deviceID", "NO_ID");

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

        fabChangeShrink = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_shrink);
        fabChangeExpand = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_expand);

        LayoutTransition layoutTransition = new LayoutTransition();
        ((ViewGroup) contentView).setLayoutTransition(layoutTransition);

        // Create map
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(32.603, -85.486)));
        map.setMinZoomPreference(17);
        map.setLatLngBoundsForCameraTarget(new LatLngBounds(
                new LatLng(32.598, -85.490), new LatLng(32.609, -85.481)
        ));
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.fab_main) {
            animateFAB();
            return;
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
                if (currentContext == CONTEXT_NEW) {
                    currentContext = CONTEXT_MAP;
                }
                else {
                    currentContext = CONTEXT_NEW;
                }
            }

            //v.startAnimation(fabChangeShrink);
            setFabContext(currentContext);
            //v.startAnimation(fabChangeExpand);
        }
    }

    // animate open or close expanding fabMain
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
            fab2.setImageResource(R.drawable.ic_new_report);
            fab3.setImageResource(R.drawable.ic_reports_list);

        }
        else if (CONTEXT == CONTEXT_INFO) {
            changeView(infoView);
            fab1.setImageResource(R.drawable.ic_map);
            fab2.setImageResource(R.drawable.ic_new_report);
            fab3.setImageResource(R.drawable.ic_reports_list);
        }
        else if (CONTEXT == CONTEXT_NEW) {
            fab1.setImageResource(R.drawable.ic_info);
            fab2.setImageResource(R.drawable.ic_map);
            fab3.setImageResource(R.drawable.ic_reports_list);
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
