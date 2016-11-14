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
import javax.microedition.khronos.opengles.GL10;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import edu.wuwang.opengl.utils.ShaderUtils;

/**
 * Description:
 */
public class CameraDrawer {

    private FloatBuffer vertexBuffer, textureVerticesBuffer;
    private int mProgram;
    private int mPositionHandle;
    private int mTextureCoordHandle;
    private int mMatrixHandle;

    // number of coordinates per vertex in this array
    private static final int COORDS_PER_VERTEX = 2;
    private boolean isFront=true;

    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    static final float initCoords[] = {
            -1.0f,  1.0f,
            -1.0f, -1.0f,
            1.0f, 1.0f,
            1.0f,  -1.0f,
    };

    private float squareCoords[]=new float[8];

    static float textureVertices[] = {
            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 0.0f,
            1.0f, 1.0f,
    };

    private IntBuffer pixsBuffer;
    private ByteBuffer bPixsBuffer;
    private int[] pixs;
    private byte[] bPixs;

    private float[] matrix=new float[16];

    private int texture;
    private SurfaceTexture surfaceTexture;
    private int width,height;
    private float widthAdj;
    private float heightAdj;
    private float scaleAdj;

    public CameraDrawer(Resources res){
        mProgram= ShaderUtils.createProgram(res,"camera/camera_vertex.sh",
            "camera/camera_fragment.sh");
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        mTextureCoordHandle = GLES20.glGetAttribLocation(mProgram, "inputTextureCoordinate");
        mMatrixHandle=GLES20.glGetUniformLocation(mProgram,"vMatrix");

        texture=createTextureID();
        surfaceTexture=new SurfaceTexture(texture);
    }



