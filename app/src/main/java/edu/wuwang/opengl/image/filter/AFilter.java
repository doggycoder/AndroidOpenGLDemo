/*
 *
 * AFilter.java
 * 
 * Created by Wuwang on 2016/10/17
 */
package edu.wuwang.opengl.image.filter;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import edu.wuwang.opengl.utils.ShaderUtils;

/**
 * Description:
 */
public class AFilter implements GLSurfaceView.Renderer {

    public int mProgram;
    public Context mContext;

    public Bitmap mBitmap;

    private int glHCoordinate;
    private int glHPosition;
    private int glHTexture;

    private String glVertex;
    private String glFragment;

    private float[] coordinates={
            1.0f,0.0f,
            1.0f,1.0f,
            0.0f,0.0f,
            0.0f,1.0f,
    };

    private float[] position={
            -1.0f,1.0f,
            -1.0f,-1.0f,
            1.0f,1.0f,
            1.0f,-1.0f
    };

    private FloatBuffer glDCoordinates;
    private FloatBuffer glDPosition;

    private int bgColor=0xFFFFFFFF;

    public AFilter(Context context, String vertex,String fragment){
        this.mContext=context;
        this.glVertex=vertex;
        this.glFragment=fragment;

        glDCoordinates=ByteBuffer.allocateDirect(coordinates.length*4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(coordinates);
        glDCoordinates.position(0);
        glDPosition=ByteBuffer.allocateDirect(position.length*4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(position);
        glDPosition.position(0);
    }

    public final void setBitmap(Bitmap bitmap){
        this.mBitmap=bitmap;
    }

    public final void init(){
        onInit();
    }

    protected void onInit(){
        mProgram= ShaderUtils.createProgram(mContext.getResources(),glVertex,glFragment);
        glHCoordinate= GLES20.glGetAttribLocation(mProgram,"vCoordinate");
        glHPosition=GLES20.glGetAttribLocation(mProgram,"vPosition");
        glHTexture=GLES20.glGetUniformLocation(mProgram,"vTexture");
    }



    @Override
    public final void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor((bgColor>>4&0xFF)/255f,(bgColor>>2&0xFF)/255f,
                (bgColor&0xFF)/255f,(bgColor>>6&0xFF)/255f);
        Log.e("wuwang","bgColor:"+(bgColor>>4&0xFF)/255f);
        GLES20.glEnable(GLES20.GL_TEXTURE_2D);      //允许2D纹理
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        Log.e("wuwang","开启纹理"+GLES20.glIsEnabled(GLES20.GL_TEXTURE_2D));
        init();
    }

    @Override
    public final void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0,0,width,height);
    }

    @Override
    public final void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT|GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glUseProgram(mProgram);
        GLES20.glEnableVertexAttribArray(glHCoordinate);
        GLES20.glVertexAttribPointer(glHCoordinate,2,GLES20.GL_FLOAT,false,0,glDCoordinates);
        GLES20.glEnableVertexAttribArray(glHPosition);
        GLES20.glVertexAttribPointer(glHPosition,2,GLES20.GL_FLOAT,false,0,glDPosition);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP,0,4);
        if(mBitmap!=null&&!mBitmap.isRecycled()){
            int textureId=createTexture();
            if(textureId>0){
//                GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
//                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,textureId);
                GLES20.glUniform1i(glHTexture,0);       //设置第一层纹理
                Log.e("wuwang","绑定纹理成功");
            }else{
                Log.e("wuwang","绑定纹理失败");
            }
        }else{
            Log.e("wuwang","图片为空");
        }

        GLES20.glDisableVertexAttribArray(glHPosition);
        GLES20.glDisableVertexAttribArray(glHCoordinate);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,0);
    }

    private int createTexture(){
        int[] texture=new int[1];
        if(mBitmap!=null&&!mBitmap.isRecycled()){
            GLES20.glGenTextures(1,texture,0);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,texture[0]);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_NEAREST);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_CLAMP_TO_EDGE);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_CLAMP_TO_EDGE);
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, mBitmap, 0);
            mBitmap.recycle();
            return texture[0];
        }
        return 0;
    }


}
