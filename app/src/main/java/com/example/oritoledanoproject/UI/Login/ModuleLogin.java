package com.example.oritoledanoproject.UI.Login;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.widget.EditText;

import com.example.oritoledanoproject.Data.CurrentUser.CurrentUser;
import com.example.oritoledanoproject.Data.FirebaseHelper.FirebaseHelper;
import com.example.oritoledanoproject.Data.Repository.Repository;

public class ModuleLogin {

    Repository repository;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;

    public ModuleLogin(Context context)
    {
        repository = new Repository(context);
        sharedPreferences = context.getSharedPreferences("Main", Context.MODE_PRIVATE);
        this.context = context;
        editor = sharedPreferences.edit();
    }

    public void getUser(String email, String password,FirebaseHelper.UserFound callback) { repository.getUser(email,password,callback);}


    public void RememberMe(boolean flag){
        editor.putBoolean("Remember", flag);
        editor.apply();
    }


    public boolean CredentialsExist()
    {
        return sharedPreferences.contains("username");
    }

    public String getSharedName() { return sharedPreferences.getString("username", "");}


}
