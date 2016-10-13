/*
 *
 * FGLViewActivity.java
 * 
 * Created by Wuwang on 2016/9/30
 * Copyright © 2016年 深圳哎吖科技. All rights reserved.
 */
package edu.wuwang.opengl;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;

import java.util.HashMap;

/**
 * Description:
 */
public class FGLViewActivity extends Activity {

    private Button mChange;
    private FGLView mGLView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fglview);
        init();
    }

    private void init(){
        mChange= (Button) findViewById(R.id.mChange);
        mGLView= (FGLView) findViewById(R.id.mGLView);
    }

}
