/*
 *
 * FGLView.java
 * 
 * Created by Wuwang on 2016/9/29
 * Copyright © 2016年 深圳哎吖科技. All rights reserved.
 */
package edu.wuwang.opengl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import edu.wuwang.opengl.render.FGLRender;

/**
 * Description:
 */
public class FGLView extends GLSurfaceView {

    private Renderer renderer;

    public FGLView(Context context) {
        this(context,null);
    }

    public FGLView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        setEGLContextClientVersion(2);
        setRenderer(renderer=new FGLRender());
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }
}
