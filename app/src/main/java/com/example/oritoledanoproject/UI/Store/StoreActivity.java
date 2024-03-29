package com.example.oritoledanoproject.UI.Store;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.oritoledanoproject.R;

public class StoreActivity extends AppCompatActivity {


    TextView tvName;
    Button btnAddProduct;

    static String[] Credentials;
    ModuleStore moduleStore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        tvName = findViewById(R.id.userNameTextView);
        btnAddProduct= findViewById(R.id.btnAddProduct);

        moduleStore = new ModuleStore(this);

        if(moduleStore.DoesRemember() )
        Credentials = moduleStore.getCredentials();
        else if (Credentials == null) {
            Credentials = moduleStore.getCredentials();
            moduleStore.DoNotRemember();
        }

        tvName.setText(Credentials[0]);

        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentAdd = new Intent(StoreActivity.this, newProduct.class);
                startActivity(intentAdd);
            }
        });




    }
}