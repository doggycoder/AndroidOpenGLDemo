/*
 *
 * SGLRender.java
 * 
 * Created by Wuwang on 2016/10/15
 */
package edu.wuwang.opengl.image;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.util.Log;
import android.view.View;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import edu.wuwang.opengl.utils.ShaderUtils;

/**
 * Description:
 */
public class SGLRender implements GLSurfaceView.Renderer {

    private View mView;
    private int mProgram;
    private int hPosition;
    private int hTexture;
    private int hCoord;
    private Bitmap bitmap;

    private FloatBuffer bPos;
    private FloatBuffer bCoord;

    private int textureId;

    private int hParams;
    private int hStepoffest;

    //美颜参数
    private final float toneLevel = 0.5f;
    private final float beautyLevel = 1.0f;
    private final float brightLevel = 0.1f;

    private final float[] sPos={
            -1.0f,1.0f,0.0f,
            -1.0f,-1.0f,0.0f,
            1.0f,1.0f,0.0f,
            1.0f,-1.0f,0.0f
    };

    private final float[] sCoord={
            1.0f,0.0f,
            1.0f,1.0f,
            0.0f,0.0f,
            0.0f,1.0f,
    };

    public SGLRender(View mView){
        this.mView=mView;
        ByteBuffer bb=ByteBuffer.allocateDirect(sPos.length*4);
        bb.order(ByteOrder.nativeOrder());
        bPos=bb.asFloatBuffer();
        bPos.put(sPos);
        bPos.position(0);
        ByteBuffer cc=ByteBuffer.allocateDirect(sCoord.length*4);
        cc.order(ByteOrder.nativeOrder());
        bCoord=cc.asFloatBuffer();
        bCoord.put(sCoord);
        bCoord.position(0);
    }

    public void setImageBuffer(int[] buffer,int width,int height){
        bitmap= Bitmap.createBitmap(buffer,width,height, Bitmap.Config.RGB_565);
    }

    public void setImage(Bitmap bitmap){
        this.bitmap=bitmap;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(1.0f,1.0f,1.0f,1.0f);
        GLES20.glEnable(GLES20.GL_TEXTURE_2D);
        mProgram=ShaderUtils.createProgram(mView.getResources(),"vshader/ImageDefault.sh","fshader/Beauty.sh");
        hPosition=GLES20.glGetAttribLocation(mProgram,"vPosition");
        hCoord=GLES20.glGetAttribLocation(mProgram,"aCoord");
        hTexture=GLES20.glGetUniformLocation(mProgram,"vTexture");
        hParams = GLES20.glGetUniformLocation(mProgram, "pParams");
        hStepoffest=GLES20.glGetUniformLocation(mProgram,"pStepOffset");

        GLES20.glUniform1f(hParams,0.1f);
        GLES20.glUniform1i(hTexture, 0);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0,0,width,height);
        float[] fTexelSize={2.0f/width,2.0f/height};
        GLES20.glUniform2fv(hStepoffest,1,FloatBuffer.wrap(fTexelSize));
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT|GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glUseProgram(mProgram);
        GLES20.glUniform1i(hTexture, 0);
        GLES20.glEnableVertexAttribArray(hPosition);
        GLES20.glEnableVertexAttribArray(hCoord);
        textureId=createTexture();
        if(textureId!=0){
            Log.e("wuwang","绑定贴图");
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,textureId);
        }else{
            Log.e("wuwang","贴图创建失败");
        }
        GLES20.glVertexAttribPointer(hPosition,3,GLES20.GL_FLOAT,false,0,bPos);
        GLES20.glVertexAttribPointer(hCoord,2,GLES20.GL_FLOAT,false,0,bCoord);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP,0,4);
    }

    private int createTexture(){
        int[] texture=new int[1];
        if(bitmap!=null&&!bitmap.isRecycled()){
            GLES20.glGenTextures(1,texture,0);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,texture[0]);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_NEAREST);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_CLAMP_TO_EDGE);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_CLAMP_TO_EDGE);
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
            bitmap.recycle();
            return texture[0];
        }
        return 0;
    }

}
