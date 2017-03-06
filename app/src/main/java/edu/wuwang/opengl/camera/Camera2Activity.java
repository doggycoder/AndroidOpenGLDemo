/*
 *
 * Camera2Activity.java
 * 
 * Created by Wuwang on 2017/3/6
 * Copyright © 2016年 深圳哎吖科技. All rights reserved.
 */
package edu.wuwang.opengl.camera;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.widget.Toast;

import edu.wuwang.opengl.BaseActivity;
import edu.wuwang.opengl.R;
import edu.wuwang.opengl.utils.PermissionUtils;


/**
 * Description:
 */
public class Camera2Activity extends BaseActivity {

    private SurfaceView mSurfaceView;
    private CameraController mController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PermissionUtils.askPermission(this,new String[]{Manifest.permission.CAMERA,Manifest
            .permission.WRITE_EXTERNAL_STORAGE},10,initViewRunnable);
    }

    private Runnable initViewRunnable=new Runnable() {
        @Override
        public void run() {
            setContentView(R.layout.activity_camera2);
            mSurfaceView= (SurfaceView)findViewById(R.id.mSurface);

        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtils.onRequestPermissionsResult(requestCode == 10, grantResults, initViewRunnable,
            new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(Camera2Activity.this, "没有获得必要的权限", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("切换摄像头").setTitle("切换摄像头").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String name=item.getTitle().toString();
        if(name.equals("切换摄像头")){
        }
        return super.onOptionsItemSelected(item);
    }

}
