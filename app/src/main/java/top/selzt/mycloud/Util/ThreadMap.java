package top.selzt.mycloud.Util;

import java.util.concurrent.ConcurrentHashMap;

import top.selzt.mycloud.TransmissionThread.UploadThread;

public class ThreadMap {
    public static ConcurrentHashMap<String, UploadThread> uploadThreadMap = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String,Thread> downloadThreadMap = new ConcurrentHashMap<>();
}
