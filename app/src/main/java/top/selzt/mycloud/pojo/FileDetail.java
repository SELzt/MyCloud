package top.selzt.mycloud.pojo;

public class FileDetail {
    //文件名
    private String fileName;
    //文件类型 文件或文件夹 0 / 1
    private String fileType;
    //文件大小
    private String fileSize;
    //修改时间
    private String modifyTime;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }
}
