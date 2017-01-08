package edu.wuwang.opengl.obj;

import android.content.res.Resources;
import android.opengl.GLES20;

import edu.wuwang.opengl.filter.AFilter;

/**
 * Created by wuwang on 2017/1/8
 */

public class ObjFilter extends AFilter {

    private int vertCount;

    private int mHNormal;

    public ObjFilter(Resources mRes) {
        super(mRes);
    }

    public void setObj3D(ObjReader.Obj3D obj){
        mVerBuffer=obj.vert;
        vertCount=obj.vertCount;
    }

    @Override
    protected void initBuffer() {
        super.initBuffer();
    }

    @Override
    protected void onCreate() {
        createProgramByAssetsFile("3dres/obj.vert","3dres/obj.frag");
        mHNormal=GLES20.glGetAttribLocation(mProgram,"vNormal");
        //打开深度检测
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        //打开背面剪裁
        GLES20.glEnable(GLES20.GL_CULL_FACE);
    }

    @Override
    protected void onClear() {
        super.onClear();
    }

    @Override
    protected void onDraw() {
        GLES20.glEnableVertexAttribArray(mHPosition);
        GLES20.glVertexAttribPointer(mHPosition,3, GLES20.GL_FLOAT, false, 3*4,mVerBuffer);
        GLES20.glEnableVertexAttribArray(mHNormal);
        GLES20.glVertexAttribPointer(mHNormal,3, GLES20.GL_FLOAT, false, 3*4,mVerBuffer);
//        GLES20.glEnableVertexAttribArray(mHCoord);
//        GLES20.glVertexAttribPointer(mHCoord, 2, GLES20.GL_FLOAT, false, 0, mTexBuffer);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES,0,vertCount);
        GLES20.glDisableVertexAttribArray(mHPosition);
        GLES20.glDisableVertexAttribArray(mHNormal);
//        GLES20.glDisableVertexAttribArray(mHCoord);
    }

    @Override
    protected void onSizeChanged(int width, int height) {

    }
}
