package wfc.auft.mobile.listeners;

import android.content.DialogInterface;

import wfc.auft.mobile.MainDelegate;
import wfc.auft.mobile.tasks.SendReportAsyncTask;

public class ReportDialogClickListener implements DialogInterface.OnClickListener {

    private MainDelegate activity;
    private String deviceId;
    private String locationId;

    public ReportDialogClickListener(MainDelegate activity, String deviceID, String locationID) {
        this.activity = activity;
        this.deviceId = deviceID;
        this.locationId = locationID;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        String truckId;
        switch (which) {
            case 0: // Chick in a Box
                truckId = "chkbox";
                break;
            case 1: // Firetruck Bar-B-Que
                truckId = "ftrbbq";
                break;
            case 2: // General Lee Hibachi
                truckId = "genlee";
                break;
            case 3: // Philly Connection
                truckId = "phlcon";
                break;
            case 4: // Smooth-N-Groove
                truckId = "smtgrv";
                break;
            case 5: // University Donut Company
                truckId = "udcomp";
                break;
            default:
                return;
        }
        dialog.dismiss();
        new SendReportAsyncTask(activity, deviceId, locationId, truckId).execute();
    }
}
