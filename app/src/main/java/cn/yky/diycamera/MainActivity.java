package cn.yky.diycamera;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.yky.camera.DIYCameraActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvOpenCamera;
    private ImageView ivPreview;
    /**
     * 打开相机
     */
    private final int OPENCAMERA = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initListener();
    }

    private void initListener() {
        tvOpenCamera.setOnClickListener(this);
    }

    private void initView() {
        tvOpenCamera = (TextView) findViewById(R.id.tv_open_camera);
        ivPreview = (ImageView) findViewById(R.id.iv_preview);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_open_camera:
                Intent intent = new Intent(this, DIYCameraActivity.class);
                startActivityForResult(intent, OPENCAMERA);
                break;
        }
    }
}
