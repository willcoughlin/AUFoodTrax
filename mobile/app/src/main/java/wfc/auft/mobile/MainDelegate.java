package wfc.auft.mobile;

import android.support.v7.app.AppCompatActivity;

import java.util.Map;

public abstract class MainDelegate extends AppCompatActivity {
    public abstract void updateMap(Map<String, Map<String, String>> locations);
}
