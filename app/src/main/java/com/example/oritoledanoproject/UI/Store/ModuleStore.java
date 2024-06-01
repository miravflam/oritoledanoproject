package com.example.oritoledanoproject.UI.Store;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.oritoledanoproject.Data.FirebaseHelper.FirebaseHelper;
import com.example.oritoledanoproject.Data.Repository.Repository;
import com.example.oritoledanoproject.R;

public class ModuleStore {
    Repository repository;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    // בנאי שמאתחל את מחסן הנתונים (repository) ואת SharedPreferences
    public ModuleStore(Context context) {
        repository = new Repository(context);
        sharedPreferences = context.getSharedPreferences("Main", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    // משיג מוצרים מהמחסן דרך ה-Firebase
    public void getProducts(FirebaseHelper.productsFetched callback) {
        repository.getProducts(callback);
    }

    // מסיר את המידע של המשתמש מ-SharedPreferences (משתמש לא רוצה להיזכר)
    public void DoNotRemember() {
        editor.remove("useremail");
        editor.remove("userpass");
        editor.remove("Remember");
        editor.apply();
    }

    //sharedPreferencesזוכר את המשתמש ב
    public boolean DoesRemember() {
        return sharedPreferences.getBoolean("Remember", false);
    }

    // מחזיר את המשתמש
    public String[] getCredentials() {
        return new String[]{
                sharedPreferences.getString("username", ""),
                sharedPreferences.getString("email", "")
        };
    }
}
