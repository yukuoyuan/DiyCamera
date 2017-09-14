package cn.yky.camera.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.view.Surface;

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

    /**
     * 获取最大的尺寸
     *
     * @param supportedPreviewSizes
     * @return
     */
    public Camera.Size getMaxSize(List<Camera.Size> supportedPreviewSizes) {
        Collections.sort(supportedPreviewSizes, new ComparatorByHeight());
        return supportedPreviewSizes.get(supportedPreviewSizes.size() - 1);
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

    /**
     * 因为拍照返回的照片都是旋转的,所以需要手动旋转一下
     *
     * @param id     哪个摄像头
     * @param bitmap
     * @return
     */
    public Bitmap setTakePicktrueOrientation(int id, Bitmap bitmap) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(id, info);
        bitmap = rotaingImageView(id, info.orientation, bitmap);
        return bitmap;
    }

    /**
     * 把相机拍照返回照片转正
     *
     * @param angle 旋转角度
     * @return bitmap 图片
     */
    public Bitmap rotaingImageView(int id, int angle, Bitmap bitmap) {
        //旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        //加入翻转 把相机拍照返回照片转正
        if (id == 1) {
            matrix.postScale(-1, 1);
        }
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }

    /**
     * 保证预览方向正确
     *
     * @param activity 当前界面
     * @param cameraId 哪个相机
     * @param camera   相机实例
     */
    public void setCameraDisplayOrientation(Activity activity,
                                            int cameraId, Camera camera) {
        android.hardware.Camera.CameraInfo info =
                new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }
}
