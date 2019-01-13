package edu.wuwang.opengl;

import edu.wuwang.opengl.clazz1.Class1Activity;
import edu.wuwang.opengl.clazz2.Class2Activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView mList;
    private ArrayList<MenuBean> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mList= findViewById(R.id.mList);
        mList.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        data=new ArrayList<>();
        add("第一课：绘制三角形",Class1Activity.class);
        add("第二课：正方形和圆形",Class2Activity.class);
        mList.setAdapter(new MenuAdapter());
    }

    private void add(String name,Class<?> clazz){
        MenuBean bean=new MenuBean();
        bean.name=name;
        bean.clazz=clazz;
        data.add(bean);
    }

    private class MenuBean{

        String name;
        Class<?> clazz;

    }

    private class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuHolder>{


        @Override
        public MenuHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MenuHolder(getLayoutInflater().inflate(R.layout.item_button,parent,false));
        }

        @Override
        public void onBindViewHolder(MenuHolder holder, int position) {
            holder.setPosition(position);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class MenuHolder extends RecyclerView.ViewHolder{

            private Button mBtn;

            MenuHolder(View itemView) {
                super(itemView);
                mBtn= (Button)itemView.findViewById(R.id.mBtn);
                mBtn.setOnClickListener(MainActivity.this);
            }

            public void setPosition(int position){
                MenuBean bean=data.get(position);
                mBtn.setText(bean.name);
                mBtn.setTag(position);
            }
        }

    }

    @Override
    public void onClick(View view){
        int position= (int)view.getTag();
        MenuBean bean=data.get(position);
        startActivity(new Intent(this,bean.clazz));
    }
}
