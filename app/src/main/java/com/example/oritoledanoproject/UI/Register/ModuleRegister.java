package com.example.oritoledanoproject.UI.Register;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.EditText;
import android.widget.Toast;

import com.example.oritoledanoproject.Data.FirebaseHelper.FirebaseHelper;
import com.example.oritoledanoproject.Data.Repository.Repository;

public class ModuleRegister {

    Context context;
    Repository repository;

    public ModuleRegister(Context context)
    {
        this.context = context;
        repository = new Repository(context);
    }


    public static boolean isHebrewAndSpacesOnly(String str) {
        return str.matches("^[\u0590-\u05FF\\s]+$");
    }
    public static boolean isEnglish(String str) {
        return str.matches("^[a-zA-Z\\s]+$");
    }
    public static boolean areBothStringsSameLanguage(String str1, String str2) {
        boolean isStr1English = isEnglish(str1);
        boolean isStr1Hebrew = isHebrewAndSpacesOnly(str1);
        boolean isStr2English = isEnglish(str2);
        boolean isStr2Hebrew = isHebrewAndSpacesOnly(str2);

        return (isStr1English && isStr2English) || (isStr1Hebrew && isStr2Hebrew);
    }



    public void addUserToFirebase(String name, String password, String email, String address, String phone) { repository.addUserToFirebase(name, password, email, address, phone);}

    public void emailIsExist(String email , FirebaseHelper.UserFetched callback) { repository.emailIsExist(email, callback); }

    public Boolean CheckUps(EditText etUser, EditText etEmail, EditText etPassword, EditText etPasswordConfirmation, EditText etPhone, EditText etAddress)
    {
        // username validity checkups
        if(etUser.getText().toString().isEmpty())
        {
            etUser.setError("מלא שם משתמש");
            return false;
        }
         String name = etUser.getText().toString();
        if (name.length()>18||name.length()<2)
        {
            etUser.setError("שם מלא חייב להיות בין 2 - 18 תווים");
            return false;
        }
        if (!name.matches("^[a-zA-Z\u0590-\u05FF ]+$")) {
            etUser.setError("השם יכול להכיל רק אותיות באנגלית או בעברית ורווחים");
            return false;
        }

        if(etEmail.getText().toString().isEmpty())
        {
            etEmail.setError("מלא איימיל");
            return false;
        }

        // email validity checkups
        if(etEmail.getText().toString().indexOf("@") == -1)
        {
            etEmail.setError("אימייל חייב להכיל שטרודל");
            return false;
        }
        if(etEmail.getText().toString().indexOf("@") != etEmail.getText().toString().lastIndexOf("@"))
        {
            etEmail.setError("איימיל לא תקין");
            return false;
        }
        if(etEmail.getText().toString().indexOf(".") - etEmail.getText().toString().indexOf("@") <= 3)
        {
            etEmail.setError("איימיל לא תקין");
            return false;
        }
        if(etEmail.getText().toString().indexOf(".") != etEmail.getText().toString().lastIndexOf("."))
        {
            etEmail.setError("איימיל לא תקין");
            return false;
        }
        if(!(etEmail.getText().toString().contains(".com")) && !(etEmail.getText().toString().contains(".co.")))
        {
            etEmail.setError("איימיל לא תקין");
            return false;
        }



        //password validity checkups
        if(etPassword.getText().toString().equals(""))
        {
            etPassword.setError("מלא סיסמה");
            return false;
        }
        if(etPassword.getText().toString().length() < 3)
        {
            etPassword.setError("סיסמא לא חזקה מספיק ");
            return false;
        }
        if(!(etPassword.getText().toString().equals(etPasswordConfirmation.getText().toString())))
        {
            etPassword.setError("אישור הסיסמא לא תואם לסיסמא");
            return false;
        }


        String phone =etPhone.getText().toString();
        //phone validity checkups
        if(phone.isEmpty())
        {
            etPhone.setError("מלא טלפון");
            return false;
        }
        if (phone.length()!=10)
        {
            etPhone.setError("מספר טלפון חייב להכיל 10 מספרים");
            return false;
        }
        if (!phone.matches("\\d+")) {
            etPhone.setError("מספר טלפון חייב להכיל רק ספרות");
            return false;
        }




        String address = etAddress.getText().toString();
        if(etAddress.getText().toString().isEmpty())
        {
            etAddress.setError("מלא כתובת");
            return false;
        }
        else {
            int firstCommaIndex = address.indexOf(',');
            int secondCommaIndex = address.indexOf(',', firstCommaIndex + 1);

            //בודק אם הפורמט חוקי
            if (firstCommaIndex == -1 || secondCommaIndex == -1) {
                etAddress.setError("פורמט כתובת לא חוקי. השתמש ב-'רחוב, עיר, מספר בית'");
                return false;
            }


            String street = address.substring(0, firstCommaIndex).trim();
            String city = address.substring(firstCommaIndex + 1, secondCommaIndex).trim();
            String houseNumber = address.substring(secondCommaIndex + 1).trim();

            if (street.isEmpty()) {
                etAddress.setError("רחוב לא יכול להיות ריק");
                return false;
            }


            if (city.isEmpty()) {
                etAddress.setError("עיר לא יכול להיות ריקה");
                return false;
            }

            //בודק שרק ספרות
            if (!houseNumber.matches("\\d+")) {
                etAddress.setError("מספר בית חייב להיות מספר");
                return false;
            }

            if (!areBothStringsSameLanguage(street, city)) {
                Toast.makeText(context, "רחוב ועיר צריך להיות אותו שפה(אנגלית/עברית) ולא יכול להכיל סימנים", Toast.LENGTH_LONG).show();
                return false;
                }

        }

















        return true;
    }
}
