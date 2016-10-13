/*
 *
 * FGLRender.java
 * 
 * Created by Wuwang on 2016/9/29
 * Copyright © 2016年 深圳哎吖科技. All rights reserved.
 */
package edu.wuwang.opengl.render;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Description:
 */
public class FGLRender implements GLSurfaceView.Renderer {

    private Shape shape;
    private Class<? extends Shape> clazz=Cube.class;

    public void setShape(Class<? extends Shape> shape){
        this.clazz=shape;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.5f,0.5f,0.5f,1.0f);
        Log.e("wuwang","onSurfaceCreated");
        try {
            shape=clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            shape=new Cube();
        }
        shape.onSurfaceCreated(gl,config);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        Log.e("wuwang","onSurfaceChanged");
        GLES20.glViewport(0,0,width,height);

        shape.onSurfaceChanged(gl, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        Log.e("wuwang","onDrawFrame");
        shape.onDrawFrame(gl);
    }

}
