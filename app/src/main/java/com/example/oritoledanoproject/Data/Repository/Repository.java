package com.example.oritoledanoproject.Data.Repository;

import android.content.Context;
import com.example.oritoledanoproject.Data.DB.MyDatabaseHelper;

public class Repository {
    Context context;
    MyDatabaseHelper myDatabaseHelper;

    public Repository(Context context)
    {
        this.context = context;
        myDatabaseHelper = new MyDatabaseHelper(this.context);
    }

    public boolean FindUser(String user) { return myDatabaseHelper.FindUser(user);}

    public boolean LoginUser(String user, String password, int EmailLogin) { return myDatabaseHelper.LoginUser(user, password, EmailLogin); }

    public void addUser(String Username, String Email, String Password) { myDatabaseHelper.addUser(Username, Email, Password);}

    public void deleteAllData() { myDatabaseHelper.deleteAllData(); }

}
