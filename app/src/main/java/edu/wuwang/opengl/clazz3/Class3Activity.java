package edu.wuwang.opengl.clazz3;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import edu.wuwang.opengl.R;
import edu.wuwang.opengl.tool.GlTool;

public class Class3Activity extends AppCompatActivity implements GLSurfaceView.Renderer {

    private GLSurfaceView glView;
    private int width;
    private int height;
    private float[] matrix = new float[16];

    private float[] vertexCo = new float[]{
             1, 1, 1,
            -1, 1, 1,
            -1,-1, 1,
             1,-1, 1,
             1, 1,-1,
            -1, 1,-1,
            -1,-1,-1,
             1,-1,-1
    };

    private float[] color = new float[]{
            1,0,0,1,
            1,0,0,1,
            1,0,0,1,
            1,0,0,1,

            0,0,1,1,
            0,0,1,1,
            0,0,1,1,
            0,0,1,1,

    };

    private short[] index = new short[]{
            0,1,2, 0,2,3,       //前面
            0,4,5, 0,5,1,       //上面
            0,3,7, 0,7,4,       //右面

            6,5,1, 6,1,2,       //左面
            6,7,3, 6,3,2,       //下面
            6,5,4, 6,4,7        //后面
    };

    private int glProgramId;
    private int glMatrixId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glview);
        glView = findViewById(R.id.glView);
        glView.setEGLContextClientVersion(2);
        glView.setRenderer(this);
        glView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    private ByteBuffer createFloatByteBuffer(float[] data){
        ByteBuffer vertexCoBuffer = ByteBuffer.allocateDirect(data.length * 4);
        vertexCoBuffer.order(ByteOrder.nativeOrder());
        vertexCoBuffer.asFloatBuffer().put(data);
        vertexCoBuffer.position(0);
        return vertexCoBuffer;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        glProgramId = GlTool.compileProgramFromAssets(getApplicationContext(),
                "shader/clazz3/matrixColor.vert","shader/clazz3/color.frag");
        GLES20.glUseProgram(glProgramId);
        int glVertexCo = GLES20.glGetAttribLocation(glProgramId,"aVertexCo");
        int glColor = GLES20.glGetAttribLocation(glProgramId,"aColor");
        glMatrixId = GLES20.glGetUniformLocation(glProgramId,"uVertexMat");


        Matrix.setIdentityM(matrix,0);
        GLES20.glUniformMatrix4fv(glMatrixId,1,false,matrix,0);

        ByteBuffer vertexCoBuffer = createFloatByteBuffer(vertexCo);
        GLES20.glEnableVertexAttribArray(glVertexCo);
        GLES20.glVertexAttribPointer(glVertexCo,3,GLES20.GL_FLOAT,false,0,vertexCoBuffer);

        ByteBuffer colorBuffer = createFloatByteBuffer(color);
        GLES20.glEnableVertexAttribArray(glColor);
        GLES20.glVertexAttribPointer(glColor,4,GLES20.GL_FLOAT,false,0,colorBuffer);

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        this.width = width;
        this.height = height;
        Matrix.setIdentityM(matrix,0);
        Matrix.scaleM(matrix,0,0.5f,0.5f*width/(float)height,0.5f);
        Matrix.rotateM(matrix,0,45,1,0,0);
        Matrix.rotateM(matrix,0,135,0,0,1);
        GLES20.glUniformMatrix4fv(glMatrixId,1,false,matrix,0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glClearColor(1,1,1,1);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT|GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glDrawElements(GLES20.GL_TRIANGLES,index.length,GLES20.GL_UNSIGNED_SHORT,ShortBuffer.wrap(index));
    }
}
