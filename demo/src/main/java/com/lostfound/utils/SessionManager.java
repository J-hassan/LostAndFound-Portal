package com.lostfound.utils;

public class SessionManager {
    private static String currentUserEmail;
    private static String currentUserName;
    private static String tempUserEmail; // for "Remember Me" feature
    private static String tempUserPassword;

    public static void setCurrentUserEmail(String email) {
        currentUserEmail = email;
    }

    public static void setCredentials(String email, String password) {
        tempUserEmail = email;
        tempUserPassword = password;
    }

    public static String getTempUserEmail() {
        return tempUserEmail;
    }

    public static String getTempUserPassword() {
        return tempUserPassword;
    }

    public static void clearCredentials() {
        tempUserEmail = null;
        tempUserPassword = null;
    }

    public static String getCurrentUserEmail() {
        return currentUserEmail;
    }

    public static String getCurrentUserName() {
        return currentUserName;
    }

    public static void clearSession() {
        currentUserEmail = null;
        currentUserName = null;
    }

    public static void setCurrentUserName(String name) {
        currentUserName = name;
    }
}
