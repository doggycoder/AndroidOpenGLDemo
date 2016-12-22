/*
 *
 * ZipActivity.java
 * 
 * Created by Wuwang on 2016/12/8
 * Copyright © 2016年 深圳哎吖科技. All rights reserved.
 */
package edu.wuwang.opengl.etc;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import edu.wuwang.opengl.R;
import edu.wuwang.opengl.utils.Gl2Utils;

/**
 * Description:
 */
public class ZipActivity extends AppCompatActivity {

    private ZipAniView mAniView;
    private String nowMenu="assets/etczip/cc.zip";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        AssetsUtils.copyFileFromAssets(getAssets(),"effect",getExternalFilesDir(Environment
//            .DIRECTORY_PICTURES)
//            .getAbsolutePath());
        setContentView(R.layout.activity_zip);
        mAniView= (ZipAniView)findViewById(R.id.mAni);
        mAniView.setScaleType(Gl2Utils.TYPE_FITSTART);
        mAniView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mAniView.isPlay()){
//                    File f=new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),
//                        "firezip/fire.zip");
                    mAniView.setAnimation(nowMenu,40);
                    mAniView.start();
                }
            }
        });
        mAniView.setStateChangeListener(new StateChangeListener() {
            @Override
            public void onStateChanged(int lastState, int nowState) {
                if(nowState==STOP){
                    if(!mAniView.isPlay()){
//                    File f=new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),
//                        "firezip/fire.zip");
                        mAniView.setAnimation(nowMenu,40);
                        mAniView.start();
                    }
                }
            }
        });
    }

}
