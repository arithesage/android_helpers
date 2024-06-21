package me.arithesage.java.android.libs.helpers;

import android.os.AsyncTask;


public class OSHelpers {
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
    public static void RunAsync (AndroidAsyncTask task) {
        AsyncTask.execute (task);
    }
}
