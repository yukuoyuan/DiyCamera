package cn.yky.camera.utils;

import android.hardware.Camera;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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

    /**
     * 获取最小的尺寸
     *
     * @param supportedPreviewSizes
     * @return
     */
    public Camera.Size getMinSize(List<Camera.Size> supportedPreviewSizes) {
        Collections.sort(supportedPreviewSizes, new ComparatorByHeight());
        return supportedPreviewSizes.get(0);
    }

    public class ComparatorByHeight implements Comparator<Camera.Size> {
        public int compare(Camera.Size lhs, Camera.Size rhs) {
            if (lhs.height == rhs.height) {
                return 0;
            } else if (lhs.height > rhs.height) {
                return 1;
            } else {
                return -1;
            }
        }
    }
}
