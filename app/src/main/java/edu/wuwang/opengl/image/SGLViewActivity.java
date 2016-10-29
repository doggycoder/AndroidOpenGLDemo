/*
 *
 * SGLViewActivity.java
 * 
 * Created by Wuwang on 2016/10/15
 */
package edu.wuwang.opengl.image;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import edu.wuwang.opengl.R;
import edu.wuwang.opengl.image.filter.ColorFilter;
import edu.wuwang.opengl.image.filter.ContrastColorFilter;

/**
 * Description:
 */
public class SGLViewActivity extends Activity implements PopupMenu.OnMenuItemClickListener {

    private SGLView mGLView;
    private PopupMenu mFilterMenu;
    private boolean isHalf=false;
    private Button btChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        mGLView= (SGLView) findViewById(R.id.glView);
        btChange= (Button) findViewById(R.id.btChange);
    }

    private void showMenu(){
        if(mFilterMenu==null){
            mFilterMenu=new PopupMenu(this,findViewById(R.id.btChange));
            getMenuInflater().inflate(R.menu.menu_filter,mFilterMenu.getMenu());
            mFilterMenu.setOnMenuItemClickListener(this);
        }
        mFilterMenu.show();
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.btChange:
                showMenu();
                break;
            case R.id.btDealWith:
                isHalf=!isHalf;
                if(isHalf){
                    ((TextView)view).setText("处理一半");
                }else{
                    ((TextView)view).setText("全部处理");
                }
                mGLView.getRender().getFilter().setHalf(isHalf);
                mGLView.getRender().refresh();
                mGLView.requestRender();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGLView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGLView.onPause();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        btChange.setText(item.getTitle());
        switch (item.getItemId()){
            case R.id.mDefault:
                mGLView.setFilter(new ContrastColorFilter(this, ColorFilter.Filter.NONE));
                break;
            case R.id.mGray:
                mGLView.setFilter(new ContrastColorFilter(this, ColorFilter.Filter.GRAY));
                break;
            case R.id.mCool:
                mGLView.setFilter(new ContrastColorFilter(this, ColorFilter.Filter.COOL));
                break;
            case R.id.mWarm:
                mGLView.setFilter(new ContrastColorFilter(this, ColorFilter.Filter.WARM));
                break;
            case R.id.mBlur:
                mGLView.setFilter(new ContrastColorFilter(this, ColorFilter.Filter.BLUR));
                break;
            case R.id.mMagn:
                mGLView.setFilter(new ContrastColorFilter(this, ColorFilter.Filter.MAGN));
                break;

        }
        mGLView.getRender().getFilter().setHalf(isHalf);
        mGLView.requestRender();
        return false;
    }
}
