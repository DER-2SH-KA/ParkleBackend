package ru.d2k.parkle.controller;

public interface ApiPaths {
    String API = "/api/";

    String ROLE_API = API + "roles/";
    String USER_API = API + "users/";
    String WEBSITE_API = API + "websites/";
    String AUTH_API = API + "auth/";
    String AUTH_LOGIN_API = AUTH_API + "login/";
    String AUTH_REGISTRATION_API = AUTH_API + "registration/";
}
