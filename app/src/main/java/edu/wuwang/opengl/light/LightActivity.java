package edu.wuwang.opengl.light;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.Switch;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import edu.wuwang.opengl.R;
import edu.wuwang.opengl.utils.MatrixUtils;
import edu.wuwang.opengl.utils.ShaderUtils;

/**
 * @author wuwang
 * @version 1.00 , 2018/03/25
 */

public class LightActivity extends AppCompatActivity implements GLSurfaceView.Renderer,CompoundButton.OnCheckedChangeListener {

    private GLSurfaceView glView;

    private int glProgramId;
    private int glUMatrix;
    private int glULightPosition;
    private int glAPosition;
    private int glACoord;
    private int glANormal;
    private int glUAmbientStrength;
    private int glUDiffuseStrength;
    private int glUSpecularStrength;
    private int glUBaseColor;
    private int glULightColor;

    private float[] matrix;
    private float[] lambMatrix;
    private FloatBuffer buffer;

    private final float DEFAULT_AMBIENT=0.3f;
    private final float DEFAULT_DIFFUSE=0.5f;
    private final float DEFAULT_SPECULAR=0.8f;

    private float ambientStrength=0;
    private float diffuseStrength=0;
    private float specularStrength=0;

    private final float[] data=new float[]{
            -0.5f, -0.5f, -0.5f,  0.0f,  0.0f, -1.0f,
            0.5f, -0.5f, -0.5f,  0.0f,  0.0f, -1.0f,
            0.5f,  0.5f, -0.5f,  0.0f,  0.0f, -1.0f,
            0.5f,  0.5f, -0.5f,  0.0f,  0.0f, -1.0f,
            -0.5f,  0.5f, -0.5f,  0.0f,  0.0f, -1.0f,
            -0.5f, -0.5f, -0.5f,  0.0f,  0.0f, -1.0f,

            -0.5f, -0.5f,  0.5f,  0.0f,  0.0f, 1.0f,
            0.5f, -0.5f,  0.5f,  0.0f,  0.0f, 1.0f,
            0.5f,  0.5f,  0.5f,  0.0f,  0.0f, 1.0f,
            0.5f,  0.5f,  0.5f,  0.0f,  0.0f, 1.0f,
            -0.5f,  0.5f,  0.5f,  0.0f,  0.0f, 1.0f,
            -0.5f, -0.5f,  0.5f,  0.0f,  0.0f, 1.0f,

            -0.5f,  0.5f,  0.5f, -1.0f,  0.0f,  0.0f,
            -0.5f,  0.5f, -0.5f, -1.0f,  0.0f,  0.0f,
            -0.5f, -0.5f, -0.5f, -1.0f,  0.0f,  0.0f,
            -0.5f, -0.5f, -0.5f, -1.0f,  0.0f,  0.0f,
            -0.5f, -0.5f,  0.5f, -1.0f,  0.0f,  0.0f,
            -0.5f,  0.5f,  0.5f, -1.0f,  0.0f,  0.0f,

            0.5f,  0.5f,  0.5f,  1.0f,  0.0f,  0.0f,
            0.5f,  0.5f, -0.5f,  1.0f,  0.0f,  0.0f,
            0.5f, -0.5f, -0.5f,  1.0f,  0.0f,  0.0f,
            0.5f, -0.5f, -0.5f,  1.0f,  0.0f,  0.0f,
            0.5f, -0.5f,  0.5f,  1.0f,  0.0f,  0.0f,
            0.5f,  0.5f,  0.5f,  1.0f,  0.0f,  0.0f,

            -0.5f, -0.5f, -0.5f,  0.0f, -1.0f,  0.0f,
            0.5f, -0.5f, -0.5f,  0.0f, -1.0f,  0.0f,
            0.5f, -0.5f,  0.5f,  0.0f, -1.0f,  0.0f,
            0.5f, -0.5f,  0.5f,  0.0f, -1.0f,  0.0f,
            -0.5f, -0.5f,  0.5f,  0.0f, -1.0f,  0.0f,
            -0.5f, -0.5f, -0.5f,  0.0f, -1.0f,  0.0f,

            -0.5f,  0.5f, -0.5f,  0.0f,  1.0f,  0.0f,
            0.5f,  0.5f, -0.5f,  0.0f,  1.0f,  0.0f,
            0.5f,  0.5f,  0.5f,  0.0f,  1.0f,  0.0f,
            0.5f,  0.5f,  0.5f,  0.0f,  1.0f,  0.0f,
            -0.5f,  0.5f,  0.5f,  0.0f,  1.0f,  0.0f,
            -0.5f,  0.5f, -0.5f,  0.0f,  1.0f,  0.0f

    };

