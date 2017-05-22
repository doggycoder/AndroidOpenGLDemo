package edu.wuwang.opengl.vr;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import edu.wuwang.opengl.utils.Gl2Utils;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

/**
 * Created by aiya on 2017/5/19.
 */

public class SkySphere{

    private static final float UNIT_SIZE = 1f;// 单位尺寸
    private float r = 0.5f; // 球的半径
    final int angleSpan = 2;// 将球进行单位切分的角度
    int vCount = 0;// 顶点个数，先初始化为0

    private float step=2f;
    private Resources res;

    private int mHProgram;
    private int mHUTexture;
    private int mHMatrix;
    private int mHPosition;
    private int mHCoordinate;

    private float[] mViewMatrix=new float[16];
    private float[] mProjectMatrix=new float[16];
    private float[] mMVPMatrix=new float[16];
    private float[] mFinalMatrix=new float[16];

    private FloatBuffer posBuffer;
    private FloatBuffer cooBuffer;
    private int vSize;
    private float skyRate=0.2f;

    private Bitmap mBitmap;

    public SkySphere(Context context,String texture){
        this.res=context.getResources();
        try {
            mBitmap=BitmapFactory.decodeStream(context.getAssets().open(texture));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void create(){
        mHProgram=Gl2Utils.createGlProgramByRes(res,"vr/skysphere.vert","vr/skysphere.frag");
        mHMatrix=GLES20.glGetUniformLocation(mHProgram,"uMatrix");
        mHUTexture=GLES20.glGetUniformLocation(mHProgram,"uTexture");
        mHPosition=GLES20.glGetAttribLocation(mHProgram,"aPosition");
        mHCoordinate=GLES20.glGetAttribLocation(mHProgram,"aCoordinate");
        createTexture();
        calculateAttribute();
    }

    public void setSize(int width,int height){
        //计算宽高比
        float ratio=(float)width/height;
        //设置透视投影
        Matrix.frustumM(mProjectMatrix, 0, -ratio*skyRate, ratio*skyRate, -1*skyRate, 1*skyRate, 1, 200);
        //设置相机位置
        Matrix.setLookAtM(mViewMatrix, 0, 0f, 0.0f, 2f, 0f, 0f, 0.0f, 0f, -1.0f, 0.0f);
        //计算变换矩阵
        Matrix.multiplyMM(mMVPMatrix,0,mProjectMatrix,0,mViewMatrix,0);
        System.arraycopy(mMVPMatrix,0,mFinalMatrix,0,16);
    }

    public void setMatrix(float[] matrix){
        Matrix.multiplyMM(mFinalMatrix,0,mMVPMatrix,0,matrix,0);
    }

    public void draw(){

        GLES20.glUseProgram(mHProgram);
        GLES20.glUniformMatrix4fv(mHMatrix,1,false,mFinalMatrix,0);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,mHUTexture);

        GLES20.glEnableVertexAttribArray(mHPosition);
        GLES20.glVertexAttribPointer(mHPosition,3,GLES20.GL_FLOAT,false,0,posBuffer);
        GLES20.glEnableVertexAttribArray(mHCoordinate);
        GLES20.glVertexAttribPointer(mHCoordinate,2,GLES20.GL_FLOAT,false,0,cooBuffer);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount);

        GLES20.glDisableVertexAttribArray(mHPosition);
    }


