package me.arithesage.java.android.libs.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

import me.arithesage.java.libs.Utils;




/**
 * Contains functions to work with the current running application
 * @noinspection RedundantIfStatement
 */
@SuppressWarnings ({"JavadocBlankLines", "unused"})
public class ApplicationHelpers {
    private static ApplicationHelpers instance = null;
    private Context appContext = null;

    private List<String> deniedPermissions = null;
    private List<String> requestedPermissions = null;


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
            instance.deniedPermissions = new ArrayList<String>();
            instance.requestedPermissions = new ArrayList<String>();
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
     *
     * @param aboutPermission An explanation of why we are requesting
     *                        this permission. The function asks for an
     *                        array with two strings: One for the message
     *                        title and a second one for the message itself.
     *
     *
     */
    public void RequestPermission (Activity activity,
                                   String permissionId,
                                   String[] aboutPermission)
    {
        if (!deniedPermissions.contains (permissionId)) {
            if (requestedPermissions.contains(permissionId)) {
                if (ActivityCompat.shouldShowRequestPermissionRationale
                        (
                                activity,
                                permissionId
                        )) {
                    if (!DialogHelpers.Initialized()) {
                        DialogHelpers.Init(appContext);
                    }

                    String messageTitle = aboutPermission[0];
                    String message = aboutPermission[1];

                    DialogHelpers.Get().ShowMessage(messageTitle, message);
                }
            }

            int requestId = Utils.GenerateID();
            String[] permissionsIDs = Utils.ArrayFrom(permissionId);

            ActivityCompat.requestPermissions(activity, permissionsIDs, requestId);
        }
    }


    /**
     * Requests the given permissions for the given activity
     *
     * @param activity The activity that needs the permissions
     * @param permissionsIDs All the needed permissions IDs
     *
     * @param aboutPermissions An explanation of why we are requesting
     *                        this permission. The function asks for an
     *                        array with two strings: One for the message
     *                        title and a second one for the message itself.
     *
     * @return True only if all the required permissions have been granted
     */
    public boolean RequestPermissions (Activity activity,
                                       String[] permissionsIDs,
                                       String[] aboutPermissions) {

        if (Utils.InCollection(deniedPermissions, permissionsIDs, false)) {

        }

        int requestId = Utils.GenerateID();

        ActivityCompat.requestPermissions (activity, permissionsIDs, requestId);

        return HasPermissions (permissionsIDs);
    }


    /**
     * This permission has been requested twice and we denied both,
     * so don't bother with it anymore.
     *
     * @param permissionId
     */
    public void stopRequestingThisPermission (String permissionId) {
        if (!deniedPermissions.contains (permissionId)) {
            deniedPermissions.add (permissionId);
        }
    }
}






