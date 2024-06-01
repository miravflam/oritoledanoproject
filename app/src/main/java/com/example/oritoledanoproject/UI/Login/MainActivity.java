package com.example.oritoledanoproject.UI.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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

        // יצירת אובייקט של ModuleLogin לתקשורת עם Firebase
        moduleLogin = new ModuleLogin(this);

        etUser = findViewById(R.id.etUser);
        etPass = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.buttonLogin);
        tvReg = findViewById(R.id.tvRegister);
        cb = findViewById(R.id.checkbox);

        btnLogin.setOnClickListener(this);
        tvReg.setOnClickListener(this);

        // בדיקה אם קיימות נתוני התחברות זכורים ואם כן - ניסיון להתחבר
        if (moduleLogin.CredentialsExist()) {
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("מתחבר");
            progressDialog.setCancelable(false);
            progressDialog.show();
            moduleLogin.emailIsExist(moduleLogin.getSharedName(), new FirebaseHelper.UserFetched() {
                @Override
                public void onUserFetched(boolean flag) {
                    if (flag) {
                        moduleLogin.getUser(moduleLogin.getSharedName(), moduleLogin.getSharedPass(), new FirebaseHelper.UserFound() {
                            @Override
                            public void onUserFound() {
                                progressDialog.dismiss();
                                Intent intent = new Intent(MainActivity.this, StoreActivity.class);
                                startActivity(intent);
                            }
                        });
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, "המשתמש לא נמצא", Toast.LENGTH_SHORT).show();
                        moduleLogin.deleteData();
                    }
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        // בדיקה איזה רכיב נלחץ ופעולה המתאימה לכך
        if (v == tvReg) {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        }

        if (v == btnLogin) {
            // בדיקת תקינות הקלט וניסיון להתחבר
            if (etUser.getText().toString().equals("")) {
                Toast.makeText(this, "מלא איימיל", Toast.LENGTH_SHORT).show();
                return;
            }
            if (etPass.getText().toString().equals("")) {
                Toast.makeText(this, "מלא סיסמא", Toast.LENGTH_SHORT).show();
                return;
            }
            moduleLogin.getUser(etUser.getText().toString(), etPass.getText().toString(), new FirebaseHelper.UserFound() {
                @Override
                public void onUserFound() {
                    Intent intent = new Intent(MainActivity.this, StoreActivity.class);
                    // במידה והסימן "זכור אותי" מסומן, שמירת נתוני התחברות
                    moduleLogin.RememberMe(cb.isChecked(), etUser.getText().toString(), etPass.getText().toString());
                    startActivity(intent);
                }
            });
        }
    }
}
