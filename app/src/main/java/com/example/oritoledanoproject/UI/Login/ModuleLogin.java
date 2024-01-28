package com.example.oritoledanoproject.UI.Login;

import android.content.Context;

import com.example.oritoledanoproject.Data.Repository.Repository;

public class ModuleLogin {

    Repository repository;

    Context context;

    public ModuleLogin(Context context)
    {
        repository = new Repository(context);
        this.context = context;
    }

    public boolean CheckUps()
    {


        return true;
    }
}
