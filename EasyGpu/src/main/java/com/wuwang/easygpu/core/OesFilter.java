package com.wuwang.easygpu.core;

import android.content.res.Resources;
import android.opengl.GLES11Ext;

/**
 * Created by wuwang on 2016/12/20
 */

public class OesFilter extends BaseFilter {

    public OesFilter(Resources res) {
        super(res,"shader/oes.vert","shader/oes.vert");
        setTextureTarget(GLES11Ext.GL_TEXTURE_EXTERNAL_OES);
    }

    @Override
    protected void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onBindTexture() {
        super.onBindTexture();
    }

    @Override
    protected void onSizeChanged(int width, int height) {
        super.onSizeChanged(width, height);
    }
}
