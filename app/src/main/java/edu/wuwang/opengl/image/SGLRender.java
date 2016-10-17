/*
 *
 * SGLRender.java
 * 
 * Created by Wuwang on 2016/10/15
 */
package edu.wuwang.opengl.image;

import android.graphics.Bitmap;
import android.opengl.GLSurfaceView;
import android.view.View;


import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import edu.wuwang.opengl.image.filter.AFilter;

/**
 * Description:
 */
public class SGLRender implements GLSurfaceView.Renderer {

    private AFilter mFilter;
    private Bitmap bitmap;

    public SGLRender(View mView){
        mFilter=new AFilter(mView.getContext(),"filter/default_vertex.sh","filter/default_fragment.sh");
    }

    public void setImageBuffer(int[] buffer,int width,int height){
        bitmap= Bitmap.createBitmap(buffer,width,height, Bitmap.Config.RGB_565);
        mFilter.setBitmap(bitmap);
    }

    public void setImage(Bitmap bitmap){
        this.bitmap=bitmap;
        mFilter.setBitmap(bitmap);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        mFilter.onSurfaceCreated(gl, config);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        mFilter.onSurfaceChanged(gl, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        mFilter.onDrawFrame(gl);
    }
}
