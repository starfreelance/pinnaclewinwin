package com.pinnacle.winwin.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import android.view.View;

import com.pinnacle.winwin.R;
import com.pinnacle.winwin.app.ASOnlineApplication;

import java.util.LinkedHashMap;

public class PermissionUtils {

    public static final int REQUEST_STORAGE = 120;
    public static final int REQUEST_CAMERA = 121;
    public static final int REQUEST_SMS = 122;
    public static final int REQUEST_PHONE_STATE = 123;
    public static final int REQUEST_WRITE_STORAGE = 124;

    public static final String DENY_STRING = "denyString";
    //    private Context context;
    private static PermissionUtils utils;

    public interface SettingListener {
        void openSettingActivity();
    }

    public interface DialogButtonListener {
        void didPositiveButtonSelected();

        void didNegativeButtonSelected();
    }

    private SettingListener listener;
    private DialogButtonListener buttonListener;

    public DialogButtonListener getButtonListener() {
        return buttonListener;
    }

    public void setButtonListener(DialogButtonListener buttonListener) {
        this.buttonListener = buttonListener;
    }

    public SettingListener getListener() {
        return listener;
    }

    public void setListener(SettingListener listener) {
        this.listener = listener;
    }

    private PermissionUtils() {
//        context = JugadPosApplication.getInstance();
    }

    public static PermissionUtils sharedInstance() {
        if (utils == null) {
            utils = new PermissionUtils();
        }
        return utils;
    }

