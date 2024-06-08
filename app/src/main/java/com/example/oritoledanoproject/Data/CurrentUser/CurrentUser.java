package com.example.oritoledanoproject.Data.CurrentUser;

public class CurrentUser {
    // משתנים סטטיים לשמות המשתמש, האימייל והמזהה ב-Firebase
    static String name;
    static String email;
    static String fireID;

    // פונקציה סטטית לקבלת השם של המשתמש הנוכחי
    public static String getName() {
        return name;
    }

    // פונקציה סטטית לקבלת המזהה ב-Firebase של המשתמש הנוכחי
    public static String getFireID() {
        return fireID;
    }

    public static String getEmail(){return  email;}

    // פונקציה סטטית לאתחול המשתמש הנוכחי
    public static void initializeUser(String name, String email, String fireID) {
        // הגדרת שמות המשתנים הסטטיים עם הערכים המתקבלים
        CurrentUser.name = name;
        CurrentUser.email = email;
        CurrentUser.fireID = fireID;
    }
}

