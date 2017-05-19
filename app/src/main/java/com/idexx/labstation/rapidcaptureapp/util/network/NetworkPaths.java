package com.idexx.labstation.rapidcaptureapp.util.network;

/**
 * Created by mhansen on 4/23/2017.
 */
public class NetworkPaths
{
    public static final String API_CONTEXT = "api/";

    public static final String AUTH_PATH = "auth/";
    public static final String ADMIN_PATH = "admin/";
    public static final String USER_PATH = "user/";

    //Admin paths
    public static final String NEW_USER_PATH = ADMIN_PATH + "user/";

    //User management paths
    public static final String UPDATE_USER_PATH = USER_PATH + "update/";
    public static final String USER_DETAILS_PATH = USER_PATH + "details/";

    //API paths
    public static final String CLINICS_PATH = API_CONTEXT + "clinics/";
}
