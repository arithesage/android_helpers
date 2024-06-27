package me.arithesage.java.android.libs.helpers;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

/**
 * Singleton class for creating dialogs.
 *
 * Unused warning suppressed.
 *
 * We don't want Android Studio bothering us because
 * we aren't using something.
 */
@SuppressWarnings ("unused")
public class DialogHelpers {
    private static DialogHelpers instance = null;
    private Context appContext = null;


    /**
     * Returns the singleton
     */
    public static DialogHelpers Get() {
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
            instance = new DialogHelpers ();
            instance.appContext = appContext;
        }
    }


    /**
     * Returns if the singleton has been initialized
     */
    public static boolean Initialized () {
        return (DialogHelpers.instance != null);
    }


    /**
     * Shortcut for showing a message without a title.
     * The dialog will have a close button with the default "Close" caption.
     *
     * @param message Message text
     */
    public void ShowMessage (String message) {

        ShowMessage (null, message, "Close");
    }


    /**
     * Shortcut for showing a message with a title
     * The dialog will have a close button with the default "Close" caption.
     *
     * @param title Message title
     * @param message Message text
     */
    public void ShowMessage (String title, String message) {
        ShowMessage (title, message, "Close");
    }


    /**
     * Shows a simple message.
     *
     * @param title Message title
     * @param message Message text
     * @param closeButtonCaption Close button's label
     */
    public void ShowMessage (String title,
                             String message,
                             String closeButtonCaption) {

        if (appContext == null) {
            return;
        }

        AlertDialog.Builder dialog = new AlertDialog.Builder(appContext);

        if (title != null) {
            dialog.setTitle (title);
        }

        dialog.setMessage (message);

        dialog.setPositiveButton(closeButtonCaption,
                                 new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        dialog.show ();
    }
}
