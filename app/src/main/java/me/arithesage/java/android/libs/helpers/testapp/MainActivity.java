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
            boolean permissionGranted =
                    ApplicationHelpers.Get().RequestPermission
                    (
                            this,
                            Manifest.permission.POST_NOTIFICATIONS
                    );

            if (!permissionGranted) {
                Log.w ("Helpers", "Failed granting permission");
            }
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

        if (granted.length == permissions.length) {
            DialogHelpers.Get().ShowMessage
                    (
                            "All requested permissions were granted."
                    );
        } else {
            StringBuilder notGrantedPermissions = new StringBuilder();

            notGrantedPermissions.append ("Rejected permissions:\n");

            for (int p = 0; p < permissions.length; p ++) {
                if (granted[p] != PackageManager.PERMISSION_GRANTED) {
                    notGrantedPermissions.append ("- ");
                    notGrantedPermissions.append (permissions[p]);

                    if (p != (permissions.length - 1)) {
                        notGrantedPermissions.append("\n");
                    }
                }
            }

            DialogHelpers.Get().ShowMessage
                    (
                            notGrantedPermissions.toString()
                    );
        }
    }
}