    private void calculateAttribute(){
        ArrayList<Float> alVertix = new ArrayList<Float>();// 存放顶点坐标的ArrayList
        ArrayList<Float> textureVertix = new ArrayList<Float>();// 存放纹理坐标的ArrayList
        for (int vAngle = 0; vAngle < 180; vAngle = vAngle + angleSpan)// 垂直方向angleSpan度一份
        {
            for (int hAngle = 0; hAngle < 360; hAngle = hAngle + angleSpan)// 水平方向angleSpan度一份
            {
                // 纵向横向各到一个角度后计算对应的此点在球面上的坐标
                float x0 = (float) (r * UNIT_SIZE
                    * Math.sin(Math.toRadians(vAngle)) * Math.cos(Math
                    .toRadians(hAngle)));
                float y0 = (float) (r * UNIT_SIZE
                    * Math.sin(Math.toRadians(vAngle)) * Math.sin(Math
                    .toRadians(hAngle)));
                float z0 = (float) (r * UNIT_SIZE * Math.cos(Math
                    .toRadians(vAngle)));
                // Log.w("x0 y0 z0","" + x0 + "  "+y0+ "  " +z0);

                float x1 = (float) (r * UNIT_SIZE
                    * Math.sin(Math.toRadians(vAngle)) * Math.cos(Math
                    .toRadians(hAngle + angleSpan)));
                float y1 = (float) (r * UNIT_SIZE
                    * Math.sin(Math.toRadians(vAngle)) * Math.sin(Math
                    .toRadians(hAngle + angleSpan)));
                float z1 = (float) (r * UNIT_SIZE * Math.cos(Math
                    .toRadians(vAngle)));

                float x2 = (float) (r * UNIT_SIZE
                    * Math.sin(Math.toRadians(vAngle + angleSpan)) * Math
                    .cos(Math.toRadians(hAngle + angleSpan)));
                float y2 = (float) (r * UNIT_SIZE
                    * Math.sin(Math.toRadians(vAngle + angleSpan)) * Math
                    .sin(Math.toRadians(hAngle + angleSpan)));
                float z2 = (float) (r * UNIT_SIZE * Math.cos(Math
                    .toRadians(vAngle + angleSpan)));
                // Log.w("x2 y2 z2","" + x2 + "  "+y2+ "  " +z2);
                float x3 = (float) (r * UNIT_SIZE
                    * Math.sin(Math.toRadians(vAngle + angleSpan)) * Math
                    .cos(Math.toRadians(hAngle)));
                float y3 = (float) (r * UNIT_SIZE
                    * Math.sin(Math.toRadians(vAngle + angleSpan)) * Math
                    .sin(Math.toRadians(hAngle)));
                float z3 = (float) (r * UNIT_SIZE * Math.cos(Math
                    .toRadians(vAngle + angleSpan)));
                // Log.w("x3 y3 z3","" + x3 + "  "+y3+ "  " +z3);
                // 将计算出来的XYZ坐标加入存放顶点坐标的ArrayList
                alVertix.add(x1);
                alVertix.add(y1);
                alVertix.add(z1);
                alVertix.add(x3);
                alVertix.add(y3);
                alVertix.add(z3);
                alVertix.add(x0);
                alVertix.add(y0);
                alVertix.add(z0);

                // *****************************************************************
                float s0 = hAngle / 360.0f;
                float s1 = (hAngle + angleSpan)/360.0f ;
                float t0 = 1 - vAngle / 180.0f;
                float t1 = 1 - (vAngle + angleSpan) / 180.0f;

                textureVertix.add(s1);// x1 y1对应纹理坐标
                textureVertix.add(t0);
                textureVertix.add(s0);// x3 y3对应纹理坐标
                textureVertix.add(t1);
                textureVertix.add(s0);// x0 y0对应纹理坐标
                textureVertix.add(t0);

                // *****************************************************************
                alVertix.add(x1);
                alVertix.add(y1);
                alVertix.add(z1);
                alVertix.add(x2);
                alVertix.add(y2);
                alVertix.add(z2);
                alVertix.add(x3);
                alVertix.add(y3);
                alVertix.add(z3);

                // *****************************************************************
                textureVertix.add(s1);// x1 y1对应纹理坐标
                textureVertix.add(t0);
                textureVertix.add(s1);// x2 y3对应纹理坐标
                textureVertix.add(t1);
                textureVertix.add(s0);// x3 y3对应纹理坐标
                textureVertix.add(t1);
                // *****************************************************************
            }
        }
        vCount = alVertix.size() / 3;// 顶点的数量
        // 将alVertix中的坐标值转存到一个float数组中
        float vertices[] = new float[vCount * 3];
        for (int i = 0; i < alVertix.size(); i++) {
            vertices[i] = alVertix.get(i);
        }
        posBuffer = ByteBuffer
            .allocateDirect(vertices.length * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer();
        // 把坐标们加入FloatBuffer中
        posBuffer.put(vertices);
        // 设置buffer，从第一个坐标开始读
        posBuffer.position(0);
        // *****************************************************************
        float textures[] = new float[textureVertix.size()];
        for(int i=0;i<textureVertix.size();i++){
            textures[i] = textureVertix.get(i);
        }
        cooBuffer = ByteBuffer
            .allocateDirect(textures.length * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer();
        // 把坐标们加入FloatBuffer中
        cooBuffer.put(textures);
        // 设置buffer，从第一个坐标开始读
        cooBuffer.position(0);
    }

    private FloatBuffer convertToFloatBuffer(ArrayList<Float> data){
        float[] d=new float[data.size()];
        for (int i=0;i<d.length;i++){
            d[i]=data.get(i);
        }

        ByteBuffer buffer=ByteBuffer.allocateDirect(data.size()*4);
        buffer.order(ByteOrder.nativeOrder());
        FloatBuffer ret=buffer.asFloatBuffer();
        ret.put(d);
        ret.position(0);
        return ret;
    }

    private int createTexture(){
        int[] texture=new int[1];
        if(mBitmap!=null&&!mBitmap.isRecycled()){
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
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, mBitmap, 0);
            return texture[0];
        }
        return 0;
    }

}
