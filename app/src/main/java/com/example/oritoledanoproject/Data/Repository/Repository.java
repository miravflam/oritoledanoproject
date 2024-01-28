package com.example.oritoledanoproject.Data.Repository;

import android.content.Context;
import android.database.Cursor;

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

    public void addUser(String Username, String Email, String Password, String Phone, String Address) { myDatabaseHelper.addUser(Username, Email, Password, Phone, Address);}

    public void deleteAllData() { myDatabaseHelper.deleteAllData(); }

}
