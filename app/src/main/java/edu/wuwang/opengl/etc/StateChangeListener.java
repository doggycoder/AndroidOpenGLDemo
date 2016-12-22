/*
 *
 * StateCallback.java
 * 
 * Created by Wuwang on 2016/11/30
 * Copyright © 2016年 深圳哎吖科技. All rights reserved.
 */
package edu.wuwang.opengl.etc;

/**
 * Description:
 */
public interface StateChangeListener {

    int START=1;
    int STOP=2;
    int PLAYING=3;
    int INIT=4;
    int PAUSE=5;
    int RESUME=6;

    void onStateChanged(int lastState, int nowState);

}
