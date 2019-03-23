package com.wuzp.mylibluancher.anim;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.wuzp.mylibluancher.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * @author wuzp
 */
public class AnimActivity extends AppCompatActivity {

    @BindView(R.id.btn_anim)
    Button btnAnim;
    @BindView(R.id.btn_anim_test)
    Button btnAnimCheck;

    Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anim);
        unbinder =  ButterKnife.bind(this);
        btnAnim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testAnim();
            }
        });
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(unbinder !=null){
            unbinder.unbind();
        }
    }

    private void testAnim(){
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.scale_in);
//        animation.setDuration(3000l);
//        animation.setFillAfter(true);
        btnAnimCheck.startAnimation(animation);
    }
}
