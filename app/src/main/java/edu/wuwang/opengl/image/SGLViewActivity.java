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
import android.view.View;

import edu.wuwang.opengl.R;

/**
 * Description:
 */
public class SGLViewActivity extends Activity {

    private SGLView mGLView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        mGLView= (SGLView) findViewById(R.id.glView);
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.btChange:
                break;
            case R.id.btDealWith:
                break;
        }
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
