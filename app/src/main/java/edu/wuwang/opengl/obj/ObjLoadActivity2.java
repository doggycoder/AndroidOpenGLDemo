package edu.wuwang.opengl.obj;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import edu.wuwang.opengl.R;
import edu.wuwang.opengl.utils.Gl2Utils;

/**
 * Created by wuwang on 2017/2/23
 */

public class ObjLoadActivity2 extends AppCompatActivity {

    private GLSurfaceView mGLView;
    private List<ObjFilter2> filters;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obj);
        mGLView= (GLSurfaceView) findViewById(R.id.mGLView);
        mGLView.setEGLContextClientVersion(2);
        List<Obj3D> model=ObjReader.readMultiObj(this,"assets/3dres/pikachu.obj");
        filters=new ArrayList<>();
        for (int i=0;i<model.size();i++){
            ObjFilter2 f=new ObjFilter2(getResources());
            f.setObj3D(model.get(i));
            filters.add(f);
        }
        mGLView.setRenderer(new GLSurfaceView.Renderer() {
            @Override
            public void onSurfaceCreated(GL10 gl, EGLConfig config) {
                for (ObjFilter2 f:filters){
                    f.create();
                }
            }

            @Override
            public void onSurfaceChanged(GL10 gl, int width, int height) {
                for (ObjFilter2 f:filters){
                    f.onSizeChanged(width, height);
                    float[] matrix= Gl2Utils.getOriginalMatrix();
                    Matrix.translateM(matrix,0,0,-0.3f,0);
                    Matrix.scaleM(matrix,0,0.008f,0.008f*width/height,0.008f);
                    f.setMatrix(matrix);
                }
            }

            @Override
            public void onDrawFrame(GL10 gl) {
                GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
                GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
                for (ObjFilter2 f:filters){
                    Matrix.rotateM(f.getMatrix(),0,0.3f,0,1,0);
                    f.draw();
                }
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
