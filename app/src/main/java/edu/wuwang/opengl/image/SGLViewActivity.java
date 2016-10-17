/*
 *
 * SGLViewActivity.java
 * 
 * Created by Wuwang on 2016/10/15
 */
package edu.wuwang.opengl.image;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

/**
 * Description:
 */
public class SGLViewActivity extends Activity {

    private GLSurfaceView mGLView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(mGLView=new SGLView(this));
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
