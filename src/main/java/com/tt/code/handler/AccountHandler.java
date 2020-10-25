package com.tt.code.handler;


public class AccountHandler {

    private AccountHandler() {
    }

    private static final ThreadLocal<String> USER_INFO_THREAD_LOCAL = new ThreadLocal<>();

    public static void clear() {
        USER_INFO_THREAD_LOCAL.remove();
    }

    public static void set(String user) {
        USER_INFO_THREAD_LOCAL.set(user);
    }

    public static String getCurrentUser() {
        return USER_INFO_THREAD_LOCAL.get();
    }


}
