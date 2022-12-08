package com.adi.coldcall;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.adi.coldcall.models.Call;

public abstract class Utils {
    public static final int CALL_PERMISSION_REQUEST_CODE = 112;
    public static final int CALL_LOG_PERMISSION_REQUEST_CODE = 13;

    /*
        Check for CALL_PHONE permission
     */
    public static boolean checkCallPermission(Activity activity) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE}, CALL_PERMISSION_REQUEST_CODE);
            return false;
        }

        return true;
    }

    /*
            check whether permission for call logs is grante
            if not request the permission
     */
    public static boolean checkCallLogPermission(Activity activity) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_CALL_LOG}, CALL_LOG_PERMISSION_REQUEST_CODE);
            return false;
        }

        return true;
    }

    /*
        launch an Intent to call
     */
    public static void makeACall(String phone, Activity activity) {
        Toast.makeText(activity, "Making a call", Toast.LENGTH_SHORT).show();
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:+91" + phone));
        activity.startActivity(callIntent);
    }

    /*
        gets last 10 outgoing calls and check for the entry of last call made through Cold calling app
        if found such call log, returns object of com.adi.coldcall.models.Call class
        on no such entry found return null
     */
    public static Call getLastCallMade(Context context, String lastCallNumber){
        Call call = null;
        Cursor cursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI,
                new String[]{
                        CallLog.Calls.NUMBER,
                        CallLog.Calls.DATE,
                        CallLog.Calls.DURATION,
                        CallLog.Calls._ID
                }, CallLog.Calls.TYPE + "=" + CallLog.Calls.OUTGOING_TYPE,
                null, CallLog.Calls.DATE + " DESC", null);

        if (cursor != null && cursor.getCount() > 0){
            int i = 0;
            while (cursor.moveToNext() && i<10){
                if (cursor.getString(0).equals(lastCallNumber)){
                    call = new Call(cursor.getString(3), cursor.getString(0),
                            Long.parseLong(cursor.getString(1)),
                            Long.parseLong(cursor.getString(2)));
                    break;
                }

                i++;
            }
            cursor.close();
        }

        return call;
    }

    /*
        converts long to [seconds, minutes, hours]

        return int[] of size 3 where,

        int[0] : seconds
        int[1] : minutes
        int[2] : hours
     */
    public static int[] getDurationsFromLong(long duration){
        int[] durations = new int[3];

        durations[0] = (int) duration%60;
        durations[1] = (int) (duration/60)%60;
        durations[2] = (int) (duration/(60*60));
        return durations;
    }
}
