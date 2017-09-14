package cn.yky.camera.utils;

import java.io.File;

/**
 * Created by yukuoyuan on 2017/9/14.
 * 这是一个处理文件的工具类
 */
public class FileUtils {
    private static FileUtils sInstance;

    private FileUtils() {
    }

    public static FileUtils instance() {
        if (sInstance == null) {
            synchronized (FileUtils.class) {
                if (sInstance == null) {
                    sInstance = new FileUtils();
                }
            }
        }
        return sInstance;
    }

    /**
     * 创建文件夹
     *
     * @param path 文件路径
     */
    public void makeDir(String path) {
        File file = new File(path);
        File tempPath = new File(file.getParent());
        if (!tempPath.exists()) {
            tempPath.mkdirs();
        }
    }
}
