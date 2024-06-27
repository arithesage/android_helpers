package me.arithesage.java.android.libs.helpers.testapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import me.arithesage.java.android.libs.helpers.ApplicationHelpers;
import me.arithesage.java.android.libs.helpers.DialogHelpers;
import me.arithesage.java.android.libs.helpers.OSHelpers;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ApplicationHelpers.Init (this);
        DialogHelpers.Init (this);

        boolean havePostPermission =
                ApplicationHelpers.Get().HasPermission
                        (
                            Manifest.permission.POST_NOTIFICATIONS
                        );

        if (!havePostPermission) {
            ApplicationHelpers.Get().RequestPermission
            (
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
            );
        }

        Log.d ("DUMMY", "Done.");
        /*
        OSHelpers.Init (this);

        if (OSHelpers.Initialized()) {
            OSHelpers.Get().ShowNotification ("TestApp", "Hello!");
        }
        */
    }


    @Override
    public void onRequestPermissionsResult
            (
                int requestCode,
                @NotNull String[] permissions,
                @NotNull int[] granted
            )
    {
        super.onRequestPermissionsResult (requestCode, permissions, granted);

        int notGrantedPermissions = 0;
        StringBuilder rejectedPermissionsReport = new StringBuilder();

        rejectedPermissionsReport.append ("Rejected permissions:\n");

        for (int p = 0; p < permissions.length; p ++) {
            if (granted[p] != PackageManager.PERMISSION_GRANTED) {
                notGrantedPermissions ++;

                rejectedPermissionsReport.append ("- ");
                rejectedPermissionsReport.append (permissions[p]);

                if (p != (permissions.length - 1)) {
                    rejectedPermissionsReport.append("\n");
                }
            }
        }

        if (notGrantedPermissions == 0) {
            DialogHelpers.Get().ShowMessage
            (
                "Permission request",
                "All requested permissions were granted."
            );

        } else {
            DialogHelpers.Get().ShowMessage
            (
                rejectedPermissionsReport.toString()
            );
        }
    }
}
