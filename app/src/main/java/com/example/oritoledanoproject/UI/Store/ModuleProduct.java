package com.example.oritoledanoproject.UI.Store;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.oritoledanoproject.Data.Repository.Repository;
import com.example.oritoledanoproject.R;

public class ModuleProduct {

    Context context;
    Repository repository;

    // בנאי שמאתחל את מחסן הנתונים (repository)
    public ModuleProduct(Context context) {
        this.context = context;
        repository = new Repository(context);
    }

    // הוספת מוצר למאגר הנתונים
    public void addProduct(String gender, String type, String situation, String description, String price, Bitmap photo) {
        repository.addProduct(gender, type, situation, description, price, photo);
    }

    // בדיקת תקינות הנתונים של המוצר לפני הוספתו למחסן הנתונים
    public boolean checkUps(String gender, String quality, String type, String description, String price, ImageView imageView) {

        // בדיקת תקינות שדה המגדר
        if (gender.equals("מגדר ומידה")) {
            Toast.makeText(context, "בחר מגדר ומידה", Toast.LENGTH_SHORT).show();
            return false;
        }

        // בדיקת תקינות שדה איכות המוצר
        if (quality.equals("מצב המוצר")) {
            Toast.makeText(context, "בחר את מצב המוצר", Toast.LENGTH_SHORT).show();
            return false;
        }

        // בדיקת תקינות שדה סוג המוצר
        if (type.equals("סוג מוצר")) {
            Toast.makeText(context, "בחר את סוג מוצר", Toast.LENGTH_SHORT).show();
            return false;
        }

        // בדיקת תקינות שדה התיאור
        if (description.equals("")) {
            Toast.makeText(context, "רשום תיאור", Toast.LENGTH_SHORT).show();
            return false;
        }

        // בדיקת תקינות שדה המחיר
        if (price.isEmpty()) {
            Toast.makeText(context, "רשום מחיר", Toast.LENGTH_SHORT).show();
            return false;
        }

        // בדיקת תקינות התמונה (אם לא נוספה תמונה)
        if (imageView.getTag().equals("NoPic")) {
            Toast.makeText(context, "תוסיף תמונה", Toast.LENGTH_SHORT).show();
            return false;
        }

        // אם כל הבדיקות עברו בהצלחה, הפונקציה תחזיר true
        return true;
    }

}
