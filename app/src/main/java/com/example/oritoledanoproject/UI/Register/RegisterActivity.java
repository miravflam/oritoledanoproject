package com.example.oritoledanoproject.UI.Register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.oritoledanoproject.Data.FirebaseHelper.FirebaseHelper;
import com.example.oritoledanoproject.R;
import com.example.oritoledanoproject.UI.Login.MainActivity;
import com.example.oritoledanoproject.UI.Login.ModuleLogin;
import com.example.oritoledanoproject.UI.Store.StoreActivity;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    ModuleRegister moduleRegister;
    EditText etUser, etPass, etPassConfirm, etEmail, etAddress, etPhone;
    Button btnRegister;
    TextView tvLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        moduleRegister = new ModuleRegister(this);
        etUser = findViewById(R.id.editTextFullName);
        etPass = findViewById(R.id.editTextPassword);
        etPassConfirm = findViewById(R.id.editTextPasswordConfirmation);
        etEmail = findViewById(R.id.editTextEmail);
        etPhone = findViewById(R.id.editTextPhoneNumber);
        etAddress = findViewById(R.id.editTextAddress);
        btnRegister = findViewById(R.id.buttonRegister);
        tvLogin = findViewById(R.id.tvLogin);

        btnRegister.setOnClickListener(this);
        tvLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // בדיקה אם הכפתור שנלחץ הוא כפתור הכניסה
        if (v == tvLogin) {
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(intent);
        }

        // בדיקה אם הכפתור שנלחץ הוא כפתור ההרשמה
        if (v == btnRegister) {
            // בדיקת תקינות כל השדות בטופס ההרשמה
            if (!moduleRegister.CheckUps(etUser, etEmail, etPass, etPassConfirm, etPhone, etAddress)) {
                return;
            }

            // בדיקה אם האימייל קיים במערכת
            moduleRegister.emailIsExist(etEmail.getText().toString(), new FirebaseHelper.UserFetched() {
                @Override
                public void onUserFetched(boolean flag) {
                    if (!flag) {
                        // הוספת המשתמש החדש לפיירבייס
                        moduleRegister.addUserToFirebase(etUser.getText().toString(), etPass.getText().toString(), etEmail.getText().toString(), etAddress.getText().toString(), etPhone.getText().toString());

                        // איפוס כל שדות הטופס
                        etPass.setText("");
                        etPassConfirm.setText("");
                        etUser.setText("");
                        etEmail.setText("");
                        etPhone.setText("");
                        etAddress.setText("");

                        // מעבר לפעילות החנות (StoreActivity)
                        Intent intent = new Intent(RegisterActivity.this, StoreActivity.class);
                        startActivity(intent);
                    } else {
                        // הצגת שגיאה אם האימייל כבר קיים במערכת
                        etEmail.setError("האיימיל קיים כבר במערכת");
                    }
                }
            });
        }
    }
}