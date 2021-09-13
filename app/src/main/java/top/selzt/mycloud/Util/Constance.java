package top.selzt.mycloud.Util;

import okhttp3.MediaType;

public class Constance {
    public static final String SERVER_ADDR = "10.0.2.2:8888";
    public static final MediaType JSON = MediaType.parse("application/json; charset-utf-8");
    public static final String ROUTE_HOMEACTIVITY_URL = "/app/homeActivity";
    public static final int CREATE_FOLDER_SUCCESS = 100;
    public static final int CREATE_FOLDER_FAIL = 101;
    public static final int CREATE_FOLDER_EXISTS = 102;
    public static final int GET_LIST_SUCCESS = 200;
    public static final int GET_LIST_FAIL = 201;
    public static final int DELETE_FILE_SUCCESS = 500;
    public static final int DELETE_FILE_FAIL = 501;
}
