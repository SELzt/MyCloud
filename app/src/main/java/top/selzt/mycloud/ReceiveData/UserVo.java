package top.selzt.mycloud.ReceiveData;

import top.selzt.mycloud.pojo.UserInfo;

public class UserVo {
    private String code;
    private String token;
    private UserInfo userInfo;
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public UserInfo getUserInfo() {
        return userInfo;
    }
    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

}
