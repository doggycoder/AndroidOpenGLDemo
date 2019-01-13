package edu.wuwang.opengl.clazz1;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import edu.wuwang.opengl.R;

public class Class1Activity extends AppCompatActivity implements GLSurfaceView.Renderer {

    private GLSurfaceView glView;

    private float[] vertexCo = new float[]{
            -1,-1,0,1,
            -1,1,0,1,
            1,1,0,1
    };

    private float[] colors = new float[]{
            1,0,0,1,
            1,0,0,1,
            0,1,0,1
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glview);
        glView = findViewById(R.id.glView);
        glView.setEGLContextClientVersion(2);
        glView.setRenderer(this);
        glView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        int vertexShaderId = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        GLES20.glShaderSource(vertexShaderId,"//顶点坐标\n" +
                "attribute vec4 aVertexCo;\n" +
                "//顶点颜色\n" +
                "attribute vec4 aColor;\n" +
                "\n" +
                "varying vec4 vColor;\n" +
                "\n" +
                "void main() {\n" +
                "\tgl_Position = aVertexCo;\n" +
                "\tvColor = aColor;\n" +
                "}\n");
        GLES20.glCompileShader(vertexShaderId);
        int fragmentShaderId = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
        GLES20.glShaderSource(fragmentShaderId,"precision mediump float;\n" +
                "\n" +
                "//片源颜色\n" +
                "varying vec4 vColor;\n" +
                "\n" +
                "void main(){\n" +
                "    gl_FragColor = vColor;\n" +
                "}");
        GLES20.glCompileShader(fragmentShaderId);
        int glProgramId = GLES20.glCreateProgram();
        GLES20.glAttachShader(glProgramId,vertexShaderId);
        GLES20.glAttachShader(glProgramId,fragmentShaderId);
        GLES20.glLinkProgram(glProgramId);
        GLES20.glUseProgram(glProgramId);

        int glVertexCo = GLES20.glGetAttribLocation(glProgramId,"aVertexCo");
        int glColor = GLES20.glGetAttribLocation(glProgramId,"aColor");

        ByteBuffer vertexCoBuffer = ByteBuffer.allocateDirect(4*4*3);
        vertexCoBuffer.order(ByteOrder.nativeOrder());
        vertexCoBuffer.asFloatBuffer().put(vertexCo);
        vertexCoBuffer.position(0);
        GLES20.glEnableVertexAttribArray(glVertexCo);
        GLES20.glVertexAttribPointer(glVertexCo,4,GLES20.GL_FLOAT,false,0,vertexCoBuffer);

        ByteBuffer colorBuffer = ByteBuffer.allocateDirect(4*4*3);
        colorBuffer.order(ByteOrder.nativeOrder());
        colorBuffer.asFloatBuffer().put(colors);
        colorBuffer.position(0);
        GLES20.glEnableVertexAttribArray(glColor);
        GLES20.glVertexAttribPointer(glColor,4,GLES20.GL_FLOAT,false,0,colorBuffer);

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClearColor(1,1,1,1);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP,0,3);
    }
}
