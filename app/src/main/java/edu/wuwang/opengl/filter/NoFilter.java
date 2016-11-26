/*
 *
 * NoFilter.java
 * 
 * Created by Wuwang on 2016/11/19
 * Copyright © 2016年 深圳哎吖科技. All rights reserved.
 */
package edu.wuwang.opengl.filter;

import android.content.res.Resources;

/**
 * Description:
 */
public class NoFilter extends AFilter {

    public NoFilter(Resources res) {
        super(res);
    }

    @Override
    protected void onCreate() {
        createProgramByAssetsFile("shader/base_vertex.sh",
            "shader/base_fragment.sh");
    }

    @Override
    protected void onSizeChanged(int width, int height) {

    }
}
