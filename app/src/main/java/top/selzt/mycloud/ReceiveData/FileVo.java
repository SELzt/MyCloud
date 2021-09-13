package top.selzt.mycloud.ReceiveData;

import java.util.List;

import top.selzt.mycloud.pojo.File;

public class FileVo {
    private int code;
    private String token;
    private List<File> filesList;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<File> getFilesList() {
        return filesList;
    }

    public void setFilesList(List<File> filesList) {
        this.filesList = filesList;
    }
}
