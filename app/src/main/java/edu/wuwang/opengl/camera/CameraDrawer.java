/*
 *
 * CameraDrawer.java
 * 
 * Created by Wuwang on 2016/11/5
 * Copyright © 2016年 深圳哎吖科技. All rights reserved.
 */
package edu.wuwang.opengl.camera;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import edu.wuwang.opengl.filter.AFilter;
import edu.wuwang.opengl.filter.OesFilter;
import edu.wuwang.opengl.utils.Gl2Utils;
import edu.wuwang.opengl.utils.ShaderUtils;

/**
 * Description:
 */
public class CameraDrawer implements GLSurfaceView.Renderer {

    private float[] matrix=new float[16];
    private SurfaceTexture surfaceTexture;
    private int width,height;
    private int dataWidth,dataHeight;
    private AFilter mOesFilter;
    private int cameraId=1;

    public CameraDrawer(Resources res){
        mOesFilter=new OesFilter(res);
    }
    public void setDataSize(int dataWidth,int dataHeight){
        this.dataWidth=dataWidth;
        this.dataHeight=dataHeight;
        calculateMatrix();
    }

    public void setViewSize(int width,int height){
        this.width=width;
        this.height=height;
        calculateMatrix();
    }

    private void calculateMatrix(){
        Gl2Utils.getShowMatrix(matrix,this.dataWidth,this.dataHeight,this.width,this.height);
        if(cameraId==1){
            Gl2Utils.flip(matrix,true,false);
            Gl2Utils.rotate(matrix,90);
        }else{
            Gl2Utils.rotate(matrix,270);
        }
        mOesFilter.setMatrix(matrix);
    }

    public SurfaceTexture getSurfaceTexture(){
        return surfaceTexture;
    }

    public void setCameraId(int id){
        this.cameraId=id;
        calculateMatrix();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        int texture = createTextureID();
        surfaceTexture=new SurfaceTexture(texture);
        mOesFilter.create();
        mOesFilter.setTextureId(texture);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        setViewSize(width,height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        if(surfaceTexture!=null){
            surfaceTexture.updateTexImage();
        }
        mOesFilter.draw();
    }

    private int createTextureID(){
        int[] texture = new int[1];
        GLES20.glGenTextures(1, texture, 0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, texture[0]);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
            GL10.GL_TEXTURE_MIN_FILTER,GL10.GL_LINEAR);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
            GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
            GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
            GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);
        return texture[0];
    }

}
