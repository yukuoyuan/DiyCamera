package cn.yky.camera;

import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import cn.yky.camera.utils.CameraUtils;

/**
 * Created by yukuoyuan on 2017/9/14.
 * 这是一个自定义相机的界面
 */
public class DIYCameraActivity extends AppCompatActivity implements View.OnClickListener, SurfaceHolder.Callback {
    /**
     * 展示拍照的视图
     */
    private SurfaceView diyCameraSfvCamera;
    /**
     * 后退按钮也
     */
    private TextView diyCameraBack;
    /**
     * 切换摄像头
     */
    private TextView diyCameraSwitchCamera;
    /**
     * 拍照
     */
    private TextView diyCameraTakePicture;
    /**
     * 哪个摄像头(默认是后置摄像头)
     */
    private int mCameraId = 0;
    /**
     * 相机实例
     */
    private Camera mCamera;
    private SurfaceHolder mHolder;
    //屏幕宽高
    private int screenWidth;
    private int screenHeight;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diy_camera);
        initView();
        initListener();
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mHolder = diyCameraSfvCamera.getHolder();
        mHolder.addCallback(this);
        /**
         * 获取屏幕的宽高
         */
        DisplayMetrics dm = getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
    }

    /**
     * 把相机和界面的生命周期绑定,避免浪费资源
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (mCamera == null) {
            mCamera = CameraUtils.instance().getCamera(mCameraId);
            /**
             * 预览界面
             */
            if (mHolder != null) {
                startPreview(mCamera, mHolder);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();
    }

    /**
     * 设置监听
     */
    private void initListener() {
        diyCameraBack.setOnClickListener(this);
        diyCameraSwitchCamera.setOnClickListener(this);
        diyCameraTakePicture.setOnClickListener(this);

    }

    /**
     * 初始化控件
     */
    private void initView() {
        diyCameraSfvCamera = (SurfaceView) findViewById(R.id.diy_camera_sfv_camera);
        diyCameraBack = (TextView) findViewById(R.id.diy_camera_back);
        diyCameraSwitchCamera = (TextView) findViewById(R.id.diy_camera_switch_camera);
        diyCameraTakePicture = (TextView) findViewById(R.id.diy_camera_take_picture);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.diy_camera_back) {
            onBackPressed();
        } else if (i == R.id.diy_camera_switch_camera) {
            switchCamera();
        } else if (i == R.id.diy_camera_take_picture) {
        }
    }

    /**
     * 切换摄像头
     */
    private void switchCamera() {
        /**
         * 释放资源
         */
        releaseCamera();
        mCameraId = (mCameraId + 1) % mCamera.getNumberOfCameras();
        /**
         * 重新获取相机实例
         */
        mCamera = CameraUtils.instance().getCamera(mCameraId);
        /**
         * 预览视图
         */
        if (mHolder != null) {
            startPreview(mCamera, mHolder);
        }

    }

    /**
     * 预览视图
     *
     * @param mCamera 相机实例
     * @param mHolder
     */
    private void startPreview(Camera mCamera, SurfaceHolder mHolder) {
        /**
         * 设置相机的各项参数
         */
        setCameraParameter(mCamera);
    }

    /**
     * 配置相机的各项参数
     *
     * @param mCamera 相机的实例
     */
    private void setCameraParameter(Camera mCamera) {
        /**
         * 获取相机的参数
         */
        Camera.Parameters parameters = mCamera.getParameters();
        /**
         * 设置对焦模式
         */
        if (parameters.getSupportedFocusModes().contains(
                Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        }
        /**
         * 设置预览的最小尺寸
         */
        Camera.Size previewSize = CameraUtils.instance().getMinSize(parameters.getSupportedPreviewSizes());
        parameters.setPreviewSize(previewSize.width, previewSize.height);
        Camera.Size pictrueSize =  CameraUtils.instance().getMinSize(parameters.getSupportedPreviewSizes());
        parameters.setPictureSize(pictrueSize.width, pictrueSize.height);
        mCamera.setParameters(parameters);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(screenWidth, screenHeight);
        diyCameraSfvCamera.setLayoutParams(params);
    }

    /**
     * 释放资源
     */
    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    /**
     * 视图创建调用的方法
     *
     * @param holder
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        startPreview(mCamera, holder);
    }

    /**
     * 视图改变的时候调用的方法
     *
     * @param holder
     * @param format
     * @param width
     * @param height
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mCamera.stopPreview();
        startPreview(mCamera, holder);
    }

    /**
     * 视图销毁调用的方法
     *
     * @param holder
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        releaseCamera();
    }
}
