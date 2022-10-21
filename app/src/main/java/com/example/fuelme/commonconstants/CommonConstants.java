package com.example.fuelme.commonconstants;

public class CommonConstants {
    public static final String REMOTE_URL = "https://fuelme.azurewebsites.net/";
    public static final String REMOTE_URL_AUTH = REMOTE_URL + "api/Auth/";
    public static final String REMOTE_URL_AUTH_LOGIN = REMOTE_URL + "api/Auth/login";
    public static final String REMOTE_URL_AUTH_REGISTER = REMOTE_URL + "api/Auth/register";
    public static final String REMOTE_URL_NOTICE = REMOTE_URL + "api/Notice/";
    public static final String REMOTE_URL_NOTICE_STATION = REMOTE_URL + "api/Notice/station/";
    public static final String REMOTE_URL_NOTICE_AUTHOR = REMOTE_URL + "api/Notice/author/";
    public static final String REMOTE_URL_GET_FAVORITE_STATIONS = REMOTE_URL + "api/Favourite/GetAllFavouriteByUsernameWithStationId/";
    public static final String REMOTE_URL_DELETE_FAVORITE_STATIONS = REMOTE_URL + "api/Favourite/DeleteFavouriteByStaionId/";
}
