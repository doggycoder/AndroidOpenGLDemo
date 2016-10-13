/*
 *
 * Shape.java
 * 
 * Created by Wuwang on 2016/9/30
 * Copyright © 2016年 深圳哎吖科技. All rights reserved.
 */
package edu.wuwang.opengl.render;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

/**
 * Description:
 */
public abstract class Shape implements GLSurfaceView.Renderer {

    public int loadShader(int type, String shaderCode){
        //根据type创建顶点着色器或者片元着色器
        int shader = GLES20.glCreateShader(type);
        //将资源加入到着色器中，并编译
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }

}
