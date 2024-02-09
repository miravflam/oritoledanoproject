package com.example.oritoledanoproject.UI.Login;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.widget.EditText;

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

    public void RememberMe(boolean flag){
        editor.putBoolean("Remember", flag);
        editor.apply();
    }

    public String getUserByName(String user){ return repository.getUserIDByName(user);}


    public void SaveUser(EditText etUser)
    {
        editor.putString("username", etUser.getText().toString());
        editor.putString("email", getUserByName(repository.getUserIDByName(etUser.getText().toString())));
        editor.apply();
    }
    public boolean CredentialsExist()
    {
        return sharedPreferences.contains("username");
    }

    public String getSharedName() { return sharedPreferences.getString("username", "");}

    public boolean isExist(String user) { return !repository.FindUser(user);}
    public int isExist(EditText etUser, EditText etPass)
    {
        if(etUser.getText().toString().contains("@"))
        {
            if (!repository.LoginUser(etUser.getText().toString(), etPass.getText().toString(), 2)) {
                return 2;
            } else
                return 0;

        }
        else {
            if (!repository.LoginUser(etUser.getText().toString(), etPass.getText().toString(), 1)) {
                return 1;
            } else
                return 0;
        }
    }
}
