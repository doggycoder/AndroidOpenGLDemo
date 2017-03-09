package edu.wuwang.opengl.egl;

import java.nio.IntBuffer;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

import edu.wuwang.opengl.filter.AFilter;

/**
 * Description:
 */
public class GLES20BackEnv {

    private int mWidth;
    private int mHeight;
    private EGLHelper mEGLHelper;

    final static String TAG = "GLES20BackEnv";
    final static boolean LIST_CONFIGS = false;

    private AFilter mFilter;
    Bitmap mBitmap;
    String mThreadOwner;

    public GLES20BackEnv(int width,int height){
        this.mWidth=width;
        this.mHeight=height;
        mEGLHelper=new EGLHelper();
        mEGLHelper.eglInit(width,height);
    }

    public void setThreadOwner(String threadOwner){
        this.mThreadOwner=threadOwner;
    }

    public void setFilter(final AFilter filter) {
        mFilter = filter;

        // Does this thread own the OpenGL context?
        if (!Thread.currentThread().getName().equals(mThreadOwner)) {
            Log.e(TAG, "setRenderer: This thread does not own the OpenGL context.");
            return;
        }
        // Call the renderer initialization routines
        mFilter.create();
        mFilter.setSize(mWidth, mHeight);
    }

    public Bitmap getBitmap() {
        if (mFilter == null) {
            Log.e(TAG, "getBitmap: Renderer was not set.");
            return null;
        }
        if (!Thread.currentThread().getName().equals(mThreadOwner)) {
            Log.e(TAG, "getBitmap: This thread does not own the OpenGL context.");
            return null;
        }
        mFilter.setTextureId(createTexture(mBitmap));
        mFilter.draw();
        return convertToBitmap();
    }

    public void destroy() {
        mEGLHelper.destroy();
    }


    private Bitmap convertToBitmap() {
        int[] iat = new int[mWidth * mHeight];
        IntBuffer ib = IntBuffer.allocate(mWidth * mHeight);
        mEGLHelper.mGL.glReadPixels(0, 0, mWidth, mHeight, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE,
            ib);
        int[] ia = ib.array();

        // Convert upside down mirror-reversed image to right-side up normal
        // image.
        for (int i = 0; i < mHeight; i++) {
            System.arraycopy(ia, i * mWidth, iat, (mHeight - i - 1) * mWidth, mWidth);
        }
        Bitmap bitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
        bitmap.copyPixelsFromBuffer(IntBuffer.wrap(iat));
        return bitmap;
    }

    public void setInput(Bitmap bitmap){
        this.mBitmap=bitmap;
    }

    private int createTexture(Bitmap bmp){
        int[] texture=new int[1];
        if(bmp!=null&&!bmp.isRecycled()){
            //生成纹理
            GLES20.glGenTextures(1,texture,0);
            //生成纹理
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,texture[0]);
            //设置缩小过滤为使用纹理中坐标最接近的一个像素的颜色作为需要绘制的像素颜色
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_NEAREST);
            //设置放大过滤为使用纹理中坐标最接近的若干个颜色，通过加权平均算法得到需要绘制的像素颜色
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR);
            //设置环绕方向S，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_CLAMP_TO_EDGE);
            //设置环绕方向T，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_CLAMP_TO_EDGE);
            //根据以上指定的参数，生成一个2D纹理
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bmp, 0);
            return texture[0];
        }
        return 0;
    }



}
