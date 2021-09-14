package top.selzt.mycloud.ReceiveData;

import java.util.List;

import top.selzt.mycloud.pojo.FileDetail;

public class FileVo {
    private int code;
    private String token;
    private List<FileDetail> filesList;

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

    public List<FileDetail> getFilesList() {
        return filesList;
    }

    public void setFilesList(List<FileDetail> filesList) {
        this.filesList = filesList;
    }
}
