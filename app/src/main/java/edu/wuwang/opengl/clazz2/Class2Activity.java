package edu.wuwang.opengl.clazz2;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import edu.wuwang.opengl.R;
import edu.wuwang.opengl.tool.GlTool;

public class Class2Activity extends AppCompatActivity implements GLSurfaceView.Renderer {

    private GLSurfaceView glView;
    private int width;
    private int height;
    private float[] matrix = new float[16];

    private float[] vertexCo;

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
//        initRect();
        initCircle();
    }

    private ByteBuffer createFloatByteBuffer(float[] data){
        ByteBuffer vertexCoBuffer = ByteBuffer.allocateDirect(data.length * 4);
        vertexCoBuffer.order(ByteOrder.nativeOrder());
        vertexCoBuffer.asFloatBuffer().put(data);
        vertexCoBuffer.position(0);
        return vertexCoBuffer;
    }

    private void initRect(){
         vertexCo = new float[]{
                -1,-1,
                -1,1,
                1,1,
                1,-1,
         };
    }

    private void initCircle(){
        ArrayList<Float> data = new ArrayList<>();
        data.add(0.0f);
        data.add(0.0f);
        for (int i = 0;i<360;i++){
            data.add((float) Math.sin(i));
            data.add((float) Math.cos(i));
        }
        vertexCo = new float[data.size()];
        for (int i=0;i<data.size();i++){
            vertexCo[i] = data.get(i);
        }
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        glProgramId = GlTool.compileProgramFromAssets(getApplicationContext(),
                "shader/clazz2/matrix.vert","shader/clazz2/color.frag");
        GLES20.glUseProgram(glProgramId);
        int glVertexCo = GLES20.glGetAttribLocation(glProgramId,"aVertexCo");
        glMatrixId = GLES20.glGetUniformLocation(glProgramId,"uVertexMat");


        Matrix.setIdentityM(matrix,0);
        GLES20.glUniformMatrix4fv(glMatrixId,1,false,matrix,0);

        ByteBuffer vertexCoBuffer = createFloatByteBuffer(vertexCo);
        GLES20.glEnableVertexAttribArray(glVertexCo);
        GLES20.glVertexAttribPointer(glVertexCo,2,GLES20.GL_FLOAT,false,0,vertexCoBuffer);

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        this.width = width;
        this.height = height;
        Matrix.setIdentityM(matrix,0);
        Matrix.scaleM(matrix,0,0.5f,0.5f*width/(float)height,1);
        GLES20.glUniformMatrix4fv(glMatrixId,1,false,matrix,0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClearColor(1,1,1,1);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN,0,vertexCo.length/2);
    }
}
