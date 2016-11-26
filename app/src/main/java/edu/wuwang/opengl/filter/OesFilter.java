/*
 *
 * CameraFilter.java
 * 
 * Created by Wuwang on 2016/11/19
 * Copyright © 2016年 深圳哎吖科技. All rights reserved.
 */
package edu.wuwang.opengl.filter;

import android.content.res.Resources;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;

/**
 * Description:
 */
public class OesFilter extends AFilter{

    public OesFilter(Resources mRes) {
        super(mRes);
    }

    @Override
    protected void onCreate() {
        createProgramByAssetsFile("shader/oes_base_vertex.sh","shader/oes_base_fragment.sh");
    }

    @Override
    protected void onBindTexture() {
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0+getTextureType());
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,getTextureId());
        GLES20.glUniform1i(mHTexture,getTextureType());
    }

    @Override
    protected void onSizeChanged(int width, int height) {

    }

}
