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

/**
 * Description:
 */
public class FGLViewActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new FGLView(this));
    }
}
