package me.arithesage.java.android.libs.helpers.testapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import me.arithesage.java.android.libs.helpers.DialogHelpers;
import me.arithesage.java.android.libs.helpers.OSHelpers;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DialogHelpers.Init (this);
        DialogHelpers.Get().ShowMessage ("Hello!");
    }
}
