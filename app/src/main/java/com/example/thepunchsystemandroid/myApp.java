package com.example.thepunchsystemandroid;

import org.litepal.LitePalApplication;

import java.net.CookieStore;

public class myApp extends LitePalApplication {


    private CookieStore cookies;

    public CookieStore getCookie() {
        return cookies;
    }

    public void setCookie(CookieStore cks) {
        cookies = cks;
    }
}
