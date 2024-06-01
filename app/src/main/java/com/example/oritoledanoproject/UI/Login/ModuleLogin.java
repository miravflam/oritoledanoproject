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

    public ModuleLogin(Context context) {
        repository = new Repository(context);
        sharedPreferences = context.getSharedPreferences("Main", Context.MODE_PRIVATE);
        this.context = context;
        editor = sharedPreferences.edit();
    }

    // פונקציה לקבלת משתמש ממסד הנתונים
    public void getUser(String email, String password, FirebaseHelper.UserFound callback) {
        repository.getUser(email, password, callback);
    }

    // פונקציה לשמירת משתמש וסיסמה במכשיר
    public void RememberMe(boolean flag, String email1, String pass) {
        editor.putBoolean("Remember", flag);
        if (flag) {
            editor.putString("useremail", email1);
            editor.putString("userpass", pass);
        }
        editor.apply();
    }

    // פונקציה לבדיקת קיומם של נתוני התחברות זכורים במכשיר
    public boolean CredentialsExist() {
        return sharedPreferences.contains("useremail") && !sharedPreferences.getString("useremail", "").isEmpty() && sharedPreferences.contains("userpass") && !sharedPreferences.getString("userpass", "").isEmpty();
    }

    // פונקציה לבדיקת קיומו של כתובת האימייל במסד הנתונים של Firebase
    public void emailIsExist(String email, FirebaseHelper.UserFetched callback) {
        repository.emailIsExist(email, callback);
    }

    // פונקציה לקבלת כתובת האימייל השמורה
    public String getSharedName() {
        return sharedPreferences.getString("useremail", "");
    }

    // פונקציה לקבלת הסיסמה השמורה
    public String getSharedPass() {
        return sharedPreferences.getString("userpass", "");
    }

    // פונקציה למחיקת נתוני התחברות מהמכשיר
    public void deleteData() {
        editor.remove("useremail");
        editor.remove("userpass");
        editor.remove("Remember");
        editor.apply();
    }
}
