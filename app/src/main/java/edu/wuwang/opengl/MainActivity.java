package edu.wuwang.opengl;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import edu.wuwang.opengl.camera.CameraActivity;
import edu.wuwang.opengl.egl.EGLBackEnvActivity;
import edu.wuwang.opengl.etc.ZipActivity;
import edu.wuwang.opengl.fbo.FBOActivity;
import edu.wuwang.opengl.image.SGLViewActivity;
import edu.wuwang.opengl.obj.ObjLoadActivity;
import edu.wuwang.opengl.obj.ObjLoadActivity2;
import edu.wuwang.opengl.render.FGLViewActivity;
import edu.wuwang.opengl.vary.VaryActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.btDraw:
                startActivity(new Intent(this,FGLViewActivity.class));
                break;
            case R.id.btTexture:
                startActivity(new Intent(this,SGLViewActivity.class));
                break;
            case R.id.btVary:
                startActivity(new Intent(this, VaryActivity.class));
                break;
            case R.id.btCamera:
                startActivity(new Intent(this, CameraActivity.class));
                break;
            case R.id.btAni:
                startActivity(new Intent(this, ZipActivity.class));
                break;
            case R.id.btFbo:
                startActivity(new Intent(this, FBOActivity.class));
                break;
            case R.id.btEgl:
                startActivity(new Intent(this, EGLBackEnvActivity.class));
                break;
            case R.id.btObj:
                startActivity(new Intent(this, ObjLoadActivity.class));
                break;
            case R.id.btObj2:
                startActivity(new Intent(this, ObjLoadActivity2.class));
                break;
        }
    }
}
