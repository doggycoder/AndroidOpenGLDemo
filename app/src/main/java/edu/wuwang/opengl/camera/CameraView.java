/*
 *
 * CameraView.java
 * 
 * Created by Wuwang on 2016/11/14
 * Copyright © 2016年 深圳哎吖科技. All rights reserved.
 */
package edu.wuwang.opengl.camera;

import java.util.Queue;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;

/**
 * Description:
 */
public class CameraView extends GLSurfaceView implements GLSurfaceView.Renderer {

    private KitkatCamera mCamera;
    private CameraDrawer mCameraDrawer;
    private int cameraId=1;

    private Runnable mRunnable;

    public CameraView(Context context) {
        this(context,null);
    }

    public CameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        setEGLContextClientVersion(2);
        setRenderer(this);
        setRenderMode(RENDERMODE_WHEN_DIRTY);
        mCamera=new KitkatCamera();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        if(mRunnable!=null){
            mRunnable.run();
            mRunnable=null;
        }
        Log.e("wuwang","cameraId--"+cameraId);
        mCamera.open(cameraId);
        Point point=mCamera.getPreviewSize();
        mCameraDrawer=new CameraDrawer(getResources());
        mCameraDrawer.setPreviewSize(point.x,point.y);
        mCamera.setPreviewTexture(mCameraDrawer.getSurfaceTexture());
        mCameraDrawer.getSurfaceTexture().setOnFrameAvailableListener(new SurfaceTexture.OnFrameAvailableListener() {
            @Override
            public void onFrameAvailable(SurfaceTexture surfaceTexture) {
                requestRender();
            }
        });
        mCamera.preview();
    }

    public void switchCamera(){
        mRunnable=new Runnable() {
            @Override
            public void run() {
                mCamera.close();
                cameraId=cameraId==1?0:1;
            }
        };
        onPause();
        onResume();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        mCameraDrawer.calculateMatrix(width,height,cameraId==1,1);
        GLES20.glViewport(0,0,width,height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClearColor(1.0f,1.0f,1.0f,1.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT|GLES20.GL_DEPTH_BUFFER_BIT);
        mCameraDrawer.draw(true,true);
    }

    @Override
    public void onPause() {
        super.onPause();
        mCamera.close();
    }
}
