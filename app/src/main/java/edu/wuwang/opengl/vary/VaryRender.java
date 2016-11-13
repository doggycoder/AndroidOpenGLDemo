package edu.wuwang.opengl.vary;

import android.content.res.Resources;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import edu.wuwang.opengl.utils.VaryTools;

/**
 * Created by wuwang on 2016/10/30
 */

public class VaryRender implements GLSurfaceView.Renderer {

    private VaryTools tools;
    private Cube cube;

    public VaryRender(Resources res){
        tools=new VaryTools();
        cube=new Cube(res);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(1.0f,1.0f,1.0f,1.0f);
        //开启深度测试
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        cube.create();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0,0,width,height);
        float rate=width/(float)height;
        tools.ortho(-rate*6,rate*6,-6,6,3,20);
        tools.setCamera(0,0,10,0,0,0,0,1,0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT| GLES20.GL_DEPTH_BUFFER_BIT);
        cube.setMatrix(tools.getFinalMatrix());
        cube.drawSelf();

        //y轴正方形平移
        tools.pushMatrix();
        tools.translate(0,3,0);
        cube.setMatrix(tools.getFinalMatrix());
        cube.drawSelf();
        tools.popMatrix();

        //y轴负方向平移，然后按xyz->(0,0,0)到(1,1,1)旋转30度
        tools.pushMatrix();
        tools.translate(0,-3,0);
        tools.rotate(30f,1,1,1);
        cube.setMatrix(tools.getFinalMatrix());
        cube.drawSelf();
        tools.popMatrix();

        //x轴负方向平移，然后按xyz->(0,0,0)到(1,-1,1)旋转120度，在放大到0.5倍
        tools.pushMatrix();
        tools.translate(-3,0,0);
        tools.scale(0.5f,0.5f,0.5f);

        //在以上变换的基础上再进行变换
        tools.pushMatrix();
        tools.translate(12,0,0);
        tools.scale(1.0f,2.0f,1.0f);
        tools.rotate(30f,1,2,1);
        cube.setMatrix(tools.getFinalMatrix());
        cube.drawSelf();
        tools.popMatrix();

        //接着被中断的地方执行
        tools.rotate(30f,-1,-1,1);
        cube.setMatrix(tools.getFinalMatrix());
        cube.drawSelf();
        tools.popMatrix();
    }

}
