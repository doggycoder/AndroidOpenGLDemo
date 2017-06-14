package edu.wuwang.opengl.filter;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import edu.wuwang.opengl.utils.EasyGlUtils;
import java.io.IOException;

public class LookupFilter extends AFilter {

    private int mHMaskImage;
    private int mHIntensity;

    private float intensity;

    private int[] mastTextures=new int[1];
    private Bitmap mBitmap;

    public LookupFilter(Resources mRes) {
        super(mRes);
    }

    @Override
    protected void onCreate() {
        createProgramByAssetsFile("lookup/lookup.vert","lookup/lookup.frag");
        mHMaskImage=GLES20.glGetUniformLocation(mProgram,"maskTexture");
        mHIntensity=GLES20.glGetUniformLocation(mProgram,"intensity");
        EasyGlUtils.genTexturesWithParameter(1,mastTextures,0, GLES20.GL_RGBA,512,512);
    }

    public void setIntensity(float value){
        this.intensity=value;
    }

    public void setMaskImage(String mask){
        try {
            mBitmap=BitmapFactory.decodeStream(mRes.getAssets().open(mask));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setMaskImage(Bitmap bitmap){
        this.mBitmap=bitmap;
    }

    @Override
    protected void onSizeChanged(int width, int height) {

    }

    @Override
    protected void onBindTexture() {
        super.onBindTexture();
    }

    @Override
    protected void onSetExpandData() {
        super.onSetExpandData();
        GLES20.glUniform1f(mHIntensity,intensity);
        if(mastTextures[0]!=0){
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0+getTextureType()+1);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,mastTextures[0]);
            if(mBitmap!=null&&!mBitmap.isRecycled()){
                GLUtils.texImage2D(GLES20.GL_TEXTURE_2D,0,mBitmap,0);
                mBitmap.recycle();
            }
            GLES20.glUniform1i(mHMaskImage,getTextureType()+1);
        }


    }
}
