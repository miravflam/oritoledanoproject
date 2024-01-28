package com.example.oritoledanoproject.UI.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.oritoledanoproject.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ModuleLogin moduleLogin = new ModuleLogin(this);



    }
}