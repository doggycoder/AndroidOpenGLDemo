package edu.wuwang.opengl.blend;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import edu.wuwang.opengl.BaseActivity;
import edu.wuwang.opengl.R;
import edu.wuwang.opengl.filter.NoFilter;
import edu.wuwang.opengl.utils.EasyGlUtils;
import edu.wuwang.opengl.utils.MatrixUtils;

/**
 * Created by aiya on 2017/8/2.
 */

public class BlendActivity extends BaseActivity implements GLSurfaceView.Renderer{

    private GLSurfaceView mGLView;
    private Bitmap srcBitmap;
    private Bitmap dstBitmap;
    private NoFilter mDstFilter;
    private NoFilter mSrcFilter;
    private WheelView mSrcParamView;
    private WheelView mDstParamsView;
    private TextView mEqua;

    private int width,height;

    private int nDstPar=GLES20.GL_ONE_MINUS_SRC_ALPHA;
    private int nSrcPar=GLES20.GL_SRC_ALPHA;
    private int nEquaIndex=0;


    private String[] paramStr=new String[]{
            "GL_ZERO","GL_ONE","GL_SRC_COLOR","GL_ONE_MINUS_SRC_COLOR",
            "GL_DST_COLOR","GL_ONE_MINUS_DST_COLOR","GL_SRC_ALPHA","GL_ONE_MINUS_SRC_ALPHA",
            "GL_DST_ALPHA","GL_ONE_MINUS_DST_ALPHA","GL_CONSTANT_COLOR","GL_ONE_MINUS_CONSTANT_COLOR",
            "GL_CONSTANT_ALPHA","GL_ONE_MINUS_CONSTANT_ALPHA","GL_SRC_ALPHA_SATURATE"
    };

    private int[] paramInt=new int[]{
            GLES20.GL_ZERO,GLES20.GL_ONE,GLES20.GL_SRC_COLOR,GLES20.GL_ONE_MINUS_SRC_COLOR,
            GLES20.GL_DST_COLOR,GLES20.GL_ONE_MINUS_DST_COLOR,GLES20.GL_SRC_ALPHA,GLES20.GL_ONE_MINUS_SRC_ALPHA,
            GLES20.GL_DST_ALPHA,GLES20.GL_ONE_MINUS_DST_ALPHA,GLES20.GL_CONSTANT_COLOR,GLES20.GL_ONE_MINUS_CONSTANT_COLOR,
            GLES20.GL_CONSTANT_ALPHA,GLES20.GL_ONE_MINUS_CONSTANT_ALPHA,GLES20.GL_SRC_ALPHA_SATURATE
    };

    private int[] equaInt=new int[]{
            GLES20.GL_FUNC_ADD,GLES20.GL_FUNC_SUBTRACT,GLES20.GL_FUNC_REVERSE_SUBTRACT
    };

    private String[] equaStr=new String[]{
            "GL_FUNC_ADD","GL_FUNC_SUBTRACT","GL_FUNC_REVERSE_SUBTRACT"
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blend);
        mEqua= (TextView) findViewById(R.id.mEqua);
        mGLView= (GLSurfaceView) findViewById(R.id.mGLView);
        mGLView.setEGLContextClientVersion(2);
        mGLView.setEGLConfigChooser(8,8,8,8,16,8);
        mGLView.setRenderer(this);
        mGLView.getHolder().setFormat(PixelFormat.TRANSPARENT);
        mGLView.setZOrderOnTop(true);
        mGLView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

        mEqua.setText(equaStr[nEquaIndex]);
        mEqua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nEquaIndex++;
                if(nEquaIndex>=3)nEquaIndex=0;
                mEqua.setText(equaStr[nEquaIndex]);
            }
        });

        dstBitmap= BitmapFactory.decodeResource(getResources(),R.mipmap.bg);
        srcBitmap= BitmapFactory.decodeResource(getResources(),R.mipmap.src);
        mSrcParamView= (WheelView) findViewById(R.id.mSrcParam);
        mDstParamsView= (WheelView) findViewById(R.id.mDstParam);

        initParamData();

        mDstFilter=new NoFilter(getResources()){
            @Override
            protected void onClear() {

            }
        };
        mSrcFilter=new NoFilter(getResources()){
            @Override
            protected void onClear() {

            }
        };
    }

    private void initParamData(){
        for (int i=0;i<paramStr.length;i++){
            mSrcParamView.addData(paramStr[i],paramInt[i]);
            mDstParamsView.addData(paramStr[i],paramInt[i]);
        }
        mSrcParamView.setCenterItem("GL_SRC_COLOR");
        mDstParamsView.setCenterItem("GL_ONE_MINUS_SRC_ALPHA");
        mSrcParamView.setTextSize(10);
        mDstParamsView.setTextSize(10);
        mSrcParamView.setOnCenterItemChangedListener(new WheelView.OnCenterItemChangedListener() {
            @Override
            public void onItemChange(Object now) {
                nSrcPar= (int) now;
            }
        });
        mDstParamsView.setOnCenterItemChangedListener(new WheelView.OnCenterItemChangedListener() {
            @Override
            public void onItemChange(Object now) {
                nDstPar= (int) now;
            }
        });
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

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0,0,0,0);
        mSrcFilter.create();
        mDstFilter.create();
        int[] textures=new int[2];
        GLES20.glGenTextures(2,textures,0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,textures[0]);
        EasyGlUtils.useTexParameter();
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D,0,GLES20.GL_RGBA,srcBitmap,0);
        mSrcFilter.setTextureId(textures[0]);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,textures[1]);
        EasyGlUtils.useTexParameter();
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D,0,GLES20.GL_RGBA,dstBitmap,0);
        mDstFilter.setTextureId(textures[1]);

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        this.width=width;
        this.height=height;
        mSrcFilter.setSize(width,height);
        mDstFilter.setSize(width,height);
        MatrixUtils.getMatrix(mDstFilter.getMatrix(),MatrixUtils.TYPE_FITSTART,dstBitmap.getWidth(),dstBitmap.getHeight(),
                width,height);
        MatrixUtils.getMatrix(mSrcFilter.getMatrix(),MatrixUtils.TYPE_FITSTART,srcBitmap.getWidth(),srcBitmap.getHeight(),
                width,height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(nSrcPar,nDstPar);
        GLES20.glBlendEquation(equaInt[nEquaIndex]);
        GLES20.glViewport(0,0,width,height);
        mDstFilter.draw();
        mSrcFilter.draw();
    }
}