    public void requestPermission(Activity activity, String permission,
                                  int requestCode, String[] permissions,
                                  int[] resourceStringValues, View baseLayout,
                                  LinkedHashMap<String, String> userInfo) {
//        context = activity;
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
            showPermissionRationale(activity, resourceStringValues,
                    requestCode, permissions, baseLayout, userInfo);
        } else {
            // permissions have not been granted yet. Request them directly.
            ActivityCompat.requestPermissions(activity, permissions, requestCode);
        }
    }

    /*Show Permission rationale*/
    private void showPermissionRationale(final Activity activity,
                                         int[] resourceStringValues,
                                         final int requestCode,
                                         final String[] permissions,
                                         final View baseLayout,
                                         final LinkedHashMap<String, String> userInfo) {

        showPermissionRationaleDialog(activity, resourceStringValues[0],
                resourceStringValues[1],
                resourceStringValues[2],
                resourceStringValues[3],
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ActivityCompat
                                .requestPermissions(activity,
                                        permissions,
                                        requestCode);
                        if (buttonListener != null) {
                            buttonListener.didPositiveButtonSelected();
                        }
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Snackbar.make(baseLayout,
                                userInfo.get(PermissionUtils.DENY_STRING),
                                Snackbar.LENGTH_SHORT).show();
                        if (buttonListener != null) {
                            buttonListener.didNegativeButtonSelected();
                        }
                    }
                });
    }

    public void showPermissionRationaleDialog(Activity activity, int title, int message,
                                              int positiveBtnText,
                                              int negativeBtnText,
                                              DialogInterface.OnClickListener onClickListener
            , DialogInterface.OnClickListener onDenyClickListener) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setTitle(ASOnlineApplication.getInstance().getResources().getString(title));
        alertDialog.setMessage(ASOnlineApplication.getInstance().getResources().getString(message));
        alertDialog.setPositiveButton(ASOnlineApplication.getInstance().getResources().getString(positiveBtnText), onClickListener);
        alertDialog.setNegativeButton(ASOnlineApplication.getInstance().getResources().getString(negativeBtnText), onDenyClickListener);
        alertDialog.show();
    }

    public void showNeverAskAgainPermissionRationale(final Activity activity,
                                                     int[] resourceStringValues,
                                                     final int requestCode,
                                                     final String[] permissions,
                                                     final View baseLayout,
                                                     final LinkedHashMap<String, String> userInfo) {

        showPermissionRationaleDialog(activity, resourceStringValues[0],
                resourceStringValues[1],
                resourceStringValues[2],
                resourceStringValues[3],
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //Call Setting Activity
                        if (listener != null) {
                            listener.openSettingActivity();
                        }
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Snackbar.make(baseLayout,
                                userInfo.get(PermissionUtils.DENY_STRING), Snackbar.LENGTH_SHORT).show();
                        if (buttonListener != null) {
                            buttonListener.didNegativeButtonSelected();
                        }

                    }
                });
    }

    public void requestStoragePermission(boolean isNeverAsked,
                                         SettingListener callbackObject,
                                         final View baseLayout, Activity activity) {

        LinkedHashMap<String, String> userInfo = new LinkedHashMap<>();
        userInfo.put(PermissionUtils.DENY_STRING, "You denied storage access.");

        String[] strPermissions = new String[]
                {Manifest.permission.READ_EXTERNAL_STORAGE};
        int[] permissionStringIDs = new int[]{1, 2, 3, 4};

        if (!isNeverAsked) {
            permissionStringIDs[0] = R.string.permission_storage_title;
            permissionStringIDs[1] = R.string.permission_storage_message;
            permissionStringIDs[2] = R.string.permission_positive_btn;
            permissionStringIDs[3] = R.string.permission_negative_btn;
            requestPermission(activity,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    REQUEST_STORAGE, strPermissions, permissionStringIDs,
                    baseLayout, userInfo);
        } else {
            permissionStringIDs[0] = R.string.permission_storage_title;
            permissionStringIDs[1] = R.string.never_ask_permission_message;
            permissionStringIDs[2] = R.string.never_ask_positive_btn;
            permissionStringIDs[3] = R.string.never_ask_negative_btn;
            showNeverAskAgainPermissionRationale(activity, permissionStringIDs,
                    REQUEST_STORAGE, strPermissions, baseLayout, userInfo);
        }

        setListener(callbackObject);

    }

    public void requestWriteStoragePermission(boolean isNeverAsked,
                                         SettingListener callbackObject,
                                         final View baseLayout, Activity activity) {

        LinkedHashMap<String, String> userInfo = new LinkedHashMap<>();
        userInfo.put(PermissionUtils.DENY_STRING, "You denied storage access.");

        String[] strPermissions = new String[]
                {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        int[] permissionStringIDs = new int[]{1, 2, 3, 4};

        if (!isNeverAsked) {
            permissionStringIDs[0] = R.string.permission_storage_title;
            permissionStringIDs[1] = R.string.permission_storage_message;
            permissionStringIDs[2] = R.string.permission_positive_btn;
            permissionStringIDs[3] = R.string.permission_negative_btn;
            requestPermission(activity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    REQUEST_WRITE_STORAGE, strPermissions, permissionStringIDs,
                    baseLayout, userInfo);
        } else {
            permissionStringIDs[0] = R.string.permission_storage_title;
            permissionStringIDs[1] = R.string.never_ask_permission_message;
            permissionStringIDs[2] = R.string.never_ask_positive_btn;
            permissionStringIDs[3] = R.string.never_ask_negative_btn;
            showNeverAskAgainPermissionRationale(activity, permissionStringIDs,
                    REQUEST_WRITE_STORAGE, strPermissions, baseLayout, userInfo);
        }

        setListener(callbackObject);

    }

    public void requestCameraPermission(boolean isNeverAsked,
                                        SettingListener callbackObject,
                                        final View baseLayout, Activity activity) {

        LinkedHashMap<String, String> userInfo = new LinkedHashMap<>();
        userInfo.put(PermissionUtils.DENY_STRING, "You denied camera access.");

        String[] strPermissions = new String[]
                {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        int[] permissionStringIDs = new int[]{1, 2, 3, 4};

        if (!isNeverAsked) {
            permissionStringIDs[0] = R.string.permission_camera_title;
            permissionStringIDs[1] = R.string.permission_camera_message;
            permissionStringIDs[2] = R.string.permission_positive_btn;
            permissionStringIDs[3] = R.string.permission_negative_btn;
            requestPermission(activity,
                    Manifest.permission.CAMERA,
                    REQUEST_CAMERA, strPermissions, permissionStringIDs,
                    baseLayout, userInfo);
        } else {
            permissionStringIDs[0] = R.string.permission_camera_title;
            permissionStringIDs[1] = R.string.never_ask_permission_camera_message;
            permissionStringIDs[2] = R.string.never_ask_positive_btn;
            permissionStringIDs[3] = R.string.never_ask_negative_btn;
            showNeverAskAgainPermissionRationale(activity, permissionStringIDs,
                    REQUEST_CAMERA, strPermissions, baseLayout, userInfo);
        }

        setListener(callbackObject);

    }

    /*public void requestReadSmsPermission(boolean isNeverAsked,
                                         SettingListener callbackObject,
                                         final View baseLayout, Activity activity) {

        LinkedHashMap<String,String> userInfo = new LinkedHashMap<>();
        userInfo.put(PermissionUtils.DENY_STRING,"You denied SMS access.");

        String[] strPermissions = new String[]
                {Manifest.permission.RECEIVE_SMS};
        int[] permissionStringIDs = new int[]{1,2,3,4};

        if(!isNeverAsked){
            permissionStringIDs[0] = R.string.permission_read_sms_title;
            permissionStringIDs[1] = R.string.permission_read_sms_message;
            permissionStringIDs[2] = R.string.permission_positive_btn;
            permissionStringIDs[3] = R.string.permission_negative_btn;
            requestPermission(activity,
                    Manifest.permission.RECEIVE_SMS,
                    REQUEST_SMS,strPermissions,permissionStringIDs,
                    baseLayout,userInfo);
        } else {
            permissionStringIDs[0] = R.string.permission_read_sms_title;
            permissionStringIDs[1] = R.string.never_ask_read_sms_permission_message;
            permissionStringIDs[2] = R.string.never_ask_positive_btn;
            permissionStringIDs[3] = R.string.never_ask_negative_btn;
            showNeverAskAgainPermissionRationale(activity,permissionStringIDs,
                    REQUEST_SMS,strPermissions,baseLayout,userInfo);
        }

        setListener(callbackObject);

    }*/

    public void requestReadPhoneState(boolean isNeverAsked,
                                      SettingListener callbackObject,
                                      final View baseLayout, Activity activity) {

        LinkedHashMap<String, String> userInfo = new LinkedHashMap<>();
        userInfo.put(PermissionUtils.DENY_STRING, "You denied phone state access.");

        String[] strPermissions = new String[]
                {Manifest.permission.READ_PHONE_STATE};
        int[] permissionStringIDs = new int[]{1, 2, 3, 4};

        if (!isNeverAsked) {
            permissionStringIDs[0] = R.string.permission_read_phone_state_title;
            permissionStringIDs[1] = R.string.permission_read_phone_state_message;
            permissionStringIDs[2] = R.string.permission_positive_btn;
            permissionStringIDs[3] = R.string.permission_negative_btn;
            requestPermission(activity,
                    Manifest.permission.READ_PHONE_STATE,
                    REQUEST_PHONE_STATE, strPermissions, permissionStringIDs,
                    baseLayout, userInfo);
        } else {
            permissionStringIDs[0] = R.string.permission_read_phone_state_title;
            permissionStringIDs[1] = R.string.never_ask_read_phone_state_permission_message;
            permissionStringIDs[2] = R.string.never_ask_positive_btn;
            permissionStringIDs[3] = R.string.never_ask_negative_btn;
            showNeverAskAgainPermissionRationale(activity, permissionStringIDs,
                    REQUEST_PHONE_STATE, strPermissions, baseLayout, userInfo);
        }

        setListener(callbackObject);

    }

    /*private static final int REQUEST_IMEI = 110;
    private static final int REQUEST_LOCATION = 120;
    private static final int REQUEST_PERMISSION_SETTING = 200;*/

    /*public void requestIMEIPersmission(boolean isNeverAsked,
                                       SettingListener callbackObject,
                                       final View baseLayout, Activity activity){
        LinkedHashMap<String,String> userInfo = new LinkedHashMap<>();
        userInfo.put(PermissionUtils.DENY_STRING,"You denied phone state access.");

        String[] strPermissions = new String[]
                {Manifest.permission.READ_PHONE_STATE};
        int[] permissionStringIDs = new int[]{1,2,3,4};

        if(isNeverAsked == false){
            permissionStringIDs[0] = R.string.permission_phone_state_title;
            permissionStringIDs[1] = R.string.permission_phone_state_message;
            permissionStringIDs[2] = R.string.permission_phone_state_positive_btn;
            permissionStringIDs[3] = R.string.permission_phone_state_negative_btn;
            requestPermission(activity,
                    Manifest.permission.READ_PHONE_STATE,
                    REQUEST_IMEI,strPermissions,permissionStringIDs,
                    baseLayout,userInfo);
        }else{
            permissionStringIDs[0] = R.string.never_asked_permission_phone_state_title;
            permissionStringIDs[1] = R.string.never_asked_permission_phone_state_message;
            permissionStringIDs[2] = R.string.never_asked_permission_phone_state_positive_btn;
            permissionStringIDs[3] = R.string.never_asked_permission_phone_state_negative_btn;
            showNeverAskAgainPermissionRationale(activity,permissionStringIDs,
                    REQUEST_IMEI,strPermissions,baseLayout,userInfo);
        }

        setListener(callbackObject);
    }*/

    /*public void requestLocationPersmission(boolean isNeverAsked,
                                           SettingListener callbackObject,
                                           final View baseLayout, Activity activity){
        LinkedHashMap<String,String> userInfo = new LinkedHashMap<>();
        userInfo.put(PermissionUtils.DENY_STRING,"You denied phone location access.");

        String[] strPermissions = new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION};
        int[] permissionStringIDs = new int[]{1,2,3,4};

        if(isNeverAsked == false){
            permissionStringIDs[0] = R.string.permission_location_title;
            permissionStringIDs[1] = R.string.permission_location_message;
            permissionStringIDs[2] = R.string.permission_location_positive_btn;
            permissionStringIDs[3] = R.string.permission_location_negative_btn;
            requestPermission(activity,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    REQUEST_LOCATION,strPermissions,permissionStringIDs,
                    baseLayout,userInfo);
        }else{
            permissionStringIDs[0] = R.string.never_asked_permission_location_title;
            permissionStringIDs[1] = R.string.never_asked_permission_location_message;
            permissionStringIDs[2] = R.string.never_asked_permission_location_positive_btn;
            permissionStringIDs[3] = R.string.never_asked_permission_location_negative_btn;
            showNeverAskAgainPermissionRationale(activity,permissionStringIDs,
                    REQUEST_LOCATION,strPermissions,baseLayout,userInfo);
        }

        setListener(callbackObject);
    }

    public void requestContactPermission(boolean isNeverAsked,
                                         SettingListener callbackObject,
                                         final View baseLayout, Activity activity){
        LinkedHashMap<String,String> userInfo = new LinkedHashMap<>();
        userInfo.put(PermissionUtils.DENY_STRING,"You denied read contacts.");

        String[] strPermissions = new String[]
                {Manifest.permission.READ_CONTACTS};
        int[] permissionStringIDs = new int[]{1,2,3,4};

        if(isNeverAsked == false){
            permissionStringIDs[0] = R.string.permission_read_contact_title;
            permissionStringIDs[1] = R.string.permission_read_conatct_message;
            permissionStringIDs[2] = R.string.permission_read_contact_positive_btn;
            permissionStringIDs[3] = R.string.permission_read_contact_negative_btn;
            requestPermission(activity,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    REQUEST_LOCATION,strPermissions,permissionStringIDs,
                    baseLayout,userInfo);
        }else{
            permissionStringIDs[0] = R.string.never_asked_permission_read_contact_title;
            permissionStringIDs[1] = R.string.never_asked_permission_read_conatct_message;
            permissionStringIDs[2] = R.string.never_asked_permission_read_contact_positive_btn;
            permissionStringIDs[3] = R.string.never_asked_permission_read_contact_negative_btn;
            showNeverAskAgainPermissionRationale(activity,permissionStringIDs,
                    REQUEST_LOCATION,strPermissions,baseLayout,userInfo);
        }

        setListener(callbackObject);
    }*/


    /*public boolean isLocationServiceEnabled() {

        android.location.LocationManager lm = (android.location.LocationManager)
                ASOnlineApplication.getInstance().getSystemService(Context.LOCATION_SERVICE);
        if (lm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER) ||
                lm.isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER)) {
            return true;

        } else {
            return false;
        }
    }*/
}

