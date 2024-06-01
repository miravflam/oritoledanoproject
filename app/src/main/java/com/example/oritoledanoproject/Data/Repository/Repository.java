package com.example.oritoledanoproject.Data.Repository;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;

//import com.example.oritoledanoproject.Data.DB.MyDatabaseHelper;
import com.example.oritoledanoproject.Data.FirebaseHelper.FirebaseHelper;

public class Repository {
    Context context;
    // אובייקט של FirebaseHelper לתקשורת עם מסד הנתונים של Firebase
    FirebaseHelper myFirebaseHelper;

    // בנאי שיוצר אובייקט של FirebaseHelper
    public Repository(Context context) {
        this.context = context;
        myFirebaseHelper = new FirebaseHelper(this.context);
    }

    // פונקציות להוספת משתמש ומוצר, קבלת מוצרים ובדיקת קיומו של משתמש במסד הנתונים
    public void addUserToFirebase(String name, String password, String email, String address, String phone) {
        myFirebaseHelper.addUser(name, password, email, address, phone);
    }

    public void addProduct(String gender, String type, String situation, String description, String price, Bitmap photo) {
        myFirebaseHelper.addProduct(gender, type, situation, description, price, photo);
    }

    public void getProducts(FirebaseHelper.productsFetched callback) {
        myFirebaseHelper.getProducts(callback);
    }

    public void getUser(String email, String password, FirebaseHelper.UserFound callback) {
        myFirebaseHelper.getUser(email, password, callback);
    }

    public void emailIsExist(String email, FirebaseHelper.UserFetched callback) {
        myFirebaseHelper.emailIsExist(email, callback);
    }
}

