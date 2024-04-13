package com.example.oritoledanoproject.UI.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.service.autofill.OnClickAction;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.oritoledanoproject.Data.CurrentUser.CurrentUser;
import com.example.oritoledanoproject.Data.FirebaseHelper.FirebaseHelper;
import com.example.oritoledanoproject.R;
import com.example.oritoledanoproject.UI.Register.RegisterActivity;
import com.example.oritoledanoproject.UI.Store.StoreActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etUser, etPass;
    CheckBox cb;
    Button btnLogin;
    TextView tvReg;
    ModuleLogin moduleLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        moduleLogin = new ModuleLogin(this);
        etUser = findViewById(R.id.etUser);
        etPass = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.buttonLogin);
        tvReg = findViewById(R.id.tvRegister);
        btnLogin.setOnClickListener(this);
        tvReg.setOnClickListener(this);
        cb = findViewById(R.id.checkbox);



    }

    @Override
    public void onClick(View v) {

        if(v == tvReg)
        {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        }

        if(v == btnLogin) {

                        moduleLogin.getUser(etUser.getText().toString(), etPass.getText().toString(),new FirebaseHelper.UserFound() {
                            @Override
                            public void onUserFound() {
                                Intent intent = new Intent(MainActivity.this, StoreActivity.class);
                                moduleLogin.RememberMe(cb.isChecked());
                                startActivity(intent);
                            }
                        });





        }
    }

}