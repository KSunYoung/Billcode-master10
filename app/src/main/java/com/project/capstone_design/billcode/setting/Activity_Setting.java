package com.project.capstone_design.billcode.setting;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.project.capstone_design.billcode.R;
import com.project.capstone_design.billcode.login.LoginActivity;

import java.util.ArrayList;

public class Activity_Setting extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_setting);

        Toolbar mToolbar = findViewById(R.id.setting_toolbar);
        mToolbar.setTitleTextColor(getColor(R.color.mintLeaf));
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_chevron_left_mint_leaf_24dp);
        //getSupportActionBar().setTitle("설정");


        ListView mListView = findViewById(R.id.setting_item);

        final ArrayList<String> settingList = new ArrayList<>();
        settingList.add("로그인 설정");
        settingList.add("푸쉬알림 설정");
        settingList.add("만든 사람들");
        settingList.add("로그아웃");

        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(Activity_Setting.this, android.R.layout.simple_list_item_1, settingList) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                TextView mTextView = view.findViewById(android.R.id.text1);
                mTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                mTextView.setPadding(90,30,0,30);
                //textview.setPadding(10, 70, 10, 50);

                return view;
            }
        };

        mListView.setAdapter(listViewAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(settingList.get(position).toString().equals("푸쉬알림 설정")){
                    Intent intent = new Intent(Activity_Setting.this, Activity_Setting_PushAlert.class);
                    startActivity(intent);
                }
                else if (settingList.get(position).toString().equals("로그아웃")){

                    LoginManager.getInstance().logOut(); // fb
                    onClickLogout(); // kakao
                    Toast.makeText(Activity_Setting.this, "로그아웃 되셨습니다.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(Activity_Setting.this, "준비중 입니다.", Toast.LENGTH_SHORT).show();
                }
            }
            /*
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (settingList.get(i).toString() == "로그아웃") {
                    LoginManager.getInstance().logOut(); // fb
                    onClickLogout(); // kakao
                    Toast.makeText(Activity_Setting.this, "로그아웃 되셨습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Activity_Setting.this, "준비중 입니다.", Toast.LENGTH_SHORT).show();
                }
            }*/
        });
    }


    private void onClickLogout() {
        UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
                Intent intent = new Intent(Activity_Setting.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
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

}