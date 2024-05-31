package com.example.oritoledanoproject.UI.Register;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.EditText;

import com.example.oritoledanoproject.Data.FirebaseHelper.FirebaseHelper;
import com.example.oritoledanoproject.Data.Repository.Repository;

public class ModuleRegister {

    Context context;
    Repository repository;

    public ModuleRegister(Context context)
    {
        this.context = context;
        repository = new Repository(context);
    }

    public void addUserToFirebase(String name, String password, String email, String address, String phone) { repository.addUserToFirebase(name, password, email, address, phone);}

    public void emailIsExist(String email , FirebaseHelper.UserFetched callback) { repository.emailIsExist(email, callback); }

    public Boolean CheckUps(EditText etUser, EditText etEmail, EditText etPassword, EditText etPasswordConfirmation, EditText etPhone, EditText etAddress)
    {
        // username validity checkups
        if(etUser.getText().toString().isEmpty())
        {
            etUser.setError("מלא שם משתמש");
            return false;
        }
        if(etUser.getText().toString().length() < 3)
        {
            etUser.setError("השם חייב להיות מעל שלוש תווים");
            return false;
        }

        // email validity checkups
        if(etEmail.getText().toString().indexOf("@") <= 1)
        {
            etEmail.setError("אימייל חייב להכיל שטרודל");
            return false;
        }
        if(etEmail.getText().toString().indexOf("@") != etEmail.getText().toString().lastIndexOf("@"))
        {
            etEmail.setError("איימיל לא תקין");
            return false;
        }
        if(etEmail.getText().toString().indexOf(".") - etEmail.getText().toString().indexOf("@") <= 3)
        {
            etEmail.setError("איימיל לא תקין");
            return false;
        }
        if(etEmail.getText().toString().indexOf(".") != etEmail.getText().toString().lastIndexOf("."))
        {
            etEmail.setError("איימיל לא תקין");
            return false;
        }
        if(!(etEmail.getText().toString().contains(".com")) && !(etEmail.getText().toString().contains(".co.")))
        {
            etEmail.setError("איימיל לא תקין");
            return false;
        }



        //password validity checkups
        if(etPassword.getText().toString().equals(""))
        {
            etPassword.setError("מלא סיסמה");
            return false;
        }
        if(etPassword.getText().toString().length() < 3)
        {
            etPassword.setError("סיסמא לא חזקה מספיק ");
            return false;
        }
        if(!(etPassword.getText().toString().equals(etPasswordConfirmation.getText().toString())))
        {
            etPassword.setError("אישור הסיסמא לא תואם לסיסמא");
            return false;
        }

        //phone validity checkups
        if(etPhone.getText().toString().isEmpty())
        {
            etPhone.setError("מלא טלפון");
            return false;
        }


        //address validity checkups
        if(etAddress.getText().toString().isEmpty())
        {
            etAddress.setError("מלא כתובת");
            return false;
        }

        return true;
    }
}
