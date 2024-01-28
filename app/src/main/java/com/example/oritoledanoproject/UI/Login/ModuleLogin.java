package com.example.oritoledanoproject.UI.Login;

import android.content.Context;
import android.widget.EditText;

import com.example.oritoledanoproject.Data.Repository.Repository;

public class ModuleLogin {

    Repository repository;

    Context context;

    public ModuleLogin(Context context)
    {
        repository = new Repository(context);
        this.context = context;

    }

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
