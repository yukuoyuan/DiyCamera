package cn.yky.camera.utils;

import android.hardware.Camera;

/**
 * Created by yukuoyuan on 2017/9/14.
 * 这是一个相机的工具类
 */
public class CameraUtils {
    private static CameraUtils sInstance;

    private CameraUtils() {
    }

    public static CameraUtils instance() {
        if (sInstance == null) {
            synchronized (CameraUtils.class) {
                if (sInstance == null) {
                    sInstance = new CameraUtils();
                }
            }
        }
        return sInstance;
    }

    /**
     * 根据相机id获取相机实例
     *
     * @param mCameraId 相机id
     * @return 相机实例
     */
    public Camera getCamera(int mCameraId) {
        Camera camera = null;
        try {
            camera = Camera.open(mCameraId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return camera;
    }
}
