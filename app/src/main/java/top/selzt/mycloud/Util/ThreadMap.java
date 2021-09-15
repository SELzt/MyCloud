package top.selzt.mycloud.Util;

import java.util.concurrent.ConcurrentHashMap;

public class ThreadMap {
    public static ConcurrentHashMap<String,Thread> uploadThreadMap = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String,Thread> downloadThreadMap = new ConcurrentHashMap<>();
}
