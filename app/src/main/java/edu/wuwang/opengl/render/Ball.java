package edu.wuwang.opengl.render;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;
import android.view.View;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import edu.wuwang.opengl.utils.ShaderUtils;

/**
 * Created by wuwang on 2016/10/15
 */
public class Ball extends Shape {

    private float step=5f;
    private FloatBuffer vertexBuffer;
    private int vSize;

    private int mProgram;
    private float[] mViewMatrix=new float[16];
    private float[] mProjectMatrix=new float[16];
    private float[] mMVPMatrix=new float[16];

    public Ball(View mView) {
        super(mView);
        float[] dataPos=createBallPos();
        ByteBuffer buffer=ByteBuffer.allocateDirect(dataPos.length*4);
        buffer.order(ByteOrder.nativeOrder());
        vertexBuffer=buffer.asFloatBuffer();
        vertexBuffer.put(dataPos);
        vertexBuffer.position(0);
        vSize=dataPos.length/3;
    }
    
    private float[] createBallPos(){
        //球以(0,0,0)为中心，以R为半径，则球上任意一点的坐标为
        // ( R * cos(a) * sin(b),y0 = R * sin(a),R * cos(a) * cos(b))
        // 其中，a为圆心到点的线段与xz平面的夹角，b为圆心到点的线段在xz平面的投影与z轴的夹角
        ArrayList<Float> data=new ArrayList<>();
        float r1,r2;
        float h1,h2;
        float sin,cos;
        for(float i=-90;i<90+step;i+=step){
            r1 = (float)Math.cos(i * Math.PI / 180.0);
            r2 = (float)Math.cos((i + step) * Math.PI / 180.0);
            h1 = (float)Math.sin(i * Math.PI / 180.0);
            h2 = (float)Math.sin((i + step) * Math.PI / 180.0);
            // 固定纬度, 360 度旋转遍历一条纬线
            float step2=step*2;
            for (float j = 0.0f; j <360.0f+step; j +=step2 ) {
                cos = (float) Math.cos(j * Math.PI / 180.0);
                sin = -(float) Math.sin(j * Math.PI / 180.0);

                data.add(r2 * cos);
                data.add(h2);
                data.add(r2 * sin);
                data.add(r1 * cos);
                data.add(h1);
                data.add(r1 * sin);
            }
        }
        float[] f=new float[data.size()];
        for(int i=0;i<f.length;i++){
            f[i]=data.get(i);
        }
        return f;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        mProgram= ShaderUtils.createProgram(mView.getResources(),"vshader/Ball.sh","fshader/Cone.sh");
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        //计算宽高比
        float ratio=(float)width/height;
        //设置透视投影
        Matrix.frustumM(mProjectMatrix, 0, -ratio, ratio, -1, 1, 3, 20);
        //设置相机位置
        Matrix.setLookAtM(mViewMatrix, 0, 1.0f, -10.0f, -4.0f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        //计算变换矩阵
        Matrix.multiplyMM(mMVPMatrix,0,mProjectMatrix,0,mViewMatrix,0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glUseProgram(mProgram);
        Log.e("wuwang","mProgram:"+mProgram);
        int mMatrix=GLES20.glGetUniformLocation(mProgram,"vMatrix");
        GLES20.glUniformMatrix4fv(mMatrix,1,false,mMVPMatrix,0);
        int mPositionHandle=GLES20.glGetAttribLocation(mProgram,"vPosition");
        Log.e("wuwang","Get Position:"+mPositionHandle);
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glVertexAttribPointer(mPositionHandle,3,GLES20.GL_FLOAT,false,0,vertexBuffer);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN,0,vSize);
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }
}
