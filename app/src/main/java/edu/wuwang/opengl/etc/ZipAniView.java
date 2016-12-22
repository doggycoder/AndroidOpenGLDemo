/*
 *
 * ZipAniView.java
 * 
 * Created by Wuwang on 2016/12/8
 * Copyright © 2016年 深圳哎吖科技. All rights reserved.
 */
package edu.wuwang.opengl.etc;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

/**
 * Description:
 */
public class ZipAniView extends GLSurfaceView implements GLSurfaceView.Renderer {

    private ZipMulDrawer mDrawer;

    public ZipAniView(Context context) {
        this(context,null);
    }

    public ZipAniView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        setEGLContextClientVersion(2);
        setZOrderOnTop(true);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
        setEGLConfigChooser(8,8,8,8,16,0);
        setRenderer(this);
        setRenderMode(RENDERMODE_WHEN_DIRTY);
        mDrawer=new ZipMulDrawer(getResources());
    }

    public void setScaleType(int type){
        if(mDrawer!=null){
            mDrawer.setInt(ZipMulDrawer.TYPE,type);
        }
    }

    public void setAnimation(String path,int timeStep){
        mDrawer.setAnimation(this,path,timeStep);
    }

    public void start(){
        mDrawer.start();
    }

    public void stop(){
        mDrawer.stop();
    }

    public boolean isPlay(){
        return mDrawer.isPlay();
    }

    public void setStateChangeListener(StateChangeListener listener){
        mDrawer.setStateChangeListener(listener);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        mDrawer.create();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        mDrawer.onSizeChanged(width,height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        mDrawer.draw();
    }

}
