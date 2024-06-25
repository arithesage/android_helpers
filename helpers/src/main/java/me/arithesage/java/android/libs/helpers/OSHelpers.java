package me.arithesage.java.android.libs.helpers;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.AsyncTask;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


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
     * @param appContext The application context used by all functionss.
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
     * Runs a task async.
     *
     * Deprecation is ignored because we want to use AsyncTask for
     * compatibility with lower Android versions, like 4.4 (KitKat).
     *
     * @param task Task to perform
     */
    @SuppressWarnings("deprecation")
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
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder (appContext);

        notificationBuilder.setContentTitle (context);
        notificationBuilder.setContentText (message);
        notificationBuilder.setAutoCancel (true);

        Notification notification = notificationBuilder.build();

        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from (appContext);

        int notificationId = new java.util.Random().nextInt();
        notificationManager.notify (notificationId, notification);
    }
}








