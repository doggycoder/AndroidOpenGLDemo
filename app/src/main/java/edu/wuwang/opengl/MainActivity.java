package edu.wuwang.opengl;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import edu.wuwang.opengl.image.SGLViewActivity;

public class MainActivity extends AppCompatActivity {

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
        }
    }
}
