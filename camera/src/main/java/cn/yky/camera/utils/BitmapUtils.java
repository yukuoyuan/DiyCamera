package cn.yky.camera.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by yukuoyuan on 2017/9/14.
 * 位图的工具类
 */
public class BitmapUtils {
    private static BitmapUtils sInstance;

    private BitmapUtils() {
    }

    public static BitmapUtils instance() {
        if (sInstance == null) {
            synchronized (BitmapUtils.class) {
                if (sInstance == null) {
                    sInstance = new BitmapUtils();
                }
            }
        }
        return sInstance;
    }

    /**
     * 把一个bitmap保存到本地的文件中
     *
     * @param context    上下文
     * @param saveBitmap 要保存的bitmap
     * @param imgpath    文件路径
     * @param quality    质量压缩级别
     */
    public void saveJPEG(Context context, Bitmap saveBitmap, String imgpath, int quality) {
        File file = new File(imgpath);
        /**
         * 创建文件夹
         */
        FileUtils.instance().makeDir(imgpath);
        try {
            FileOutputStream out = new FileOutputStream(file);
            if (saveBitmap.compress(Bitmap.CompressFormat.JPEG, quality, out)) {
                out.flush();
                out.close();
            }
            MediaScannerConnection.scanFile(context, new String[]{file.getPath()}, null, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
