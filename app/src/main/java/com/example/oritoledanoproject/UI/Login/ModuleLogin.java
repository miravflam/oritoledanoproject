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


    public void RememberMe(boolean flag,String email1, String pass){
        editor.putBoolean("Remember", flag);
        if (flag){
            editor.putString("useremail",email1);
            editor.putString("userpass", pass);
        }
        editor.apply();
    }


    public boolean CredentialsExist()
    {
        return sharedPreferences.contains("useremail") && !sharedPreferences.getString("useremail", "").isEmpty()&&sharedPreferences.contains("userpass") && !sharedPreferences.getString("userpass", "").isEmpty();
    }
    public void emailIsExist(String email , FirebaseHelper.UserFetched callback){
        repository.emailIsExist(email,callback);
    }

    public String getSharedName() { return sharedPreferences.getString("useremail", "");}
    public String getSharedPass() { return sharedPreferences.getString("userpass", "");}

    public void deleteData(){
            editor.remove("useremail");
            editor.remove("userpass");
            editor.remove("Remember");
        editor.apply();}


}
