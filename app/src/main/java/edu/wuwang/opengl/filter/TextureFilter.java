/*
 *
 * AiyaEffectFilter.java
 * 
 * Created by Wuwang on 2017/2/24
 * Copyright © 2016年 深圳哎吖科技. All rights reserved.
 */
package edu.wuwang.opengl.filter;

import javax.microedition.khronos.opengles.GL10;

import android.content.res.Resources;
import android.graphics.SurfaceTexture;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;

/**
 * Description:
 */
public class TextureFilter extends AFilter {

    private float[] SM=new float[16];     //用于显示的变换矩阵
    private float[] OM;     //用于后台绘制的变换矩阵

    private SurfaceTexture mSurfaceTexture;

    private float[] infos = new float[20];
    private float[] coordMatrix=new float[16];

    private OesFilter mOesFilter;         //数据准备的Filter

    private int textureIndex = 0;
    //创建离屏buffer
    private int[] fFrame = new int[1];
    private int[] fTexture = new int[1];

    private int width=0,height=0;

    public static final int PARAMS_TYPE_FILTER = 0;

    public TextureFilter(Resources res) {
        super(res);
        mOesFilter=new OesFilter(res);
    }

    @Override
    public void setFlag(int flag) {
        super.setFlag(flag);
    }

    @Override
    protected void onCreate() {
        int texture = createTextureID();
        mSurfaceTexture = new SurfaceTexture(texture);
        mOesFilter.create();
        mOesFilter.setTextureId(texture);
    }

    public SurfaceTexture getTexture() {
        return mSurfaceTexture;
    }

    @Override
    protected void onSizeChanged(int width, int height) {
        this.width = width;
        this.height = height;
        mOesFilter.setSize(width,height);
    }

    @Override
    public float[] getMatrix() {
        return mOesFilter.getMatrix();
    }

    @Override
    public void setMatrix(float[] matrix) {
        mOesFilter.setMatrix(matrix);
    }

    //TODO　增加注释
    @Override
    public void draw() {
        if(getTexture()!=null){
            getTexture().updateTexImage();
            getTexture().getTransformMatrix(coordMatrix);
            mOesFilter.setCoordMatrix(coordMatrix);
        }
        mOesFilter.draw();
    }

    //创建显示摄像头原始数据的OES TEXTURE
    private int createTextureID() {
        int[] texture = new int[1];
        GLES20.glGenTextures(1, texture, 0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, texture[0]);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
            GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
            GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
            GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
            GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);
        return texture[0];
    }


    private void deleteFrameBuffer() {
        GLES20.glDeleteFramebuffers(1, fFrame, 0);
        GLES20.glDeleteTextures(1, fTexture, 0);
    }

}
