package com.example.oritoledanoproject.UI.Store;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.oritoledanoproject.R;

public class StoreActivity extends AppCompatActivity {


    TextView tvName;

    static String[] Credentials;
    ModuleStore moduleStore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        tvName = findViewById(R.id.userNameTextView);

        moduleStore = new ModuleStore(this);

        if(moduleStore.DoesRemember() )
        Credentials = moduleStore.getCredentials();
        else if (Credentials == null) {
            Credentials = moduleStore.getCredentials();
            moduleStore.DoNotRemember();
        }

        tvName.setText(Credentials[0]);




    }
}