package edu.wuwang.opengl.obj;

import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.io.IOException;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import edu.wuwang.opengl.BaseActivity;
import edu.wuwang.opengl.R;
import edu.wuwang.opengl.utils.Gl2Utils;

/**
 * Created by wuwang on 2017/1/7
 */

public class ObjLoadActivity extends BaseActivity {

    private GLSurfaceView mGLView;
    private ObjFilter mFilter;
    private ObjReader.Obj3D obj;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obj);
        mGLView= (GLSurfaceView) findViewById(R.id.mGLView);
        mGLView.setEGLContextClientVersion(2);
        mFilter=new ObjFilter(getResources());
        obj=new ObjReader.Obj3D();
        try {
            ObjReader.read(getAssets().open("3dres/hat.obj"),obj);
            mFilter.setObj3D(obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mGLView.setRenderer(new GLSurfaceView.Renderer() {
            @Override
            public void onSurfaceCreated(GL10 gl, EGLConfig config) {
                mFilter.create();
            }

            @Override
            public void onSurfaceChanged(GL10 gl, int width, int height) {
                mFilter.onSizeChanged(width, height);
                float[] matrix=Gl2Utils.getOriginalMatrix();
                Matrix.scaleM(matrix,0,0.2f,0.2f*width/height,0.2f);
                mFilter.setMatrix(matrix);
            }

            @Override
            public void onDrawFrame(GL10 gl) {
                Matrix.rotateM(mFilter.getMatrix(),0,0.3f,0,1,0);
                mFilter.draw();
            }
        });
        mGLView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }


    @Override
    protected void onResume() {
        super.onResume();
        mGLView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGLView.onPause();
    }
}