    private float lx,ly,lz;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light);
        glView= (GLSurfaceView) findViewById(R.id.glView);
        glView.setEGLContextClientVersion(2);
        glView.setRenderer(this);
        glView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

        buffer=ByteBuffer.allocateDirect(data.length*4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        buffer.position(0);
        buffer.put(data);

        int[] switchId=new int[]{
                R.id.ambient,R.id.diffuse,R.id.specular
        };

        for (int id:switchId){
            ((Switch)findViewById(id)).setOnCheckedChangeListener(this);
        }
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        glProgramId= ShaderUtils.createProgram(getResources(),"light/light.vert","light/light.frag");
        glAPosition=GLES20.glGetAttribLocation(glProgramId,"aPosition");
        glACoord=GLES20.glGetAttribLocation(glProgramId,"aCoord");
        glANormal=GLES20.glGetAttribLocation(glProgramId,"aNormal");
        glUMatrix=GLES20.glGetUniformLocation(glProgramId,"uMatrix");
        glULightPosition=GLES20.glGetUniformLocation(glProgramId,"uLightPosition");
        glUAmbientStrength=GLES20.glGetUniformLocation(glProgramId,"uAmbientStrength");
        glUDiffuseStrength=GLES20.glGetUniformLocation(glProgramId,"uDiffuseStrength");
        glUSpecularStrength=GLES20.glGetUniformLocation(glProgramId,"uSpecularStrength");
        glULightColor=GLES20.glGetUniformLocation(glProgramId,"uLightColor");
        glUBaseColor=GLES20.glGetUniformLocation(glProgramId,"uBaseColor");
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        lx=0f;
        ly=0.8f;
        lz=-1f;

        matrix=MatrixUtils.getOriginalMatrix();
        Matrix.scaleM(matrix,0,0.5f,0.5f*width/(float)height,0.5f);
        lambMatrix=MatrixUtils.getOriginalMatrix();
        Matrix.translateM(lambMatrix,0,lx,ly,lz);
        Matrix.scaleM(lambMatrix,0,0.09f,0.09f*width/height,0.09f);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        Matrix.rotateM(matrix,0,2,-1,-1,1);
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT|GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glClearColor(0.5f,0.5f,0.5f,1.0f);
        GLES20.glUseProgram(glProgramId);
        GLES20.glUniformMatrix4fv(glUMatrix,1,false,matrix,0);

        //环境光强度
        GLES20.glUniform1f(glUAmbientStrength,ambientStrength);
        //漫反射光强度
        GLES20.glUniform1f(glUDiffuseStrength,diffuseStrength);
        //镜面光强度
        GLES20.glUniform1f(glUSpecularStrength,specularStrength);
        //光源颜色
        GLES20.glUniform3f(glULightColor,1.0f,0.0f,0.0f);
        //物体颜色
        GLES20.glUniform4f(glUBaseColor,1.0f,1.0f,1.0f,1.0f);
        //光源位置
        GLES20.glUniform3f(glULightPosition,lx,ly,lz);
        //传入顶点信息
        GLES20.glEnableVertexAttribArray(glAPosition);
        buffer.position(0);
        GLES20.glVertexAttribPointer(glAPosition,3,GLES20.GL_FLOAT,false,6*4,buffer);
        //传入法线信息
        GLES20.glEnableVertexAttribArray(glANormal);
        buffer.position(3);
        GLES20.glVertexAttribPointer(glANormal,3,GLES20.GL_FLOAT,false,6*4,buffer);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glCullFace(GLES20.GL_FRONT);
        GLES20.glFrontFace(GLES20.GL_CW);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES,0,data.length/6);
        //再绘制一个立方体，标记光源位置
        GLES20.glUniformMatrix4fv(glUMatrix,1,false,lambMatrix,0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES,0,data.length/6);
        GLES20.glDisable(GLES20.GL_CULL_FACE);
        GLES20.glDisable(GLES20.GL_DEPTH_TEST);
        GLES20.glDisableVertexAttribArray(glAPosition);
        GLES20.glDisableVertexAttribArray(glANormal);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.ambient:
                ambientStrength=isChecked?DEFAULT_AMBIENT:0;
                break;
            case R.id.diffuse:
                diffuseStrength=isChecked?DEFAULT_DIFFUSE:0;
                break;
            case R.id.specular:
                specularStrength=isChecked?DEFAULT_SPECULAR:0;
                break;
            default:
                break;
        }
    }
}
