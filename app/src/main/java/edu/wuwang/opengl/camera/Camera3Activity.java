package edu.wuwang.opengl.camera;

import android.support.v7.widget.AppCompatSeekBar;
import android.util.Log;
import android.widget.SeekBar;
import edu.wuwang.opengl.R;
import edu.wuwang.opengl.filter.LookupFilter;

/**
 * Created by aiya on 2017/6/8.
 */

public class Camera3Activity extends Camera2Activity {

    private AppCompatSeekBar mSeek;
    private LookupFilter mFilter;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_camera3);
        mSeek=(AppCompatSeekBar) findViewById(R.id.mSeek);
        mSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.e("wuwang","process:"+progress);
                mFilter.setIntensity(progress/100f);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    protected void onFilterSet(TextureController controller) {
        mFilter=new LookupFilter(getResources());
        mFilter.setMaskImage("lookup/highkey.png");
        mFilter.setIntensity(0.0f);
        controller.addFilter(mFilter);
    }
}
