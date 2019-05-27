package com.project.capstone_design.billcode.setting;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.project.capstone_design.billcode.R;

public class Activity_Setting_PushAlert extends AppCompatActivity {

    private boolean isPushChecked;
    private boolean isTotalPushChecked;
    private SharedPreferences mAppData;
    private Switch mSwitch1;
    private Switch mSwitch2;
    private TextView mTextView3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_pushalert);

        mSwitch1 = findViewById(R.id.Setting_PushAlert_Switch1);
        mSwitch2 = findViewById(R.id.Setting_PushAlert_Switch2);
        mTextView3 = findViewById(R.id.Setting_PushAlert_TextView3);
        Toolbar mToolbar = findViewById(R.id.Setting_PushAlert_Toolbar);
        setSupportActionBar(mToolbar);

        mAppData = getSharedPreferences("AppData", MODE_PRIVATE);
        load();



        if (isPushChecked) {
            mSwitch1.setChecked(true);
            if (isTotalPushChecked)
                mSwitch2.setChecked(true);
        } else{
            mTextView3.setTextColor(getColor(R.color.whiteGray));
            mSwitch2.setClickable(false);
        }


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_chevron_left_mint_leaf_24dp);

        mSwitch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // isTotalPushChecked = isChecked; // 선택된 값으로 초기화
                if (isChecked) {
                    mTextView3.setTextColor(getColor(R.color.black));
                    mSwitch2.setClickable(true);
                } else {
                    mTextView3.setTextColor(getColor(R.color.whiteGray));
                    mSwitch2.setChecked(false);
                    mSwitch2.setClickable(false);
                }
                save();

            }
        });

        mSwitch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //isTotalPushChecked = isChecked; // 선택된 값으로 초기화
                    save();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // 설정값을 저장하는 함수
    private void save() {
        // SharedPreferences 객체만으론 저장 불가능 Editor 사용
        SharedPreferences.Editor editor = mAppData.edit();

        // 에디터객체.put타입( 저장시킬 이름, 저장시킬 값 )
        // 저장시킬 이름이 이미 존재하면 덮어씌움
        if(mSwitch1.isChecked())
            Log.i("IMPORTANT", ">>> Saved mSwitch1.isChecked()   :  True");
        else
            Log.i("IMPORTANT", ">>> Saved mSwitch1.isChecked()   :  False");

        if(mSwitch2.isChecked())
            Log.i("IMPORTANT", ">>> Saved mSwitch2.isChecked()   :  True");
        else
            Log.i("IMPORTANT", ">>> Saved mSwitch2.isChecked()   :  False");
        editor.putBoolean("IS_PUSH_CHECKED", mSwitch1.isChecked());
        editor.putBoolean("IS_TOTAL_PUSH_CHECKED", mSwitch2.isChecked());
        // apply, commit 을 안하면 변경된 내용이 저장되지 않음
        editor.apply();
    }

    // 설정값을 불러오는 함수
    private void load() {
        // SharedPreferences 객체.get타입( 저장된 이름, 기본값 )
        // 저장된 이름이 존재하지 않을 시 기본값
        //savedAppData = mAppData.getBoolean("SAVED_APP_DATA", false);
        isPushChecked = mAppData.getBoolean("IS_PUSH_CHECKED", false);
        isTotalPushChecked = mAppData.getBoolean("IS_TOTAL_PUSH_CHECKED", false);

        if(isPushChecked)
            Log.i("IMPORTANT", ">>> Loaded isPushChecked   :  True");
        else
            Log.i("IMPORTANT", ">>> Loaded isPushChecked   :  False");

        if(isTotalPushChecked)
            Log.i("IMPORTANT", ">>> Loaded isTotalPushChecked   :  True");
        else
            Log.i("IMPORTANT", ">>> Loaded isTotalPushChecked   :  False");
    }

    private String find(boolean flag){
        String str_true = "True";
        String str_false = "False";
        if (flag)
            return str_true;
        else
            return str_false;
    }
}
