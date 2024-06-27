package me.arithesage.java.android.libs.helpers;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;

import androidx.core.app.NotificationChannelCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import me.arithesage.java.libs.Utils;



@SuppressWarnings ("unused")
public class OSHelpers {
    private static OSHelpers instance = null;
    private Context appContext = null;


    /**
     * Returns the singleton
     */
    public static OSHelpers Get() {
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
            instance = new OSHelpers ();
            instance.appContext = appContext;
        }
    }


    /**
     * Returns if the singleton has been initialized
     */
    public static boolean Initialized () {
        return (OSHelpers.instance != null);
    }


    /**
     * Class made to provide a way to easily running debuggable async
     * tasks in Android.
     */
    public static class AndroidAsyncTask implements Runnable {
        private Runnable _runnable = null;

        public AndroidAsyncTask (Runnable func) {
            if (func != null) {
                _runnable = func;
            }
        }

        @Override
        public void run() {
            if (android.os.Debug.isDebuggerConnected()) {
                android.os.Debug.waitForDebugger();
            }

            if (_runnable != null) {
                _runnable.run ();
            }
        }
    }


    /**
     * Creates a notification channel
     *
     * The channelId is a random int between 0 and Integer.MAX_VALUE.
     *
     * The channelName is the appName and a random int as ID
     * in the form "appName-ID".
     *
     * The channel has default importance
     *
     * @return the created channel
     */
    public NotificationChannel CreateNotificationChannel () {
        String appName = ApplicationHelpers.Get().AppName();
        String channelId = String.valueOf (Utils.GenerateID());

        String channelName = Utils.StringFrom (
                Utils.ArrayFrom (
                        appName,
                        channelId
                )
        );

        int channelImportance = NotificationManagerCompat.IMPORTANCE_DEFAULT;

        return CreateNotificationChannel (
                        channelName,
                        channelId,
                        channelImportance
                );
    }


    /**
     * Creates a notification channel
     *
     * The channelId is a random int between 0 and Integer.MAX_VALUE.
     *
     * The channelName is the appName and a random int as ID
     * in the form "appName-ID".
     *
     * @param importance The desired importance.
     *
     * @return the created channel
     */
    public NotificationChannel CreateNotificationChannel (int importance) {
        String appName = ApplicationHelpers.Get().AppName();
        String channelId = String.valueOf (Utils.GenerateID());

        String channelName = Utils.StringFrom (
                Utils.ArrayFrom
                (
                    appName,
                    channelId
                )
        );

        return CreateNotificationChannel (channelName, channelId, importance);
    }


    /**
     * Creates a notification channel.
     *
     * @param channelName
     * @param importance
     *
     * @return
     */
    public NotificationChannel CreateNotificationChannel (String channelName,
                                                          int importance)
    {
        String channelId = String.valueOf (Utils.GenerateID());
        return CreateNotificationChannel (channelName, channelId, importance);
    }


    /**
     * Creates a new notification channel
     *
     * @param channelName
     * @param id
     * @param importance
     *
     * @return
     */
    @SuppressLint("NewApi")
    public NotificationChannel CreateNotificationChannel (String channelName,
                                                          String id,
                                                          int importance) {

        return new NotificationChannel (id, channelName, importance);
    }


    /**
     * Returns if the current Android version is below Android 5.
     * You really like retro things...
     */
    public boolean OnFossilizedAndroid () {
        return (OSVersion() < Build.VERSION_CODES.LOLLIPOP);
    }


    /**
     * Returns if the current Android version is below Android 10
     */
    public boolean OnOlderAndroid () {
        return (OSVersion() < Build.VERSION_CODES.Q);
    }


    public boolean OnModernAndroid () { return (OSVersion() >= Build.VERSION_CODES.Q); }


    /**
     * Return if the current Android version is below Android 7
     */
    public boolean OnSupraOldAndroid () {
        return (OSVersion() < Build.VERSION_CODES.N);
    }


    /**
     * Returns if the current Android version is below Android 8
     */
    public boolean OnVeryOldAndroid () {
        return (OSVersion() < Build.VERSION_CODES.O);
    }


    public int OSVersion () {
        return Build.VERSION.SDK_INT;
    }


    public PackageManager PackageManager () {
        return appContext.getPackageManager();
    }


    /**
     * You cannot show notifications in Android >= 8.0 without
     * creating a notification channel.
     */
    public boolean RequiresNotificationChannels () {
        return (OSVersion() >= Build.VERSION_CODES.O);
    }


    /**
     * Runs a task async.
     *
     * Deprecation is ignored because we want to use AsyncTask for
     * compatibility with lower Android versions, like 4.4 (KitKat).
     *
     * @param task Task to perform
     */
    @SuppressWarnings ("deprecation")
    public void RunAsync (AndroidAsyncTask task) {
        AsyncTask.execute (task);
    }


    /**
     * Shows a notification message
     *
     * @param context NOTE: Here "context" is a way to refer to the
     *                notification title, not the application context.
     * @param message
     */
    public void ShowNotification (String context, String message) {
        if (!ApplicationHelpers.Initialized()) {
            ApplicationHelpers.Init (appContext);
        }

        if (!ApplicationHelpers.Get().HasPermission
                (
                    Manifest.permission.POST_NOTIFICATIONS
                )
        ) {
            return;
        }

        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from (appContext);

        @SuppressWarnings ("deprecation")
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder (appContext);

        notificationBuilder.setContentTitle (context);
        notificationBuilder.setContentText (message);
        notificationBuilder.setAutoCancel (true);

        int notificationId = Utils.GenerateID ();

        if (RequiresNotificationChannels()) {
            String appName = ApplicationHelpers.Get().AppName();
            String channelId = String.valueOf (Utils.GenerateID());
            int channelImportance = NotificationManagerCompat.IMPORTANCE_DEFAULT;
            String channelName = Utils.StringFrom (
                    Utils.ArrayFrom (
                            appName,
                            channelId
                    )
            );

            NotificationChannelCompat.Builder notificationChannel =
                    new NotificationChannelCompat.Builder(channelId, channelImportance);

            notificationChannel.setName(channelName);

            notificationBuilder.setChannelId (channelId);
        }

        Notification notification = notificationBuilder.build();
        notificationManager.notify (notificationId, notification);
    }
}








