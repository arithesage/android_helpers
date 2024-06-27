package me.arithesage.java.android.libs.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

import me.arithesage.java.libs.Utils;

/**
 * Contains functions to work with the current running application
 */
@SuppressWarnings ("unused")
public class ApplicationHelpers {
    private static ApplicationHelpers instance = null;
    private Context appContext = null;


    /**
     * Returns the singleton
     */
    public static ApplicationHelpers Get () {
        return instance;
    }


    /**
     * Initializes the singleton with the application context.
     *
     * We could simply pass context every time we call a function, but
     * i personally prefer this way.
     *
     * @param appContext The application context used by all functions.
     */
    public static void Init (Context appContext) {
        if ((instance == null) && (appContext != null)) {
            instance = new ApplicationHelpers ();
            instance.appContext = appContext;
        }
    }


    /**
     * Returns if the singleton has been initialized
     */
    public static boolean Initialized () {
        return (ApplicationHelpers.instance != null);
    }


    public String AppName () {
        ApplicationInfo appInfo = appContext.getApplicationInfo();
        int appNameResId = appInfo.labelRes;

        if (appNameResId == 0) {
            return appInfo.nonLocalizedLabel.toString();

        } else {
            return appContext.getString (appNameResId);
        }
    }


    /**
     * Check if the app has the given permission
     *
     * @param permissionId Permission ID
     */
    public boolean HasPermission (String permissionId) {
        int currentPermissionStatus = ActivityCompat.checkSelfPermission
                (
                        appContext,
                        permissionId
                );

        if (currentPermissionStatus == PackageManager.PERMISSION_GRANTED) {
            return true;
        }

        return false;
    }


    /**
     * Check if the app has the given permissions.
     *
     * @param permissionsIDs A list of permission IDs.
     * @return True only if all permissions have been granted.
     */
    public boolean HasPermissions (String... permissionsIDs) {
        int totalPermissions = permissionsIDs.length;
        int granted = 0;

        for (String permissionID : permissionsIDs) {
            int currentPermissionStatus = ActivityCompat.checkSelfPermission
                    (
                            appContext,
                            permissionID
                    );

            if (currentPermissionStatus == PackageManager.PERMISSION_GRANTED) {
                granted ++;
            }
        }

        if (granted != totalPermissions) {
            return false;
        }

        return true;
    }


    /**
     * Request the given permission for the given activity
     *
     * @param activity The activity that needs the permission
     * @param permissionId The permission ID
     * @return
     */
    public boolean RequestPermission (Activity activity, String permissionId) {
        int requestId = new java.util.Random().nextInt(Integer.MAX_VALUE);
        String[] permissionsIDs = Utils.ArrayFrom (permissionId);

        ActivityCompat.requestPermissions (activity, permissionsIDs, requestId);

        return HasPermissions (permissionsIDs);
    }


    /**
     * Requests the given permissions for the given activity
     *
     * @param activity The activity that needs the permissions
     * @param permissionsIDs All the needed permissions IDs
     *
     * @return True only if all the required permissions have been granted
     */
    public boolean RequestPermissions (Activity activity, String... permissionsIDs) {
        int requestId = new java.util.Random().nextInt(Integer.MAX_VALUE);

        ActivityCompat.requestPermissions (activity, permissionsIDs, requestId);

        return HasPermissions (permissionsIDs);
    }
}