    public void draw(boolean update,boolean isDraw){
        if(update){
            surfaceTexture.updateTexImage();
        }
        if(isDraw){
            GLES20.glUseProgram(mProgram);
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
            GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, texture);
            GLES20.glUniformMatrix4fv(mMatrixHandle,1,false,matrix,0);
            GLES20.glEnableVertexAttribArray(mPositionHandle);
            GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, vertexBuffer);
            GLES20.glEnableVertexAttribArray(mTextureCoordHandle);
            GLES20.glVertexAttribPointer(mTextureCoordHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, textureVerticesBuffer);
            GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP,0,4);
            GLES20.glDisableVertexAttribArray(mPositionHandle);
            GLES20.glDisableVertexAttribArray(mTextureCoordHandle);
        }
    }

    public Bitmap getBitmap(){
        int widthSize= (int)(width*scaleAdj);
        int heightSize= (int)(height*scaleAdj);
        if(pixsBuffer==null){
            int size= widthSize*heightSize;
            pixs=new int[size];
            pixsBuffer=IntBuffer.wrap(pixs);
        }else{
            pixsBuffer.clear();
        }
        GLES20.glReadPixels(width-widthSize,height-heightSize,widthSize,heightSize,GLES20
            .GL_RGBA,
            GLES20.GL_UNSIGNED_BYTE,pixsBuffer);
        Log.e("wuwang","cropStart-->"+(width-widthSize)+"/"+(height-heightSize));
        pixsBuffer.get(pixs);
        Bitmap bmp=Bitmap.createBitmap(pixs,widthSize,heightSize, Bitmap.Config.ARGB_8888);
        Log.e("wuwang","bmpSize-->"+widthSize+"/"+heightSize);
        return bmp;
    }

    public byte[] getPixs(){
        int widthSize= (int)(width*widthAdj*scaleAdj);
        int heightSize= (int)(height*heightAdj*scaleAdj);
        if(bPixsBuffer==null){
            int size= previewWidth*previewHeight*4;
            bPixs=new byte[size];
            bPixsBuffer=ByteBuffer.wrap(bPixs);
        }else{
            bPixsBuffer.clear();
        }
        GLES20.glReadPixels(width-widthSize,height-heightSize,previewWidth,previewHeight,GLES20
                .GL_RGBA,
            GLES20.GL_UNSIGNED_BYTE,bPixsBuffer);
        return bPixs;
    }

    private int previewWidth,previewHeight;
    public void setPreviewSize(int previewWidth,int previewHeight){
        this.previewWidth=previewWidth;
        this.previewHeight=previewHeight;
        Log.e("wuwang","previewWidth/previewHeight="+this.previewWidth+"/"+this.previewHeight);
    }

    public void calculateMatrix(int width,int height,boolean isFront,float size){
        this.width=width;
        this.height=height;
        this.isFront=isFront;
        Log.e("wuwang","width/height="+this.width+"/"+this.height);
        float[] or=new float[16];
        float[] ca=new float[16];
        adjustVertex(size);
        adjustCoord(isFront);

        Matrix.orthoM(or,0,-1,1,-1,1,0,1);
        Matrix.setLookAtM(ca,0,0,0,1,0,0,0,0,1,0);
        Matrix.multiplyMM(matrix,0,or,0,ca,0);
    }

    public SurfaceTexture getSurfaceTexture(){
        return surfaceTexture;
    }

    public void setOnFrameAvailableListener(SurfaceTexture.OnFrameAvailableListener listener){
        this.surfaceTexture.setOnFrameAvailableListener(listener);
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

    private void adjustVertex(float scaleSize) {
        float scalePreview=(float)previewWidth/previewHeight;
        float scaleScreen=(float)width/height;
        this.scaleAdj=scaleSize;
        //理想状况下 x/y=scalePreview/scaleScreen=1;则不会变形
        //保证centerCrop，则x和y都必须大于或等于1，且必须有一个为1
        //所以得到以下数据
        if(scaleScreen/scalePreview>1){
            widthAdj=1;
            heightAdj=scaleScreen/scalePreview;
        }else if(scaleScreen/scalePreview<1){
            widthAdj=scalePreview/scaleScreen;
            heightAdj=1;
        }else{
            widthAdj=1;
            heightAdj=1;
        }
        Log.e("wuwang","widthAdj/heightAdj-->"+widthAdj+"/"+heightAdj);
        squareCoords= Arrays.copyOf(initCoords,8);
        //保证图片不变形
        for (int i=0;i<squareCoords.length;i++){
            if(i%2==0){
                squareCoords[i]*=widthAdj;
            }else{
                squareCoords[i]*=heightAdj;
            }
            //缩小到右上角
            if(scaleSize<1){
                if(squareCoords[i]>=1){
                    squareCoords[i]=1;
                }else{
                    squareCoords[i]=1-(1-squareCoords[i])*scaleSize;
                }
            }
            Log.e("wuwang","squareCoords["+i+"]="+squareCoords[i]);
        }
        if(vertexBuffer==null){
            ByteBuffer bb = ByteBuffer.allocateDirect(squareCoords.length * 4);
            bb.order(ByteOrder.nativeOrder());
            vertexBuffer = bb.asFloatBuffer();
        }else{
            vertexBuffer.clear();
        }
        vertexBuffer.put(squareCoords);
        vertexBuffer.position(0);
    }

    private void adjustCoord(boolean isFront){
        if(isFront){
            //正面倒立
//            textureVertices=new float[] {
//                0.0f, 1.0f,
//                1.0f, 1.0f,
//                0.0f, 0.0f,
//                1.0f, 0.0f,
//            };
            //正面正立
            textureVertices=new float[]{
                1.0f, 1.0f,
                0.0f, 1.0f,
                1.0f, 0.0f,
                0.0f, 0.0f,
            };
        }else{
            textureVertices=new float[]{
                0.0f, 1.0f,
                1.0f, 1.0f,
                0.0f, 0.0f,
                1.0f, 0.0f,
            };
        }
        if(textureVerticesBuffer==null){
            ByteBuffer bb2 = ByteBuffer.allocateDirect(textureVertices.length * 4);
            bb2.order(ByteOrder.nativeOrder());
            textureVerticesBuffer = bb2.asFloatBuffer();
        }else{
            textureVerticesBuffer.clear();
        }
        textureVerticesBuffer.put(textureVertices);
        textureVerticesBuffer.position(0);
    }


}
