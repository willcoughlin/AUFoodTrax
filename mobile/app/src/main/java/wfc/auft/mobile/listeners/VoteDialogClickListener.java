package wfc.auft.mobile.listeners;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import wfc.auft.mobile.MainDelegate;
import wfc.auft.mobile.tasks.VoteAsyncTask;

public class VoteDialogClickListener implements DialogInterface.OnClickListener {

    private MainDelegate activity;
    private String reportId;

    public VoteDialogClickListener(MainDelegate activity, String reportId) {
        this.activity = activity;
        this.reportId = reportId;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
        switch (which) {
            case AlertDialog.BUTTON_NEGATIVE:
                new VoteAsyncTask(activity, reportId, false).execute();
                break;
            case AlertDialog.BUTTON_POSITIVE:
                new VoteAsyncTask(activity, reportId, true).execute();
                break;
            default:
        }
    }
}
