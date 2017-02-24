package wfc.auft.mobile;

import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.android.gms.maps.OnMapReadyCallback;

import java.util.Map;

public abstract class MainDelegate extends AppCompatActivity {
    public abstract void updateMap(Map<String, Map<String, String>> locations);
}
