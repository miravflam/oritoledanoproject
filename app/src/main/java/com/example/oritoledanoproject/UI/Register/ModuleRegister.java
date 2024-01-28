package com.example.oritoledanoproject.UI.Register;

import android.content.Context;
import android.widget.EditText;

import com.example.oritoledanoproject.Data.Repository.Repository;

public class ModuleRegister {

    Context context;
    Repository repository;

    public ModuleRegister(Context context)
    {
        this.context = context;
        repository = new Repository(context);
    }

    public Boolean CheckUps(EditText etUser, EditText etEmail, EditText etPassword, EditText etPasswordConfirmation, EditText etPhone, EditText etAddress)
    {
        // username validity checkups
        if(etUser.getText().toString().isEmpty())
        {
            etUser.setError("Fill Username");
            return false;
        }
        if(etUser.getText().toString().length() < 3)
        {
            etUser.setError("Username must be over 3 characters");
            return false;
        }

        // email validity checkups
        if(etEmail.getText().toString().indexOf("@") <= 1)
        {
            etEmail.setError("invalid email (x@)");
            return false;
        }
        if(etEmail.getText().toString().indexOf("@") != etEmail.getText().toString().lastIndexOf("@"))
        {
            etEmail.setError("invalid email (@@)");
            return false;
        }
        if(etEmail.getText().toString().indexOf(".") - etEmail.getText().toString().indexOf("@") <= 3)
        {
            etEmail.setError("invalid email (.@)");
            return false;
        }
        if(etEmail.getText().toString().indexOf(".") != etEmail.getText().toString().lastIndexOf("."))
        {
            etEmail.setError("invalid email (..)");
            return false;
        }
        if(!(etEmail.getText().toString().contains(".com")) && !(etEmail.getText().toString().contains(".co.")))
        {
            etEmail.setError("invalid email (.com/.co)");
            return false;
        }



        //password validity checkups
        if(etPassword.getText().toString().equals(""))
        {
            etPassword.setError("Fill Password");
            return false;
        }
        if(etPassword.getText().toString().length() < 3)
        {
            etPassword.setError("Password isn't strong enough");
            return false;
        }
        if(!(etPassword.getText().toString().equals(etPasswordConfirmation.getText().toString())))
        {
            etPassword.setError("Password Confirmation does not match");
            return false;
        }
        if(!(repository.FindUser(etUser.getText().toString())))
        {
            etUser.setError("Username already exists");
            return false;
        }

        //phone validity checkups
        if(etPhone.getText().toString().isEmpty())
        {
            etPhone.setError("Fill Phone");
            return false;
        }


        //address validity checkups
        if(etAddress.getText().toString().isEmpty())
        {
            etAddress.setError("Fill Address");
            return false;
        }

        repository.addUser(etUser.getText().toString(),etPassword.getText().toString(),etEmail.getText().toString(), etPhone.getText().toString(), etAddress.getText().toString());
        etPassword.setText("");
        etPasswordConfirmation.setText("");
        etUser.setText("");
        etEmail.setText("");
        etPhone.setText("");
        etAddress.setText("");


        return true;
    }
}
