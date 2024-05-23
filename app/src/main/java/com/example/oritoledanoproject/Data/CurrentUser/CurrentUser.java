package com.example.oritoledanoproject.Data.CurrentUser;

public class   CurrentUser {
    static String name;
    static String email;
    static String fireID;

    public static String getName() {
        return name;
    }

    public static String getEmail() {
        return email;
    }

    public static String getFireID() {
        return fireID;
    }

    public static void initializeUser(String name, String email, String fireID)
    {
        CurrentUser.name = name;
        CurrentUser.email = email;
        CurrentUser.fireID = fireID;
    }
}
