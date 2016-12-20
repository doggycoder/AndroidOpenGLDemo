/*
 *
 * BaseFilter.java
 * 
 * Created by Wuwang on 2016/12/20
 * Copyright © 2016年 深圳哎吖科技. All rights reserved.
 */
package com.wuwang.easygpu.core;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.res.Resources;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

/**
 * Description:
 */
public abstract class BaseFilter implements GLSurfaceView.Renderer {

    //顶点坐标
    private float pos[] = {
        -1.0f,  1.0f,
        -1.0f, -1.0f,
        1.0f, 1.0f,
        1.0f,  -1.0f,
    };

    //纹理坐标
    private float[] coord={
        0.0f, 0.0f,
        0.0f,  1.0f,
        1.0f,  0.0f,
        1.0f, 1.0f,
    };

    protected Resources mRes;
    protected int mProgram;
    protected int mUMatrix;
    protected int mACoord;
    protected int mAPosition;
    protected int mUTexture;

    private float[] matrix=Gl2Utils.getOriginalMatrix();
    private int textureId;
    private int textureIndex;

    protected FloatBuffer mVerBuffer;
    protected FloatBuffer mTexBuffer;

    public BaseFilter(Resources res){
        this.mRes=res;
        initBuffer();
    }

    public void createProgram(String vertex,String fragment){
        mProgram=Gl2Utils.createGlProgram(vertex, fragment);
        mUMatrix=GLES20.glGetUniformLocation(mProgram,"uMatrix");
        mACoord=GLES20.glGetAttribLocation(mProgram,"aCoord");
        mAPosition=GLES20.glGetAttribLocation(mProgram,"aPosition");
        mUTexture=GLES20.glGetUniformLocation(mProgram,"uTexture");
    }

    public void createProgramByAssets(String vertexRes,String fragmentRes){
        String vertex=Gl2Utils.getAssetsText(mRes,vertexRes);
        String fragment=Gl2Utils.getAssetsText(mRes,fragmentRes);
        createProgram(vertex,fragment);
    }

    protected void initBuffer(){
        ByteBuffer a=ByteBuffer.allocateDirect(32);
        a.order(ByteOrder.nativeOrder());
        mVerBuffer=a.asFloatBuffer();
        mVerBuffer.put(pos);
        mVerBuffer.position(0);
        ByteBuffer b=ByteBuffer.allocateDirect(32);
        b.order(ByteOrder.nativeOrder());
        mTexBuffer=b.asFloatBuffer();
        mTexBuffer.put(coord);
        mTexBuffer.position(0);
    }

    public void setMatrix(float[] matrix){
        this.matrix=matrix;
    }

    public float[] getMatrix(){
        return matrix;
    }

    public void setTextureId(int textureId){
        this.textureId=textureId;
    }

    public int getTextureId(){
        return textureId;
    }

    public void setTextureIndex(int index){
        this.textureIndex=index;
    }

    public int getTextureIndex(){
        return textureIndex;
    }

    protected abstract void onCreate();

    protected abstract void onSizeChanged(int width,int height);

    protected void onClear(){
        GLES20.glClearColor(1.0f,1.0f,1.0f,1.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT|GLES20.GL_DEPTH_BUFFER_BIT);
    }

    protected void onBindExpand(){
        GLES20.glUniformMatrix4fv(mUMatrix,1,false,matrix,0);
    }

    protected void onBindTexture(){
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0+getTextureIndex());
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,getTextureId());
        GLES20.glUniform1i(mUTexture,getTextureIndex());
    }

    protected void onDrawArrays(){
        GLES20.glEnableVertexAttribArray(mAPosition);
        GLES20.glVertexAttribPointer(mAPosition,2, GLES20.GL_FLOAT, false, 0,mVerBuffer);
        GLES20.glEnableVertexAttribArray(mACoord);
        GLES20.glVertexAttribPointer(mACoord, 2, GLES20.GL_FLOAT, false, 0, mTexBuffer);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP,0,4);
        GLES20.glDisableVertexAttribArray(mAPosition);
        GLES20.glDisableVertexAttribArray(mACoord);
    }

    @Override
    public final void onSurfaceCreated(GL10 gl, EGLConfig config) {
        onCreate();
    }

    @Override
    public final void onSurfaceChanged(GL10 gl, int width, int height) {
        onSizeChanged(width,height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        onClear();
        onBindTexture();
        onBindExpand();
        onDrawArrays();
    }

}
