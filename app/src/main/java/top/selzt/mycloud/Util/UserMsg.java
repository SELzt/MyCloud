package top.selzt.mycloud.Util;

import top.selzt.mycloud.pojo.UserInfo;

public class UserMsg {
    private static UserMsg userMsg = new UserMsg();
    private String token;
    private UserInfo userInfo;
    private String nowPath = "";
    private boolean success = false;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getNowPath() {
        return nowPath;
    }

    public void setNowPath(String nowPath) {
        this.nowPath = nowPath;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public static UserMsg getInstance(){
        return userMsg;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
